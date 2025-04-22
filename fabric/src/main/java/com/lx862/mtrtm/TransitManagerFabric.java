package com.lx862.mtrtm;

import net.fabricmc.api.ModInitializer;

public class TransitManagerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        TransitManager.init();
    }
}
