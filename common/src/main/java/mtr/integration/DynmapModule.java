package mtr.integration;

import mtr.MTR;
import mtr.api.RailwayDataModule;
import mtr.api.events.MTRAreaUpdateEvent;
import mtr.data.AreaBase;
import mtr.data.IGui;
import mtr.data.Rail;
import mtr.data.RailwayData;
import mtr.packet.IUpdateWebMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;
import org.dynmap.markers.AreaMarker;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DynmapModule extends RailwayDataModule implements IGui, IUpdateWebMap, MTRAreaUpdateEvent {

	private static DynmapCommonAPI dynmapCommonAPI;

	static {
		try {
			DynmapCommonAPIListener.register(new DynmapCommonAPIListener() {

				@Override
				public void apiEnabled(DynmapCommonAPI dynmapCommonAPI) {
					DynmapModule.dynmapCommonAPI = dynmapCommonAPI;
					try {
						final org.dynmap.markers.MarkerAPI markerAPI = dynmapCommonAPI.getMarkerAPI();
						IUpdateWebMap.readResource(STATION_ICON_PATH, inputStream -> markerAPI.createMarkerIcon(STATION_ICON_KEY, STATION_ICON_KEY, inputStream));
						IUpdateWebMap.readResource(DEPOT_ICON_PATH, inputStream -> markerAPI.createMarkerIcon(DEPOT_ICON_KEY, DEPOT_ICON_KEY, inputStream));
					} catch (Exception e) {
						MTR.LOGGER.error("", e);
					}
				}
			});
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	public static final String NAME = "map_dynmap";

	public DynmapModule(RailwayData railwayData, Level level, Map<BlockPos, Map<BlockPos, Rail>> rails) {
		super(NAME, railwayData, level, rails);
	}

	@Override
	public void onAreaUpdate() {
		try {
			syncData(level, railwayData);
		} catch (NoClassDefFoundError | Exception ignored) {
		}
	}

	public static void syncData(Level level, RailwayData railwayData) {
		try {
			syncData(level, railwayData.stations, MARKER_SET_STATIONS_ID, MARKER_SET_STATIONS_TITLE, MARKER_SET_STATION_AREAS_ID, MARKER_SET_STATION_AREAS_TITLE, STATION_ICON_KEY);
			syncData(level, railwayData.depots, MARKER_SET_DEPOTS_ID, MARKER_SET_DEPOTS_TITLE, MARKER_SET_DEPOT_AREAS_ID, MARKER_SET_DEPOT_AREAS_TITLE, DEPOT_ICON_KEY);
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	private static <T extends AreaBase> void syncData(Level level, Set<T> areas, String areasId, String areasTitle, String areaAreasId, String areaAreasTitle, String iconKey) {
		if (dynmapCommonAPI != null) {
			final String worldId;
			switch (level.dimension().location().toString()) {
				case "minecraft:overworld":
					final MinecraftServer minecraftServer = level.getServer();
					worldId = minecraftServer == null ? "world" : minecraftServer.getWorldData().getLevelName();
					break;
				case "minecraft:the_nether":
					worldId = "DIM-1";
					break;
				case "minecraft:the_end":
					worldId = "DIM1";
					break;
				default:
					worldId = level.dimension().location().getPath();
					break;
			}

			final int areaY = level.getSeaLevel();
			final org.dynmap.markers.MarkerAPI markerAPI = dynmapCommonAPI.getMarkerAPI();

			final org.dynmap.markers.MarkerSet markerSetAreas;
			org.dynmap.markers.MarkerSet tempMarkerSetAreas = markerAPI.getMarkerSet(areasId);
			markerSetAreas = tempMarkerSetAreas == null ? markerAPI.createMarkerSet(areasId, areasTitle, Collections.singleton(markerAPI.getMarkerIcon(iconKey)), false) : tempMarkerSetAreas;
			markerSetAreas.getMarkers().forEach(marker -> {
				if (marker.getMarkerID().startsWith(worldId)) {
					marker.deleteMarker();
				}
			});

			final org.dynmap.markers.MarkerSet markerSetAreaAreas;
			org.dynmap.markers.MarkerSet tempMarkerSetAreaAreas = markerAPI.getMarkerSet(areaAreasId);
			markerSetAreaAreas = tempMarkerSetAreaAreas == null ? markerAPI.createMarkerSet(areaAreasId, areaAreasTitle, new HashSet<>(), false) : tempMarkerSetAreaAreas;
			markerSetAreaAreas.setHideByDefault(true);
			markerSetAreaAreas.getAreaMarkers().forEach(marker -> {
				if (marker.getMarkerID().startsWith(worldId)) {
					marker.deleteMarker();
				}
			});

			IUpdateWebMap.iterateAreas(areas, (id, name, color, areaCorner1X, areaCorner1Z, areaCorner2X, areaCorner2Z, areaX, areaZ) -> {
				markerSetAreas.createMarker(worldId + id, name, worldId, areaX, areaY, areaZ, markerAPI.getMarkerIcon(iconKey), false);
				final AreaMarker areaMarker = markerSetAreaAreas.createAreaMarker(worldId + id, name, false, worldId, new double[]{areaCorner1X, areaCorner2X}, new double[]{areaCorner1Z, areaCorner2Z}, false);
				areaMarker.setFillStyle(0.5, color.getRGB() & RGB_WHITE);
				areaMarker.setLineStyle(1, 1, color.darker().getRGB() & RGB_WHITE);
			});
		}
	}
}
