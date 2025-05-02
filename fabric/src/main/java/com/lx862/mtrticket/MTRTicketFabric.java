package com.lx862.mtrticket;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;

public class MTRTicketFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        MTRTicket.initialize(FabricLoader.getInstance().getConfigDir(), (cb) -> {
            net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, third) -> cb.accept(dispatcher));
        });

        /* EVENTS REGISTRATION */
        ServerTickEvents.START_SERVER_TICK.register(TickManager::onTick);
        UseBlockCallback.EVENT.register(TicketHandler::tap);
    }
}
