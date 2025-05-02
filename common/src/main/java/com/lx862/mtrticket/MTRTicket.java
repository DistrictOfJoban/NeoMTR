package com.lx862.mtrticket;

import com.lx862.mtrticket.commands.BuyTicketsCommand;
import com.lx862.mtrticket.commands.ReloadTicketCommand;
import com.lx862.mtrticket.config.TicketConfig;
import com.mojang.brigadier.CommandDispatcher;
import mtr.registry.MTRAddonRegistry;

import net.minecraft.commands.CommandSourceStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.function.Consumer;

public class MTRTicket {
    public static final String MOD_ID = "mtrticket";
    public static final String NAME = "MTR: Transport Tickets";
    public static final Logger LOGGER = LogManager.getLogger(NAME);
    private static final MTRAddonRegistry.MTRAddon ADDON = new MTRAddonRegistry.MTRAddon(MOD_ID, NAME, "1.0.0");

    public static void initialize(Path configDirectory, Consumer<Consumer<CommandDispatcher<CommandSourceStack>>> registerCommand) {
        TicketConfig.load(configDirectory);

        registerCommand.accept(dispatcher -> {
            BuyTicketsCommand.register(dispatcher);
            ReloadTicketCommand.register(dispatcher);
        });

        MTRAddonRegistry.registerAddon(ADDON);
    }
}
