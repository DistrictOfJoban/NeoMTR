package mtr.block;

import mtr.data.RailAngle;
import mtr.mappings.BlockDirectionalMapper;
import mtr.mappings.EntityBlockMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class BlockSignalLightBase extends BlockDirectionalMapper implements IBlock, EntityBlockMapper {
	private final int shapeX;
	private final int shapeHeight;

	public BlockSignalLightBase(Properties settings, int shapeX, int shapeHeight) {
		super(settings);
		this.shapeX = shapeX;
		this.shapeHeight = shapeHeight;
	}

	// TODO backwards compatibility
	@Deprecated
	public BlockSignalLightBase(Properties settings) {
		this(settings, 2, 14);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final int quadrant = RailAngle.getQuadrant(ctx.getRotation(), true);
		return defaultBlockState().setValue(FACING, Direction.from2DDataValue(quadrant / 4)).setValue(IS_22_5, EnumBooleanInverted.fromBoolean(quadrant % 2 == 1)).setValue(IS_45, EnumBooleanInverted.fromBoolean(quadrant % 4 >= 2));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		return Block.box(shapeX, 0, shapeX, 16 - shapeX, shapeHeight, 16 - shapeX);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, IS_22_5, IS_45);
	}
}
