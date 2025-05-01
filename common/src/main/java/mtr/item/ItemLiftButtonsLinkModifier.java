package mtr.item;

import mtr.block.IBlock;
import mtr.registry.CreativeModeTabs;
import mtr.block.BlockLiftButtons;
import mtr.block.BlockLiftPanelBase;
import mtr.block.BlockLiftTrackFloor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ItemLiftButtonsLinkModifier extends ItemBlockClickingBase {

	private final boolean isConnector;

	public ItemLiftButtonsLinkModifier(boolean isConnector) {
		super(CreativeModeTabs.ESCALATORS_LIFTS, properties -> properties.stacksTo(1));
		this.isConnector = isConnector;
	}

	@Override
	protected void onStartClick(UseOnContext context, CompoundTag compoundTag) {
	}

	@Override
	protected void onEndClick(UseOnContext context, BlockPos posEnd, CompoundTag compoundTag) {
		final Level world = context.getLevel();
		final BlockPos posStart = context.getClickedPos();
		final Block blockStart = world.getBlockState(posStart).getBlock();
		final Block blockEnd = world.getBlockState(posEnd).getBlock();

		if (blockStart instanceof BlockLiftTrackFloor && blockEnd instanceof BlockLiftButtons || blockStart instanceof BlockLiftButtons && blockEnd instanceof BlockLiftTrackFloor || blockStart instanceof BlockLiftTrackFloor && blockEnd instanceof BlockLiftPanelBase || blockStart instanceof BlockLiftPanelBase && blockEnd instanceof BlockLiftTrackFloor) {
			final BlockPos posFloor;
			final BlockPos posButtons;
			if (blockStart instanceof BlockLiftTrackFloor) {
				posFloor = posStart;
				posButtons = posEnd;
			} else {
				posFloor = posEnd;
				posButtons = posStart;
			}

			final BlockState buttonState = world.getBlockState(posButtons);
			final BlockEntity blockEntity = world.getBlockEntity(posButtons);
			if (blockEntity instanceof BlockLiftButtons.TileEntityLiftButtons) {
				((BlockLiftButtons.TileEntityLiftButtons) blockEntity).registerFloor(posFloor, isConnector);
			}

			if (blockEntity instanceof BlockLiftPanelBase.TileEntityLiftPanel1Base panelBlockEntity) {
				final Direction panelFacing = IBlock.getStatePropertySafe(buttonState, HorizontalDirectionalBlock.FACING);
				BlockPos leftSide = posButtons.relative(panelFacing.getCounterClockWise());
				BlockState leftSideState = world.getBlockState(leftSide);
				BlockPos rightSide = posButtons.relative(panelFacing.getClockWise());
				BlockState rightSideState = world.getBlockState(rightSide);

				final BlockPos blockToPropergate;

				if(leftSideState.getBlock() instanceof BlockLiftPanelBase) {
					blockToPropergate = leftSide;
				} else if(rightSideState.getBlock() instanceof BlockLiftPanelBase) {
					blockToPropergate = rightSide;
				} else {
					blockToPropergate = null;
				}

				panelBlockEntity.registerFloor(posFloor, isConnector);

				if(blockToPropergate != null) {
					BlockEntity be = world.getBlockEntity(blockToPropergate);
					if(be instanceof BlockLiftPanelBase.TileEntityLiftPanel1Base panelBlockEntity2) {
						panelBlockEntity2.registerFloor(posFloor, isConnector);
					}
				}
			}
		}
	}

	@Override
	protected boolean clickCondition(UseOnContext context) {
		final Block block = context.getLevel().getBlockState(context.getClickedPos()).getBlock();
		return block instanceof BlockLiftTrackFloor || block instanceof BlockLiftButtons || block instanceof BlockLiftPanelBase;
	}
}
