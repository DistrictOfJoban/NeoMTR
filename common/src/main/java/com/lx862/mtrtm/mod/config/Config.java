package com.lx862.mtrtm.mod.config;

import com.google.gson.*;
import com.lx862.mtrtm.mod.TransitManager;
import net.minecraft.client.Minecraft;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class Config {
    public static int mtrJourneyPlannerTickTime = 0;
    public static int shearPSDOpLevel = 0;

    public static boolean load(Path path) {
        if(!Files.exists(path.resolve("config.json"))) {
            TransitManager.LOGGER.info("[TransitManager] Config not found, generating one...");
            writeConfig(path);
            return true;
        }

        TransitManager.LOGGER.info("[TransitManager] Reading Train Config...");
        try {
            final JsonObject jsonConfig = JsonParser.parseString(String.join("", Files.readAllLines(path.resolve("config.json")))).getAsJsonObject();
            try {
                mtrJourneyPlannerTickTime = jsonConfig.get("mtrJourneyPlannerTickTime").getAsInt();
            } catch (Exception ignored) {}

            try {
                shearPSDOpLevel = jsonConfig.get("shearPSDOpLevel").getAsInt();
            } catch (Exception ignored) {}

        } catch (Exception e) {
            TransitManager.LOGGER.error("[TransitManager] Failed to read config!", e);
            return false;
        }
        return true;
    }

    public static void writeConfig(Path path) {
        TransitManager.LOGGER.info("[TransitManager] Writing Config...");
        final JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty("mtrJourneyPlannerTickTime", mtrJourneyPlannerTickTime);
        jsonConfig.addProperty("shearPSDOpLevel", shearPSDOpLevel);

        try {
            Files.createDirectories(path);
            Files.write(path.resolve("config.json"), Collections.singleton(new GsonBuilder().setPrettyPrinting().create().toJson(jsonConfig)));
        } catch (Exception e) {
            TransitManager.LOGGER.error("[TransitManager] Failed to write config!", e);
        }
    }
}
