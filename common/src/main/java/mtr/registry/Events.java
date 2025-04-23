package mtr.registry;

import mtr.*;
import mtr.block.BlockTactileMap;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.RailwayData;
import mtr.data.Station;
import mtr.loader.MTRRegistry;
import mtr.loader.MTRRegistryClient;
import mtr.packet.PacketTrainDataGuiServer;
import mtr.servlet.Webserver;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.*;

import static mtr.MTRClient.TACTILE_MAP_SOUND_INSTANCE;

public class Events {
    public static void register() {
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

    public static void registerClient() {
        MTRRegistryClient.registerPlayerJoinEvent(player -> {
            MTRClient.probeForMod(player);

            if (!Keys.LIFTS_ONLY) {
                final Minecraft minecraft = Minecraft.getInstance();
                if (!minecraft.hasSingleplayerServer()) {
                    Webserver.callback = minecraft::execute;
                    Webserver.getWorlds = () -> minecraft.level == null ? new ArrayList<>() : Collections.singletonList(minecraft.level);
                    Webserver.getRoutes = railwayData -> ClientData.ROUTES;
                    Webserver.getDataCache = railwayData -> ClientData.DATA_CACHE;
                    Webserver.start(Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("mtr_webserver_port.txt"));
                }
            }
        });

        if (!Keys.LIFTS_ONLY) {
            Webserver.init();
            MTRRegistry.registerPlayerQuitEvent(player -> Webserver.stop());

            BlockTactileMap.TileEntityTactileMap.updateSoundSource = TACTILE_MAP_SOUND_INSTANCE::setPos;
            BlockTactileMap.TileEntityTactileMap.onUse = pos -> {
                final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
                if (station != null) {
                    IDrawing.narrateOrAnnounce(IGui.insertTranslation("gui.mtr.welcome_station_cjk", "gui.mtr.welcome_station", 1, IGui.textOrUntitled(station.name)));
                }
            };
        }
    }
}
