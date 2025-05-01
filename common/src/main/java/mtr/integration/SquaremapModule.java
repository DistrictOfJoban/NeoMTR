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
import net.minecraft.world.level.Level;
import xyz.jpenilla.squaremap.api.*;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class SquaremapModule extends RailwayDataModule implements IGui, IUpdateWebMap, MTRAreaUpdateEvent {

	static {
		try {
			final Registry<BufferedImage> iconRegistry = SquaremapProvider.get().iconRegistry();
			IUpdateWebMap.readResource(STATION_ICON_PATH, inputStream -> {
				try {
					iconRegistry.register(Key.of(STATION_ICON_KEY), ImageIO.read(inputStream));
				} catch (IOException e) {
					MTR.LOGGER.error("", e);
				}
			});
			IUpdateWebMap.readResource(DEPOT_ICON_PATH, inputStream -> {
				try {
					iconRegistry.register(Key.of(DEPOT_ICON_KEY), ImageIO.read(inputStream));
				} catch (IOException e) {
					MTR.LOGGER.error("", e);
				}
			});
		} catch (IllegalStateException ignored) {

		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	public static final String NAME = "map_squaremap";

	public SquaremapModule(RailwayData railwayData, Level level, Map<BlockPos, Map<BlockPos, Rail>> rails) {
		super(NAME, railwayData, level, rails);
	}

	@Override
	public void init() {
		try {
			syncData(level, railwayData);
			MTR.LOGGER.info("[NeoMTR] Squaremap is detected");
		} catch (NoClassDefFoundError | Exception ignored) {
		}
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
		} catch (IllegalStateException ignored) {
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	private static <T extends AreaBase> void syncData(Level level, Set<T> areas, String areasId, String areasTitle, String areaAreasId, String areaAreasTitle, String iconKey) {
		final MapWorld mapWorld = SquaremapProvider.get().getWorldIfEnabled(WorldIdentifier.parse(level.dimension().location().toString())).orElse(null);
		if (mapWorld == null) {
			return;
		}

		final Registry<LayerProvider> layerRegistry = mapWorld.layerRegistry();

		final SimpleLayerProvider providerAreas;
		if (layerRegistry.hasEntry(Key.of(areasId))) {
			providerAreas = (SimpleLayerProvider) layerRegistry.get(Key.of(areasId));
			providerAreas.clearMarkers();
		} else {
			providerAreas = SimpleLayerProvider.builder(areasTitle).showControls(true).build();
			layerRegistry.register(Key.of(areasId), providerAreas);
		}

		final SimpleLayerProvider providerAreaAreas;
		if (layerRegistry.hasEntry(Key.of(areaAreasId))) {
			providerAreaAreas = (SimpleLayerProvider) layerRegistry.get(Key.of(areaAreasId));
			providerAreaAreas.clearMarkers();
		} else {
			providerAreaAreas = SimpleLayerProvider.builder(areaAreasTitle).showControls(true).defaultHidden(true).build();
			layerRegistry.register(Key.of(areaAreasId), providerAreaAreas);
		}

		IUpdateWebMap.iterateAreas(areas, (id, name, color, areaCorner1X, areaCorner1Z, areaCorner2X, areaCorner2Z, areaX, areaZ) -> {
			final MarkerOptions markerOptions = MarkerOptions.builder().hoverTooltip(name).fillColor(color).strokeColor(color.darker()).build();
			providerAreas.addMarker(Key.of(id), Marker.icon(Point.of(areaX, areaZ), Key.of(iconKey), ICON_SIZE).markerOptions(markerOptions));
			providerAreaAreas.addMarker(Key.of(id), Marker.rectangle(Point.of(areaCorner1X, areaCorner1Z), Point.of(areaCorner2X, areaCorner2Z)).markerOptions(markerOptions));
		});
	}
}
