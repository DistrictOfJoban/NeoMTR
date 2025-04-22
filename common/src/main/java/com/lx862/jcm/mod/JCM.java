package com.lx862.jcm.mod;

import cn.zbx1425.mtrsteamloco.RegistriesWrapper;
import com.lx862.jcm.mod.config.JCMConfig;
import com.lx862.jcm.mod.registry.*;
import com.lx862.jcm.mod.util.JCMLogger;
import mtr.Keys;
import mtr.registry.MTRAddonRegistry;

public class JCM {
    private static final JCMConfig config = new JCMConfig();
    public static final MTRAddonRegistry.MTRAddon ADDON = new MTRAddonRegistry.MTRAddon(Constants.MOD_ID, "NeoJCM", Constants.MOD_VERSION);

    public static void init(RegistriesWrapper registriesWrapper) {
        try {
            JCMLogger.info("Joban Client Mod v{} @ NeoMTR {}", Constants.MOD_VERSION, Keys.class.getField("MOD_VERSION").get(null));
        } catch (Exception e) {
            JCMLogger.warn("Cannot obtain NeoMTR Version, countdown to disaster...");
        }
        config.read();
        MTRAddonRegistry.registerAddon(ADDON);
        Blocks.register(registriesWrapper);
        BlockEntities.register(registriesWrapper);
        Items.register(registriesWrapper);
        Networking.register();
        Events.register();
    }

    public static JCMConfig getConfig() {
        return config;
    }
}