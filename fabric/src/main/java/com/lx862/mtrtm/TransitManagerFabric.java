package com.lx862.mtrtm;

import com.lx862.mtrtm.mod.TransitManager;
import net.fabricmc.api.ModInitializer;

public class TransitManagerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        TransitManager.init();
    }
}
