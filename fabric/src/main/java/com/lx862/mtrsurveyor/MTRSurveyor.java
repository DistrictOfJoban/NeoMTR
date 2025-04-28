package com.lx862.mtrsurveyor;

import mtr.data.RailwayData;
import mtr.registry.MTRAddonRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MTRSurveyor implements ModInitializer {

    public static final String SURVEYOR_MOD_ID = "surveyor";
    public static final String MOD_ID = "mtr_surveyor";
    public static final String MOD_NAME = "MTR: Surveyor Integration";
    public static final MTRAddonRegistry.MTRAddon ADDON = new MTRAddonRegistry.MTRAddon(MOD_ID, MOD_NAME, "1.0.0");
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitialize() {
        if(FabricLoader.getInstance().isModLoaded(SURVEYOR_MOD_ID)) {
            MTRLandmarkComponentTypes.init();
            RailwayData.registerRailwayModule(SurveyorRailwayModule::new);
            MTRAddonRegistry.registerAddon(ADDON);
        } else {
            LOGGER.info("[{}] Cannot find Surveyor Map Framework, not loading.", MOD_NAME);
        }
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
