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

public class StationA6 extends BlockDirectionalMapper {

	public StationA6(BlockBehaviour.Properties settings) {
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
				IBlock.getVoxelShapeByDirection(0, 4, 0, 16, 6, 2, facing),
				IBlock.getVoxelShapeByDirection(0, 6, 0, 16, 7, 3, facing),
				IBlock.getVoxelShapeByDirection(0, 7, 0, 16, 8, 4, facing),
				IBlock.getVoxelShapeByDirection(0, 8, 0, 16, 9, 5, facing),
				IBlock.getVoxelShapeByDirection(0, 10, 0, 16, 12, 7, facing),
				IBlock.getVoxelShapeByDirection(0, 14, 0, 16, 15, 10, facing),
				IBlock.getVoxelShapeByDirection(0, 12, 0, 16, 13, 8, facing),
				IBlock.getVoxelShapeByDirection(0, 15, 0, 16, 16, 11, facing),
				IBlock.getVoxelShapeByDirection(0, 13, 0, 16, 14, 9, facing),
				IBlock.getVoxelShapeByDirection(0, 9, 0, 16, 10, 6, facing),
				IBlock.getVoxelShapeByDirection(0, 2, 0, 16, 4, 1, facing)
		);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
