package net.londonunderground.mod.registry;

import net.londonunderground.mod.LUAddon;
import net.londonunderground.loader.LUAddonRegistry;
import net.minecraft.sounds.SoundEvent;

public final class SoundEvents {

	static {
		SOUND_EVENT_OUTSIDE_AMBIENT = LUAddonRegistry.registerSoundEvent("cityambient", net.minecraft.sounds.SoundEvent.createVariableRangeEvent(LUAddon.id("cityambient")));
		SOUND_EVENT_SEE_IT_SAY_IT = LUAddonRegistry.registerSoundEvent("seeitsayitsorted", net.minecraft.sounds.SoundEvent.createVariableRangeEvent(LUAddon.id("seeitsayitsorted")));
		SOUNT_EVENT_TUBE_STATION_AMBIENCE1 = LUAddonRegistry.registerSoundEvent("ambient1", net.minecraft.sounds.SoundEvent.createVariableRangeEvent(LUAddon.id("ambient1")));

	}

	public static final SoundEvent SOUND_EVENT_OUTSIDE_AMBIENT;
	public static final SoundEvent SOUND_EVENT_SEE_IT_SAY_IT;
	public static final SoundEvent SOUNT_EVENT_TUBE_STATION_AMBIENCE1;

	public static void init() {
		LUAddon.LOGGER.info("Registering MTR London Underground Addon sound events");
	}
}
