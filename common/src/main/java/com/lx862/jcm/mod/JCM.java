package com.lx862.jcm.mod;

import com.lx862.jcm.mod.config.JCMConfig;
import com.lx862.jcm.mod.registry.*;
import com.lx862.jcm.mod.util.JCMLogger;
import mtr.Keys;
import mtr.loader.MTRRegistry;
import mtr.registry.MTRAddonRegistry;

public class JCM {
    public static final MTRAddonRegistry.MTRAddon ADDON = new MTRAddonRegistry.MTRAddon(Constants.MOD_ID, "NeoJCM", Constants.MOD_VERSION);
    private static final JCMConfig config = new JCMConfig();

    public static void init() {
        try {
            JCMLogger.info("Joban Client Mod v{} @ NeoMTR {}", Constants.MOD_VERSION, Keys.class.getField("MOD_VERSION").get(null));
        } catch (Exception e) {
            JCMLogger.warn("Cannot obtain NeoMTR Version, countdown to disaster...");
        }
        MTRAddonRegistry.registerAddon(ADDON);
        Blocks.register();
        BlockEntities.register();
        Items.register();
        Networking.register();
        Events.register();

        MTRRegistry.registerServerStartingEvent(minecraftServer -> {
            config.read(minecraftServer.getServerDirectory().resolve("config").resolve("jsblock.json"));
        });
    }

    public static JCMConfig getConfig() {
        return config;
    }
}