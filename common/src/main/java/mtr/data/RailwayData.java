package mtr.data;

import io.netty.buffer.Unpooled;
import mtr.MTR;
import mtr.api.RailwayDataModule;
import mtr.api.events.MTRAreaUpdateEvent;
import mtr.api.events.MTRPlayerConnectionEvent;
import mtr.integration.BlueMapModule;
import mtr.integration.DynmapModule;
import mtr.integration.SquaremapModule;
import mtr.loader.MTRRegistry;
import mtr.block.BlockNode;
import mtr.client.ClientData;
import mtr.mappings.PersistentStateMapper;
import mtr.packet.*;
import mtr.path.PathData;
import mtr.registry.Networking;
import mtr.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class RailwayData extends PersistentStateMapper {

	public final Set<Station> stations = new HashSet<>();
	public final Set<Platform> platforms = new HashSet<>();
	public final Set<Siding> sidings = new HashSet<>();
	public final Set<Route> routes = new HashSet<>();
	public final Set<Depot> depots = new HashSet<>();
	public final Set<LiftServer> lifts = new HashSet<>();
	public final DataCache dataCache = new DataCache(stations, platforms, sidings, routes, depots, lifts);

	private int prevPlatformCount;
	private int prevSidingCount;

	private final Level level;
	private final Map<BlockPos, Map<BlockPos, Rail>> rails = new HashMap<>();
	private final SignalBlocks signalBlocks = new SignalBlocks();

    private final List<Map<UUID, Long>> trainPositions = new ArrayList<>(2);
	private final Map<Player, BlockPos> playerLastUpdatedPositions = new HashMap<>();
	private final List<Player> playersToSyncSchedules = new ArrayList<>();
	private final UpdateNearbyMovingObjects<TrainServer> updateNearbyTrains;
	private final UpdateNearbyMovingObjects<LiftServer> updateNearbyLifts;
	private final Map<Long, List<ScheduleEntry>> schedulesForPlatform = new HashMap<>();
	private final Map<Long, Map<BlockPos, TrainDelay>> trainDelays = new HashMap<>();

	private static final int PLAYER_MOVE_UPDATE_THRESHOLD = 16;
	private static final int SCHEDULE_UPDATE_TICKS = 60;

	private static final int DATA_VERSION = 1;

	private static final String NAME = "mtr_train_data";
	private static final String KEY_RAW_MESSAGE_PACK = "raw_message_pack";
	private static final String KEY_DATA_VERSION = "mtr_data_version";
	private static final String KEY_STATIONS = "stations";
	private static final String KEY_PLATFORMS = "platforms";
	private static final String KEY_SIDINGS = "sidings";
	private static final String KEY_ROUTES = "routes";
	private static final String KEY_DEPOTS = "depots";
	private static final String KEY_LIFTS = "lifts";
	private static final String KEY_RAILS = "rails";
	private static final String KEY_SIGNAL_BLOCKS = "signal_blocks";

	private final HashMap<String, RailwayDataModule> modules;
	private static final List<RailwayDataModuleCallback> railwayModulesCallbacks = new ArrayList<>();

	public RailwayData(Level level) {
		super(NAME);
		this.level = level;
		this.modules =  new HashMap<>();

		trainPositions.add(new HashMap<>());
		trainPositions.add(new HashMap<>());

		addModule(RailwayDataFileSaveModule.NAME, new RailwayDataFileSaveModule(this, level, rails, signalBlocks));
		addModule(RailwayDataLoggingModule.NAME, new RailwayDataLoggingModule(this, level, rails));
		addModule(RailwayDataPathGenerationModule.NAME, new RailwayDataPathGenerationModule(this, level, rails));
		addModule(RailwayDataRailActionsModule.NAME, new RailwayDataRailActionsModule(this, level, rails));
		addModule(RailwayDataMountModule.NAME, new RailwayDataMountModule(this, level, rails));
		addModule(RailwayDataDriveTrainModule.NAME, new RailwayDataDriveTrainModule(this, level, rails));
		addModule(RailwayDataRouteFinderModule.NAME, new RailwayDataRouteFinderModule(this, level, rails));

		for(RailwayDataModuleCallback railwayDataModuleCallback : railwayModulesCallbacks) {
			RailwayDataModule module = railwayDataModuleCallback.getModule(this, level, rails);
			if(module != null) {
				addModule(module.getName(), module);
			}
		}

		updateNearbyTrains = new UpdateNearbyMovingObjects<>(Networking.PACKET_DELETE_TRAINS, Networking.PACKET_UPDATE_TRAINS);
		updateNearbyLifts = new UpdateNearbyMovingObjects<>(Networking.PACKET_DELETE_LIFTS, Networking.PACKET_UPDATE_LIFTS);
	}

	public static int getStationColor(BlockPos pos) {
		final int defaultColor = 0x7F7F7F;
		if (pos == null) {
			return defaultColor;
		} else {
			final Station station = getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
			return station == null ? defaultColor : station.color;
		}
	}

	@Override
	public void load(CompoundTag compoundTag) {
		// TODO temporary code start
		if (compoundTag.contains(KEY_RAW_MESSAGE_PACK)) {
			try {
				final MessageUnpacker messageUnpacker = MessagePack.newDefaultUnpacker(compoundTag.getByteArray(KEY_RAW_MESSAGE_PACK));
				final int mapSize = messageUnpacker.unpackMapHeader();

				for (int i = 0; i < mapSize; ++i) {
					final String key = messageUnpacker.unpackString();
					if (key.equals(KEY_DATA_VERSION)) {
						if (messageUnpacker.unpackInt() > DATA_VERSION) {
							throw new IllegalArgumentException("Unsupported data version");
						}
						continue;
					}

					final int arraySize = messageUnpacker.unpackArrayHeader();
					switch (key) {
						case KEY_STATIONS:
							for (int j = 0; j < arraySize; ++j) {
								stations.add(new Station(readMessagePackSKMap(messageUnpacker)));
							}
							break;
						case KEY_PLATFORMS:
							for (int j = 0; j < arraySize; ++j) {
								platforms.add(new Platform(readMessagePackSKMap(messageUnpacker)));
							}
							break;
						case KEY_SIDINGS:
							for (int j = 0; j < arraySize; ++j) {
								sidings.add(new Siding(readMessagePackSKMap(messageUnpacker)));
							}
							break;
						case KEY_ROUTES:
							for (int j = 0; j < arraySize; ++j) {
								routes.add(new Route(readMessagePackSKMap(messageUnpacker)));
							}
							break;
						case KEY_DEPOTS:
							for (int j = 0; j < arraySize; ++j) {
								depots.add(new Depot(readMessagePackSKMap(messageUnpacker)));
							}
							break;
						case KEY_LIFTS:
							for (int j = 0; j < arraySize; ++j) {
								lifts.add(new LiftServer(readMessagePackSKMap(messageUnpacker)));
							}
							break;
						case KEY_RAILS:
							for (int j = 0; j < arraySize; ++j) {
								final RailEntry railEntry = new RailEntry(readMessagePackSKMap(messageUnpacker));
								rails.put(railEntry.pos, railEntry.connections);
							}
							break;
						case KEY_SIGNAL_BLOCKS:
							for (int j = 0; j < arraySize; ++j) {
								signalBlocks.signalBlocks.add(new SignalBlocks.SignalBlock(readMessagePackSKMap(messageUnpacker)));
							}
							break;
					}
				}
			} catch (IOException e) {
				MTR.LOGGER.error("", e);
			}
		} else {
			try {
				final CompoundTag tagStations = compoundTag.getCompound(KEY_STATIONS);
				for (final String key : tagStations.getAllKeys()) {
					stations.add(new Station(tagStations.getCompound(key)));
				}

				final CompoundTag tagNewPlatforms = compoundTag.getCompound(KEY_PLATFORMS);
				for (final String key : tagNewPlatforms.getAllKeys()) {
					platforms.add(new Platform(tagNewPlatforms.getCompound(key)));
				}

				final CompoundTag tagNewSidings = compoundTag.getCompound(KEY_SIDINGS);
				for (final String key : tagNewSidings.getAllKeys()) {
					sidings.add(new Siding(tagNewSidings.getCompound(key)));
				}

				final CompoundTag tagNewRoutes = compoundTag.getCompound(KEY_ROUTES);
				for (final String key : tagNewRoutes.getAllKeys()) {
					routes.add(new Route(tagNewRoutes.getCompound(key)));
				}

				final CompoundTag tagNewDepots = compoundTag.getCompound(KEY_DEPOTS);
				for (final String key : tagNewDepots.getAllKeys()) {
					depots.add(new Depot(tagNewDepots.getCompound(key)));
				}

				final CompoundTag tagNewRails = compoundTag.getCompound(KEY_RAILS);
				for (final String key : tagNewRails.getAllKeys()) {
					final RailEntry railEntry = new RailEntry(tagNewRails.getCompound(key));
					rails.put(railEntry.pos, railEntry.connections);
				}

				final CompoundTag tagNewSignalBlocks = compoundTag.getCompound(KEY_SIGNAL_BLOCKS);
				for (final String key : tagNewSignalBlocks.getAllKeys()) {
					signalBlocks.signalBlocks.add(new SignalBlocks.SignalBlock(tagNewSignalBlocks.getCompound(key)));
				}
			} catch (Exception e) {
				MTR.LOGGER.error("", e);
			}
		}

		for(RailwayDataModule railwayDataModule : modules.values()) {
			railwayDataModule.earlyInit();
		}

		validateData();
		dataCache.sync();
		signalBlocks.writeCache();

		for(RailwayDataModule railwayDataModule : modules.values()) {
			railwayDataModule.init();
		}

		try {
			DynmapModule.syncData(level, this);
			MTR.LOGGER.info("[NeoMTR] Dynmap is detected");
		} catch (NoClassDefFoundError | Exception ignored) {
		}
        try {
			BlueMapModule.syncData(level, this);
			MTR.LOGGER.info("[NeoMTR] BlueMap is detected");
		} catch (NoClassDefFoundError | Exception ignored) {
		}
        try {
			SquaremapModule.syncData(level, this);
			MTR.LOGGER.info("[NeoMTR] Squaremap is detected");
		} catch (NoClassDefFoundError | Exception ignored) {
		}
    }

	@Override
	public void save(File file, HolderLookup.Provider registries) {
		final MinecraftServer minecraftServer = ((ServerLevel) level).getServer();
		if (minecraftServer.isStopped() || !minecraftServer.isRunning()) {
			for(RailwayDataModule railwayDataModule : modules.values()) {
				railwayDataModule.fullSave();
			}
		} else {
			for(RailwayDataModule railwayDataModule : modules.values()) {
				railwayDataModule.autoSave();
			}
		}

		setDirty();
		super.save(file, registries);
	}

	@Override
	public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider registries) {
		return compoundTag;
	}

	public void simulateTrains(ServerLevel serverLevel) {
		final List<? extends Player> players = level.players();
		players.forEach(player -> {
			final BlockPos playerBlockPos = player.blockPosition();
			final Vec3 playerPos = player.position();

			int railUpdateDistance = (serverLevel.getServer().getPlayerList().getViewDistance() + 1) * 16;

			if (!playerLastUpdatedPositions.containsKey(player) || playerLastUpdatedPositions.get(player).distManhattan(playerBlockPos) > PLAYER_MOVE_UPDATE_THRESHOLD) {
				final Map<BlockPos, Map<BlockPos, Rail>> railsToAdd = new HashMap<>();
				rails.forEach((startPos, blockPosRailMap) -> blockPosRailMap.forEach((endPos, rail) -> {
					if (new AABB(startPos.getX(), startPos.getY(), startPos.getZ(), endPos.getX(), endPos.getY(), endPos.getZ()).inflate(railUpdateDistance).contains(playerPos)) {
						if (!railsToAdd.containsKey(startPos)) {
							railsToAdd.put(startPos, new HashMap<>());
						}
						railsToAdd.get(startPos).put(endPos, rail);
					}
				}));

				final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
				packet.writeInt(railsToAdd.size());
				railsToAdd.forEach((posStart, railMap) -> {
					packet.writeBlockPos(posStart);
					packet.writeInt(railMap.size());
					railMap.forEach((posEnd, rail) -> {
						packet.writeBlockPos(posEnd);
						rail.writePacket(packet);
					});
				});

				if (packet.readableBytes() <= Networking.MAX_PACKET_BYTES) {
					MTRRegistry.sendToPlayer((ServerPlayer) player, Networking.PACKET_WRITE_RAILS, packet);
				}
				playerLastUpdatedPositions.put(player, playerBlockPos);
			}
		});

		updateNearbyTrains.startTick();
		trainPositions.removeFirst();
		trainPositions.add(new HashMap<>());
		schedulesForPlatform.clear();
		signalBlocks.resetOccupied();
		sidings.forEach(siding -> {
			siding.setSidingData(level, dataCache.sidingIdToDepot.get(siding.id), rails);
			siding.simulateTrain(dataCache, getModule(RailwayDataDriveTrainModule.NAME), trainPositions, signalBlocks, updateNearbyTrains.newDataSetInPlayerRange, updateNearbyTrains.dataSetToSync, schedulesForPlatform, trainDelays);
		});
		depots.forEach(depot -> depot.deployTrain(this, level));

		updateNearbyLifts.startTick();
		lifts.forEach(lift -> lift.tickServer(level, updateNearbyLifts.newDataSetInPlayerRange, updateNearbyLifts.dataSetToSync));

		for(RailwayDataModule railwayDataModule : modules.values()) {
			railwayDataModule.tick();
		}
		updateNearbyTrains.tick();
		updateNearbyLifts.tick();

		if (MTR.isGameTickInterval(SCHEDULE_UPDATE_TICKS)) {
			players.forEach(player -> {
				if (!playersToSyncSchedules.contains(player)) {
					playersToSyncSchedules.add(player);
				}
			});
		}
		if (!playersToSyncSchedules.isEmpty()) {
			final Player player = playersToSyncSchedules.removeFirst();
			final BlockPos playerBlockPos = player.blockPosition();
			final Vec3 playerPos = player.position();

			final Set<Long> platformIds = platforms.stream().filter(platform -> {
				if (platform.isCloseToSavedRail(playerBlockPos, PLAYER_MOVE_UPDATE_THRESHOLD, PLAYER_MOVE_UPDATE_THRESHOLD, PLAYER_MOVE_UPDATE_THRESHOLD)) {
					return true;
				}
				final Station station = dataCache.platformIdToStation.get(platform.id);
				return station != null && station.inArea(playerBlockPos.getX(), playerBlockPos.getZ());
			}).map(platform -> platform.id).collect(Collectors.toSet());

			int railUpdateDistance = (serverLevel.getServer().getPlayerList().getViewDistance() + 1) * 16;

			final Set<UUID> railsToAdd = new HashSet<>();
			rails.forEach((startPos, blockPosRailMap) -> blockPosRailMap.forEach((endPos, rail) -> {
				if (new AABB(startPos.getX(), startPos.getY(), startPos.getZ(), endPos.getX(), endPos.getY(), endPos.getZ()).inflate(railUpdateDistance).contains(playerPos)) {
					railsToAdd.add(PathData.getRailProduct(startPos, endPos));
				}
			}));
			final Map<Long, Boolean> signalBlockStatus = new HashMap<>();
			final Map<UUID, Boolean> occupiedRails = new HashMap<>();
			railsToAdd.forEach(rail -> {
				signalBlocks.getSignalBlockStatus(signalBlockStatus, rail);
				occupiedRails.put(rail, trainPositions.get(1).containsKey(rail));
			});

			if (!platformIds.isEmpty() || !signalBlockStatus.isEmpty() || !occupiedRails.isEmpty()) {
				final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
				packet.writeInt(platformIds.size());
				platformIds.forEach(platformId -> {
					packet.writeLong(platformId);
					final List<ScheduleEntry> scheduleEntries = schedulesForPlatform.get(platformId);
					if (scheduleEntries == null) {
						packet.writeInt(0);
					} else {
						packet.writeInt(scheduleEntries.size());
						scheduleEntries.forEach(scheduleEntry -> scheduleEntry.writePacket(packet));
					}
				});

				packet.writeInt(signalBlockStatus.size());
				signalBlockStatus.forEach((id, occupied) -> {
					packet.writeLong(id);
					packet.writeBoolean(occupied);
				});

				packet.writeInt(occupiedRails.size());
				occupiedRails.forEach((rail, occupied) -> {
					packet.writeUUID(rail);
					packet.writeBoolean(occupied);
				});

				if (packet.readableBytes() <= Networking.MAX_PACKET_BYTES) {
					MTRRegistry.sendToPlayer((ServerPlayer) player, Networking.PACKET_UPDATE_SCHEDULE, packet);
				}
			}
		}

		if (prevPlatformCount != platforms.size() || prevSidingCount != sidings.size()) {
			dataCache.sync();
		}
		prevPlatformCount = platforms.size();
		prevSidingCount = sidings.size();

		for(RailwayDataModule railwayDataModule : modules.values()) {
			railwayDataModule.lateTick();
		}
	}

	public void onPlayerJoin(ServerPlayer serverPlayer) {
		PacketTrainDataGuiServer.sendAllInChunks(serverPlayer, stations, platforms, sidings, routes, depots, lifts, signalBlocks);

		for(RailwayDataModule railwayDataModule : modules.values()) {
			if(railwayDataModule instanceof MTRPlayerConnectionEvent evt) {
				evt.onPlayerConnect(serverPlayer);
			}
		}
	}

	// writing data

	public long addRail(Player player, TransportMode transportMode, BlockPos posStart, BlockPos posEnd, Rail rail, boolean validate) {
		final RailwayDataLoggingModule loggingModule = getModule(RailwayDataLoggingModule.NAME);
		final long newId = validate ? new Random().nextLong() : 0;
		addRail(rails, platforms, sidings, transportMode, posStart, posEnd, rail, newId);

		if (validate) {
			final Rail backwardsRail = DataCache.tryGet(rails, posEnd, posStart);
			if (backwardsRail != null) {
				final List<String> dataList = new ArrayList<>();
				dataList.add(String.format("type:%s", rail.railType));
				dataList.add(String.format("one_way:%s", backwardsRail.railType == RailType.NONE));
				loggingModule.addEvent((ServerPlayer) player, Rail.class, new ArrayList<>(), dataList, posStart, posEnd);
			}
			validateData();
		}

		return newId;
	}

	public long addSignal(Player player, DyeColor color, BlockPos posStart, BlockPos posEnd) {
		final RailwayDataLoggingModule loggingModule = getModule(RailwayDataLoggingModule.NAME);
		loggingModule.addEvent((ServerPlayer) player, SignalBlocks.SignalBlock.class, new ArrayList<>(), Collections.singletonList(String.format("color:%s", color)), posStart, posEnd);
		return signalBlocks.add(0, color, PathData.getRailProduct(posStart, posEnd));
	}

	public void removeNode(Player player, BlockPos pos, TransportMode transportMode) {
		RailwayDataLoggingModule loggingModule = getModule(RailwayDataLoggingModule.NAME);
		loggingModule.addEvent((ServerPlayer) player, BlockNode.class, Collections.singletonList(String.format("type:%s", transportMode)), new ArrayList<>(), pos);
		removeNode(level, rails, pos);
		validateData();
		final FriendlyByteBuf packet = signalBlocks.getValidationPacket(rails);
		if (packet != null) {
			level.players().forEach(player2 -> MTRRegistry.sendToPlayer((ServerPlayer) player2, Networking.PACKET_REMOVE_SIGNALS, packet));
		}
	}

	public void removeRailConnection(Player player, BlockPos pos1, BlockPos pos2) {
		final Rail rail1 = DataCache.tryGet(rails, pos1, pos2);
		final Rail rail2 = DataCache.tryGet(rails, pos2, pos1);
		final RailwayDataLoggingModule loggingModule = getModule(RailwayDataLoggingModule.NAME);
		if (rail1 != null && rail2 != null) {
			final List<String> dataList = new ArrayList<>();
			dataList.add(String.format("type:%s", (rail1.railType == RailType.NONE ? rail2 : rail1).railType));
			dataList.add(String.format("one_way:%s", (rail1.railType == RailType.NONE ? rail1 : rail2).railType == RailType.NONE));
			loggingModule.addEvent((ServerPlayer) player, Rail.class, dataList, new ArrayList<>(), pos1, pos2);
		}

		removeRailConnection(level, rails, pos1, pos2);
		validateData();
		final FriendlyByteBuf packet = signalBlocks.getValidationPacket(rails);
		if (packet != null) {
			level.players().forEach(player2 -> MTRRegistry.sendToPlayer((ServerPlayer) player2, Networking.PACKET_REMOVE_SIGNALS, packet));
		}
	}

	public void onAreaUpdated() {
		for(RailwayDataModule railwayDataModule : modules.values()) {
			if(railwayDataModule instanceof MTRAreaUpdateEvent evt) {
				evt.onAreaUpdate();
			}
		}
	}

	public void removeLiftFloorTrack(BlockPos pos) {
		removeLiftFloorTrack(level, lifts, pos);
		dataCache.sync();
	}

	public boolean hasSavedRail(BlockPos pos) {
		return rails.containsKey(pos) && rails.get(pos).values().stream().anyMatch(rail -> rail.railType.hasSavedRail);
	}

	public boolean containsRail(BlockPos pos1, BlockPos pos2) {
		return containsRail(rails, pos1, pos2);
	}

	public long removeSignal(Player player, DyeColor color, BlockPos posStart, BlockPos posEnd) {
		final RailwayDataLoggingModule loggingModule = getModule(RailwayDataLoggingModule.NAME);
		loggingModule.addEvent((ServerPlayer) player, SignalBlocks.SignalBlock.class, Collections.singletonList(String.format("color:%s", color)), new ArrayList<>(), posStart, posEnd);
		return signalBlocks.remove(0, color, PathData.getRailProduct(posStart, posEnd));
	}

	public void disconnectPlayer(Player player) {
		for(RailwayDataModule railwayDataModule : modules.values()) {
			if(railwayDataModule instanceof MTRPlayerConnectionEvent evt) {
				evt.onPlayerDisconnect(player);
			}
		}
		playerLastUpdatedPositions.remove(player);
	}



	public <T extends RailwayDataModule> T getModule(String key) {
		RailwayDataModule module = modules.get(key);
		if(module == null) throw new IllegalArgumentException("Railway module \"" + key + "\" doesn't exist!");
		return (T)module;
	}

	public boolean hasModule(String key) {
		return modules.containsKey(key);
	}

	private void addModule(String key, RailwayDataModule module) {
		if(modules.containsKey(key)) throw new IllegalStateException("Railway module \"" + key + "\" has already been added!");
		modules.put(key, module);
	}

	public static void registerRailwayModule(RailwayDataModuleCallback createRailwayModule) {
		railwayModulesCallbacks.add(createRailwayModule);
	}



	public void getSchedulesForStation(Map<Long, List<ScheduleEntry>> schedulesForStation, long stationId) {
		schedulesForPlatform.forEach((platformId, schedules) -> {
			final Station station = dataCache.platformIdToStation.get(platformId);
			if (station != null && station.id == stationId) {
				schedulesForStation.put(platformId, schedules);
			}
		});
	}

	public List<ScheduleEntry> getSchedulesAtPlatform(long platformId) {
		return schedulesForPlatform.get(platformId);
	}

	public Map<Long, Map<BlockPos, TrainDelay>> getTrainDelays() {
		return trainDelays;
	}

	public void resetTrainDelays(Depot depot) {
		depot.routeIds.forEach(trainDelays::remove);
	}

	private void validateData() {
		removeSavedRailS2C(level, platforms, rails, Networking.PACKET_DELETE_PLATFORM);
		removeSavedRailS2C(level, sidings, rails, Networking.PACKET_DELETE_SIDING);

		final List<BlockPos> railsToRemove = new ArrayList<>();
		rails.forEach((startPos, railMap) -> railMap.forEach((endPos, rail) -> {
			if (rail.railType.hasSavedRail && SavedRailBase.isInvalidSavedRail(rails, endPos, startPos)) {
				railsToRemove.add(startPos);
				railsToRemove.add(endPos);
			}
		}));
		for (int i = 0; i < railsToRemove.size() - 1; i += 2) {
			removeRailConnection(null, rails, railsToRemove.get(i), railsToRemove.get(i + 1));
		}
	}

	// static finders

	public static Platform getPlatformByPos(Set<Platform> platforms, BlockPos pos) {
		try {
			return platforms.stream().filter(platform -> platform.containsPos(pos)).findFirst().orElse(null);
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
			return null;
		}
	}

	// other

	public static void addRail(Map<BlockPos, Map<BlockPos, Rail>> rails, Set<Platform> platforms, Set<Siding> sidings, TransportMode transportMode, BlockPos posStart, BlockPos posEnd, Rail rail, long savedRailId) {
		try {
			if (!rails.containsKey(posStart)) {
				rails.put(posStart, new HashMap<>());
			}
			rails.get(posStart).put(posEnd, rail);

			if (savedRailId != 0) {
				if (rail.railType == RailType.PLATFORM && platforms.stream().noneMatch(platform -> platform.containsPos(posStart) || platform.containsPos(posEnd))) {
					platforms.add(new Platform(savedRailId, transportMode, posStart, posEnd));
				} else if (rail.railType == RailType.SIDING && sidings.stream().noneMatch(siding -> siding.containsPos(posStart) || siding.containsPos(posEnd))) {
					sidings.add(new Siding(savedRailId, transportMode, posStart, posEnd, (int) Math.floor(rail.getLength())));
				}
			}
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	public static void removeNode(Level world, Map<BlockPos, Map<BlockPos, Rail>> rails, BlockPos pos) {
		try {
			rails.remove(pos);
			rails.forEach((startPos, railMap) -> {
				railMap.remove(pos);
				if (railMap.isEmpty() && world != null) {
					BlockNode.resetRailNode(world, startPos);
				}
			});
			if (world != null) {
				validateRails(world, rails);
			}
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	public static <T extends Lift> void removeLiftFloorTrack(Level world, Set<T> lifts, BlockPos pos) {
		lifts.removeIf(lift -> lift.hasFloor(pos));
		if (world != null) {
			validateLifts(world, lifts);
		}
	}

	public static void removeRailConnection(Level world, Map<BlockPos, Map<BlockPos, Rail>> rails, BlockPos pos1, BlockPos pos2) {
		try {
			if (rails.containsKey(pos1)) {
				rails.get(pos1).remove(pos2);
				if (rails.get(pos1).isEmpty() && world != null) {
					BlockNode.resetRailNode(world, pos1);
				}
			}
			if (rails.containsKey(pos2)) {
				rails.get(pos2).remove(pos1);
				if (rails.get(pos2).isEmpty() && world != null) {
					BlockNode.resetRailNode(world, pos2);
				}
			}
			if (world != null) {
				validateRails(world, rails);
			}
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	public static boolean containsRail(Map<BlockPos, Map<BlockPos, Rail>> rails, BlockPos pos1, BlockPos pos2) {
		return rails.containsKey(pos1) && rails.get(pos1).containsKey(pos2);
	}

	public static Station getStation(Set<Station> stations, DataCache dataCache, BlockPos pos) {
		try {
			if (dataCache.blockPosToStation.containsKey(pos)) {
				return dataCache.blockPosToStation.get(pos);
			} else {
				return stations.stream().filter(station -> station.inArea(pos.getX(), pos.getZ())).findFirst().orElse(null);
			}
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
			return null;
		}
	}

	public static long getClosePlatformId(Set<Platform> platforms, DataCache dataCache, BlockPos pos) {
		return getClosePlatformId(platforms, dataCache, pos, 5, 0, 4);
	}

	public static long getClosePlatformId(Set<Platform> platforms, DataCache dataCache, BlockPos pos, int radius, int lower, int upper) {
		try {
			final long posLong = pos.asLong();
			if (dataCache.blockPosToPlatformId.containsKey(posLong)) {
				return dataCache.blockPosToPlatformId.get(posLong);
			} else {
				long platformId = 0;
				for (int i = 1; i <= radius; i++) {
					final int searchRadius = i;
					platformId = platforms.stream().filter(platform -> platform.isCloseToSavedRail(pos, searchRadius, lower, upper)).min(Comparator.comparingInt(platform -> platform.getMidPos().distManhattan(pos))).map(platform -> platform.id).orElse(0L);
					if (platformId != 0) {
						break;
					}
				}
				dataCache.blockPosToPlatformId.put(posLong, platformId);
				return platformId;
			}
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
			return 0;
		}
	}

	public static boolean useRoutesAndStationsFromIndex(int stopIndex, List<Long> routeIds, DataCache dataCache, RouteAndStationsCallback routeAndStationsCallback) {
		if (stopIndex < 0) {
			return false;
		}

		int sum = 0;
		for (int i = 0; i < routeIds.size(); i++) {
			final Route thisRoute = dataCache.routeIdMap.get(routeIds.get(i));
			final Route nextRoute = i < routeIds.size() - 1 && !dataCache.routeIdMap.get(routeIds.get(i + 1)).isHidden ? dataCache.routeIdMap.get(routeIds.get(i + 1)) : null;
			if (thisRoute != null) {
				final int difference = stopIndex - sum;
				sum += thisRoute.platformIds.size();
				if (!thisRoute.platformIds.isEmpty() && nextRoute != null && !nextRoute.platformIds.isEmpty() && thisRoute.getLastPlatformId() == nextRoute.getFirstPlatformId()) {
					sum--;
				}
				if (stopIndex < sum) {
					final Station thisStation = dataCache.platformIdToStation.get(thisRoute.platformIds.get(difference).platformId);
					final Station nextStation = difference < thisRoute.platformIds.size() - 1 ? dataCache.platformIdToStation.get(thisRoute.platformIds.get(difference + 1).platformId) : null;
					final Station lastStation = thisRoute.platformIds.isEmpty() ? null : dataCache.platformIdToStation.get(thisRoute.getLastPlatformId());
					routeAndStationsCallback.routeAndStationsCallback(difference, thisRoute, nextRoute, thisStation, nextStation, lastStation);
					return true;
				}
			}
		}
		return false;
	}

	public static void writeMessagePackDataset(MessagePacker messagePacker, Collection<? extends SerializedDataBase> dataSet, String key) throws IOException {
		messagePacker.packString(key);
		messagePacker.packArrayHeader(dataSet.size());
		for (final SerializedDataBase data : dataSet) {
			messagePacker.packMapHeader(data.messagePackLength());
			data.toMessagePack(messagePacker);
		}
	}

	public static Map<String, Value> castMessagePackValueToSKMap(Value value) {
		final Map<Value, Value> oldMap = value == null ? new HashMap<>() : value.asMapValue().map();
		final HashMap<String, Value> resultMap = new HashMap<>(oldMap.size());
		oldMap.forEach((key, newValue) -> resultMap.put(key.asStringValue().asString(), newValue));
		return resultMap;
	}

	public static RailwayData getInstance(Level world) {
		return getInstance(world, () -> new RailwayData(world), NAME);
	}

	public static void benchmark(Runnable runnable, float threshold) {
		final long nanos = System.nanoTime();
		runnable.run();
		final float duration = (System.nanoTime() - nanos) / 1000000000F;
		if (duration >= threshold) {
			System.out.println(duration);
		}
	}

	private static void validateRails(Level world, Map<BlockPos, Map<BlockPos, Rail>> rails) {
		final Set<BlockPos> railsToRemove = new HashSet<>();
		final Set<BlockPos> railsNodesToRemove = new HashSet<>();
		rails.forEach((startPos, railMap) -> {
			final boolean chunkLoaded = BlockUtil.chunkLoaded(world, startPos);
			if (chunkLoaded && !(world.getBlockState(startPos).getBlock() instanceof BlockNode)) {
				railsNodesToRemove.add(startPos);
			}

			if (railMap.isEmpty()) {
				railsToRemove.add(startPos);
			}
		});
		railsToRemove.forEach(rails::remove);
		railsNodesToRemove.forEach(pos -> removeNode(null, rails, pos));
	}

	private static <T extends Lift> void validateLifts(Level world, Set<T> lifts) {
		lifts.removeIf(lift -> lift.isInvalidLift(world));
	}

	private static void removeSavedRailS2C(Level world, Set<? extends SavedRailBase> savedRailBases, Map<BlockPos, Map<BlockPos, Rail>> rails, ResourceLocation packetId) {
		savedRailBases.removeIf(savedRailBase -> {
			final boolean delete = savedRailBase.isInvalidSavedRail(rails);
			if (delete) {
				final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
				packet.writeLong(savedRailBase.id);
				world.players().forEach(player -> MTRRegistry.sendToPlayer((ServerPlayer) player, packetId, packet));
			}
			return delete;
		});
	}

	// TODO temporary code start
	private static Map<String, Value> readMessagePackSKMap(MessageUnpacker messageUnpacker) throws IOException {
		final int size = messageUnpacker.unpackMapHeader();
		final HashMap<String, Value> result = new HashMap<>(size);
		for (int i = 0; i < size; ++i) {
			result.put(messageUnpacker.unpackString(), messageUnpacker.unpackValue());
		}
		return result;
	}

	@Deprecated
	private static class RailEntry extends SerializedDataBase {

		public final BlockPos pos;
		public final Map<BlockPos, Rail> connections;

		private static final String KEY_NODE_POS = "node_pos";
		private static final String KEY_RAIL_CONNECTIONS = "rail_connections";

		public RailEntry(BlockPos pos, Map<BlockPos, Rail> connections) {
			this.pos = pos;
			this.connections = connections;
		}

		public RailEntry(CompoundTag compoundTag) {
			pos = BlockPos.of(compoundTag.getLong(KEY_NODE_POS));
			connections = new HashMap<>();

			final CompoundTag tagConnections = compoundTag.getCompound(KEY_RAIL_CONNECTIONS);
			for (final String keyConnection : tagConnections.getAllKeys()) {
				connections.put(BlockPos.of(tagConnections.getCompound(keyConnection).getLong(KEY_NODE_POS)), new Rail(tagConnections.getCompound(keyConnection)));
			}
		}

		public RailEntry(Map<String, Value> map) {
			final MessagePackHelper messagePackHelper = new MessagePackHelper(map);
			pos = BlockPos.of(messagePackHelper.getLong(KEY_NODE_POS));
			connections = new HashMap<>();
			messagePackHelper.iterateArrayValue(KEY_RAIL_CONNECTIONS, value -> {
				final Map<String, Value> mapSK = RailwayData.castMessagePackValueToSKMap(value);
				connections.put(BlockPos.of(new MessagePackHelper(mapSK).getLong(KEY_NODE_POS)), new Rail(mapSK));
			});
		}

		@Override
		public void toMessagePack(MessagePacker messagePacker) throws IOException {
			messagePacker.packString(KEY_NODE_POS).packLong(pos.asLong());

			messagePacker.packString(KEY_RAIL_CONNECTIONS).packArrayHeader(connections.size());
			for (final Map.Entry<BlockPos, Rail> entry : connections.entrySet()) {
				final BlockPos endNodePos = entry.getKey();
				messagePacker.packMapHeader(entry.getValue().messagePackLength() + 1);
				messagePacker.packString(KEY_NODE_POS).packLong(endNodePos.asLong());
				entry.getValue().toMessagePack(messagePacker);
			}
		}

		@Override
		public int messagePackLength() {
			return 2;
		}

		@Override
		public void writePacket(FriendlyByteBuf packet) {
		}
	}
	// TODO temporary code end

	@FunctionalInterface
	public interface RouteAndStationsCallback {
		void routeAndStationsCallback(int currentStationIndex, Route thisRoute, Route nextRoute, Station thisStation, Station nextStation, Station lastStation);
	}

	@FunctionalInterface
	public interface RailwayDataModuleCallback {
		RailwayDataModule getModule(RailwayData railwayData, Level level, Map<BlockPos, Map<BlockPos, Rail>> rails);
	}
}
