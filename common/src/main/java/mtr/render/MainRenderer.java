package mtr.render;

import cn.zbx1425.mtrsteamloco.game.TrainVirtualDrive;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.MTRClient;
import mtr.block.BlockNode;
import mtr.block.BlockPlatform;
import mtr.block.BlockSignalLightBase;
import mtr.block.BlockSignalSemaphoreBase;
import mtr.client.*;
import mtr.data.*;
import mtr.item.ItemNodeModifierBase;
import mtr.item.ItemRailModifier;
import mtr.mappings.Text;
import mtr.mappings.Utilities;
import mtr.mappings.UtilitiesClient;
import mtr.path.PathData;
import mtr.util.BlockUtil;
import mtr.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MainRenderer implements IGui {

	public static int maxTrainRenderDistance;
	public static ResourcePackCreatorProperties creatorProperties = new ResourcePackCreatorProperties();

	private static double lastSimulatedTick;
	private static float newLastFrameDuration;

	private static int prevPlatformCount;
	private static int prevSidingCount;
	private static UUID renderedUuid;

	public static final int PLAYER_RENDER_OFFSET = 1000;

	public static final Set<String> AVAILABLE_TEXTURES = new HashSet<>();
	public static final Set<String> UNAVAILABLE_TEXTURES = new HashSet<>();

	public static final int DETAIL_RADIUS = 32;
	public static final int DETAIL_RADIUS_SQUARED = DETAIL_RADIUS * DETAIL_RADIUS;
	public static final int LIFT_LIGHT_COLOR = 0xFFFF0000;
	private static final int MAX_RADIUS_REPLAY_MOD = 64 * 16;
	private static final int TICKS_PER_SECOND = 20;
	private static final int DISMOUNT_PROGRESS_BAR_LENGTH = 30;
	private static final int TOTAL_RENDER_STAGES = 2;
	private static final List<List<Map<ResourceLocation, Set<BiConsumer<PoseStack, VertexConsumer>>>>> RENDERS = new ArrayList<>(TOTAL_RENDER_STAGES);
	private static final List<List<Map<ResourceLocation, Set<BiConsumer<PoseStack, VertexConsumer>>>>> CURRENT_RENDERS = new ArrayList<>(TOTAL_RENDER_STAGES);
	private static final ResourceLocation LIFT_TEXTURE = ResourceLocation.parse("mtr:textures/entity/lift_1.png");
	private static final ResourceLocation ARROW_TEXTURE = ResourceLocation.parse("mtr:textures/block/sign/lift_arrow.png");

	static {
		for (int i = 0; i < TOTAL_RENDER_STAGES; i++) {
			final int renderStageCount = QueuedRenderLayer.values().length;
			final List<Map<ResourceLocation, Set<BiConsumer<PoseStack, VertexConsumer>>>> rendersList = new ArrayList<>(renderStageCount);
			final List<Map<ResourceLocation, Set<BiConsumer<PoseStack, VertexConsumer>>>> currentRendersList = new ArrayList<>(renderStageCount);

			for (int j = 0; j < renderStageCount; j++) {
				rendersList.add(j, new HashMap<>());
				currentRendersList.add(j, new HashMap<>());
			}

			RENDERS.add(i, rendersList);
			CURRENT_RENDERS.add(i, currentRendersList);
		}
	}

	public static void simulate() {
		final Minecraft client = Minecraft.getInstance();
		final LocalPlayer player = client.player;
		final Level world = client.level;
		if (world == null) return;
		final float lastFrameDuration = MTRClient.getLastFrameDuration();
		newLastFrameDuration = client.isPaused() || lastSimulatedTick == MTRClient.getGameTick() ? 0 : lastFrameDuration;
		final boolean useAnnouncements = Config.useTTSAnnouncements() || Config.showAnnouncementMessages();

		ClientData.TRAINS.forEach(train -> train.simulateTrain(world, newLastFrameDuration, (speed, stopIndex, routeIds) -> {
			final Route thisRoute = train.getThisRoute();
			final Station thisStation = train.getThisStation();
			final Station nextStation = train.getNextStation();
			final Station lastStation = train.getLastStation();

			if (showShiftProgressBar() && (!train.isCurrentlyManual() || !Train.isHoldingKey(player))) {
				if (train.getDoorValue() == 0 || thisRoute == null || thisStation == null || lastStation == null) {
					if (!(train instanceof TrainVirtualDrive)) { // TODO something more elegant
						player.displayClientMessage(Text.translatable("gui.mtr.vehicle_speed", Util.round(speed, 1), Util.round(speed * 3.6F, 1)), true);
					}
				} else {
					final Component text;
					switch ((int) ((net.minecraft.Util.getMillis() / 1000) % 3)) {
						default:
							text = getStationText(thisStation, "this");
							break;
						case 1:
							if (nextStation == null) {
								text = getStationText(thisStation, "this");
							} else {
								text = getStationText(nextStation, "next");
							}
							break;
						case 2:
							text = getStationText(lastStation, "last_" + thisRoute.transportMode.toString().toLowerCase(Locale.ENGLISH));
							break;
					}
					player.displayClientMessage(text, true);
				}
			}
		}, (stopIndex, routeIds) -> {
			final Route thisRoute = train.getThisRoute();
			final Route nextRoute = train.getNextRoute();
			final Station nextStation = train.getNextStation();
			final Station lastStation = train.getLastStation();

			if (useAnnouncements && thisRoute != null && nextStation != null && !thisRoute.disableNextStationAnnouncements) {
				final List<String> messages = new ArrayList<>();

				final boolean isLightRailRoute = thisRoute.isLightRailRoute;
				messages.add(IGui.insertTranslation(isLightRailRoute ? "gui.mtr.next_station_light_rail_announcement_cjk" : "gui.mtr.next_station_announcement_cjk", isLightRailRoute ? "gui.mtr.next_station_light_rail_announcement" : "gui.mtr.next_station_announcement", 1, nextStation.name));

				final String mergedInterchangeRoutes = getInterchangeRouteNames(nextStation, thisRoute, nextRoute);
				if (!mergedInterchangeRoutes.isEmpty()) {
					messages.add(IGui.insertTranslation("gui.mtr.interchange_announcement_cjk", "gui.mtr.interchange_announcement", 1, mergedInterchangeRoutes));
				}

				final List<String> connectingStationList = new ArrayList<>();
				ClientData.DATA_CACHE.stationIdToConnectingStations.get(nextStation).forEach(connectingStation -> {
					final String connectingStationMergedInterchangeRoutes = getInterchangeRouteNames(connectingStation, thisRoute, nextRoute);
					if (!connectingStationMergedInterchangeRoutes.isEmpty()) {
						connectingStationList.add(IGui.insertTranslation("gui.mtr.connecting_station_interchange_announcement_part_cjk", "gui.mtr.connecting_station_interchange_announcement_part", 2, connectingStationMergedInterchangeRoutes, connectingStation.name));
					}
				});
				if (!connectingStationList.isEmpty()) {
					messages.add(IGui.insertTranslation("gui.mtr.connecting_station_part_cjk", "gui.mtr.connecting_station_part", 1, IGui.mergeStationsWithCommas(connectingStationList)));
				}

				final String thisRouteSplit = thisRoute.name.split("\\|\\|")[0];
				final String nextRouteSplit = nextRoute == null ? null : nextRoute.name.split("\\|\\|")[0];
				if (lastStation != null && nextStation.id == lastStation.id && nextRoute != null && !nextRoute.platformIds.isEmpty() && !nextRouteSplit.equals(thisRouteSplit)) {
					final Station nextFinalStation = ClientData.DATA_CACHE.platformIdToStation.get(nextRoute.getLastPlatformId());
					if (nextFinalStation != null) {
						final String modeString = thisRoute.transportMode.toString().toLowerCase(Locale.ENGLISH);
						if (nextRoute.isLightRailRoute) {
							messages.add(IGui.insertTranslation("gui.mtr.next_route_" + modeString + "_light_rail_announcement_cjk", "gui.mtr.next_route_" + modeString + "_light_rail_announcement", nextRoute.lightRailRouteNumber, 1, nextFinalStation.name.split("\\|\\|")[0]));
						} else {
							messages.add(IGui.insertTranslation("gui.mtr.next_route_" + modeString + "_announcement_cjk", "gui.mtr.next_route_" + modeString + "_announcement", 2, nextRouteSplit, nextFinalStation.name.split("\\|\\|")[0]));
						}
					}
				}

				IDrawing.narrateOrAnnounce(IGui.mergeStations(messages, "", " "));
			}
		}, (stopIndex, routeIds) -> {
			final Route thisRoute = train.getThisRoute();
			final Station lastStation = train.getLastStation();

			if (useAnnouncements && thisRoute != null && thisRoute.isLightRailRoute && lastStation != null) {
				IDrawing.narrateOrAnnounce(IGui.insertTranslation("gui.mtr.light_rail_route_announcement_cjk", "gui.mtr.light_rail_route_announcement", thisRoute.lightRailRouteNumber, 1, lastStation.name));
			}
		}));

		ClientData.LIFTS.forEach(lift -> lift.tickClient(world, newLastFrameDuration));
	}

	public static void render(float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource) {
		final Minecraft client = Minecraft.getInstance();

		final LocalPlayer player = client.player;
		final Level world = client.level;

		if (player == null || world == null) {
			return;
		}

		final int renderDistanceChunks = UtilitiesClient.getRenderDistance();
		maxTrainRenderDistance = renderDistanceChunks * (Config.trainRenderDistanceRatio() + 1);

		final Vec3 cameraPos = client.gameRenderer.getMainCamera().getPosition();

		poseStack.pushPose();
		poseStack.translate(-cameraPos.x(), -cameraPos.y(), -cameraPos.z());
		renderAbsolute(client, client.player, client.level, tickDelta, poseStack, bufferSource);
		poseStack.popPose();

		poseStack.pushPose();
		renderRelative(client, client.player, client.level, tickDelta, poseStack, bufferSource);
		poseStack.popPose();

		// Clean up
		if (prevPlatformCount != ClientData.PLATFORMS.size() || prevSidingCount != ClientData.SIDINGS.size()) {
			ClientData.DATA_CACHE.sync();
		}
		prevPlatformCount = ClientData.PLATFORMS.size();
		prevSidingCount = ClientData.SIDINGS.size();
		ClientData.DATA_CACHE.clearDataIfNeeded();
		lastSimulatedTick = MTRClient.getGameTick();
	}

	public static void renderRelative(Minecraft client, LocalPlayer player, Level level, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource) {
		TrainRendererBase.setupStaticInfo(poseStack, bufferSource, tickDelta);
		TrainRendererBase.setBatch(false);
		renderTrains(level, tickDelta, poseStack, bufferSource);
		renderLifts(level, tickDelta, poseStack, bufferSource);
		doBatchedRendering(poseStack, bufferSource);
	}

	private static void renderLifts(Level level, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource) {
		ClientData.LIFTS.forEach(lift -> lift.render(level, (x, y, z, frontDoorValue, backDoorValue) -> {
			final BlockPos posAverage = TrainRendererBase.applyAverageTransform(x, y, z);
			if (posAverage == null) {
				return;
			}

			MainRenderer.transformRelativeToCamera(poseStack, x, y, z);
			UtilitiesClient.rotateXDegrees(poseStack, 180);
			UtilitiesClient.rotateYDegrees(poseStack, 180 + lift.facing.toYRot());
			final int light = LightTexture.pack(level.getBrightness(LightLayer.BLOCK, posAverage), level.getBrightness(LightLayer.SKY, posAverage));
			lift.getModel().render(poseStack, bufferSource, lift, LIFT_TEXTURE, light, frontDoorValue, backDoorValue, false, 0, 1, false, true, false, false, false);

			for (int i = 0; i < (lift.isDoubleSided ? 2 : 1); i++) {
				UtilitiesClient.rotateYDegrees(poseStack, 180);
				poseStack.pushPose();
				poseStack.translate(0.875F, -1.5, lift.liftDepth / 2F - 0.25 - SMALL_OFFSET);
				renderLiftDisplay(poseStack, bufferSource, posAverage, ClientData.DATA_CACHE.requestLiftFloorText(lift.getCurrentFloorBlockPos())[0], lift.getLiftDirection(), 0.1875F, 0.3125F);
				poseStack.popPose();
			}

			poseStack.popPose();
		}, newLastFrameDuration));
	}

	private static void renderTrains(Level level, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource) {
		ClientData.TRAINS.forEach(train -> train.renderTrain(level, newLastFrameDuration));
		if (!Config.hideTranslucentParts()) {
			TrainRendererBase.setBatch(true);
			ClientData.TRAINS.forEach(TrainClient::renderTranslucent);
		}
	}

	public static void renderAbsolute(Minecraft client, LocalPlayer player, Level level, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource) {
		poseStack.pushPose();

		TrainRendererBase.setupStaticInfo(poseStack, bufferSource, tickDelta);
		TrainRendererBase.setBatch(false);

		final boolean renderColors = isHoldingRailRelated(player);
		final int maxRailDistance = client.options.renderDistance().get() * 16;
		final Map<UUID, RailType> renderedRailMap = new HashMap<>();
		ClientData.RAILS.forEach((startPos, railMap) -> railMap.forEach((endPos, rail) -> {
			if (!Util.isBetween(player.getX(), startPos.getX(), endPos.getX(), maxRailDistance) || !Util.isBetween(player.getZ(), startPos.getZ(), endPos.getZ(), maxRailDistance)) {
				return;
			}

			final UUID railProduct = PathData.getRailProduct(startPos, endPos);
			if (renderedRailMap.containsKey(railProduct)) {
				if (renderedRailMap.get(railProduct) == rail.railType) {
					return;
				}
			} else {
				renderedRailMap.put(railProduct, rail.railType);
			}

			switch (rail.transportMode) {
				case TRAIN:
					renderRailStandard(level, rail, 0.0625F + SMALL_OFFSET, renderColors, 1);
					if (renderColors) {
						renderSignalsStandard(level, poseStack, bufferSource, rail, startPos, endPos);
					}
					break;
				case BOAT:
					if (renderColors) {
						renderRailStandard(level, rail, 0.0625F + SMALL_OFFSET, true, 0.5F);
						renderSignalsStandard(level, poseStack, bufferSource, rail, startPos, endPos);
					}
					break;
				case CABLE_CAR:
					if (rail.railType.hasSavedRail || rail.railType == RailType.CABLE_CAR_STATION) {
						renderRailStandard(level, rail, 0.25F + SMALL_OFFSET, renderColors, 0.25F, "mtr:textures/block/metal.png", 0.25F, 0, 0.75F, 1);
					}
					if (renderColors && !rail.railType.hasSavedRail) {
						renderRailStandard(level, rail, 0.5F + SMALL_OFFSET, true, 1, "mtr:textures/block/one_way_rail_arrow.png", 0, 0.75F, 1, 0.25F);
					}

					if (rail.railType != RailType.NONE) {
						rail.render((x1, z1, x2, z2, x3, z3, x4, z4, y1, y2) -> {
							final int r = renderColors ? (rail.railType.color >> 16) & 0xFF : 0;
							final int g = renderColors ? (rail.railType.color >> 8) & 0xFF : 0;
							final int b = renderColors ? rail.railType.color & 0xFF : 0;
							IDrawing.drawLine(poseStack, bufferSource, (float) x1, (float) y1 + 0.5F, (float) z1, (float) x3, (float) y2 + 0.5F, (float) z3, r, g, b);
						}, 0, 0);
					}

					break;
				case AIRPLANE:
					if (renderColors) {
						renderRailStandard(level, rail, 0.0625F + SMALL_OFFSET, true, 1);
						renderSignalsStandard(level, poseStack, bufferSource, rail, startPos, endPos);
					} else {
						renderRailStandard(level, rail, 0.0625F + SMALL_OFFSET, false, 0.25F, "textures/block/iron_block.png", 0.25F, 0, 0.75F, 1);
					}
					break;
			}
		}));

		ItemStack playerHolding = player.getMainHandItem();
		if (playerHolding.getItem() instanceof ItemRailModifier) {
			renderRailPreview(level, player, playerHolding);
		}

		poseStack.popPose();
	}

	private static void doBatchedRendering(PoseStack poseStack, MultiBufferSource bufferSource) {
		if (lastSimulatedTick != MTRClient.getGameTick()) {
			for (int i = 0; i < TOTAL_RENDER_STAGES; i++) {
				for (int j = 0; j < QueuedRenderLayer.values().length; j++) {
					CURRENT_RENDERS.get(i).get(j).clear();
					CURRENT_RENDERS.get(i).get(j).putAll(RENDERS.get(i).get(j));
					RENDERS.get(i).get(j).clear();
				}
			}
		}

		for (int i = 0; i < TOTAL_RENDER_STAGES; i++) {
			for (int j = 0; j < QueuedRenderLayer.values().length; j++) {
				final QueuedRenderLayer queuedRenderLayer = QueuedRenderLayer.values()[j];
				CURRENT_RENDERS.get(i).get(j).forEach((key, value) -> {
					final RenderType renderType;
					switch (queuedRenderLayer) {
						case LIGHT:
							renderType = MoreRenderLayers.getLight(key, false);
							break;
						case LIGHT_TRANSLUCENT:
							renderType = MoreRenderLayers.getLight(key, true);
							break;
						case INTERIOR:
							renderType = MoreRenderLayers.getInterior(key);
							break;
						default:
							renderType = MoreRenderLayers.getExterior(key);
							break;
					}
					final VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
					value.forEach(renderer -> renderer.accept(poseStack, vertexConsumer));
				});
			}
		}
	}

	private static void renderRailPreview(Level level, Player localPlayer, ItemStack playerHolding) {
		final ItemRailModifier itemRailModifier = (ItemRailModifier) playerHolding.getItem();
		BlockPos posEnd = itemRailModifier.getFirstBlockPos(playerHolding);
		HitResult hitResult = Minecraft.getInstance().hitResult;

		if(posEnd != null && hitResult instanceof BlockHitResult blockHitResult) {
			final BlockPos startPosRaw = blockHitResult.getBlockPos();
			final BlockState stateStart = level.getBlockState(startPosRaw);
			final BlockPos posStart = stateStart.getBlock() instanceof BlockNode ? startPosRaw : startPosRaw.above();
			final BlockState stateEnd = level.getBlockState(posEnd);
			float angle1 = stateStart.getBlock() instanceof BlockNode ? BlockNode.getAngle(stateStart) : localPlayer.getYRot() + 90;
			float angle2 = BlockNode.getAngle(stateEnd);

			final float angleDifference = (float) Math.toDegrees(Math.atan2(posEnd.getZ() - posStart.getZ(), posEnd.getX() - posStart.getX()));

			final RailAngle railAngleStart = RailAngle.fromAngle(angle1 + (RailAngle.similarFacing(angleDifference, angle1) ? 0 : 180));
			final RailAngle railAngleEnd = RailAngle.fromAngle(angle2 + (RailAngle.similarFacing(angleDifference, angle2) ? 180 : 0));

			final ItemRailModifier.RailConnectionResult railConnectionResult = itemRailModifier.getRails(TransportMode.TRAIN, posStart, itemRailModifier.getFirstBlockPos(playerHolding), stateStart, stateEnd, railAngleStart, railAngleEnd);
			if(railConnectionResult.failMessage() != null) {
				localPlayer.displayClientMessage(railConnectionResult.failMessage().withStyle(ChatFormatting.RED), true);
			} else {
				// TODO: Maybe we should draw our own HUD instead to free up the action bar for something else
				// Make old error disappear
				localPlayer.displayClientMessage(Component.empty(), true);

				Rail rail1 = railConnectionResult.oppositeRail();
				Rail rail2 = railConnectionResult.rail();
				renderRailStandard(level, rail1, 0, true, 1);
				renderRailStandard(level, rail2, 0, true, 1);
			}
		}
	}

	public static boolean shouldNotRender(BlockPos pos, int maxDistance, Direction facing) {
		final Entity camera = Minecraft.getInstance().cameraEntity;
		return shouldNotRender(camera == null ? null : camera.position(), pos, maxDistance, facing);
	}

	public static void clearTextureAvailability() {
		AVAILABLE_TEXTURES.clear();
		UNAVAILABLE_TEXTURES.clear();
	}

	public static void renderLiftDisplay(PoseStack matrices, MultiBufferSource vertexConsumers, BlockPos pos, String floorNumber, Lift.LiftDirection liftDirection, float maxWidth, float height) {
		if (MainRenderer.shouldNotRender(pos, Math.min(RenderPIDS.MAX_VIEW_DISTANCE, MainRenderer.maxTrainRenderDistance), null)) {
			return;
		}

		final MultiBufferSource.BufferSource immediate = Minecraft.getInstance().renderBuffers().bufferSource();
		IDrawing.drawStringWithFont(matrices, Minecraft.getInstance().font, immediate, floorNumber, IGui.HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM, 0, height, maxWidth, -1, 18 / maxWidth, LIFT_LIGHT_COLOR, false, MAX_LIGHT_GLOWING, null);
		immediate.endBatch();

		if (liftDirection != Lift.LiftDirection.NONE) {
			IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getLight(ARROW_TEXTURE, true)), -maxWidth / 6, 0, maxWidth / 3, maxWidth / 3, 0, liftDirection == Lift.LiftDirection.UP ? 0 : 1, 1, liftDirection == Lift.LiftDirection.UP ? 1 : 0, Direction.UP, LIFT_LIGHT_COLOR, MAX_LIGHT_GLOWING);
		}
	}

	public static boolean isHoldingRailRelated(Player player) {
		return Utilities.isHolding(player,
				item -> item instanceof ItemNodeModifierBase ||
						Block.byItem(item) instanceof BlockSignalLightBase ||
						Block.byItem(item) instanceof BlockNode ||
						Block.byItem(item) instanceof BlockSignalSemaphoreBase ||
						Block.byItem(item) instanceof BlockPlatform
		);
	}

	public static boolean showShiftProgressBar() {
		final Minecraft client = Minecraft.getInstance();
		final LocalPlayer player = client.player;
		final float shiftHoldingTicks = ClientData.getShiftHoldingTicks();

		if (shiftHoldingTicks > 0 && player != null) {
			final int progressFilled = Mth.clamp((int) (shiftHoldingTicks * DISMOUNT_PROGRESS_BAR_LENGTH / RailwayDataMountModule.SHIFT_ACTIVATE_TICKS), 0, DISMOUNT_PROGRESS_BAR_LENGTH);
			final String progressBar = String.format("§6%s§7%s", StringUtils.repeat('|', progressFilled), StringUtils.repeat('|', DISMOUNT_PROGRESS_BAR_LENGTH - progressFilled));
			player.displayClientMessage(Text.translatable("gui.mtr.dismount_hold", client.options.keyShift.getTranslatedKeyMessage(), progressBar), true);
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Transform an absolute world coordinate to a coordinate relative to the player's camera
	 * Used for WorldRenderEvents to translate a pose to a particular point in the world
	 */
	public static Vec3 getOffsetPos(Vec3 vec3) {
		final Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
		return cameraPos.subtract(vec3);
	}

	public static void transformRelativeToCamera(PoseStack poseStack, double worldX, double worldY, double worldZ) {
		final Vec3 offsetPos = getOffsetPos(new Vec3(worldX, worldY, worldZ));
		poseStack.translate(-offsetPos.x(), -offsetPos.y(), -offsetPos.z());
	}

	@Deprecated // TODO remove later
	public static void scheduleRender(ResourceLocation resourceLocation, boolean priority, Function<ResourceLocation, RenderType> getVertexConsumer, BiConsumer<PoseStack, VertexConsumer> callback) {
		scheduleRender(resourceLocation, priority, QueuedRenderLayer.EXTERIOR, callback);
	}

	public static void scheduleRender(ResourceLocation resourceLocation, boolean priority, QueuedRenderLayer queuedRenderLayer, BiConsumer<PoseStack, VertexConsumer> callback) {
		final Map<ResourceLocation, Set<BiConsumer<PoseStack, VertexConsumer>>> map = RENDERS.get(priority ? 1 : 0).get(queuedRenderLayer.ordinal());
		if (!map.containsKey(resourceLocation)) {
			map.put(resourceLocation, new HashSet<>());
		}
		map.get(resourceLocation).add(callback);
	}

	public static String getInterchangeRouteNames(Station station, Route thisRoute, Route nextRoute) {
		final String thisRouteSplit = thisRoute.name.split("\\|\\|")[0];
		final String nextRouteSplit = nextRoute == null ? null : nextRoute.name.split("\\|\\|")[0];
		final Map<Integer, ClientCache.ColorNameTuple> routesInStation = ClientData.DATA_CACHE.stationIdToRoutes.get(station.id);
		if (routesInStation != null) {
			final List<String> interchangeRoutes = routesInStation.values().stream().filter(interchangeRoute -> {
				final String routeName = interchangeRoute.name.split("\\|\\|")[0];
				return !routeName.equals(thisRouteSplit) && !routeName.equals(nextRouteSplit);
			}).map(interchangeRoute -> interchangeRoute.name).collect(Collectors.toList());
			return IGui.mergeStationsWithCommas(interchangeRoutes);
		} else {
			return "";
		}
	}

	private static double maxDistanceXZ(Vec3 pos1, BlockPos pos2) {
		return Math.max(Math.abs(pos1.x - pos2.getX()), Math.abs(pos1.z - pos2.getZ()));
	}

	private static boolean shouldNotRender(Vec3 cameraPos, BlockPos pos, int maxDistance, Direction facing) {
		final boolean playerFacingAway;
		if (cameraPos == null || facing == null) {
			playerFacingAway = false;
		} else {
			if (facing.getAxis() == Direction.Axis.X) {
				final double playerXOffset = cameraPos.x - pos.getX() - 0.5;
				playerFacingAway = Math.signum(playerXOffset) == facing.getStepX() && Math.abs(playerXOffset) >= 0.5;
			} else {
				final double playerZOffset = cameraPos.z - pos.getZ() - 0.5;
				playerFacingAway = Math.signum(playerZOffset) == facing.getStepZ() && Math.abs(playerZOffset) >= 0.5;
			}
		}
		return cameraPos == null || playerFacingAway || maxDistanceXZ(cameraPos, pos) > (MTRClient.isReplayMod() ? MAX_RADIUS_REPLAY_MOD : maxDistance);
	}

	private static void renderRailStandard(Level world, Rail rail, float yOffset, boolean renderColors, float railWidth) {
		renderRailStandard(world, rail, yOffset, renderColors, railWidth, renderColors && rail.railType == RailType.QUARTZ ? "mtr:textures/block/rail_preview.png" : "textures/block/rail.png", -1, -1, -1, -1);
	}

	private static void renderRailStandard(Level world, Rail rail, float yOffset, boolean renderColors, float railWidth, String texture, float u1, float v1, float u2, float v2) {
		final int maxRailDistance = UtilitiesClient.getRenderDistance() * 16;

		rail.render((absX1, absZ1, absX2, absZ2, absX3, absZ3, absX4, absZ4, absY1, absY2) -> {
			final BlockPos pos2 = BlockUtil.newBlockPos(absX1, absY1, absZ1);
			if (shouldNotRender(pos2, maxRailDistance, null)) {
				return;
			}
			final int light2 = LightTexture.pack(world.getBrightness(LightLayer.BLOCK, pos2), world.getBrightness(LightLayer.SKY, pos2));

			final double x2 = absX2 - absX1;
			final double z2 = absZ2 - absZ1;
			final double x3 = absX3 - absX1;
			final double z3 = absZ3 - absZ1;
			final double x4 = absX4 - absX1;
			final double z4 = absZ4 - absZ1;
			final double y2 = absY2 - absY1;

			if (rail.railType == RailType.NONE) {
				if (rail.transportMode != TransportMode.CABLE_CAR && renderColors) {
					scheduleRender(ResourceLocation.parse("mtr:textures/block/one_way_rail_arrow.png"), false, QueuedRenderLayer.EXTERIOR, (matrices, vertexConsumer) -> {
						matrices.pushPose();
						MainRenderer.transformRelativeToCamera(matrices, absX1, absY1, absZ1);
						IDrawing.drawTexture(matrices, vertexConsumer, (float) 0, (float) 0 + yOffset, (float) 0, (float) x2, (float) 0 + yOffset + SMALL_OFFSET, (float) z2, (float) x3, (float) y2 + yOffset, (float) z3, (float) x4, (float) y2 + yOffset + SMALL_OFFSET, (float) z4, 0, 0.25F, 1, 0.75F, Direction.UP, -1, light2);
						IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) 0 + yOffset + SMALL_OFFSET, (float) z2, (float) 0, (float) 0 + yOffset, (float) 0, (float) x4, (float) y2 + yOffset + SMALL_OFFSET, (float) z4, (float) x3, (float) y2 + yOffset, (float) z3, 0, 0.25F, 1, 0.75F, Direction.UP, -1, light2);
						matrices.popPose();
					});
				}
			} else {
				final float textureOffset = (((int) (absX1 + absZ1)) % 4) * 0.25F + (float) Config.trackTextureOffset() / Config.TRACK_OFFSET_COUNT;
				final int color = renderColors || !Config.hideSpecialRailColors() && rail.railType.hasSavedRail ? rail.railType.color : -1;
				scheduleRender(ResourceLocation.parse(texture), false, QueuedRenderLayer.EXTERIOR, (matrices, vertexConsumer) -> {
					matrices.pushPose();
					MainRenderer.transformRelativeToCamera(matrices, absX1, absY1, absZ1);
					IDrawing.drawTexture(matrices, vertexConsumer, (float) 0, (float) 0 + yOffset, (float) 0, (float) x2, (float) 0 + yOffset + SMALL_OFFSET, (float) z2, (float) x3, (float) y2 + yOffset, (float) z3, (float) x4, (float) y2 + yOffset + SMALL_OFFSET, (float) z4, u1 < 0 ? 0 : u1, v1 < 0 ? 0.1875F + textureOffset : v1, u2 < 0 ? 1 : u2, v2 < 0 ? 0.3125F + textureOffset : v2, Direction.UP, color, light2);
					IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) 0 + yOffset + SMALL_OFFSET, (float) z2, (float) 0, (float) 0 + yOffset, (float) 0, (float) x4, (float) y2 + yOffset + SMALL_OFFSET, (float) z4, (float) x3, (float) y2 + yOffset, (float) z3, u1 < 0 ? 0 : u1, v1 < 0 ? 0.1875F + textureOffset : v1, u2 < 0 ? 1 : u2, v2 < 0 ? 0.3125F + textureOffset : v2, Direction.UP, color, light2);
					matrices.popPose();
				});
			}
		}, -railWidth, railWidth);
	}

	private static void renderSignalsStandard(Level world, PoseStack matrices, MultiBufferSource vertexConsumers, Rail rail, BlockPos startPos, BlockPos endPos) {
		final int maxRailDistance = UtilitiesClient.getRenderDistance() * 16;
		final List<SignalBlocks.SignalBlock> signalBlocks = ClientData.SIGNAL_BLOCKS.getSignalBlocksAtTrack(PathData.getRailProduct(startPos, endPos));
		final float width = 1F / DyeColor.values().length;

		for (int i = 0; i < signalBlocks.size(); i++) {
			final SignalBlocks.SignalBlock signalBlock = signalBlocks.get(i);
			final boolean shouldGlow = signalBlock.isOccupied() && (((int) Math.floor(MTRClient.getGameTick())) % TICKS_PER_SECOND) < TICKS_PER_SECOND / 2;
			final VertexConsumer vertexConsumer = shouldGlow ? vertexConsumers.getBuffer(MoreRenderLayers.getLight(ResourceLocation.parse("mtr:textures/block/white.png"), false)) : vertexConsumers.getBuffer(MoreRenderLayers.getExterior(ResourceLocation.parse("textures/block/white_wool.png")));
			final float u1 = width * i + 1 - width * signalBlocks.size() / 2;
			final float u2 = u1 + width;

			final int color = ARGB_BLACK | signalBlock.color.getMapColor().col;
			rail.render((absX1, absZ1, absX2, absZ2, absX3, absZ3, absX4, absZ4, absY1, absY2) -> {
				final BlockPos pos2 = BlockUtil.newBlockPos(absX1, absY1, absZ1);
				if (shouldNotRender(pos2, maxRailDistance, null)) {
					return;
				}
				final int light2 = shouldGlow ? MAX_LIGHT_GLOWING : LightTexture.pack(world.getBrightness(LightLayer.BLOCK, pos2), world.getBrightness(LightLayer.SKY, pos2));
				final double x1 = absX1, y1 = absY1, z1 = absZ1;
				final double x2 = absX2/* - absX1*/;
				final double z2 = absZ2/* - absZ1*/;
				final double x3 = absX3/* - absX1*/;
				final double z3 = absZ3/* - absZ1*/;
				final double x4 = absX4/* - absX1*/;
				final double z4 = absZ4/* - absZ1*/;
				final double y2 = absY2/* - absY1*/;

				IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1, (float) z1, (float) x2, (float) y1 + SMALL_OFFSET, (float) z2, (float) x3, (float) y2, (float) z3, (float) x4, (float) y2 + SMALL_OFFSET, (float) z4, u1, 0, u2, 1, Direction.UP, color, light2);
				IDrawing.drawTexture(matrices, vertexConsumer, (float) x4, (float) y2 + SMALL_OFFSET, (float) z4, (float) x3, (float) y2, (float) z3, (float) x2, (float) y1 + SMALL_OFFSET, (float) z2, (float) x1, (float) y1, (float) z1, u1, 0, u2, 1, Direction.UP, color, light2);
			}, u1 - 1, u2 - 1);
		}
	}

	private static Component getStationText(Station station, String textKey) {
		if (station != null) {
			return Text.literal(IGui.formatMTRLanguageName(IGui.insertTranslation("gui.mtr." + textKey + "_station_cjk", "gui.mtr." + textKey + "_station", 1, IGui.textOrUntitled(station.name))));
		} else {
			return Text.literal("");
		}
	}

	public enum QueuedRenderLayer {LIGHT, LIGHT_TRANSLUCENT, INTERIOR, EXTERIOR}
}
