package mtr.registry;

import mtr.Keys;
import mtr.MTR;
import mtr.MTRClient;
import mtr.block.BlockTactileMap;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.RailwayData;
import mtr.data.Station;
import mtr.integration.BlueMapModule;
import mtr.integration.DynmapModule;
import mtr.integration.SquaremapModule;
import mtr.loader.MTRRegistry;
import mtr.loader.MTRRegistryClient;
import mtr.packet.PacketTrainDataGuiServer;
import mtr.servlet.Webserver;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static mtr.MTRClient.TACTILE_MAP_SOUND_INSTANCE;

public class EventsClient {
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
