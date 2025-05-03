package com.lx862.mtrtdm;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;


public class TrainDrivingModuleFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        TrainDrivingModule.initialize(FabricLoader.getInstance().getConfigDir());

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, third) -> {
            TDMCommands.registerCommands(dispatcher);
        });

        /* EVENTS REGISTRATION */
        ServerTickEvents.START_SERVER_TICK.register(TDMEvents::onServerTick);
    }
}
