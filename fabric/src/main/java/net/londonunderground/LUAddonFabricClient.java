package net.londonunderground;

import net.fabricmc.api.ClientModInitializer;
import net.londonunderground.mod.LUAddonClient;

public class LUAddonFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		LUAddonClient.init();
	}
}
