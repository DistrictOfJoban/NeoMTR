package com.lx862.mtrtdm.mixin;

import com.lx862.mtrtdm.TrainDrivingModule;
import mtr.data.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Mixin(value = TrainServer.class, remap = false)
public class TrainServerMixin {
    @Shadow private int manualCoolDown;

    @Inject(method = "simulateTrain", at = @At("HEAD"))
    public void JobanBreakTrainServer(Level world, float ticksElapsed, Depot depot, DataCache dataCache, List<Map<UUID, Long>> trainPositions, Map<Player, Set<TrainServer>> trainsInPlayerRange, Map<Long, List<ScheduleEntry>> schedulesForPlatform, Map<Long, Map<BlockPos, TrainDelay>> trainDelays, CallbackInfoReturnable<Boolean> cir) {
        if (!TrainDrivingModule.brokendownTrain.containsKey(((Train)((TrainServer)(Object)this)).sidingId)) return;
        // Don't let manual driving time run out, the train is "broke" so it shouldn't run
        manualCoolDown = 0;
    }
}