package net.londonunderground.blocks;

import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.londonunderground.registry.BlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockRoundelStationTop extends BlockRoundelBase {


	public BlockRoundelStationTop(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 16, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityBlockRoundelStationTop(pos, state);
	}

	public static class TileEntityBlockRoundelStationTop extends TileEntityBlockRoundelBase {

		public TileEntityBlockRoundelStationTop(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.BLOCK_ROUNDEL_STATION_TOP_TILE_ENTITY.get(), pos, state);
		}

		@Override
		public boolean shouldRender() {
			return true;
		}
	}
}
