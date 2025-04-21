package com.lx862.jcm.mod.registry;

import com.lx862.jcm.mod.JCMClient;
import com.lx862.jcm.mod.data.JCMServerStats;
import com.lx862.jcm.mod.resources.JCMResourceManager;
import mtr.Registry;
import mtr.RegistryClient;
import mtr.client.CustomResources;

public class Events {
    public static void register() {
        // Start Tick Event for counting tick
        Registry.registerTickEvent((server) -> {
            JCMServerStats.incrementGameTick();
        });
    }

    public static void registerClient() {
        CustomResources.registerReloadListener(resourceManager -> {
            JCMResourceManager.reload();
        });

        RegistryClient.registerTickEvent((minecraft) -> {
            JCMClient.getMcMetaManager().tick();
        });
    }
}
