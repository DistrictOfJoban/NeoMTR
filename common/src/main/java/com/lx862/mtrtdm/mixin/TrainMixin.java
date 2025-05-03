package com.lx862.mtrtdm.mixin;

import com.lx862.mtrtdm.config.TrainConfig;
import com.lx862.mtrtdm.data.TrainBehavior;
import com.lx862.mtrtdm.TrainDrivingModule;
import com.lx862.mtrtdm.data.TrainSound;
import io.netty.buffer.Unpooled;
import mtr.loader.MTRRegistry;
import mtr.registry.Items;
import mtr.MTR;
import mtr.data.*;
import mtr.path.PathData;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

import static mtr.registry.Networking.PACKET_UPDATE_TRAINS;

@Mixin(value = Train.class, remap = false)
public abstract class TrainMixin {
    @Unique
    private static final double EM_BRAKE_FACTOR = 1.5;
    @Unique
    private static final double OVERSPEED_WARNING_TOLERANCE = 5;
    @Unique
    private static final int OVERSPEED_TOLERANCE_KMH = 10;
    public double oldTargetSpeed;
    public double targetSpeed;
    public double maxTargetSpeed;
    public boolean overSpeed;
    public double lastSpeed;
    public int oldAccelerationSign;
    public boolean deceleratingToZero;
    public boolean speedChanging;
    public double nextPathSpeedLimit;
    public boolean isDirty = false;
    private int syncFrequency = 2;
    private boolean canCalculatePhysics;
    private boolean enteredSiding;
    private Vec3[] lastCarPos = null;

    @Shadow
    @Final
    public int spacing;

    @Shadow
    public abstract int getIndex(int car, int trainSpacing, boolean roundDown);

    @Shadow
    protected abstract boolean isRailBlocked(int i);

    @Shadow
    @Final
    protected Set<UUID> ridingEntities;

    @Shadow @Final public long sidingId;

    @Shadow protected int manualNotch;

    @Shadow protected float speed;

    @Shadow @Final
    public List<PathData> path;

    @Shadow @Final
    public float accelerationConstant;

    @Shadow @Final
    public boolean isManualAllowed;

    @Shadow
    public abstract void writePacket(FriendlyByteBuf packet);

    @Shadow @Final protected List<Double> distances;

    @Shadow protected double railProgress;

    @Shadow protected int nextStoppingIndex;

    @Shadow protected boolean isCurrentlyManual;

    @Shadow @Final public int trainCars;

    @Shadow protected boolean reversed;

    @Shadow protected abstract Vec3 getRoutePosition(double offset);

    @Shadow public abstract float getElapsedDwellTicks();

    @Shadow public abstract int getTotalDwellTicks();

    @Shadow public abstract float getDoorValue();

    @Inject(method = "simulateTrain", at = @At("HEAD"))
    public void JobanBreakTrain(Level world, float tickElapsed, Depot depot, CallbackInfo ci) {
        if (world.isClientSide() || !this.isManualAllowed) return;
        if(!TrainDrivingModule.brokendownTrain.containsKey(sidingId)) return;

        manualNotch = Math.min(0, manualNotch);

        // Break train only update every 10 tick
        if(!MTR.isGameTickInterval(10) || this.ridingEntities.isEmpty()) return;

        for (UUID uuid : ridingEntities) {
            ServerPlayer player = (ServerPlayer) world.getPlayerByUUID(uuid);
            if(player != null) {
                syncTrainToPlayer(player);
                if (player.isHolding(Items.DRIVER_KEY.get())) {
                    String reason = TrainDrivingModule.brokendownTrain.get(sidingId);
                    ResourceLocation noteblockHarpId = SoundEvents.NOTE_BLOCK_HARP.value().getLocation();
                    player.connection.send(new ClientboundSoundPacket(Holder.direct(SoundEvent.createVariableRangeEvent(noteblockHarpId)), SoundSource.MASTER, player.position().x, player.position().y, player.position().z, 10000000, 1, world.getRandom().nextLong()));
                    player.connection.send(new ClientboundSetTitlesAnimationPacket(0, 20, 10));
                    player.connection.send(new ClientboundSetTitleTextPacket(Component.literal(reason).withStyle(ChatFormatting.RED)));
                    player.connection.send(new ClientboundSetSubtitleTextPacket(Component.literal("Stop the train and report immediately!").withStyle(ChatFormatting.YELLOW)));
                }
            }
        }
    }

    @Inject(method = "simulateTrain", at = @At("HEAD"))
    public void JobanPhysics(Level world, float tickElapsed, Depot depot, CallbackInfo ci) {
        if (world.isClientSide() || !this.isCurrentlyManual || !canCalculatePhysics || this.ridingEntities.isEmpty() || path.isEmpty()) return;

        TrainBehavior behavior = getBehaviorConfig(sidingId);
        if(behavior == null) return;

        float speedDragDifference = (float) (speed * (behavior.drag / 2000));
        speed = Math.max(0, speed - speedDragDifference);

        if(speedDragDifference != 0) {
            isDirty = true;
            syncFrequency = 4;
        }

        /* Calculate speed changes caused by slope */
        final Vec3[] positions = new Vec3[trainCars + 1];
        final Vec3[] frontPos = new Vec3[trainCars + 1];
        for (int i = 0; i <= trainCars; i++) {
            positions[i] = getRoutePosition((reversed ? trainCars - i : i) * spacing);
            frontPos[i] = getRoutePosition((reversed ? trainCars - i : i) * 0);
        }

        if(lastCarPos == null) {
            lastCarPos = positions;
            return;
        }

        float finalSpeedChange = 0;
        for(int i = 0; i < lastCarPos.length; i++) {
            double yDifference = (frontPos[i].y - positions[i].y);
            if(yDifference != 0) {
                finalSpeedChange = (float) ((yDifference / 750) * behavior.slopeSpeedChange);
            }
        }

        if(finalSpeedChange != 0) {
            syncFrequency = 2;
            speed = Math.max(0, speed - finalSpeedChange);
            isDirty = true;
        }

        lastCarPos = positions;
    }

    @Inject(method = "simulateTrain", at = @At("HEAD"))
    public void JobanSafetySystem(Level world, float tickElapsed, Depot depot, CallbackInfo ci) {
        if (world.isClientSide() || !this.isCurrentlyManual) return;
        if (this.ridingEntities.isEmpty()) return;
        TrainBehavior behavior = getBehaviorConfig(sidingId);
        if (behavior == null) return;
        double currentSpeedKmh = kmh(speed);
        double lastSpeedKmh = kmh(lastSpeed);

        TrainSound speedDropSound = behavior.speedDropSound;
        TrainSound speedRaiseSound = behavior.speedRaiseSound;
        final float newAcceleration = accelerationConstant * tickElapsed;
        final int baseIndex = this.getIndex(0, this.spacing, true);
        double curRailSpeedKmh = this.path.get(baseIndex).rail.railType.speedLimit;
        double factor = behavior.decelFactor;

        updateTargetSpeed(factor, curRailSpeedKmh, baseIndex, behavior.pathLookup, speedDropSound, world);

        if (oldTargetSpeed != targetSpeed && !speedChanging) {
            if(oldTargetSpeed > targetSpeed) {
                speedDropSound.play(ridingEntities, true, world);
            } else {
                speedRaiseSound.play(ridingEntities, true, world);
            }
        }

        // Underrun alarm
        if(speed == 0 && this.path.get(baseIndex).dwellTime > 0 && (distances.get(Math.max(0, baseIndex)) - this.railProgress) != 0) {
            boolean justStopped = speed < lastSpeed;
            if(behavior.incorrectStopPosSound != null) {
                behavior.incorrectStopPosSound.play(ridingEntities, justStopped, world);
            }

            if(justStopped) {
                for (UUID uuid : ridingEntities) {
                    ServerPlayer player = (ServerPlayer) world.getPlayerByUUID(uuid);
                    if(player == null) continue;

                    if (player.isHolding(Items.DRIVER_KEY.get())) {
                        player.sendSystemMessage(Component.literal("Incorrect stop position, please correct stop position.").withStyle(ChatFormatting.RED), false);
                    }
                }
            }
        }

        lastSpeed = speed;
        oldTargetSpeed = targetSpeed;

        /* Don't let the player continue to accelerate */
        if (behavior.restrictToRailSpeed && currentSpeedKmh >= targetSpeed) {
            manualNotch = Math.min(manualNotch, 0);
            isDirty = true;
        }

        boolean lastOverspeed = overSpeed;

        /* Over the speed limit */
        if (currentSpeedKmh > maxTargetSpeed && (behavior.restrictToRailSpeed || behavior.overSpeedDecelToZero)) {
            oldAccelerationSign = manualNotch;
            manualNotch = -3;
            if (behavior.overSpeedDecelToZero) {
                deceleratingToZero = true;
            }

            isDirty = true;
            overSpeed = true;
            syncFrequency = 2;
        }

        /* Under the speed limit */
        if (currentSpeedKmh <= maxTargetSpeed) {
            /* If previously overspeed and just got within the safe limit */
            if (overSpeed) {
                manualNotch = 0;
                isDirty = true;
                overSpeed = false;
            }
        }

        /* Train has just overspeed, or already overspeed but train needs to loop the overspeed sound */
        if(overSpeed) {
            if(behavior.overspeedSound != null) {
                behavior.overspeedSound.play(ridingEntities, !lastOverspeed, world);
            }
        }

        double speedLimitWarn = targetSpeed + OVERSPEED_WARNING_TOLERANCE;
        // About to hit the overspeed limit, alarm the driver to brake!
        if(currentSpeedKmh >= speedLimitWarn && !overSpeed) {
            if(behavior.needBrakeSound != null) {
                behavior.needBrakeSound.play(ridingEntities, lastSpeedKmh < speedLimitWarn, world);
            }
        }

        double lastDistance = distances.size() == 0 ? -1 : distances.get(distances.size() - 1);
        double sidingLength = path.get(0).rail.getLength();

        if(lastDistance > 0 && lastDistance - this.railProgress <= sidingLength) {
            if(behavior.enterSidingSound != null) {
                behavior.enterSidingSound.play(ridingEntities, !enteredSiding, world);
            }

            if(!enteredSiding) {
                enteredSiding = true;
            }
        }

        float emAcceleration = (float)(newAcceleration * EM_BRAKE_FACTOR);
        /* if deceleratingToZero and speed is still not 0, it should still be counted as overspeed until it reaches 0 */
        if (deceleratingToZero && speed > 0) {
            isDirty = true;
            overSpeed = true;
            manualNotch = -3;
            syncFrequency = 2;
            speed = Math.max(speed - emAcceleration, 0);
        } else if (overSpeed) {
            isDirty = true;
            manualNotch = -3;
            syncFrequency = 2;
            speed = (float) Math.max(speed - emAcceleration, getBlocksPerTickSpeed(targetSpeed));
        }

        /* Cancel decelerate to Zero if reached 0km/h */
        if (deceleratingToZero && speed == 0) {
            manualNotch = -2;
            deceleratingToZero = false;
            overSpeed = false;
        }

        if((deceleratingToZero && speed > 0) || overSpeed) {
            canCalculatePhysics = false;
        } else {
            canCalculatePhysics = true;
        }
    }

    @Inject(method = "simulateTrain", at = @At("TAIL"))
    public void JobanSyncTrainToAll(Level world, float ticksElapsed, Depot depot, CallbackInfo ci) {
        /* Sync to all player */
        if (isDirty && MTR.isGameTickInterval(syncFrequency)) {
            isDirty = false;
            for (Player player : world.players()) {
                syncTrainToPlayer((ServerPlayer) player);
            }
        }
    }

    @Inject(method = "simulateTrain", at = @At("TAIL"))
    public void JobanGUI(Level world, float tickElapsed, Depot depot, CallbackInfo ci) {
        if (world.isClientSide() || !this.isCurrentlyManual) return;

        TrainBehavior behavior = getBehaviorConfig(sidingId);
        if(behavior == null) return;

        ServerBossEvent mainBossbar = TrainDrivingModule.mainBossbar.getOrDefault(sidingId, new ServerBossEvent(Component.literal("N/A"), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS));
        mainBossbar.removeAllPlayers();

        ServerBossEvent speedLimitBossbar = TrainDrivingModule.altBossbar.getOrDefault(sidingId, new ServerBossEvent(Component.literal("N/A"), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS));
        speedLimitBossbar.removeAllPlayers();

        ServerBossEvent dwellBossbar = TrainDrivingModule.dwellBossbar.getOrDefault(sidingId, new ServerBossEvent(Component.literal("N/A"), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS));
        dwellBossbar.removeAllPlayers();

        if (this.ridingEntities.isEmpty()) {
            TrainDrivingModule.mainBossbar.remove(sidingId);
            TrainDrivingModule.altBossbar.remove(sidingId);
            TrainDrivingModule.dwellBossbar.remove(sidingId);
            return;
        }

        double currentSpeedKmh = kmh(speed);
        int baseIndex = this.getIndex(0, this.spacing, true);
        double speedLimitKmh = this.path.get(baseIndex).rail.railType.speedLimit;
        double nextStopMeter = distances.get(nextStoppingIndex) - railProgress;

        /* Setup bossbar Color & bossbar */
        BossEvent.BossBarColor mainBossbarColor = (currentSpeedKmh > targetSpeed - 0.5 && currentSpeedKmh < targetSpeed + 0.5) ? BossEvent.BossBarColor.BLUE : currentSpeedKmh < targetSpeed ? BossEvent.BossBarColor.GREEN : currentSpeedKmh > maxTargetSpeed ? BossEvent.BossBarColor.RED : BossEvent.BossBarColor.YELLOW;
        /* Action Bar to represent whether next X path are cleared */
        Component actionBarText = getPathHeadsupText(behavior, baseIndex);

        String bossbarString = behavior.mainBossbarContent
                .replace("{curSpeed}", String.format("%.1f", currentSpeedKmh))
                .replace("{curSpeedMph}", String.format("%.1f", currentSpeedKmh / 1.609344))
                .replace("{curSpeedKt}", String.format("%.1f", currentSpeedKmh / 1.852))
                .replace("{speedLimit}", String.format("%.1f", targetSpeed))
                .replace("{speedLimitMph}", String.format("%.1f", targetSpeed / 1.609344))
                .replace("{speedLimitKt}", String.format("%.1f", targetSpeed / 1.852))
                .replace("{nextStopMeter}", String.format("%.1f", nextStopMeter));

        String dwellString;
        float remainingDwellTick = getTotalDwellTicks() - getElapsedDwellTicks();
        if(remainingDwellTick > 0) {
            dwellString = "Dwell: " + String.format("%.1f", remainingDwellTick / SharedConstants.TICKS_PER_SECOND) + "s / " + String.format("%.1f", (float)(getTotalDwellTicks() / SharedConstants.TICKS_PER_SECOND)) + "s";
        } else {
            dwellString = "Ready to close door and depart!";
        }

        mainBossbar.setName(Component.literal(bossbarString));
        speedLimitBossbar.setName(Component.literal("TARGET: {speedLimit}km/h".replace("{speedLimit}", String.format("%.1f", nextPathSpeedLimit))));
        dwellBossbar.setName(Component.literal(dwellString));
        mainBossbar.setProgress((float) Math.min((currentSpeedKmh / speedLimitKmh), 1));
        speedLimitBossbar.setProgress((float) Math.min((targetSpeed / speedLimitKmh), 1));
        dwellBossbar.setProgress(1 - (Math.max(0, getElapsedDwellTicks() / getTotalDwellTicks())));
        mainBossbar.setColor(mainBossbarColor);

        for (UUID uuid : ridingEntities) {
            ServerPlayer player = (ServerPlayer) world.getPlayerByUUID(uuid);
            if(player == null) continue;

            if (player.isHolding(Items.DRIVER_KEY.get())) {
                if(behavior.showMainBossbar) {
                    mainBossbar.addPlayer(player);
                }
                if(speed > 0 && speedChanging && behavior.showLimitBossBar) {
                    speedLimitBossbar.addPlayer(player);
                }

                if(behavior.showDwellBossBar && getDoorValue() > 0) {
                    dwellBossbar.addPlayer(player);
                }

                if(overSpeed && !behavior.overSpeedTitle.isEmpty()) {
                    player.connection.send(new ClientboundSetTitlesAnimationPacket(0, 20, 10));
                    player.connection.send(new ClientboundSetTitleTextPacket(Component.literal(behavior.overSpeedTitle).withStyle(ChatFormatting.RED)));
                }

                if(actionBarText != null) {
                    player.sendSystemMessage(actionBarText, true);
                }
            }
        }
        TrainDrivingModule.mainBossbar.put(sidingId, mainBossbar);
        TrainDrivingModule.altBossbar.put(sidingId, speedLimitBossbar);
        TrainDrivingModule.dwellBossbar.put(sidingId, dwellBossbar);
    }

    public double kmh(double mtrTrainSpeed) {
        return (mtrTrainSpeed * 20) * 3.6;
    }

    public double getBlocksPerTickSpeed(double speed) {
        return (speed / 20) / 3.6;
    }

    public @Nullable MutableComponent getPathHeadsupText(TrainBehavior behavior, int currentPathIndex) {
        final Component separator = Component.literal(" > ").withStyle(ChatFormatting.GOLD);

        MutableComponent actionBarText;
        if(behavior.nextPathClearance != 0) {
            actionBarText = Component.literal("Current Section").append(separator);
            for(int i = 0; i < behavior.nextPathClearance; i++) {
                int nextIndex = currentPathIndex + i+1;
                if(this.isRailBlocked(nextIndex)) {
                    actionBarText.append(Component.literal("Occupied").withStyle(ChatFormatting.RED));
                } else {
                    actionBarText.append(Component.literal("Cleared").withStyle(ChatFormatting.GREEN));
                }

                // Don't append separator if last
                if(i != behavior.nextPathClearance - 1) {
                    actionBarText.append(separator);
                }
            }
        } else {
            actionBarText = null;
        }
        return actionBarText;
    }

    @Unique
    public void updateTargetSpeed(double factor, double currentRailSpeedKmh, int currentRailIndex, int pathLookup, TrainSound sound, Level world) {
        int nextPathIndex = 0;
        double lowestTarget = 10000;
        /* Find <Path Lookup> path ahead where speed limit will change */
        for(int i = 0; i < pathLookup; i++) {
            int thisPathIndex = currentRailIndex + (i+1);
            /* If it runs out of bound, stop finding the next path */
            if(thisPathIndex >= path.size()) break;
            /* If we're going to stop anyway, don't calculate the speed differences */
            if(thisPathIndex >= nextStoppingIndex) break;

            /* Find lowest speed */
            int thisRailLimit = this.path.get(thisPathIndex).rail.railType.speedLimit;
            final double nodeEndDistance = distances.get(Math.max(0, thisPathIndex - 1)) - this.railProgress;
            final double thisTargetSpeed = jobanCalculateTarget(factor, accelerationConstant, nodeEndDistance, thisRailLimit / 3.6);

            if(thisRailLimit < currentRailSpeedKmh && thisTargetSpeed < lowestTarget) {
                nextPathIndex = thisPathIndex;
                lowestTarget = thisTargetSpeed;
            }
        }

        if(nextPathIndex != 0) {
            nextPathSpeedLimit = this.path.get(nextPathIndex).rail.railType.speedLimit;

            if (lowestTarget - ((accelerationConstant * 20 * 20) * 3.6) < currentRailSpeedKmh) {
                targetSpeed = Math.min(currentRailSpeedKmh, lowestTarget);

                if(!speedChanging) {
                    /* Target speed starts dropping */
                    sound.play(ridingEntities, true, world);
                    speedChanging = true;
                }
            } else {
                targetSpeed = currentRailSpeedKmh;
                speedChanging = false;
            }
        } else {
            targetSpeed = currentRailSpeedKmh;
            speedChanging = false;
        }

        maxTargetSpeed = targetSpeed + OVERSPEED_TOLERANCE_KMH;
    }

    @Unique
    public double jobanCalculateTarget(double factor, double acceleration, double distance, double speed)
    {
        double convertedFactor = factor * 100 * 2;
        double result = Math.sqrt((2 * (acceleration * convertedFactor) * distance) + (speed * speed));
        return result * 3.6;
    }

    @Unique
    public void syncTrainToPlayer(ServerPlayer player) {
        FriendlyByteBuf trainPacket = new FriendlyByteBuf(Unpooled.buffer());
        this.writePacket(trainPacket);

        FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBytes(trainPacket);
        MTRRegistry.sendToPlayer(player, PACKET_UPDATE_TRAINS, packet);
    }

    @Unique
    public TrainBehavior getBehaviorConfig(long sidingId) {
        TrainBehavior behavior = null;

        if(!TrainDrivingModule.sidingToRouteMap.containsKey(sidingId)) {
            return null;
        }

        long currentRouteId = TrainDrivingModule.sidingToRouteMap.get(sidingId);
        for(TrainBehavior trainBehavior : TrainConfig.routeConfig) {
            if(trainBehavior.routeId.contains(currentRouteId)) {
                behavior = trainBehavior;
            }
        }

        return behavior;
    }
}