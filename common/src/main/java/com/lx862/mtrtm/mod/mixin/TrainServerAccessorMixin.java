package com.lx862.mtrtm.mod.mixin;

import mtr.data.TrainServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;


@Mixin(value = TrainServer.class, remap = false)
public interface TrainServerAccessorMixin {
    @Accessor
    long getRouteId();

    @Accessor
    int getManualCoolDown();

    @Accessor
    void setManualCoolDown(int manualCooldown);
}
