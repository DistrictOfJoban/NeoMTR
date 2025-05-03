package com.lx862.mtrtdm;

import com.lx862.mtrtdm.commands.BreakTrainCommand;
import com.lx862.mtrtdm.commands.FixAllTrainCommand;
import com.lx862.mtrtdm.commands.TrainDrivingModuleCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public class TDMCommands {
    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        BreakTrainCommand.register(dispatcher);
        FixAllTrainCommand.register(dispatcher);
        TrainDrivingModuleCommand.register(dispatcher);
    }
}
