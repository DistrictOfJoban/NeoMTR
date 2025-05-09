package com.lx862.mtrtm.mod;

import com.lx862.mtrtm.mod.config.Config;
import com.lx862.mtrtm.mod.data.TrainState;
import com.lx862.mtrtm.loader.Loader;
import it.unimi.dsi.fastutil.longs.*;
import mtr.loader.MTRRegistry;
import mtr.registry.MTRAddonRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TransitManager {
    public static final Logger LOGGER = LogManager.getLogger(Constants.BRAND);
    public static final LongList stopPathGenDepotList = new LongArrayList();
    public static final Long2IntOpenHashMap trainStateList = new Long2IntOpenHashMap();
    public static final Long2LongArrayMap pathGenerationTimer = new Long2LongArrayMap();
    private static final MTRAddonRegistry.MTRAddon ADDON = new MTRAddonRegistry.MTRAddon(Constants.MOD_ID, Constants.BRAND, Constants.VERSION);

    public static void init() {
        LOGGER.info("[TransitManager] TransitManager initialized \\(＾▽＾)/");

        MTRRegistry.registerServerStartingEvent(minecraftServer -> {
            Config.load(minecraftServer.getServerDirectory().resolve("config").resolve("transitmanager"));
        });

        Loader.registerCommands(TransitManagerCommands::registerCommands);
        MTRAddonRegistry.registerAddon(ADDON);
    }

    public static boolean getTrainState(long trainId, TrainState trainState) {
        int state = trainStateList.get(trainId);
        int pos = trainState.getPos();

        return ((state >> pos) & 1) == 1;
    }

    public static void setTrainState(long trainId, TrainState trainState, boolean value) {
        int state = trainStateList.getOrDefault(trainId, 0);
        int pos = trainState.getPos();
        if(value) {
            state = state | (1 << pos);
        } else {
            state = state & ~(1 << pos);
        }


        trainStateList.put(trainId, state);
    }
}
