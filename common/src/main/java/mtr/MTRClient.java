package mtr;

import mtr.client.ClientData;
import mtr.client.Config;
import mtr.client.Patreon;
import mtr.registry.Networking;
import mtr.registry.*;
import mtr.screen.NewConfigScreen;
import mtr.sound.LoopingSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.function.Consumer;

public class MTRClient {

	private static boolean isReplayMod;
	private static boolean isVivecraft;
	private static double gameTick = 0;
	private static double lastPlayedTrainSoundsTick = 0;

	public static final int TICKS_PER_SPEED_SOUND = 4;
	public static final LoopingSoundInstance TACTILE_MAP_SOUND_INSTANCE = new LoopingSoundInstance("tactile_map_music");

	private static final HashMap<MTRAddonRegistry.MTRAddon, Consumer<Screen>> addonScreenCallback = new HashMap<>();

	public static void init() {
		Blocks.registerClient();
		BlockEntityRenderers.registerClient();
		Networking.registerClient();
		KeyMappings.registerClient();
		EventsClient.registerClient();

		Config.readConfig();
		Patreon.getPatreonList(Config.PATREON_LIST);
	}

	public static void probeForMod(Player player) {
		isReplayMod = player.getClass().toGenericString().toLowerCase(Locale.ENGLISH).contains("replaymod");
		try {
			Class.forName("org.vivecraft.main.VivecraftMain");
			isVivecraft = true;
		} catch (Exception ignored) {
			isVivecraft = false;
		}

		if (isReplayMod) {
			MTR.LOGGER.info("[NeoMTR] Running in Replay Mod mode");
		}
		if (isVivecraft) {
			MTR.LOGGER.info("[NeoMTR] Vivecraft detected");
		}
	}

	public static boolean isReplayMod() {
		return isReplayMod;
	}

	public static boolean isVivecraft() {
		return isVivecraft;
	}

	public static double getGameTick() {
		return gameTick;
	}

	public static void registerAddonConfigGUICallback(MTRAddonRegistry.MTRAddon addon, Consumer<Screen> openScreen) {
		addonScreenCallback.put(addon, openScreen);
	}

	public static Consumer<Screen> getAddonConfigScreen(MTRAddonRegistry.MTRAddon addon) {
		return addonScreenCallback.get(addon);
	}

	public static void incrementGameTick() {
		gameTick += getLastFrameDuration();
		ClientData.tick();
	}

	public static float getLastFrameDuration() {
		if (Minecraft.getInstance().isPaused()) return 0;
		return isReplayMod ? 20F / 60 : Minecraft.getInstance().getTimer().getGameTimeDeltaTicks();
	}

	public static boolean canPlaySound() {
		if (gameTick - lastPlayedTrainSoundsTick >= TICKS_PER_SPEED_SOUND) {
			lastPlayedTrainSoundsTick = gameTick;
		}
		return gameTick == lastPlayedTrainSoundsTick && !Minecraft.getInstance().isPaused();
	}

	public static Screen getConfigScreen(Screen previous) {
		return new NewConfigScreen().withPreviousScreen(previous);
	}
}
