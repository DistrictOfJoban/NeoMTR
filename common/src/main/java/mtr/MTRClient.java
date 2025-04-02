package mtr;

import mtr.client.ClientData;
import mtr.client.Config;
import mtr.registry.Networking;
import mtr.registry.*;
import mtr.sound.LoopingSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import java.util.Locale;

public class MTRClient {

	private static boolean isReplayMod;
	private static boolean isVivecraft;
	private static boolean isPehkui;
	private static float gameTick = 0;
	private static float lastPlayedTrainSoundsTick = 0;

	private static int tick;
	private static long startSampleMillis;
	private static float startSampleGameTick;
	private static float gameTickTest;
	private static int skipTicks;
	private static int lastSkipTicks;

	public static final int TICKS_PER_SPEED_SOUND = 4;
	public static final LoopingSoundInstance TACTILE_MAP_SOUND_INSTANCE = new LoopingSoundInstance("tactile_map_music");
	private static final int SAMPLE_MILLIS = 1000;

	public static void init() {
		Blocks.registerClient();
		BlockEntityTypes.registerClient();
		Networking.registerClient();
		KeyMappings.registerClient();
		Events.registerClient();

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
		try {
			Class.forName("virtuoel.pehkui.Pehkui");
			isPehkui = true;
		} catch (Exception ignored) {
			isPehkui = false;
		}

		if (isReplayMod) {
			MTR.LOGGER.info("[NeoMTR] Running in Replay Mod mode");
		}
		if (isVivecraft) {
			MTR.LOGGER.info("[NeoMTR] Vivecraft detected");
		}
		if (isPehkui) {
			MTR.LOGGER.info("[NeoMTR] Pehkui detected");
		}
	}

	public static boolean isReplayMod() {
		return isReplayMod;
	}

	public static boolean isVivecraft() {
		return isVivecraft;
	}

	public static boolean isPehkui() {
		return isPehkui;
	}

	public static float getGameTick() {
		return gameTick;
	}

	public static void incrementGameTick() {
		final float lastFrameDuration = getLastFrameDuration();
		gameTickTest += lastFrameDuration;
		if (isReplayMod || tick == 0) {
			gameTick += lastFrameDuration;
		}
		tick++;
		if (tick >= skipTicks) {
			tick = 0;
		}

		final long millis = System.currentTimeMillis();
		if (millis - startSampleMillis >= SAMPLE_MILLIS) {
			skipTicks = Math.round((gameTickTest - startSampleGameTick) * 50 / (millis - startSampleMillis));
			startSampleMillis = millis;
			startSampleGameTick = gameTickTest;
			if (skipTicks != lastSkipTicks) {
				MTR.LOGGER.info("[NeoMTR] Tick skip updated to {}", skipTicks);
			}
			lastSkipTicks = skipTicks;
		}
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
}
