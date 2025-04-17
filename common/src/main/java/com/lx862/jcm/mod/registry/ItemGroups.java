package com.lx862.jcm.mod.registry;

import com.lx862.jcm.mod.Constants;
import mtr.registry.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;

public class ItemGroups {
    public static CreativeModeTabs.Wrapper MAIN = new CreativeModeTabs.Wrapper(Constants.id("main"), () -> new ItemStack(Blocks.MTR_STAIRS.get()));
    public static CreativeModeTabs.Wrapper PIDS = new CreativeModeTabs.Wrapper(Constants.id("pids"), () -> new ItemStack(Blocks.RV_PIDS.get()));
}
