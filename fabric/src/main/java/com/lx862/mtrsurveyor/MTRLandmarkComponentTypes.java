package com.lx862.mtrsurveyor;

import com.mojang.serialization.Codec;
import folk.sisby.surveyor.landmark.component.LandmarkComponentType;
import folk.sisby.surveyor.landmark.component.LandmarkComponentTypes;
import net.minecraft.network.chat.Component;

public class MTRLandmarkComponentTypes {
    public static final LandmarkComponentType<Long> AREA_ID = LandmarkComponentTypes.register(MTRSurveyor.id("area_id"), Codec.LONG, (id) -> {
        return Component.literal("ID: " + id);
    });

    public static void init() {
        // static initialization
    }
}
