package com.lx862.mtrtdm.commands;

import com.lx862.mtrtdm.data.ExposedTrainData;
import com.lx862.mtrtdm.TrainDrivingModule;
import com.lx862.mtrtdm.mixin.SidingAccessorMixin;
import com.lx862.mtrtdm.mixin.TrainAccessorMixin;
import com.lx862.mtrtdm.mixin.TrainServerAccessorMixin;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import mtr.data.*;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class BreakTrainCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("breaktrain")
                .requires(ctx -> ctx.hasPermission(4))
                    .then(Commands.argument("player", EntityArgument.player())
                    .then(Commands.argument("reason", StringArgumentType.greedyString())
                    .executes(context -> {
                        ServerPlayer player = EntityArgument.getPlayer(context, "player");
                        String reason = StringArgumentType.getString(context, "reason");
                        RailwayData data = RailwayData.getInstance(context.getSource().getLevel());

                        ExposedTrainData trainData = null;

                        /* Loop through each siding */
                        for(Siding siding : data.sidings) {
                            /* Loop through each train in each siding */
                            for(TrainServer train : ((SidingAccessorMixin)siding).getTrains()) {
                                if(((TrainAccessorMixin) train).getRidingEntities().contains(player.getUUID())) {
                                    trainData = new ExposedTrainData(train, ((TrainServerAccessorMixin)train).getRouteId(), null, ((TrainAccessorMixin)train).getIsManualAllowed());
                                    break;
                                }
                            }
                        }

                        if(trainData == null) {
                            context.getSource().sendSuccess(() -> Component.literal("Cannot find trains that player is onboard.").withStyle(ChatFormatting.RED), false);
                            return 1;
                        }

                        if(trainData.train.isCurrentlyManual()) {
                            TrainDrivingModule.brokendownTrain.put(trainData.train.sidingId, reason);
                            context.getSource().sendSuccess(() -> Component.literal("Train broken down.").withStyle(ChatFormatting.GOLD), false);
                        } else {
                            context.getSource().sendSuccess(() -> Component.literal("Train not in manual.").withStyle(ChatFormatting.GOLD), false);
                        }
                        return 1;
                    })))
        );
    }
}
