package mtr.integration;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.markers.MarkerSet;
import de.bluecolored.bluemap.api.markers.POIMarker;
import de.bluecolored.bluemap.api.markers.ShapeMarker;
import de.bluecolored.bluemap.api.math.Color;
import de.bluecolored.bluemap.api.math.Shape;
import mtr.MTR;
import mtr.api.RailwayDataModule;
import mtr.api.events.MTRAreaUpdateEvent;
import mtr.data.AreaBase;
import mtr.data.IGui;
import mtr.data.Rail;
import mtr.data.RailwayData;
import mtr.packet.IUpdateWebMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.Set;

public class BlueMapModule extends RailwayDataModule implements IGui, IUpdateWebMap, MTRAreaUpdateEvent {
	public static final String NAME = "map_bluemap";

	public BlueMapModule(RailwayData railwayData, Level level, Map<BlockPos, Map<BlockPos, Rail>> rails) {
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
			syncData(level, railwayData.stations, MARKER_SET_STATIONS_ID, MARKER_SET_STATIONS_TITLE, MARKER_SET_STATION_AREAS_ID, MARKER_SET_STATION_AREAS_TITLE, STATION_ICON_PATH);
			syncData(level, railwayData.depots, MARKER_SET_DEPOTS_ID, MARKER_SET_DEPOTS_TITLE, MARKER_SET_DEPOT_AREAS_ID, MARKER_SET_DEPOT_AREAS_TITLE, DEPOT_ICON_PATH);
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	private static <T extends AreaBase> void syncData(Level level, Set<T> areas, String areasId, String areasTitle, String areaAreasId, String areaAreasTitle, String iconKey) {
		final BlueMapAPI api = BlueMapAPI.getInstance().orElse(null);
		if (api == null) {
			return;
		}

		final String worldId = level.dimension().location().getPath();
		final BlueMapMap map = api.getMaps().stream().filter(map1 -> worldId.contains(map1.getId())).findFirst().orElse(null);
		if (map == null) {
			return;
		}

		final int areaY = level.getSeaLevel();

		final MarkerSet markerSetAreas = MarkerSet.builder().label(areasTitle).build();
		markerSetAreas.getMarkers().clear();
		map.getMarkerSets().put(areasId, markerSetAreas);

		final MarkerSet markerSetAreaAreas = MarkerSet.builder().label(areaAreasTitle).defaultHidden(true).build();
		markerSetAreaAreas.getMarkers().clear();
		map.getMarkerSets().put(areaAreasId, markerSetAreaAreas);

		IUpdateWebMap.iterateAreas(areas, (id, name, color, areaCorner1X, areaCorner1Z, areaCorner2X, areaCorner2Z, areaX, areaZ) -> {
			final POIMarker markerArea = POIMarker.toBuilder()
					.position(areaX, areaY, areaZ)
					.label(name)
					.icon(iconKey, ICON_SIZE / 2, ICON_SIZE / 2)
					.build();
			markerSetAreas.getMarkers().put("1_" + worldId + id, markerArea);
			final ShapeMarker markerAreaArea = ShapeMarker.builder()
					.position(areaX, areaY, areaZ)
					.shape(Shape.createRect(areaCorner1X, areaCorner1Z, areaCorner2X, areaCorner2Z), areaY)
					.label(name)
					.fillColor(new Color(color.getRGB() & RGB_WHITE, 0.5F))
					.lineColor(new Color(color.darker().getRGB()))
					.build();
			markerSetAreas.getMarkers().put("2_" + worldId + id, markerAreaArea);
		});
	}
}
