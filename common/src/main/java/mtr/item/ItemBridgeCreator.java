package mtr.item;

import mtr.data.RailwayData;
import mtr.data.RailwayDataRailActionsModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class ItemBridgeCreator extends ItemNodeModifierSelectableBlockBase {

	public ItemBridgeCreator(int width) {
		super(true, 0, width);
	}

	@Override
	protected boolean onConnect(Player player, ItemStack stack, RailwayData railwayData, BlockPos posStart, BlockPos posEnd, int radius, int height) {
		final BlockState state = getSavedState(stack);
		final RailwayDataRailActionsModule railActionsModule = railwayData.getModule(RailwayDataRailActionsModule.NAME);
		return state == null || railActionsModule.markRailForBridge(player, posStart, posEnd, radius, state);
	}
}
