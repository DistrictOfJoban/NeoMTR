package mtr.registry;

import mtr.*;
import mtr.data.RailwayData;
import mtr.integration.SquaremapModule;
import mtr.loader.MTRRegistry;
import mtr.packet.PacketTrainDataGuiServer;
import mtr.integration.BlueMapModule;
import mtr.integration.DynmapModule;
import mtr.servlet.Webserver;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.*;

public class Events {
    public static void register() {
        RailwayData.registerRailwayModule((railwayData, level, rails) -> {
            try {
                return new DynmapModule(railwayData, level, rails);
            } catch (NoClassDefFoundError e) {
                return null;
            }
        });

        RailwayData.registerRailwayModule((railwayData, level, rails) -> {
            try {
                return new BlueMapModule(railwayData, level, rails);
            } catch (NoClassDefFoundError e) {
                return null;
            }
        });

        RailwayData.registerRailwayModule((railwayData, level, rails) -> {
            try {
                return new SquaremapModule(railwayData, level, rails);
            } catch (NoClassDefFoundError e) {
                return null;
            }
        });

        MTRRegistry.registerTickEvent(minecraftServer -> {
            for(ServerLevel serverLevel : minecraftServer.getAllLevels()) {
                final RailwayData railwayData = RailwayData.getInstance(serverLevel);
                if (railwayData != null) {
                    railwayData.simulateTrains(serverLevel);
                }
            }

            MTR.incrementGameTick();
        });

        MTRRegistry.registerPlayerJoinEvent(player -> {
            PacketTrainDataGuiServer.versionCheckS2C(player);
            final RailwayData railwayData = RailwayData.getInstance(player.level());
            if (railwayData != null) {
                railwayData.onPlayerJoin(player);
            }
        });

        MTRRegistry.registerPlayerQuitEvent(player -> {
            final RailwayData railwayData = RailwayData.getInstance(player.level());
            if (railwayData != null) {
                railwayData.disconnectPlayer(player);
            }
        });

        if (!Keys.LIFTS_ONLY) {
            // TODO: Move the webserver away
            Webserver.init();
            MTRRegistry.registerServerStartingEvent(minecraftServer -> {
                Webserver.callback = minecraftServer::execute;
                Webserver.getWorlds = () -> {
                    final List<Level> worlds = new ArrayList<>();
                    minecraftServer.getAllLevels().forEach(worlds::add);
                    return worlds;
                };
                Webserver.getRoutes = railwayData -> railwayData == null ? new HashSet<>() : railwayData.routes;
                Webserver.getDataCache = railwayData -> railwayData == null ? null : railwayData.dataCache;
                Webserver.start(minecraftServer.getServerDirectory().resolve("config").resolve("mtr_webserver_port.txt"));
            });
            MTRRegistry.registerServerStoppingEvent(minecraftServer -> Webserver.stop());
        }
    }
}
