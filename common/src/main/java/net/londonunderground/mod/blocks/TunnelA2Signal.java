package net.londonunderground.mod.blocks;

import mtr.block.BlockSignalLightBase;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.londonunderground.mod.registry.BlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TunnelA2Signal extends BlockSignalLightBase {

	public TunnelA2Signal(BlockBehaviour.Properties settings) {
		super(settings, 2, 14);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 12, 16, 16, facing);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityTunnelSignalLight1(pos, state);
	}

	public static class TileEntityTunnelSignalLight1 extends BlockEntityMapper {

		public TileEntityTunnelSignalLight1(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TUNNEL_A2_SIGNAL.get(), pos, state);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getClockWise()).setValue(IS_45, EnumBooleanInverted.FALSE).setValue(IS_22_5, EnumBooleanInverted.FALSE);
	}
}
