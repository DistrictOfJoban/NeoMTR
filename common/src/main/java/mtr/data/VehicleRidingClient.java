package mtr.data;

import io.netty.buffer.Unpooled;
import mtr.MTRClient;
import mtr.RegistryClient;
import mtr.client.ClientData;
import mtr.mappings.Utilities;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.render.TrainRendererBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.Consumer;

public class VehicleRidingClient {

	private float clientPrevYaw;
	private float oldPercentageX;
	private float oldPercentageZ;
	private double lastSentX;
	private double lastSentY;
	private double lastSentZ;
	private float lastSentTicks;
	private int interval;
	private int previousInterval;

	private final Map<UUID, Vector3f> riderRatioPos = new HashMap<>();
	private final Map<UUID, Vector3f> riderRatioPosNew = new HashMap<>();
	private final Map<UUID, Vec3> riderPositions = new HashMap<>();
	private final Set<UUID> ridingEntities;
	private final ResourceLocation packetId;

	private static final float VEHICLE_WALKING_SPEED_MULTIPLIER = 0.125F;
	private static final int VEHICLE_PERCENTAGE_UPDATE_INTERVAL = 20;
	private static final boolean DEBUG_SKIP_RENDER_TRAIN_AND_PLAYERS = false;

	public VehicleRidingClient(Set<UUID> ridingEntities, ResourceLocation packetId) {
		this.ridingEntities = ridingEntities;
		this.packetId = packetId;
	}

	public void renderPlayers() {
		if (DEBUG_SKIP_RENDER_TRAIN_AND_PLAYERS) {
			final Player player = Minecraft.getInstance().player;
			if (player != null && ridingEntities.contains(player.getUUID())) {
				return;
			}
		}

		riderPositions.forEach(TrainRendererBase::renderRidingPlayer);
	}

	public void movePlayer(Consumer<UUID> ridingEntityCallback) {
		riderPositions.clear();

		final LocalPlayer clientPlayer = Minecraft.getInstance().player;
		if (clientPlayer == null) {
			return;
		}

		ridingEntities.forEach(uuid -> {
			if (!riderRatioPos.containsKey(uuid) || !riderRatioPosNew.containsKey(uuid)) {
				riderRatioPos.put(uuid, new Vector3f(0.5f, 0, 0.5f));
				riderRatioPosNew.put(uuid, new Vector3f(0.5f, 0, 0.5f));
			}

			ridingEntityCallback.accept(uuid);
		});
	}

	public void setOffsets(UUID uuid, double x, double y, double z, float yaw, float pitch, double length, int width, boolean doorLeftOpen, boolean doorRightOpen, boolean hasPitchAscending, boolean hasPitchDescending, float riderOffset, float riderOffsetDismounting, boolean shouldSetOffset, boolean shouldSetYaw, Runnable clientPlayerCallback) {
		final LocalPlayer clientPlayer = Minecraft.getInstance().player;
		if (clientPlayer == null) {
			return;
		}

		final boolean isClientPlayer = uuid.equals(clientPlayer.getUUID());
		final double percentageX = getValueFromPercentage(riderRatioPos.get(uuid).x, width);
		final float riderOffsetNew = doorLeftOpen && percentageX < 0 || doorRightOpen && percentageX > 1 ? riderOffsetDismounting : riderOffset;
		final Vec3 playerOffset = new Vec3(percentageX, riderOffsetNew, getValueFromPercentage(Mth.frac(riderRatioPos.get(uuid).z), length)).xRot((pitch < 0 ? hasPitchAscending : hasPitchDescending) ? pitch : 0).yRot(yaw);
		ClientData.updatePlayerRidingOffset(uuid);
		riderPositions.put(uuid, playerOffset.add(x, y, z));

		if (isClientPlayer) {
			final double moveX = x + playerOffset.x;
			final double moveY = y + playerOffset.y;
			final double moveZ = z + playerOffset.z;
			// HACK Vivecraft support removed

			clientPlayer.fallDistance = 0;
			clientPlayer.setDeltaMovement(0, 0, 0);
			clientPlayer.setSpeed(0);
			if (MTRClient.getGameTick() > 40) {
				clientPlayer.absMoveTo(moveX, moveY, moveZ);
			}

			clientPlayerCallback.run();

			if (shouldSetYaw) {
				float angleDifference = (float) Math.toDegrees(clientPrevYaw - yaw);
				if (angleDifference > 180) {
					angleDifference -= 360;
				} else if (angleDifference < -180) {
					angleDifference += 360;
				}
				Utilities.incrementYaw(clientPlayer, angleDifference);
			}

			clientPrevYaw = yaw;
		}
	}

	public void moveSelf(long id, UUID uuid, double length, int width, float yaw, int percentageOffset, int maxPercentage, boolean doorLeftOpen, boolean doorRightOpen, boolean noGangwayConnection, float ticksElapsed) {
		final float speedMultiplier = ticksElapsed * VEHICLE_WALKING_SPEED_MULTIPLIER;
		final float newPercentageX;
		final float newPercentageZ;
		final LocalPlayer clientPlayer = Minecraft.getInstance().player;

		if (clientPlayer == null) {
			return;
		}

		if (uuid.equals(clientPlayer.getUUID())) {
			final Vec3 movement = new Vec3(Math.abs(clientPlayer.xxa) > 0.5 ? Math.copySign(speedMultiplier, clientPlayer.xxa) : 0, 0, Math.abs(clientPlayer.zza) > 0.5 ? Math.copySign(speedMultiplier, clientPlayer.zza) : 0).yRot((float) -Math.toRadians(Utilities.getYaw(clientPlayer)) - yaw);
			final float tempPercentageX = riderRatioPos.get(uuid).x + (float) movement.x / width;
			final float tempPercentageZ = riderRatioPos.get(uuid).z + (length == 0 ? 0 : (float) movement.z / (float) length);
			newPercentageX = Mth.clamp(tempPercentageX, doorLeftOpen ? -3 : 0, doorRightOpen ? 4 : 1);
			newPercentageZ = Mth.clamp(tempPercentageZ, (noGangwayConnection ? percentageOffset + 0.05F : 0) + 0.01F, (noGangwayConnection ? percentageOffset + 0.95F : maxPercentage) - 0.01F);

			if (previousInterval != interval && (newPercentageX != oldPercentageX || newPercentageZ != oldPercentageZ)) {
				final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
				packet.writeLong(id);
				packet.writeFloat(newPercentageX);
				packet.writeFloat(newPercentageZ);
				packet.writeUUID(uuid);
				RegistryClient.sendToServer(packetId, packet);
				oldPercentageX = newPercentageX;
				oldPercentageZ = newPercentageZ;
			}
		} else {
			final double distanceX = getValueFromPercentage(riderRatioPosNew.get(uuid).x, width) - getValueFromPercentage(riderRatioPos.get(uuid).x, width);
			final double distanceZ = getValueFromPercentage(riderRatioPosNew.get(uuid).z, length) - getValueFromPercentage(riderRatioPos.get(uuid).z, length);
			final double manhattanDistance = Math.abs(distanceX + distanceZ);
			if (manhattanDistance == 0 || distanceX * distanceX + distanceZ * distanceZ < speedMultiplier * speedMultiplier) {
				newPercentageX = riderRatioPosNew.get(uuid).x;
				newPercentageZ = riderRatioPosNew.get(uuid).z;
			} else {
				newPercentageX = riderRatioPos.get(uuid).x + (float) (distanceX / manhattanDistance * speedMultiplier / width);
				newPercentageZ = riderRatioPos.get(uuid).z + (float) (length == 0 ? 0 : distanceZ / manhattanDistance * speedMultiplier / length);
			}
		}

		riderRatioPos.get(uuid).set(newPercentageX, 0, newPercentageZ);
	}

	public void begin() {
		interval = (int) Math.floor(MTRClient.getGameTick() / VEHICLE_PERCENTAGE_UPDATE_INTERVAL);
	}

	public void end() {
		previousInterval = interval;
	}

	public void startRiding(UUID uuid, float percentageX, float percentageZ) {
		ridingEntities.add(uuid);
		riderRatioPos.put(uuid, new Vector3f(percentageX, 0, percentageZ));
		riderRatioPosNew.put(uuid, new Vector3f(percentageX, 0, percentageZ));
	}

	public void stopRiding(UUID uuid) {
		// Normally shouldn't use this (Updated from server side)
		ridingEntities.remove(uuid);
		riderRatioPos.remove(uuid);
		riderRatioPosNew.remove(uuid);
		riderPositions.remove(uuid);
	}

	public void updateRiderPercentages(UUID uuid, float percentageX, float percentageZ) {
		riderRatioPosNew.get(uuid).set(percentageX, 0, percentageZ);
	}

	public float getPercentageX(UUID uuid) {
		return riderRatioPos.get(uuid).x;
	}

	public float getPercentageZ(UUID uuid) {
		return riderRatioPos.get(uuid).z;
	}

	private static double getValueFromPercentage(double percentage, double total) {
		return (percentage - 0.5) * total;
	}
}
