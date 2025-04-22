package net.londonunderground.blocks;

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

public class StationA2 extends BlockDirectionalMapper {

	public StationA2(BlockBehaviour.Properties settings) {
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
				IBlock.getVoxelShapeByDirection(0, 0, 8, 16, 1, 9, facing),
				IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 7, 8, facing),
				IBlock.getVoxelShapeByDirection(0, 0, 6, 16, 16, 7, facing),
				IBlock.getVoxelShapeByDirection(0, 0, 5, 16, 16, 6, facing),
				IBlock.getVoxelShapeByDirection(0, 0, 4, 16, 16, 5, facing),
				IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 4, facing)
		);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
