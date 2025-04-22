package net.londonunderground.blocks;

import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.londonunderground.registry.BlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockRoundel3BigEven extends BlockRoundelBase {

	public BlockRoundel3BigEven(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final Direction side = ctx.getClickedFace();
		if (side != Direction.UP && side != Direction.DOWN) {
			return defaultBlockState().setValue(FACING, side.getOpposite());
		} else {
			return null;
		}
	}


	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 1, IBlock.getStatePropertySafe(state, FACING));
	}


	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityBlockRoundel3BigEven(pos, state);
	}

	public static class TileEntityBlockRoundel3BigEven extends TileEntityBlockRoundelBase {

		public TileEntityBlockRoundel3BigEven(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.BLOCK_ROUNDEL3_BIG_EVEN_TILE_ENTITY.get(), pos, state);
		}

		@Override
		public boolean shouldRender() {
			return true;
		}
	}
}
