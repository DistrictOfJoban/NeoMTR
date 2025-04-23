package top.mcmtr.mod;

import mtr.registry.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;

public interface MSDCreativeModeTabs {
    CreativeModeTabs.Wrapper MSD_BLOCKS = new CreativeModeTabs.Wrapper(MSDMain.id("msd_blocks"), () -> new ItemStack(MSDBlocks.RAILING_STAIR_FLAT.get()));
    CreativeModeTabs.Wrapper MSD_Station_Decoration = new CreativeModeTabs.Wrapper(MSDMain.id("msd_station_blocks"), () -> new ItemStack(MSDBlocks.DECORATION_STAIR.get()));
}