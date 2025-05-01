package mtr.registry;

import mtr.Keys;
import mtr.block.*;
import mtr.data.PIDSType;
import mtr.loader.MTRRegistryClient;
import mtr.render.*;

public class BlockEntityRenderers {
	public static void registerClient() {
		if (!Keys.LIFTS_ONLY) {
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_SMALL_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 12, 1, 1, 15, 16, 14, 14, false, false, PIDSType.ARRIVAL_PROJECTOR, 0xFF9900, 0xFF9900));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_MEDIUM_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 12, 1, -15, 15, 16, 30, 46, false, false, PIDSType.ARRIVAL_PROJECTOR, 0xFF9900, 0xFF9900));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_LARGE_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, 16, 1, -15, 15, 16, 46, 46, false, false, PIDSType.ARRIVAL_PROJECTOR, 0xFF9900, 0xFF9900));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.BOAT_NODE_TILE_ENTITY.get(), RenderBoatNode::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.CLOCK_TILE_ENTITY.get(), RenderClock::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.PSD_DOOR_1_TILE_ENTITY.get(), dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 0));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.PSD_DOOR_2_TILE_ENTITY.get(), dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 1));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.PSD_TOP_TILE_ENTITY.get(), RenderPSDTop::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.APG_GLASS_TILE_ENTITY.get(), RenderAPGGlass::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.APG_DOOR_TILE_ENTITY.get(), dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 2));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.PIDS_1_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS1.TileEntityBlockPIDS1.MAX_ARRIVALS, BlockPIDS1.TileEntityBlockPIDS1.LINES_PER_ARRIVAL, 1, 3.25F, 6, 2.5F, 30, true, false, PIDSType.PIDS, 0xFF9900, 0xFF9900));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.PIDS_2_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS2.TileEntityBlockPIDS2.MAX_ARRIVALS, BlockPIDS2.TileEntityBlockPIDS2.LINES_PER_ARRIVAL, 1.5F, 7.5F, 6, 6.5F, 29, true, true, PIDSType.PIDS, 0xFF9900, 0xFF9900));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.PIDS_3_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS3.TileEntityBlockPIDS3.MAX_ARRIVALS, BlockPIDS3.TileEntityBlockPIDS3.LINES_PER_ARRIVAL, 2.5F, 7.5F, 6, 6.5F, 27, true, false, PIDSType.PIDS, 0xFF9900, 0x33CC00, 1.25F, true));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.PIDS_4_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS4.TileEntityBlockPIDS4.MAX_ARRIVALS, BlockPIDS4.TileEntityBlockPIDS4.LINES_PER_ARRIVAL, 2F, 14F, 15, 28F, 12, false, false, PIDSType.PIDS_VERTICAL, 0xFF9900, 0xFF9900));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.PIDS_SINGLE_ARRIVAL_1_TILE_ENTITY.get(), dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDSSingleArrival1.TileEntityBlockPIDSSingleArrival1.MAX_ARRIVALS, BlockPIDSSingleArrival1.TileEntityBlockPIDSSingleArrival1.LINES_PER_ARRIVAL, 2F, 14F, 15, 28F, 12, false, false, PIDSType.PIDS_SINGLE_ARRIVAL, 0xFF9900, 0xFF9900));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_2_EVEN_TILE_ENTITY.get(), RenderRailwaySign::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_2_ODD_TILE_ENTITY.get(), RenderRailwaySign::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_3_EVEN_TILE_ENTITY.get(), RenderRailwaySign::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_3_ODD_TILE_ENTITY.get(), RenderRailwaySign::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_4_EVEN_TILE_ENTITY.get(), RenderRailwaySign::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_4_ODD_TILE_ENTITY.get(), RenderRailwaySign::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_5_EVEN_TILE_ENTITY.get(), RenderRailwaySign::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_5_ODD_TILE_ENTITY.get(), RenderRailwaySign::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_6_EVEN_TILE_ENTITY.get(), RenderRailwaySign::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_6_ODD_TILE_ENTITY.get(), RenderRailwaySign::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_7_EVEN_TILE_ENTITY.get(), RenderRailwaySign::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_7_ODD_TILE_ENTITY.get(), RenderRailwaySign::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_STANDING_LIGHT_TILE_ENTITY.get(), RenderRouteSign::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_STANDING_METAL_TILE_ENTITY.get(), RenderRouteSign::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_WALL_LIGHT_TILE_ENTITY.get(), RenderRouteSign::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_WALL_METAL_TILE_ENTITY.get(), RenderRouteSign::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_1.get(), dispatcher -> new RenderSignalLight2Aspect<>(dispatcher, true, false, 0xFF0000FF));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_2.get(), dispatcher -> new RenderSignalLight2Aspect<>(dispatcher, false, false, 0xFF0000FF));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_3.get(), dispatcher -> new RenderSignalLight2Aspect<>(dispatcher, true, true, 0xFF00FF00));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_2_ASPECT_4.get(), dispatcher -> new RenderSignalLight2Aspect<>(dispatcher, false, true, 0xFF00FF00));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_3_ASPECT_1.get(), dispatcher -> new RenderSignalLight3Aspect<>(dispatcher, true));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_3_ASPECT_2.get(), dispatcher -> new RenderSignalLight3Aspect<>(dispatcher, false));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_4_ASPECT_1.get(), dispatcher -> new RenderSignalLight4Aspect<>(dispatcher, true));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_4_ASPECT_2.get(), dispatcher -> new RenderSignalLight4Aspect<>(dispatcher, false));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_SEMAPHORE_1.get(), dispatcher -> new RenderSignalSemaphore<>(dispatcher, true));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_SEMAPHORE_2.get(), dispatcher -> new RenderSignalSemaphore<>(dispatcher, false));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_ENTRANCE_TILE_ENTITY.get(), dispatcher -> new RenderStationNameTiled<>(dispatcher, true));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_BLOCK_TILE_ENTITY.get(), RenderStationNameTall::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_BLOCK_DOUBLE_SIDED_TILE_ENTITY.get(), RenderStationNameTall::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_WALL_TILE_ENTITY.get(), RenderStationNameTall::new);
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_WALL_WHITE_TILE_ENTITY.get(), dispatcher -> new RenderStationNameTiled<>(dispatcher, false));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_WALL_GRAY_TILE_ENTITY.get(), dispatcher -> new RenderStationNameTiled<>(dispatcher, false));
			MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_WALL_BLACK_TILE_ENTITY.get(), dispatcher -> new RenderStationNameTiled<>(dispatcher, false));
		}

		MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.LIFT_BUTTONS_1_TILE_ENTITY.get(), RenderLiftButtons::new);
		MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.LIFT_PANEL_EVEN_1_TILE_ENTITY.get(), dispatcher -> new RenderLiftPanel<>(dispatcher, false, false));
		MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.LIFT_PANEL_ODD_1_TILE_ENTITY.get(), dispatcher -> new RenderLiftPanel<>(dispatcher, true, false));
		MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.LIFT_PANEL_EVEN_2_TILE_ENTITY.get(), dispatcher -> new RenderLiftPanel<>(dispatcher, false, true));
		MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.LIFT_PANEL_ODD_2_TILE_ENTITY.get(), dispatcher -> new RenderLiftPanel<>(dispatcher, true, true));
		MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.LIFT_DOOR_EVEN_1_TILE_ENTITY.get(), dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 3));
		MTRRegistryClient.registerTileEntityRenderer(BlockEntityTypes.LIFT_DOOR_ODD_1_TILE_ENTITY.get(), dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 4));
	}
}
