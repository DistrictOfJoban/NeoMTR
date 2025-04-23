package com.lx862.mtrtm.loader.neoforge;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

import java.util.function.Consumer;

public class LoaderImpl {
    private static Consumer<CommandDispatcher<CommandSourceStack>> cmdCallback;

    public static void registerCommands(Consumer<CommandDispatcher<CommandSourceStack>> commandRegisterCallback) {
        cmdCallback = commandRegisterCallback;
    }

    public static void invokeRegisterCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        if(cmdCallback != null) cmdCallback.accept(dispatcher);
    }
}
