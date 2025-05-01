package com.lx862.mtrsurveyor;

import com.lx862.mtrsurveyor.util.Util;
import folk.sisby.surveyor.WorldSummary;
import folk.sisby.surveyor.landmark.Landmark;
import folk.sisby.surveyor.landmark.WorldLandmarks;
import folk.sisby.surveyor.landmark.component.LandmarkComponentMap;
import folk.sisby.surveyor.landmark.component.LandmarkComponentTypes;
import mtr.api.RailwayDataModule;
import mtr.api.events.MTRAreaUpdateEvent;
import mtr.data.*;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyorRailwayModule extends RailwayDataModule implements MTRAreaUpdateEvent {
    public static final String NAME = "mtr_surveyor";
    private final Level level;
    private final RailwayData railwayData;

    public SurveyorRailwayModule(RailwayData railwayData, Level level, Map<BlockPos, Map<BlockPos, Rail>> rails) {
        super(NAME, railwayData, level, rails);
        this.level = level;
        this.railwayData = railwayData;
    }

    @Override
    public void init() {
        syncLandmarks();
    }

    private void syncLandmarks() {
        WorldSummary worldSummary = WorldSummary.of(level);
        WorldLandmarks landmarks = worldSummary.landmarks();
        if(landmarks == null) return;

        MTRSurveyor.LOGGER.info("[{}] Syncing landmarks", MTRSurveyor.MOD_NAME);
        HashMap<Long, AreaBase> mtrAreas = new HashMap<>();

        for(AreaBase area : railwayData.stations) {
            mtrAreas.put(area.id, area);
        }
        for(AreaBase area : railwayData.depots) {
            mtrAreas.put(area.id, area);
        }

        for(AreaBase area : mtrAreas.values()) {
            Landmark landmark = getLandmark(area);
            landmarks.put(level, landmark);
        }

        landmarks.removeAll(level, landmark -> {
            ResourceLocation landmarkId = landmark.id();
            if(landmarkId.getNamespace().equals(MTRSurveyor.MOD_ID)) {
                String areaIdStr = landmarkId.getPath().split("/")[1];
                long id = Long.parseLong(areaIdStr);
                if(!mtrAreas.containsKey(id)) {
                    return true;
                }
            }

            return false;
        });
    }

    private Landmark getLandmark(AreaBase areaBase) {
        if(areaBase instanceof Station station) {
            return getStationLandmark(station);
        } else if(areaBase instanceof Depot depot) {
            return getDepotLandmark(depot);
        }
        return null;
    }

    private Landmark getStationLandmark(Station station) {
        return Landmark.create(WorldLandmarks.GLOBAL, MTRSurveyor.id("stations/" + station.id), builder -> {
           fillAreaComponents(station, builder);
           builder.add(LandmarkComponentTypes.NAME, Component.literal(Util.getCamelCase(station.transportMode.toString()) + " station: " + IGui.formatMTRLanguageName(station.name)));
           builder.add(LandmarkComponentTypes.LORE, List.of(Component.literal("Fare zone: " + station.zone)));
           builder.add(MTRLandmarkComponentTypes.FARE_ZONE, (long)station.zone);
           return builder;
        });
    }

    private Landmark getDepotLandmark(Depot depot) {
        return Landmark.create(WorldLandmarks.GLOBAL, MTRSurveyor.id("depots/" + depot.id), builder -> {
            fillAreaComponents(depot, builder);
            builder.add(LandmarkComponentTypes.NAME, Component.literal(Util.getCamelCase(depot.transportMode.toString()) + " depot: " + IGui.formatMTRLanguageName(depot.name)));
            return builder;
        });
    }

    private void fillAreaComponents(AreaBase areaBase, LandmarkComponentMap.Builder builder) {
        builder.add(LandmarkComponentTypes.COLOR, areaBase.color);
        builder.add(LandmarkComponentTypes.POS, areaBase.getCenter());
        builder.add(LandmarkComponentTypes.BOX, Util.getBoundingBox(areaBase.corner1, areaBase.corner2));
        builder.add(LandmarkComponentTypes.STACK, Util.getItemStackForTransportMode(areaBase.transportMode, areaBase instanceof Depot));
        builder.add(MTRLandmarkComponentTypes.TRANSPORT_TYPE, areaBase.transportMode.toString());
    }

    @Override
    public void onAreaUpdate() {
        WorldSummary worldSummary = WorldSummary.of(level);
        WorldLandmarks landmarks = worldSummary.landmarks();
        if(landmarks == null) return;

        syncLandmarks();
    }
}
