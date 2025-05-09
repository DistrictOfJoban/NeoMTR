package com.lx862.jcm.mod.block.entity;

import com.lx862.jcm.mod.registry.BlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class LCDPIDSBlockEntity extends PIDSBlockEntity {
    public LCDPIDSBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntities.LCD_PIDS.get(), blockPos, blockState);
    }

    @Override
    public String getPIDSType() {
        return "lcd_pids";
    }

    @Override
    public String getDefaultPresetId() {
        return "lcd_pids";
    }

    @Override
    public int getRowAmount() {
        return 4;
    }
}
