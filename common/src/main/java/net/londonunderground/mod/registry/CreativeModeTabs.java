package net.londonunderground.mod.registry;

import net.londonunderground.mod.LUAddon;
import net.minecraft.world.item.ItemStack;

public final class CreativeModeTabs {

	public static mtr.registry.CreativeModeTabs.Wrapper TFL_BLOCKS = new mtr.registry.CreativeModeTabs.Wrapper(LUAddon.id("tfl_blocks"), () -> new ItemStack(Blocks.ROUNDEL_POLE.get()));
	public static mtr.registry.CreativeModeTabs.Wrapper TFL_STATION = new mtr.registry.CreativeModeTabs.Wrapper(LUAddon.id("tfl_station"), () -> new ItemStack(Blocks.STATION_A1.get()));
	public static mtr.registry.CreativeModeTabs.Wrapper TFL_SIGNS = new mtr.registry.CreativeModeTabs.Wrapper(LUAddon.id("tfl_signs"), () -> new ItemStack(Blocks.BLOCK_ROUNDEL_1.get()));
}
