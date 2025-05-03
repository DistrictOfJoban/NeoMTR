package com.lx862.mtrtdm.mixin;

import mtr.data.Train;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;
import java.util.UUID;


@Mixin(value = Train.class, remap = false)
public interface TrainAccessorMixin {
    @Accessor
    boolean getReversed();

    @Accessor
    boolean getIsManualAllowed();

    @Accessor
    boolean getIsCurrentlyManual();

    @Accessor
    int getManualNotch();

    @Accessor
    int getManualToAutomaticTime();

    @Accessor
    Set<UUID> getRidingEntities();

    @Invoker("getRoutePosition")
    Vec3 getTheRoutePosition(double offset);
}
