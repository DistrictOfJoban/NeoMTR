package mtr.registry;

import mtr.MTR;
import mtr.loader.MTRRegistry;
import net.minecraft.sounds.SoundEvent;

public class SoundEvents {
	public static final SoundEvent TICKET_BARRIER = SoundEvent.createVariableRangeEvent(MTR.id("ticket_barrier"));
	public static final SoundEvent TICKET_BARRIER_CONCESSIONARY = SoundEvent.createVariableRangeEvent(MTR.id("ticket_barrier_concessionary"));
	public static final SoundEvent TICKET_PROCESSOR_ENTRY = SoundEvent.createVariableRangeEvent(MTR.id("ticket_processor_entry"));
	public static final SoundEvent TICKET_PROCESSOR_ENTRY_CONCESSIONARY = SoundEvent.createVariableRangeEvent(MTR.id("ticket_processor_entry_concessionary"));
	public static final SoundEvent TICKET_PROCESSOR_EXIT = SoundEvent.createVariableRangeEvent(MTR.id("ticket_processor_exit"));
	public static final SoundEvent TICKET_PROCESSOR_EXIT_CONCESSIONARY = SoundEvent.createVariableRangeEvent(MTR.id("ticket_processor_exit_concessionary"));
	public static final SoundEvent TICKET_PROCESSOR_FAIL = SoundEvent.createVariableRangeEvent(MTR.id("ticket_processor_fail"));

	public static void register() {
		MTRRegistry.registerSoundEvent("ticket_barrier", TICKET_BARRIER);
		MTRRegistry.registerSoundEvent("ticket_barrier_concessionary", TICKET_BARRIER_CONCESSIONARY);
		MTRRegistry.registerSoundEvent("ticket_processor_entry", TICKET_PROCESSOR_ENTRY);
		MTRRegistry.registerSoundEvent("ticket_processor_entry_concessionary", TICKET_PROCESSOR_ENTRY_CONCESSIONARY);
		MTRRegistry.registerSoundEvent("ticket_processor_exit", TICKET_PROCESSOR_EXIT);
		MTRRegistry.registerSoundEvent("ticket_processor_exit_concessionary", TICKET_PROCESSOR_EXIT_CONCESSIONARY);
		MTRRegistry.registerSoundEvent("ticket_processor_fail", TICKET_PROCESSOR_FAIL);
	}
}
