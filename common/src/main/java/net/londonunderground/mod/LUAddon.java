package net.londonunderground.mod;

import mtr.registry.MTRAddonRegistry;
import net.londonunderground.mod.registry.*;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class LUAddon {

	public static final String MOD_ID = "londonunderground";
	public static final Logger LOGGER = LogManager.getLogger("MTRLondonUndergroundAddon");
	public static final MTRAddonRegistry.MTRAddon ADDON = new MTRAddonRegistry.MTRAddon(MOD_ID, "London Underground", "4.0.0-prerelease.1");

	public static void init() {
		Blocks.init();
		BlockEntityTypes.init();
		SoundEvents.init();
		MTRAddonRegistry.registerAddon(ADDON);
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
