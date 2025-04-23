package com.lx862.jcm;

import com.lx862.jcm.loader.neoforge.JCMRegistryImpl;
import net.neoforged.bus.api.IEventBus;

public class JCMForge {
    public static void init(IEventBus eventBus) {
        JCMRegistryImpl.registerAllDeferred(eventBus);
    }
}
