package com.lx862.jcm.mod.registry;

import cn.zbx1425.mtrsteamloco.RegistriesWrapper;
import com.lx862.jcm.mod.Constants;
import com.lx862.jcm.mod.block.entity.*;
import com.lx862.jcm.mod.util.JCMLogger;
import mtr.MTR;
import mtr.Registry;
import mtr.mappings.BlockEntityMapper;
import mtr.registry.RegistryObject;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class BlockEntities {
    public static final RegistryObject<BlockEntityType<APGDoorDRLBlockEntity>> APG_DOOR_DRL = new RegistryObject<>(() -> Registry.getBlockEntityType(APGDoorDRLBlockEntity::new, Blocks.APG_DOOR_DRL.get()));
    public static final RegistryObject<BlockEntityType<AutoIronDoorBlockEntity>> AUTO_IRON_DOOR = new RegistryObject<>(() -> Registry.getBlockEntityType(AutoIronDoorBlockEntity::new, Blocks.AUTO_IRON_DOOR.get()));
    public static final RegistryObject<BlockEntityType<APGGlassDRLBlockEntity>> APG_GLASS_DRL = new RegistryObject<>(() -> Registry.getBlockEntityType(APGGlassDRLBlockEntity::new, Blocks.APG_GLASS_DRL.get()));
    public static final RegistryObject<BlockEntityType<FareSaverBlockEntity>> FARE_SAVER = new RegistryObject<>(() -> Registry.getBlockEntityType(FareSaverBlockEntity::new, Blocks.FARE_SAVER.get()));
    public static final RegistryObject<BlockEntityType<SignalBlockInvertedEntityRedAbove>> SIGNAL_LIGHT_INVERTED_RED_ABOVE = new RegistryObject<>(() -> Registry.getBlockEntityType(SignalBlockInvertedEntityRedAbove::new, Blocks.SIGNAL_LIGHT_INVERTED_RED_ABOVE.get()));
    public static final RegistryObject<BlockEntityType<SignalBlockInvertedEntityRedBelow>> SIGNAL_LIGHT_INVERTED_RED_BELOW = new RegistryObject<>(() -> Registry.getBlockEntityType(SignalBlockInvertedEntityRedBelow::new, Blocks.SIGNAL_LIGHT_INVERTED_RED_BOTTOM.get()));
    public static final RegistryObject<BlockEntityType<StaticSignalLightBlockEntity>> SIGNAL_LIGHT_RED_BELOW = new RegistryObject<>(() -> Registry.getBlockEntityType((blockPos, blockState) -> new StaticSignalLightBlockEntity(StaticSignalLightBlockEntity.SignalType.RED_BELOW, blockPos, blockState), Blocks.STATIC_SIGNAL_LIGHT_RED_BELOW.get()));
    public static final RegistryObject<BlockEntityType<StaticSignalLightBlockEntity>> SIGNAL_LIGHT_RED_TOP = new RegistryObject<>(() -> Registry.getBlockEntityType((blockPos, blockState) -> new StaticSignalLightBlockEntity(StaticSignalLightBlockEntity.SignalType.RED_TOP, blockPos, blockState), Blocks.STATIC_SIGNAL_LIGHT_RED_TOP.get()));
    public static final RegistryObject<BlockEntityType<StaticSignalLightBlockEntity>> SIGNAL_LIGHT_BLUE = new RegistryObject<>(() -> Registry.getBlockEntityType((blockPos, blockState) -> new StaticSignalLightBlockEntity(StaticSignalLightBlockEntity.SignalType.BLUE, blockPos, blockState), Blocks.STATIC_SIGNAL_LIGHT_BLUE.get()));
    public static final RegistryObject<BlockEntityType<StaticSignalLightBlockEntity>> SIGNAL_LIGHT_GREEN = new RegistryObject<>(() -> Registry.getBlockEntityType((blockPos, blockState) -> new StaticSignalLightBlockEntity(StaticSignalLightBlockEntity.SignalType.GREEN, blockPos, blockState), Blocks.STATIC_SIGNAL_LIGHT_GREEN.get()));
    public static final RegistryObject<BlockEntityType<SubsidyMachineBlockEntity>> SUBSIDY_MACHINE = new RegistryObject<>(() -> Registry.getBlockEntityType(SubsidyMachineBlockEntity::new, Blocks.SUBSIDY_MACHINE.get()));
    public static final RegistryObject<BlockEntityType<SoundLooperBlockEntity>> SOUND_LOOPER = new RegistryObject<>(() -> Registry.getBlockEntityType(SoundLooperBlockEntity::new, Blocks.SOUND_LOOPER.get()));
    public static final RegistryObject<BlockEntityType<ButterflyLightBlockEntity>> BUTTERFLY_LIGHT = new RegistryObject<>(() -> Registry.getBlockEntityType(ButterflyLightBlockEntity::new, Blocks.BUTTERFLY_LIGHT.get()));
    public static final RegistryObject<BlockEntityType<DepartureTimerBlockEntity>> DEPARTURE_TIMER = new RegistryObject<>(() -> Registry.getBlockEntityType(DepartureTimerBlockEntity::new, Blocks.DEPARTURE_TIMER.get()));
    public static final RegistryObject<BlockEntityType<KCRStationNameSignBlockEntity>> KCR_STATION_NAME_SIGN = new RegistryObject<>(() -> Registry.getBlockEntityType((blockPos, blockState) -> new KCRStationNameSignBlockEntity(blockPos, blockState, false), Blocks.KCR_STATION_NAME_SIGN.get()));
    public static final RegistryObject<BlockEntityType<KCRStationNameSignBlockEntity>> KCR_STATION_NAME_SIGN_STATION_COLOR = new RegistryObject<>(() -> Registry.getBlockEntityType((blockPos, blockState) -> new KCRStationNameSignBlockEntity(blockPos, blockState, true), Blocks.KCR_STATION_NAME_SIGN_STATION_COLOR.get()));
    public static final RegistryObject<BlockEntityType<PIDSProjectorBlockEntity>> PIDS_PROJECTOR = new RegistryObject<>(() -> Registry.getBlockEntityType(PIDSProjectorBlockEntity::new, Blocks.PIDS_PROJECTOR.get()));
    public static final RegistryObject<BlockEntityType<PIDS1ABlockEntity>> PIDS_1A = new RegistryObject<>(() -> Registry.getBlockEntityType(PIDS1ABlockEntity::new, Blocks.PIDS_1A.get()));
    public static final RegistryObject<BlockEntityType<LCDPIDSBlockEntity>> LCD_PIDS = new RegistryObject<>(() -> Registry.getBlockEntityType(LCDPIDSBlockEntity::new, Blocks.LCD_PIDS.get()));
    public static final RegistryObject<BlockEntityType<RVPIDSBlockEntity>> RV_PIDS = new RegistryObject<>(() -> Registry.getBlockEntityType(RVPIDSBlockEntity::new, Blocks.RV_PIDS.get()));
    public static final RegistryObject<BlockEntityType<RVPIDSSIL1BlockEntity>> RV_PIDS_SIL_1 = new RegistryObject<>(() -> Registry.getBlockEntityType(RVPIDSSIL1BlockEntity::new, Blocks.RV_PIDS_SIL_1.get()));
    public static final RegistryObject<BlockEntityType<RVPIDSSIL2BlockEntity>> RV_PIDS_SIL_2 = new RegistryObject<>(() -> Registry.getBlockEntityType(RVPIDSSIL2BlockEntity::new, Blocks.RV_PIDS_SIL_2.get()));
    public static final RegistryObject<BlockEntityType<StationNameStandingBlockEntity>> STATION_NAME_STANDING = new RegistryObject<>(() -> Registry.getBlockEntityType(StationNameStandingBlockEntity::new, Blocks.STATION_NAME_STANDING.get()));

    public static void register(RegistriesWrapper registriesWrapper) {
        registriesWrapper.registerBlockEntityType("apg_door_1", APG_DOOR_DRL);
        // We just load the class and it will be registered, nothing else
        JCMLogger.debug("Registering block entity...");

         registriesWrapper.registerBlockEntityType("auto_iron_door", AUTO_IRON_DOOR);
         registriesWrapper.registerBlockEntityType("apg_glass_1", APG_GLASS_DRL);
         registriesWrapper.registerBlockEntityType("faresaver_1", FARE_SAVER);
         registriesWrapper.registerBlockEntityType("signal_light_inverted_1", SIGNAL_LIGHT_INVERTED_RED_ABOVE);
         registriesWrapper.registerBlockEntityType("signal_light_inverted_2", SIGNAL_LIGHT_INVERTED_RED_BELOW);
         registriesWrapper.registerBlockEntityType("signal_light_red_1", SIGNAL_LIGHT_RED_BELOW);
         registriesWrapper.registerBlockEntityType("signal_light_red_2", SIGNAL_LIGHT_RED_TOP);
         registriesWrapper.registerBlockEntityType("signal_light_blue", SIGNAL_LIGHT_BLUE);
         registriesWrapper.registerBlockEntityType("signal_light_green", SIGNAL_LIGHT_GREEN);
         registriesWrapper.registerBlockEntityType("subsidy_machine", SUBSIDY_MACHINE);
         registriesWrapper.registerBlockEntityType("sound_looper", SOUND_LOOPER);
         registriesWrapper.registerBlockEntityType("butterfly_light", BUTTERFLY_LIGHT);
         registriesWrapper.registerBlockEntityType("departure_timer", DEPARTURE_TIMER);
         registriesWrapper.registerBlockEntityType("kcr_name_sign", KCR_STATION_NAME_SIGN);
         registriesWrapper.registerBlockEntityType("kcr_name_sign_station_color", KCR_STATION_NAME_SIGN_STATION_COLOR);
         registriesWrapper.registerBlockEntityType("pids_projector", PIDS_PROJECTOR);
         registriesWrapper.registerBlockEntityType("pids_4", PIDS_1A);
         registriesWrapper.registerBlockEntityType("pids_4a", LCD_PIDS);
         registriesWrapper.registerBlockEntityType("pids_5", RV_PIDS);
         registriesWrapper.registerBlockEntityType("pids_rv_sil", RV_PIDS_SIL_1);
         registriesWrapper.registerBlockEntityType("pids_rv_sil_2", RV_PIDS_SIL_2);
         registriesWrapper.registerBlockEntityType("station_name_tall_stand", STATION_NAME_STANDING);
    }
}
