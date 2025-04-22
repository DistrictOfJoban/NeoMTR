package net.londonunderground;

import net.fabricmc.api.ClientModInitializer;

public class LondonUndergroundFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		LondonUndergroundClient.init();
	}
}
