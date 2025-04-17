package com.lx862.jcm.mod.registry;

import com.lx862.jcm.mod.render.block.*;
import com.lx862.jcm.mod.util.JCMLogger;
import mtr.RegistryClient;

public final class BlockEntityRenderers {
    public static void registerClient() {
        JCMLogger.debug("Registering Block Entity Renderer...");
        RegistryClient.registerTileEntityRenderer(BlockEntities.APG_DOOR_DRL.get(), (dispatcher) -> new RenderDRLAPGDoor<>(dispatcher, 2));
        RegistryClient.registerTileEntityRenderer(BlockEntities.BUTTERFLY_LIGHT.get(), ButterflyLightRenderer::new);
        RegistryClient.registerTileEntityRenderer(BlockEntities.DEPARTURE_TIMER.get(), DepartureTimerRenderer::new);
        RegistryClient.registerTileEntityRenderer(BlockEntities.FARE_SAVER.get(), FareSaverRenderer::new);
        RegistryClient.registerTileEntityRenderer(BlockEntities.KCR_STATION_NAME_SIGN.get(), KCRStationNameSignRenderer::new);
        RegistryClient.registerTileEntityRenderer(BlockEntities.KCR_STATION_NAME_SIGN_STATION_COLOR.get(), KCRStationNameSignRenderer::new);
        RegistryClient.registerTileEntityRenderer(BlockEntities.SIGNAL_LIGHT_INVERTED_RED_ABOVE.get(), (dispatcher) -> new SignalBlockInvertedRenderer<>(dispatcher, 0xFF0000FF, true));
        RegistryClient.registerTileEntityRenderer(BlockEntities.SIGNAL_LIGHT_INVERTED_RED_BELOW.get(), (dispatcher) -> new SignalBlockInvertedRenderer<>(dispatcher, 0xFF00FF00, false));
        RegistryClient.registerTileEntityRenderer(BlockEntities.SIGNAL_LIGHT_RED_BELOW.get(), (dispatcher) -> new StaticSignalLightRenderer<>(dispatcher, 0xFFFF0000, false));
        RegistryClient.registerTileEntityRenderer(BlockEntities.SIGNAL_LIGHT_RED_TOP.get(), (dispatcher) -> new StaticSignalLightRenderer<>(dispatcher, 0xFFFF0000, true));
        RegistryClient.registerTileEntityRenderer(BlockEntities.SIGNAL_LIGHT_BLUE.get(), (dispatcher) -> new StaticSignalLightRenderer<>(dispatcher, 0xFF0000FF, true));
        RegistryClient.registerTileEntityRenderer(BlockEntities.SIGNAL_LIGHT_GREEN.get(), (dispatcher) -> new StaticSignalLightRenderer<>(dispatcher, 0xFF00FF00, false));
        RegistryClient.registerTileEntityRenderer(BlockEntities.PIDS_1A.get(), PIDS1ARenderer::new);
        RegistryClient.registerTileEntityRenderer(BlockEntities.PIDS_PROJECTOR.get(), PIDSProjectorRenderer::new);
        RegistryClient.registerTileEntityRenderer(BlockEntities.LCD_PIDS.get(), LCDPIDSRenderer::new);
        RegistryClient.registerTileEntityRenderer(BlockEntities.RV_PIDS.get(), RVPIDSRenderer::new);
        RegistryClient.registerTileEntityRenderer(BlockEntities.RV_PIDS_SIL_1.get(), RVPIDSSILRenderer::new);
        RegistryClient.registerTileEntityRenderer(BlockEntities.RV_PIDS_SIL_2.get(), RVPIDSSILRenderer::new);
        RegistryClient.registerTileEntityRenderer(BlockEntities.STATION_NAME_STANDING.get(), StationNameStandingRenderer::new);
    }
}
