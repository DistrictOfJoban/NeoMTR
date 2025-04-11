package mtr.data;

import io.netty.buffer.Unpooled;
import mtr.Registry;
import mtr.mappings.Utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class RailwayDataMountModule extends RailwayDataModule {

	public static final String NAME = "mount";
	private final Map<Player, Integer> playerRidingCoolDown = new HashMap<>();
	private final Map<Player, Long> playerRidingRoute = new HashMap<>();
	private final Map<Player, Integer> playerShiftCoolDowns = new HashMap<>();

	public final Set<Player> playerInVirtualDrive = new HashSet<>();

	public static final int SHIFT_ACTIVATE_TICKS = 30;

	// Server mount configuration
	private static final float INNER_PADDING = 0.5F;
	private static final int BOX_PADDING = 3;

	public RailwayDataMountModule(RailwayData railwayData, Level level, Map<BlockPos, Map<BlockPos, Rail>> rails) {
		super(NAME, railwayData, level, rails);
	}

	public void tryMountRider(Set<UUID> ridingEntities, long id, long routeId, double carX, double carY, double carZ, double length, double width, float carYaw, float carPitch, boolean doorOpen, boolean canMount, int percentageOffset, ResourceLocation packetId, Function<Player, Boolean> canRide, Consumer<Player> ridingCallback) {
		final double halfLength = length / 2;
		final double halfWidth = width / 2;

		if (canMount) {
			final double margin = halfLength + BOX_PADDING;
			level.getEntitiesOfClass(Player.class, new AABB(carX + margin, carY + margin, carZ + margin, carX - margin, carY - margin, carZ - margin), player -> !player.isSpectator() && !ridingEntities.contains(player.getUUID()) && canRide(player) && canRide.apply(player)).forEach(player -> {
				final Vec3 positionRotated = player.position().subtract(carX, carY, carZ).yRot(-carYaw).xRot(-carPitch);
				if (Math.abs(positionRotated.x) < halfWidth + INNER_PADDING && Math.abs(positionRotated.y) < 2.5 && Math.abs(positionRotated.z) <= halfLength && !shouldDismount(player)) {
					ridingEntities.add(player.getUUID());
					final float percentageX = (float) (positionRotated.x / width + 0.5);
					final float percentageZ = (float) (length == 0 ? 0 : positionRotated.z / length + 0.5) + percentageOffset;
					final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
					packet.writeLong(id);
					packet.writeFloat(percentageX);
					packet.writeFloat(percentageZ);
					packet.writeUUID(player.getUUID());
					level.players().forEach(worldPlayer -> Registry.sendToPlayer((ServerPlayer) worldPlayer, packetId, packet));
				}
			});
		}

		final Set<UUID> ridersToRemove = new HashSet<>();
		ridingEntities.forEach(uuid -> {
			final Player player = level.getPlayerByUUID(uuid);

			if (player != null) {
				final boolean remove;

				if (player.isSpectator() || shouldDismount(player)) {
					remove = true;
				} else if (doorOpen) {
					final Vec3 positionRotated = player.position().subtract(carX, carY, carZ).yRot(-carYaw).xRot(-carPitch);
					remove = Math.abs(positionRotated.z) <= halfLength && (Math.abs(positionRotated.x) > halfWidth + INNER_PADDING || Math.abs(positionRotated.y) > 10);
				} else {
					remove = false;
				}

				if (remove) {
					ridersToRemove.add(uuid);
				}

				updatePlayerRiding(player, routeId);
				ridingCallback.accept(player);
			}
		});

		if (!ridersToRemove.isEmpty()) {
			ridersToRemove.forEach(ridingEntities::remove);
		}
	}

	public void tick() {
		level.players().forEach(player -> {
			final int oldShiftCoolDown = playerShiftCoolDowns.getOrDefault(player, 0);
			final int shiftCoolDown;
			if (player.isShiftKeyDown()) {
				shiftCoolDown = Math.min(SHIFT_ACTIVATE_TICKS, oldShiftCoolDown + 1);
			} else {
				shiftCoolDown = 0;
			}
			if (shiftCoolDown != oldShiftCoolDown) {
				playerShiftCoolDowns.put(player, shiftCoolDown);
			}
		});

		playerInVirtualDrive.forEach(player -> {
			playerRidingCoolDown.put(player, 2);
			Registry.setInTeleportationState(player, true);
		});

		final Set<Player> playersToRemove = new HashSet<>();
		playerRidingCoolDown.forEach((player, coolDown) -> {
			if (coolDown <= 0) {
				updatePlayerRiding(player, 0);
				playersToRemove.add(player);
				player.stopRiding();
			}
			playerRidingCoolDown.put(player, coolDown - 1);
		});
		playersToRemove.forEach(player -> {
			playerRidingCoolDown.remove(player);
			playerRidingRoute.remove(player);
		});
	}

	public void onPlayerJoin(ServerPlayer serverPlayer) {
		playerRidingCoolDown.put(serverPlayer, 2);
		playerShiftCoolDowns.put(serverPlayer, 0);
	}

	public void onPlayerDisconnect(Player player) {
		playerShiftCoolDowns.remove(player);
		playerInVirtualDrive.remove(player);
	}

	public void updatePlayerRiding(Player player, long routeId) {
		final boolean isRiding = routeId != 0;
		player.fallDistance = 0;
		player.setNoGravity(isRiding);
		player.noPhysics = isRiding;
		if (isRiding) {
			Utilities.getAbilities(player).mayfly = true;
			playerRidingCoolDown.put(player, 2);
			playerRidingRoute.put(player, routeId);
		} else {
			playerInVirtualDrive.remove(player);
			((ServerPlayer) player).gameMode.getGameModeForPlayer().updatePlayerAbilities(Utilities.getAbilities(player));
		}
		Registry.setInTeleportationState(player, isRiding);
	}

	public boolean canRide(Player player) {
		return !playerRidingCoolDown.containsKey(player);
	}

	public void updatePlayerInVirtualDrive(Player player, boolean isRiding) {
		player.fallDistance = 0;
		player.setNoGravity(isRiding);
		player.noPhysics = isRiding;
		if (isRiding) {
			Utilities.getAbilities(player).mayfly = true;
			playerInVirtualDrive.add(player);
		} else {
			playerInVirtualDrive.remove(player);
			((ServerPlayer) player).gameMode.getGameModeForPlayer().updatePlayerAbilities(Utilities.getAbilities(player));
		}
		Registry.setInTeleportationState(player, isRiding);
	}

	public Route getRidingRoute(Player player) {
		if (playerRidingRoute.containsKey(player)) {
			return railwayData.dataCache.routeIdMap.get(playerRidingRoute.get(player));
		} else {
			return null;
		}
	}

	public boolean shouldDismount(Player player) {
		return playerShiftCoolDowns.getOrDefault(player, 0) == SHIFT_ACTIVATE_TICKS;
	}
}
