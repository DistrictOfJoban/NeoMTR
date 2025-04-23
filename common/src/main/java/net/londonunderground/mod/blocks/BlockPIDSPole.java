package net.londonunderground.mod.blocks;

import mtr.block.BlockPIDSBaseHorizontal;
import mtr.block.BlockPoleCheckBase;
import mtr.block.IBlock;
import mtr.mappings.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockPIDSPole extends BlockPoleCheckBase {

	public BlockPIDSPole(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		return IBlock.getVoxelShapeByDirection(7.5, 0, 12.5, 8.5, 16, 13.5, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	protected boolean isBlock(Block block) {
		return block instanceof BlockPIDSBaseHorizontal || block instanceof BlockPIDSPole;
	}

	@Override
	protected Component getTooltipBlockText() {
		return Text.translatable("block.mtr.pids_1");
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
