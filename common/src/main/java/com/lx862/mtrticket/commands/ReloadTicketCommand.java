package com.lx862.mtrticket.commands;

import com.lx862.mtrticket.config.TicketConfig;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ReloadTicketCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ticketReload")
            .requires(ctx -> ctx.hasPermission(2))
            .executes(context -> {
                    TicketConfig.load(context.getSource().getServer().getServerDirectory().resolve("config"));
                    context.getSource().sendSuccess(() -> Component.literal("Ticket reloaded.").withStyle(ChatFormatting.GREEN), false);
                    return 1;
            })
        );
    }
}
