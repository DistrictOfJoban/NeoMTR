package com.lx862.jcm.mod.block.entity;

import com.lx862.jcm.mod.registry.BlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class RVPIDSBlockEntity extends PIDSBlockEntity {

    public RVPIDSBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntities.RV_PIDS.get(), blockPos, blockState);
    }

    @Override
    public String getPIDSType() {
        return "rv_pids";
    }

    @Override
    public String getDefaultPresetId() {
        return "rv_pids";
    }

    @Override
    public int getRowAmount() {
        return 4;
    }
}
