package cn.zbx1425.mtrsteamloco.mixin;

import cn.zbx1425.mtrsteamloco.gui.BrushEditRailScreen;
import cn.zbx1425.mtrsteamloco.network.PacketScreen;
import mtr.item.ItemWithCreativeTabBase;
import mtr.registry.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemWithCreativeTabBase.class)
public abstract class ItemWithCreativeTabBaseMixin extends Item {

    public ItemWithCreativeTabBaseMixin(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (this == Items.BRUSH.get()) {
            Level level = context.getLevel();
            BlockState blockState = level.getBlockState(context.getClickedPos());
            if (blockState.getBlock() instanceof mtr.block.BlockNode) {
                if (context.isSecondaryUseActive()) {
                    if (level.isClientSide) {
                        BrushEditRailScreen.acquirePickInfoWhenUse();
                        return super.useOn(context);
                    } else {
                        PacketScreen.sendScreenBlockS2C((ServerPlayer) context.getPlayer(), "brush_edit_rail", BlockPos.ZERO);
                    }
                } else {
                    if (level.isClientSide) {
                        BrushEditRailScreen.acquirePickInfoWhenUse();
                        CompoundTag railBrushProp = context.getPlayer().getMainHandItem().getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                        BrushEditRailScreen.applyBrushToPickedRail(railBrushProp, true);
                    } else {
                        return super.useOn(context);
                    }
                }
                return InteractionResult.SUCCESS;
            } else {
                return super.useOn(context);
            }
        } else {
            return super.useOn(context);
        }
    }
}