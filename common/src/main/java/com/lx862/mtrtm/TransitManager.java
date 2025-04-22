package com.lx862.mtrtm;

import com.lx862.mtrtm.config.Config;
import com.lx862.mtrtm.data.TrainState;
import it.unimi.dsi.fastutil.longs.*;
import mtr.registry.MTRAddonRegistry;
import net.fabricmc.api.ModInitializer;
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
        Config.load();
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
