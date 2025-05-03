package com.lx862.mtrtdm.config;

import com.google.gson.*;
import com.lx862.mtrtdm.data.TrainBehavior;
import com.lx862.mtrtdm.TDMEvents;
import com.lx862.mtrtdm.TrainDrivingModule;
import com.lx862.mtrtdm.data.TrainSound;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class TrainConfig {
    public static final List<TrainBehavior> routeConfig = new ArrayList<>();

    public static boolean load(Path rootPath) {
        Path configPath = rootPath.resolve("joban").resolve("train.json");

        if(!Files.exists(configPath)) {
            TrainDrivingModule.LOGGER.warn("[{}] Train Config file not found.", TrainDrivingModule.NAME);
            return false;
        }

        routeConfig.clear();
        TrainDrivingModule.LOGGER.info("[{}] Reading Train Config...", TrainDrivingModule.NAME);
        try {
            final JsonArray jsonConfig = JsonParser.parseString(String.join("", Files.readAllLines(configPath))).getAsJsonArray();
            jsonConfig.forEach(jsonElement -> {
                TrainBehavior trainBehavior = new TrainBehavior();
                JsonObject obj = jsonElement.getAsJsonObject();
                List<String> routeNames = new ArrayList<>();
                if(obj.has("routeName")) {
                    obj.get("routeName").getAsJsonArray().forEach(e -> {
                         routeNames.add(e.getAsString());
                    });
                }

                if(obj.has("restrictToRailSpeed")) {
                    trainBehavior.restrictToRailSpeed = obj.get("restrictToRailSpeed").getAsBoolean();
                }

                if(obj.has("speedChangeSound")) {
                    trainBehavior.speedDropSound = new TrainSound(obj.get("speedChangeSound"));
                    trainBehavior.speedRaiseSound = new TrainSound(obj.get("speedChangeSound"));
                }

                if(obj.has("speedRaiseSound")) {
                    trainBehavior.speedRaiseSound = new TrainSound(obj.get("speedRaiseSound"));
                }

                if(obj.has("speedDropSound")) {
                    trainBehavior.speedDropSound = new TrainSound(obj.get("speedDropSound"));
                }

                if(obj.has("overSpeedDecelToZero")) {
                    trainBehavior.overSpeedDecelToZero = obj.get("overSpeedDecelToZero").getAsBoolean();
                }

                if(obj.has("nextPathClearance")) {
                    trainBehavior.nextPathClearance = obj.get("nextPathClearance").getAsInt();
                }

                if(obj.has("overSpeedTitle")) {
                    trainBehavior.overSpeedTitle = obj.get("overSpeedTitle").getAsString();
                }

                if(obj.has("overSpeedSound")) {
                    trainBehavior.overspeedSound = new TrainSound(obj.get("overSpeedSound"));
                }

                if(obj.has("needBrakeSound")) {
                    trainBehavior.needBrakeSound = new TrainSound(obj.get("needBrakeSound"));
                }

                if(obj.has("enterSidingSound")) {
                    trainBehavior.enterSidingSound = new TrainSound(obj.get("enterSidingSound"));
                }

                if(obj.has("pathLookup")) {
                    trainBehavior.pathLookup = obj.get("pathLookup").getAsInt();
                }

                if(obj.has("mainBossbar")) {
                    trainBehavior.mainBossbarContent = obj.get("mainBossbar").getAsString();
                }

                if(obj.has("decelFactor")) {
                    trainBehavior.decelFactor = obj.get("decelFactor").getAsDouble();
                }

                if(obj.has("showSpdLimitBossbar")) {
                    trainBehavior.showLimitBossBar = obj.get("showSpdLimitBossbar").getAsBoolean();
                }

                if(obj.has("showDwellBossbar")) {
                    trainBehavior.showDwellBossBar = obj.get("showDwellBossbar").getAsBoolean();
                }

                if(obj.has("drag")) {
                    trainBehavior.drag = obj.get("drag").getAsDouble();
                }

                if(obj.has("slopeDrag")) {
                    trainBehavior.slopeSpeedChange = obj.get("slopeDrag").getAsDouble();
                }

                if(obj.has("incorrectStopPosAlarm")) {
                    trainBehavior.incorrectStopPosAlarm = obj.get("incorrectStopPosAlarm").getAsBoolean();
                }

                if(obj.has("incorrectStopPosSound")) {
                    trainBehavior.incorrectStopPosSound = new TrainSound(obj.get("incorrectStopPosSound"));
                }

                trainBehavior.routeName = routeNames;

                if(trainBehavior.mainBossbarContent.isEmpty()) {
                    trainBehavior.showMainBossbar = false;
                } else {
                    trainBehavior.showMainBossbar = true;
                }
                routeConfig.add(trainBehavior);
            });

            TrainDrivingModule.clearBossbar(true, true, true);
            TDMEvents.setRoutesFetched(false);
        } catch (Exception e) {
            TrainDrivingModule.LOGGER.error("[{}] Failed to read config file!", TrainDrivingModule.NAME, e);
            return false;
        }
        return true;
    }

    public TrainBehavior getBehaviorConfig(long sidingId) {
        TrainBehavior behavior = null;

        if(!TrainDrivingModule.sidingToRouteMap.containsKey(sidingId)) {
            return null;
        }

        long currentRouteId = TrainDrivingModule.sidingToRouteMap.get(sidingId);
        for(TrainBehavior trainBehavior : TrainConfig.routeConfig) {
            if(trainBehavior.routeId.contains(currentRouteId)) {
                behavior = trainBehavior;
            }
        }

        return behavior;
    }
}
