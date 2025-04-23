package com.lx862.mtrtm.loader.neoforge;

import com.mojang.brigadier.CommandDispatcher;
import mtr.neoforge.mappings.ForgeUtilities;
import net.minecraft.commands.CommandSourceStack;

import java.util.function.Consumer;

public class LoaderImpl {
    public static void registerCommands(Consumer<CommandDispatcher<CommandSourceStack>> commandRegisterCallback) {
        ForgeUtilities.registerCommandListener((event) -> commandRegisterCallback.accept(event.getDispatcher()));
    }
}
