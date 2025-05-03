package com.lx862.mtrtdm.mixin;

import com.lx862.mtrtdm.TrainDrivingModule;
import mtr.data.Siding;
import mtr.data.TrainServer;
import net.minecraft.server.level.ServerBossEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;


@Mixin(value = Siding.class, remap = false)
public class SidingMixin {
    @Shadow @Final private Set<TrainServer> trains;

    @Inject(method = "clearTrains", at = @At("TAIL"))
    public void clearTrain(CallbackInfo ci) {
        /* Clear bossbar */
        for(TrainServer train : trains) {
            ServerBossEvent bossbar = TrainDrivingModule.mainBossbar.get(train.sidingId);
            ServerBossEvent altBossbar = TrainDrivingModule.altBossbar.get(train.sidingId);
            ServerBossEvent dwellBossbar = TrainDrivingModule.dwellBossbar.get(train.sidingId);
            if(bossbar == null) return;
            bossbar.removeAllPlayers();
            TrainDrivingModule.mainBossbar.remove(train.sidingId);

            if(altBossbar == null) return;
            altBossbar.removeAllPlayers();
            TrainDrivingModule.altBossbar.remove(train.sidingId);

            if(dwellBossbar == null) return;
            dwellBossbar.removeAllPlayers();
            TrainDrivingModule.dwellBossbar.remove(train.sidingId);
        }
    }
}