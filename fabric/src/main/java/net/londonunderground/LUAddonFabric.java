package net.londonunderground;

import net.fabricmc.api.ModInitializer;
import net.londonunderground.mod.LUAddon;

public class LUAddonFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		LUAddon.init();
	}
}
