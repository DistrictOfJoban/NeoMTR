package net.londonunderground.blocks;

import mtr.block.BlockPIDSBaseHorizontal;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.londonunderground.registry.BlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NorthernLinePIDS extends BlockPIDSBaseHorizontal {

	private static final int MAX_ARRIVALS = 3;

	public NorthernLinePIDS() {
		super();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		return Shapes.or(
				IBlock.getVoxelShapeByDirection(6, 0, 0, 10, 9, 16, IBlock.getStatePropertySafe(state, FACING)),
				IBlock.getVoxelShapeByDirection(7.5, 9, 12.5, 8.5, 16, 13.5, IBlock.getStatePropertySafe(state, FACING))
		);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityNorthernLinePIDS(pos, state);
	}

	public static class TileEntityNorthernLinePIDS extends TileEntityBlockPIDSBaseHorizontal {

		public TileEntityNorthernLinePIDS(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.PIDS_NORTHERN_TILE_ENTITY.get(), pos, state);
		}

		@Override
		public int getMaxArrivals() {
			return MAX_ARRIVALS;
		}
	}
}