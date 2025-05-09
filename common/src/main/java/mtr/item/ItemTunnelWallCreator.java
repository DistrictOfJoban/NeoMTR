package mtr.item;

import mtr.data.RailwayData;
import mtr.data.RailwayDataRailActionsModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class ItemTunnelWallCreator extends ItemNodeModifierSelectableBlockBase {

	public ItemTunnelWallCreator(int height, int width) {
		super(true, height, width);
	}

	@Override
	protected boolean onConnect(Player player, ItemStack stack, RailwayData railwayData, BlockPos posStart, BlockPos posEnd, int radius, int height) {
		final BlockState state = getSavedState(stack);
		final RailwayDataRailActionsModule railActionsModule = railwayData.getModule(RailwayDataRailActionsModule.NAME);
		return state == null || railActionsModule.markRailForTunnelWall(player, posStart, posEnd, radius, height, state);
	}
}
