package com.lx862.mtrtdm.commands;

import com.lx862.mtrtdm.TrainDrivingModule;
import com.lx862.mtrtdm.config.TrainConfig;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TrainDrivingModuleCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("tdm")
            .requires(ctx -> ctx.hasPermission(2))
            .then(Commands.literal("reload")
            .executes(context -> {
                context.getSource().sendSuccess(() -> Component.literal("Reloading Train Driving Module Config...").withStyle(ChatFormatting.GOLD), false);
                List<String> error = reloadConfig(TrainDrivingModule.getConfigPath());
                if(!error.isEmpty()) {
                    String failed = String.join(",", error);
                    context.getSource().sendSuccess(() -> Component.literal("Config Reloaded. " + failed + " failed to load.").withStyle(ChatFormatting.RED), false);
                    context.getSource().sendSuccess(() -> Component.literal("Please check whether the JSON syntax is correct!").withStyle(ChatFormatting.RED), false);
                } else {
                    context.getSource().sendSuccess(() -> Component.literal("TDM Config Reloaded!").withStyle(ChatFormatting.GREEN), false);

                }
                return 1;
            }))
        );
    }

    public static List<String> reloadConfig(Path configPath) {
        List<String> error = new ArrayList<>();
        if(!TrainConfig.load(configPath)) {
            error.add("Main Config");
        }

        return error;
    }
}
