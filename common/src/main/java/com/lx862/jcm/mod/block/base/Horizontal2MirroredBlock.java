package com.lx862.jcm.mod.block.base;

import com.lx862.jcm.mod.util.BlockUtil;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Cheap way for a horizontal multi block, with 2 same block facing each other.
 */
public abstract class Horizontal2MirroredBlock extends DirectionalBlock {
    public Horizontal2MirroredBlock(Properties settings) {
        super(settings);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction placeDirection = ctx.getHorizontalDirection().getOpposite();
        return BlockUtil.isReplacable(ctx.getLevel(), ctx.getClickedPos(), placeDirection, ctx, 2) && super.getStateForPlacement(ctx) != null ? super.getStateForPlacement(ctx).setValue(FACING, placeDirection) : null;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if(world.isClientSide()) return;

        Direction facing = IBlock.getStatePropertySafe(state, FACING);
        world.setBlock(pos.relative(facing), state.setValue(FACING, facing.getOpposite()), 3);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if(IBlock.getStatePropertySafe(state, FACING) == direction && !(neighborState.is(this))) {
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    public BlockPos[] getAllPos(BlockState state, Level world, BlockPos pos) {
        Direction facing = IBlock.getStatePropertySafe(state, FACING);
        BlockPos otherPos = pos.relative(facing);
        return new BlockPos[]{ pos, otherPos };
    }
}