package mtr.item;

import mtr.data.RailwayData;
import mtr.data.RailwayDataRailActionsModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemTunnelCreator extends ItemNodeModifierSelectableBlockBase {

	public ItemTunnelCreator(int height, int width) {
		super(false, height, width);
	}

	@Override
	protected boolean onConnect(Player player, ItemStack stack, RailwayData railwayData, BlockPos posStart, BlockPos posEnd, int radius, int height) {
		final RailwayDataRailActionsModule railActionsModule = railwayData.getModule(RailwayDataRailActionsModule.NAME);
		return railActionsModule.markRailForTunnel(player, posStart, posEnd, radius, height);
	}
}
