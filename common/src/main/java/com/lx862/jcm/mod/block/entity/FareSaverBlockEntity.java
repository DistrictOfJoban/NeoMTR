package com.lx862.jcm.mod.block.entity;

import com.lx862.jcm.mod.registry.BlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class FareSaverBlockEntity extends JCMBlockEntity {
    private String prefix = "$";
    private int discount = 2;
    public FareSaverBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntities.FARE_SAVER.get(), blockPos, blockState);
    }

    @Override
    public void readCompoundTag(CompoundTag compoundTag) {
        super.readCompoundTag(compoundTag);
        this.discount = compoundTag.getInt("discount");
        this.prefix = compoundTag.contains("currency") ? compoundTag.getString("currency") : "$";
    }

    @Override
    public void writeCompoundTag(CompoundTag compoundTag) {
        super.writeCompoundTag(compoundTag);
        compoundTag.putInt("discount", discount);
        compoundTag.putString("prefix", prefix);
    }

    public void setData(String prefix, int discount) {
        this.prefix = prefix;
        this.discount = discount;
        this.setChanged();
        this.syncData();
    }

    public int getDiscount() {
        return discount;
    }

    public String getPrefix() {
        return prefix;
    }
}
