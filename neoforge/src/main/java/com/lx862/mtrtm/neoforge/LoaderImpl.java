package com.lx862.mtrtm.neoforge;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

import java.util.function.Consumer;

public class LoaderImpl {
    private static Consumer<CommandDispatcher<CommandSourceStack>> callback;

    public static void registerCommands(Consumer<CommandDispatcher<CommandSourceStack>> commandRegisterCallback) {
        callback = commandRegisterCallback;
    }

    public static void invokeRegisterCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        callback.accept(dispatcher);
    }
}
