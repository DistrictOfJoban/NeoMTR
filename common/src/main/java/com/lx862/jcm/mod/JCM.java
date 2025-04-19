package com.lx862.jcm.mod;

import cn.zbx1425.mtrsteamloco.RegistriesWrapper;
import com.lx862.jcm.mod.registry.BlockEntities;
import com.lx862.jcm.mod.registry.Blocks;
import com.lx862.jcm.mod.registry.Items;
import com.lx862.jcm.mod.util.JCMLogger;
import mtr.Keys;

public class JCM {
    public static void init(RegistriesWrapper registriesWrapper) {
        try {
            JCMLogger.info("Joban Client Mod v{} @ NeoMTR {}", Constants.MOD_VERSION, Keys.class.getField("MOD_VERSION").get(null));
        } catch (Exception e) {
            JCMLogger.warn("Cannot obtain NeoMTR Version, countdown to disaster...");
        }
        Blocks.register(registriesWrapper);
        BlockEntities.register(registriesWrapper);
        Items.register(registriesWrapper);
//        Networking.register();
//        Events.register();
    }
}