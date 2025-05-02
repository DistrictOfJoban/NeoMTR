package com.lx862.mtrticket.commands;

import com.lx862.mtrticket.data.Tickets;
import com.lx862.mtrticket.TickManager;
import com.lx862.mtrticket.TicketHandler;
import com.lx862.mtrticket.Util;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.ScoreAccess;

import java.util.Random;

public class BuyTicketsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("buyTickets")
                        .executes(context -> {
                                context.getSource().sendSuccess(() -> Component.literal("The following ticket exists:").withStyle(ChatFormatting.GREEN), false);
                                context.getSource().sendSuccess(() -> Component.literal(String.join(", ", TicketHandler.ticketList.keySet())).withStyle(ChatFormatting.GREEN), false);
                                context.getSource().sendSuccess(() -> Component.literal("To buy a ticket, please type /buyTicket <Ticket name above>").withStyle(ChatFormatting.GOLD), false);
                                return 1;
                        })
                        .then(Commands.argument("ticketName", StringArgumentType.string())
                                .executes(context -> {
                                    if(TicketHandler.ticketList.get(StringArgumentType.getString(context, "ticketName")) == null) {
                                        context.getSource().sendSuccess(() -> Component.literal("Specified ticket not found. The following ticket exists:").withStyle(ChatFormatting.RED), false);
                                        context.getSource().sendSuccess(() -> Component.literal(String.join(", ", TicketHandler.ticketList.keySet())).withStyle(ChatFormatting.GREEN), false);
                                        return 1;
                                    }
                                    return run(context.getSource().getPlayer(), context.getSource().getLevel(), StringArgumentType.getString(context, "ticketName"));
                                }).suggests((commandContext, suggestionsBuilder) -> SharedSuggestionProvider.suggest(TicketHandler.ticketList.keySet(), suggestionsBuilder))
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(context -> {
                                            if(TicketHandler.ticketList.get(StringArgumentType.getString(context, "ticketName")) == null) {
                                                context.getSource().sendSuccess(() -> Component.literal("Specified ticket not found. The following ticket exists:").withStyle(ChatFormatting.RED), false);
                                                context.getSource().sendSuccess(() -> Component.literal(String.join(", ", TicketHandler.ticketList.keySet())).withStyle(ChatFormatting.GREEN), false);
                                                return 1;
                                            }
                                            return run(context.getSource().getPlayer(), context.getSource().getLevel(), StringArgumentType.getString(context, "ticketName"), EntityArgument.getPlayer(context, "target"));
                                        }))
                        )
        );
    }

    public static int run(ServerPlayer player, ServerLevel world, String type, ServerPlayer target) {
        Tickets selectedTicket = TicketHandler.ticketList.get(type);
        int ticketCost = selectedTicket.price();
        int delayMin = selectedTicket.delayMin() * 1000;
        int delayMax = selectedTicket.delayMax() * 1000;
        ScoreAccess balance = Util.getScore("mtr_balance", player.getGameProfile().getName(), world);

        if (selectedTicket.opOnly() && !Util.hasPerm(player)) {
            player.sendSystemMessage(Component.literal("This ticket is out of stock at the moment.").withStyle(ChatFormatting.RED), false);
            return 1;
        }

        if (balance.get() < ticketCost) {
            player.sendSystemMessage(Component.literal("At least $" + ticketCost + " should be in your MTR balance in order to purchase the ticket selected.\n\nPlease obtain emeralds and add balance in Fuka's Automated Teller Machine (ATM).").withStyle(ChatFormatting.RED), false);
            return 1;
        }

        /* Random delay before giving the ticket */
        int delay = (new Random().nextInt(delayMax - delayMin + 1) + delayMin) / 50;
        balance.add(-ticketCost);
        player.sendSystemMessage(Component.literal(String.format("Your ticket is now being generated, please wait anywhere from %d-%d seconds for the process to complete...", delayMin / 1000, delayMax / 1000)).withStyle(ChatFormatting.AQUA), false);

        TickManager.schedule(delay, () -> {
            if (target != null) {
                target.sendSystemMessage(Component.literal(player.getGameProfile().getName() + " has gave you an ticket, hope you have a pleasant journey!").withStyle(ChatFormatting.GREEN), false);
                TicketHandler.giveTicket(target, TicketHandler.ticketList.get(type));
            } else {
                player.sendSystemMessage(Component.literal("Thank you for purchasing your ticket, hope you have a pleasant journey!").withStyle(ChatFormatting.GREEN), false);
                TicketHandler.giveTicket(player, TicketHandler.ticketList.get(type));
            }
        });
        return 1;
    }

    public static int run(ServerPlayer player, ServerLevel world, String type) {
        return run(player, world, type, null);
    }
}
