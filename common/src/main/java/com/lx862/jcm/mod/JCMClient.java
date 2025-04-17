package com.lx862.jcm.mod;

import com.lx862.jcm.mod.config.ClientConfig;
import com.lx862.jcm.mod.registry.BlockEntityRenderers;
import com.lx862.jcm.mod.registry.Blocks;
import com.lx862.jcm.mod.registry.Events;
import com.lx862.jcm.mod.render.gui.screen.ClientConfigScreen;
import com.lx862.jcm.mod.resources.mcmeta.McMetaManager;
import mtr.registry.Networking;
import net.minecraft.client.gui.screens.Screen;

public class JCMClient {
    private static final McMetaManager mcMetaManager = new McMetaManager();
    private static final ClientConfig config = new ClientConfig();

    public static void initializeClient() {
        config.read();

        // Registry
        Events.registerClient();
        Blocks.registerClient();
        BlockEntityRenderers.registerClient();
        Networking.registerClient();
    }

    public static Screen getClientConfigScreen(Screen previousScreen) {
        return new ClientConfigScreen().withPreviousScreen(previousScreen);
    }

    public static ClientConfig getConfig() {
        return config;
    }

    public static McMetaManager getMcMetaManager() {
        return mcMetaManager;
    }
}