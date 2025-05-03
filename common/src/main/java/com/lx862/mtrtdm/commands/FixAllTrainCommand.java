package com.lx862.mtrtdm.commands;

import com.lx862.mtrtdm.TrainDrivingModule;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class FixAllTrainCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("fixalltrain")
                .requires(ctx -> ctx.hasPermission(2))
                .executes(context -> {
                    TrainDrivingModule.brokendownTrain.clear();
                    return 1;
                }));
    }
}
