package com.lx862.mtrsurveyor;

import com.lx862.mtrsurveyor.util.Util;
import com.mojang.serialization.Codec;
import folk.sisby.surveyor.landmark.component.LandmarkComponentType;
import folk.sisby.surveyor.landmark.component.LandmarkComponentTypes;
import mtr.data.TransportMode;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class MTRLandmarkComponentTypes {
    public static final LandmarkComponentType<Long> FARE_ZONE = LandmarkComponentTypes.register(MTRSurveyor.id("fare_zone"), Codec.LONG, (fareZone) -> {
        return Component.literal(String.valueOf(fareZone)).withStyle(ChatFormatting.YELLOW);
    });

    public static final LandmarkComponentType<String> TRANSPORT_TYPE = LandmarkComponentTypes.register(MTRSurveyor.id("transport_mode"), Codec.STRING, (transportMode) -> {
        return Component.literal(Util.getCamelCase(transportMode)).withStyle(ChatFormatting.YELLOW);
    });

    // TODO: Backup only, surveyor currently crash for unknown components
    public static final LandmarkComponentType<Long> AREA_ID = LandmarkComponentTypes.register(MTRSurveyor.id("area_id"), Codec.LONG, (transportMode) -> {
        return Component.literal("ID");
    });

    public static void init() {
        // static initialization
    }
}
