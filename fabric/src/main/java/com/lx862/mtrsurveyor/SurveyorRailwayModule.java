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
import net.minecraft.world.level.Level;

import java.util.ArrayList;
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
        MTRSurveyor.LOGGER.info("[{}] Syncing landmarks", MTRSurveyor.MOD_NAME);
        WorldSummary worldSummary = WorldSummary.of(level);
        List<AreaBase> areas = new ArrayList<>();
        areas.addAll(railwayData.stations);
        areas.addAll(railwayData.depots);

        for(AreaBase areaBase : areas) {
            Landmark landmark = getLandmark(areaBase);
            worldSummary.landmarks().put(level, landmark);
        }

        worldSummary.landmarks().removeAll(level, landmark -> {
            Long id = landmark.get(MTRLandmarkComponentTypes.AREA_ID);
            if(id != null) {
                if(!railwayData.dataCache.stationIdMap.containsKey(id) && !railwayData.dataCache.depotIdMap.containsKey(id)) {
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
           builder.add(LandmarkComponentTypes.LORE, List.of(Component.literal("Fare zone: " + station.zone)));
           builder.add(LandmarkComponentTypes.NAME, Component.literal(Util.getCamelCase(station.transportMode.toString()) + " station: " + station.name));
           return builder;
        });
    }

    private Landmark getDepotLandmark(Depot depot) {
        return Landmark.create(WorldLandmarks.GLOBAL, MTRSurveyor.id("depots/" + depot.id), builder -> {
            fillAreaComponents(depot, builder);
            builder.add(LandmarkComponentTypes.NAME, Component.literal(Util.getCamelCase(depot.transportMode.toString()) + " depot: " + depot.name));
            return builder;
        });
    }

    private void fillAreaComponents(AreaBase areaBase, LandmarkComponentMap.Builder builder) {
        builder.add(MTRLandmarkComponentTypes.AREA_ID, areaBase.id);
        builder.add(LandmarkComponentTypes.COLOR, areaBase.color);
        builder.add(LandmarkComponentTypes.POS, areaBase.getCenter());
    }

    @Override
    public void onUpdate() {
        syncLandmarks();
    }
}
