package net.londonunderground.mod.blocks;

import mtr.block.IBlock;
import mtr.mappings.BlockDirectionalMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StationA1 extends BlockDirectionalMapper {

	public StationA1(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		return Shapes.or(
				IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 1, 15, facing),
				IBlock.getVoxelShapeByDirection(0, 1, 0, 16, 3, 14, facing),
				IBlock.getVoxelShapeByDirection(0, 3, 0, 16, 5, 13, facing),
				IBlock.getVoxelShapeByDirection(0, 5, 0, 16, 7, 12, facing),
				IBlock.getVoxelShapeByDirection(0, 7, 0, 16, 9, 11, facing),
				IBlock.getVoxelShapeByDirection(0, 9, 0, 16, 12, 10, facing),
				IBlock.getVoxelShapeByDirection(0, 12, 0, 16, 16, 9, facing)
		);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}


}
