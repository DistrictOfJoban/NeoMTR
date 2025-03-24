package mtr.registry;

import mtr.MTR;
import mtr.mappings.RegistryUtilities;
import net.minecraft.sounds.SoundEvent;

public class SoundEvents {
	public static final SoundEvent TICKET_BARRIER = RegistryUtilities.createSoundEvent(MTR.id("ticket_barrier"));
	public static final SoundEvent TICKET_BARRIER_CONCESSIONARY = RegistryUtilities.createSoundEvent(MTR.id("ticket_barrier_concessionary"));
	public static final SoundEvent TICKET_PROCESSOR_ENTRY = RegistryUtilities.createSoundEvent(MTR.id("ticket_processor_entry"));
	public static final SoundEvent TICKET_PROCESSOR_ENTRY_CONCESSIONARY = RegistryUtilities.createSoundEvent(MTR.id("ticket_processor_entry_concessionary"));
	public static final SoundEvent TICKET_PROCESSOR_EXIT = RegistryUtilities.createSoundEvent(MTR.id("ticket_processor_exit"));
	public static final SoundEvent TICKET_PROCESSOR_EXIT_CONCESSIONARY = RegistryUtilities.createSoundEvent(MTR.id("ticket_processor_exit_concessionary"));
	public static final SoundEvent TICKET_PROCESSOR_FAIL = RegistryUtilities.createSoundEvent(MTR.id("ticket_processor_fail"));
}
