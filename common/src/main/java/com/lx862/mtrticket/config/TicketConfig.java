package com.lx862.mtrticket.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lx862.mtrticket.data.TicketBarriers;
import com.lx862.mtrticket.data.Tickets;
import com.lx862.mtrticket.MTRTicket;
import com.lx862.mtrticket.TicketHandler;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class TicketConfig {
    private static ResourceLocation baseItem = null;
    private static String entranceState = null;
    private static String exitState = null;

    public static boolean load(Path configDirectory) {
        Path configPath = configDirectory.resolve("joban").resolve("tickets.json");
        if(!Files.exists(configPath)) {
            MTRTicket.LOGGER.warn("[Ticketer] No ticket config found, skipping!");
            return false;
        }

        MTRTicket.LOGGER.info("[Ticketer] Reading Ticket Config...");
        try {
            final JsonObject jsonConfig = JsonParser.parseString(String.join("", Files.readAllLines(configPath))).getAsJsonObject();
            jsonConfig.entrySet().forEach(element -> {
                if(element.getKey().equals("barriers")) {
                    JsonElement barrierElements = element.getValue();
                    barrierElements.getAsJsonObject().entrySet().forEach(e -> {
                        TicketHandler.barrierList.add(new TicketBarriers(e.getKey(), e.getValue().getAsBoolean()));
                        MTRTicket.LOGGER.info("[Ticketer] Allowing ticket barriers: "+ e.getKey());
                    });
                } else if (element.getKey().equals("config")) {
                    entranceState = element.getValue().getAsJsonObject().get("entranceState").getAsString();
                    exitState = element.getValue().getAsJsonObject().get("exitState").getAsString();
                } else if (element.getKey().equals("baseItem")) {
                    baseItem = ResourceLocation.parse(element.getValue().getAsString());
                } else {
                    loadTicket(element);
                }
            });
        } catch (Exception e) {
            MTRTicket.LOGGER.error("Failed to read ticket config!", e);
            return false;
        }
        return true;
    }

    private static void loadTicket(Map.Entry<String, JsonElement> element) {
        final boolean isSingleRide;
        final boolean opOnly;
        final boolean exitOnly;
        final String filteredZone;
        final int predicateId;
        final int price;
        final int expireDays;
        final int delayMin;
        final int delayMax;
        String displayName = element.getValue().getAsJsonObject().get("displayName").getAsString();

        if(element.getValue().getAsJsonObject().has("isSingleRide")) {
            isSingleRide = element.getValue().getAsJsonObject().get("isSingleRide").getAsBoolean();
        } else {
            isSingleRide = true;
        }

        if(element.getValue().getAsJsonObject().has("opOnly")) {
            opOnly = element.getValue().getAsJsonObject().get("opOnly").getAsBoolean();
        } else {
            opOnly = false;
        }

        if(element.getValue().getAsJsonObject().has("exitOnly")) {
            exitOnly = element.getValue().getAsJsonObject().get("exitOnly").getAsBoolean();
        } else {
            exitOnly = false;
        }

        if(element.getValue().getAsJsonObject().has("predicateID")) {
            predicateId = element.getValue().getAsJsonObject().get("predicateID").getAsInt();
        } else {
            predicateId = 10000;
        }

        if(element.getValue().getAsJsonObject().has("price")) {
            price = element.getValue().getAsJsonObject().get("price").getAsInt();
        } else {
            price = 20;
        }

        if(element.getValue().getAsJsonObject().has("expireDays")) {
            expireDays = element.getValue().getAsJsonObject().get("expireDays").getAsInt();
        } else {
            expireDays = 40;
        }

        if(element.getValue().getAsJsonObject().has("filteredZone")) {
            filteredZone = element.getValue().getAsJsonObject().get("filteredZone").getAsString();
        } else {
            filteredZone = "";
        }

        if(element.getValue().getAsJsonObject().has("delayMin")) {
            delayMin = element.getValue().getAsJsonObject().get("delayMin").getAsInt();
        } else {
            delayMin = 0;
        }

        if(element.getValue().getAsJsonObject().has("delayMax")) {
            delayMax = element.getValue().getAsJsonObject().get("delayMax").getAsInt();
        } else {
            delayMax = 10;
        }

        TicketHandler.ticketList.put(element.getKey(), new Tickets(displayName, predicateId, isSingleRide, price, expireDays, delayMin, delayMax, opOnly, exitOnly, filteredZone));
        MTRTicket.LOGGER.info("[Ticketer] Loading Tickets: " + element.getKey());
    }

    public static ResourceLocation getBaseItem() {
        if(baseItem == null) {
            return ResourceLocation.fromNamespaceAndPath("minecraft", "arrow");
        }
        return baseItem;
    }

    public static String getEntranceState() {
        return entranceState;
    }

    public static String getExitState() {
        return exitState;
    }
}
