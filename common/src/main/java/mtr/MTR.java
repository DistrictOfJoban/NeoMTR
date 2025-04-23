package mtr;

import mtr.registry.Networking;
import mtr.registry.*;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MTR {

	private static int gameTick = 0;

	public static final String MOD_ID = "mtr";
	public static final Logger LOGGER = LoggerFactory.getLogger("NeoMTR");

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public static void init() {
		Blocks.register();
		BlockEntityTypes.register();
		Items.register();
		Networking.register();
		SoundEvents.register();
		Events.register();
	}

	public static void incrementGameTick() {
		gameTick++;
	}

	public static boolean isGameTickInterval(int interval) {
		return gameTick % interval == 0;
	}

	public static boolean isGameTickInterval(int interval, int offset) {
		return (gameTick + offset) % interval == 0;
	}
}
