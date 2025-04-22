package mtr.registry;

import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MTRAddonRegistry {
    private static final List<MTRAddon> registeredAddons = new ArrayList<>();

    public static void registerAddon(MTRAddon addon) {
        registeredAddons.add(addon);
    }

    public static List<MTRAddon> getRegisteredAddons() {
        return new ArrayList<>(registeredAddons);
    }

    public record MTRAddon(String modId, String name, String version) {
    }
}
