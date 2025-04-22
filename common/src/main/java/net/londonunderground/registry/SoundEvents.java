package net.londonunderground.registry;

import net.londonunderground.LondonUnderground;
import net.minecraft.sounds.SoundEvent;

public final class SoundEvents {

	static {
		SOUND_EVENT_OUTSIDE_AMBIENT = LondonUndergroundRegistry.registerSoundEvent("cityambient", net.minecraft.sounds.SoundEvent.createVariableRangeEvent(LondonUnderground.id("cityambient")));
		SOUND_EVENT_SEE_IT_SAY_IT = LondonUndergroundRegistry.registerSoundEvent("seeitsayitsorted", net.minecraft.sounds.SoundEvent.createVariableRangeEvent(LondonUnderground.id("seeitsayitsorted")));
		SOUNT_EVENT_TUBE_STATION_AMBIENCE1 = LondonUndergroundRegistry.registerSoundEvent("ambient1", net.minecraft.sounds.SoundEvent.createVariableRangeEvent(LondonUnderground.id("ambient1")));

	}

	public static final SoundEvent SOUND_EVENT_OUTSIDE_AMBIENT;
	public static final SoundEvent SOUND_EVENT_SEE_IT_SAY_IT;
	public static final SoundEvent SOUNT_EVENT_TUBE_STATION_AMBIENCE1;

	public static void init() {
		LondonUnderground.LOGGER.info("Registering MTR London Underground Addon sound events");
	}
}
