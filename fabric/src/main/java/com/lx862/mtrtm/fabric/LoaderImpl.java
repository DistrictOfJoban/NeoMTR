package com.lx862.mtrtm.fabric;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import java.util.function.Consumer;

public class LoaderImpl {
    public static void registerCommands(Consumer<CommandDispatcher<CommandSourceStack>> commandRegisterCallback) {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, third) -> {
            commandRegisterCallback.accept(dispatcher);
        });
    }
}
