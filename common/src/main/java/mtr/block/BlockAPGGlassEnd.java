package mtr.block;

import mtr.registry.Items;
import net.minecraft.world.item.Item;

public class BlockAPGGlassEnd extends BlockPSDAPGGlassEndBase {

	@Override
	public Item asItem() {
		return Items.APG_GLASS_END.get();
	}
}
