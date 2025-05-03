package com.lx862.mtrtdm;

import com.lx862.mtrtdm.config.TrainConfig;
import com.lx862.mtrtdm.data.TrainBehavior;
import com.lx862.mtrtdm.mixin.SidingAccessorMixin;
import com.lx862.mtrtdm.mixin.TrainServerAccessorMixin;
import mtr.MTR;
import mtr.data.RailwayData;
import mtr.data.Route;
import mtr.data.Siding;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class TDMEvents {
    private static boolean routesFetched = false;

    public static void onServerTick(MinecraftServer server) {
        TickManager.onTick();

        if(!routesFetched) {
            for (ServerLevel world : server.getAllLevels()) {
                updateConfigRouteDetails(world);
            }
            routesFetched = true;
        }

        /* Siding ID : Current Running Route (Map) */
        if(MTR.isGameTickInterval(100)) {
            for (ServerLevel world : server.getAllLevels()) {
                RailwayData data = RailwayData.getInstance(world);
                if(data == null) continue;

                for(Siding siding : data.sidings) {
                    ((SidingAccessorMixin)siding).getTrains().forEach(trainServer -> {
                        Long ogRouteId = TrainDrivingModule.sidingToRouteMap.getOrDefault(trainServer.sidingId, ((TrainServerAccessorMixin)trainServer).getRouteId());

                        if(ogRouteId != 0) {
                            TrainDrivingModule.sidingToRouteMap.put(trainServer.sidingId, ogRouteId);
                        } else {
                            TrainDrivingModule.sidingToRouteMap.remove(trainServer.sidingId);
                        }
                    });
                }
            }
        }
    }

    public static void setRoutesFetched(boolean bool) {
        routesFetched = bool;
    }

    public static void updateConfigRouteDetails(Level world) {
        RailwayData data = RailwayData.getInstance(world);
        if(data == null) return;

        for(int i = 0; i < TrainConfig.routeConfig.size(); i++) {
            TrainBehavior config = TrainConfig.routeConfig.get(i);
            if(!config.routeId.isEmpty()) continue;

            List<Long> routeId = new ArrayList<>();

            for(Route route : data.routes) {
                if(config.routeName.contains(route.name)) {
                    routeId.add(route.id);
                }
            }
            config.routeId = routeId;
            TrainConfig.routeConfig.set(i, config);
        }
    }
}