package net.londonunderground.blocks;

import mtr.block.BlockSignalLightBase;
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

public class BlockTunnelSignal extends BlockSignalLightBase {

	public BlockTunnelSignal(BlockBehaviour.Properties settings) {
		super(settings, 2, 14);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 16, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getClockWise()).setValue((IS_45), EnumBooleanInverted.FALSE).setValue((IS_22_5), EnumBooleanInverted.FALSE);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityTunnelSignalLight1(pos, state);
	}

	public static class TileEntityTunnelSignalLight1 extends BlockEntityMapper {

		public TileEntityTunnelSignalLight1(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TUNNEL_BLOCK_2_SIGNAL.get(), pos, state);
		}
	}
}
