package com.lx862.mtrtdm;

import com.lx862.mtrtdm.config.TrainConfig;
import mtr.registry.MTRAddonRegistry;
import net.minecraft.server.level.ServerBossEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class TrainDrivingModule {
    public static final String NAME = "TrainDrivingModule";
    public static final Logger LOGGER = LogManager.getLogger(NAME);
    public static final MTRAddonRegistry.MTRAddon ADDON = new MTRAddonRegistry.MTRAddon("mtrtdm", NAME, "1.0.0");

    public static final Map<Long, String> brokendownTrain = new HashMap<>();
    public static final HashMap<Long, Long> sidingToRouteMap = new HashMap<>();
    public static final HashMap<Long, ServerBossEvent> mainBossbar = new HashMap<>();
    public static final HashMap<Long, ServerBossEvent> altBossbar = new HashMap<>();
    public static final HashMap<Long, ServerBossEvent> dwellBossbar = new HashMap<>();

    private static Path CONFIG_PATH;

    public static void initialize(Path configPath) {
        CONFIG_PATH = configPath;
        TrainConfig.load(configPath);
        MTRAddonRegistry.registerAddon(ADDON);
    }

    public static void clearBossbar(boolean isMain, boolean isAlt, boolean isDwell) {
        if(isMain) {
            for(ServerBossEvent bossbar : mainBossbar.values()) {
                bossbar.removeAllPlayers();
            }
            mainBossbar.clear();
        }
        if(isAlt) {
            for(ServerBossEvent bossbar : altBossbar.values()) {
                bossbar.removeAllPlayers();
            }
            altBossbar.clear();
        }
        if(isDwell) {
            for(ServerBossEvent bossbar : dwellBossbar.values()) {
                bossbar.removeAllPlayers();
            }
            dwellBossbar.clear();
        }
    }

    public static Path getConfigPath() {
        return CONFIG_PATH;
    }
}
