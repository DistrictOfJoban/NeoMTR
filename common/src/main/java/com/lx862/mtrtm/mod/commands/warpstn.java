package com.lx862.mtrtm.mod.commands;

import com.lx862.mtrtm.mod.MtrUtil;
import com.lx862.mtrtm.mod.Util;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import mtr.data.RailwayData;
import mtr.data.Station;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.List;

public class warpstn {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("warpstn")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("name", StringArgumentType.greedyString())
                        .suggests((commandContext, suggestionsBuilder) -> {
                            RailwayData data = RailwayData.getInstance(commandContext.getSource().getLevel());
                                String target = suggestionsBuilder.getRemainingLowerCase();

                                List<String> toBeSuggested = Util.formulateMatchingString(target, data.stations.stream().map(e -> e.name).toList());
                                for(String stn : toBeSuggested) {
                                    suggestionsBuilder.suggest(stn);
                                }
                                return suggestionsBuilder.buildFuture();
                            }
                        )
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            Level world = context.getSource().getLevel();
                            String name = StringArgumentType.getString(context, "name");
                            Station stn = MtrUtil.findStation(name, context.getSource().getLevel());
                            if(stn == null) {
                                context.getSource().sendFailure(Component.literal("Cannot find station \"" + name + "\""));
                                return 1;
                            }

                            double midpointX = getMidPoint(stn.corner1.getA(), stn.corner2.getA());
                            double midpointZ = getMidPoint(stn.corner1.getB(), stn.corner2.getB());
                            double playerY = player.getY();
                            BlockPos targetPos = new BlockPos((int)midpointX, (int)playerY, (int)midpointZ);
                            BlockPos finalPos = Util.getNonOccupiedPos(world, targetPos);
                            
                            player.removeVehicle();
                            player.teleportTo(finalPos.getX(), finalPos.getY(), finalPos.getZ());
                            context.getSource().sendSuccess(() -> Component.literal("Warped to " + String.join(" ", getStationName(stn.name))).withStyle(ChatFormatting.GREEN), false);
                            return 1;
                        }))
        );
    }

    public static double getMidPoint(int p1, int p2) {
        return (p1 + p2) / 2.0;
    }

    public static String[] getStationName(String stationName) {
        return stationName.split("\\|");
    }
}
