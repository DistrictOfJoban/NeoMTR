package com.lx862.jcm.mod;

import com.lx862.jcm.mod.config.JCMClientConfig;
import com.lx862.jcm.mod.registry.BlockEntityRenderers;
import com.lx862.jcm.mod.registry.Blocks;
import com.lx862.jcm.mod.registry.Events;
import com.lx862.jcm.mod.registry.Networking;
import com.lx862.jcm.mod.render.gui.screen.ClientConfigScreen;
import com.lx862.jcm.mod.resources.mcmeta.McMetaManager;
import mtr.MTRClient;
import net.minecraft.client.gui.screens.Screen;

public class JCMClient {
    private static final McMetaManager mcMetaManager = new McMetaManager();
    private static final JCMClientConfig config = new JCMClientConfig();

    public static void initializeClient() {
        config.read();

        // Registry
        Blocks.registerClient();
        BlockEntityRenderers.registerClient();
        Networking.registerClient();
        Events.registerClient();

        MTRClient.registerAddonConfigGUICallback(JCM.ADDON, JCMClient::getClientConfigScreen);
    }

    public static Screen getClientConfigScreen(Screen previousScreen) {
        return new ClientConfigScreen().withPreviousScreen(previousScreen);
    }

    public static JCMClientConfig getConfig() {
        return config;
    }

    public static McMetaManager getMcMetaManager() {
        return mcMetaManager;
    }
}