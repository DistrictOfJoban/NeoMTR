package mtr.registry;

import mtr.MTR;
import mtr.Registry;
import mtr.RegistryClient;
import mtr.client.ClientData;
import mtr.data.Depot;
import mtr.data.Route;
import mtr.data.Station;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class Networking {

	public static final ResourceLocation PACKET_VERSION_CHECK = MTR.id("packet_version_check");

	public static final ResourceLocation PACKET_OPEN_DASHBOARD_SCREEN = MTR.id("packet_open_dashboard_screen");
	public static final ResourceLocation PACKET_OPEN_PIDS_CONFIG_SCREEN = MTR.id("packet_open_pids_config_screen");
	public static final ResourceLocation PACKET_OPEN_ARRIVAL_PROJECTOR_CONFIG_SCREEN = MTR.id("packet_open_arrival_projector_config_screen");
	public static final ResourceLocation PACKET_OPEN_RAILWAY_SIGN_SCREEN = MTR.id("packet_open_railway_sign_screen");
	public static final ResourceLocation PACKET_OPEN_TICKET_MACHINE_SCREEN = MTR.id("packet_open_ticket_machine_screen");
	public static final ResourceLocation PACKET_OPEN_TRAIN_SENSOR_SCREEN = MTR.id("packet_open_train_sensor_screen");
	public static final ResourceLocation PACKET_OPEN_LIFT_TRACK_FLOOR_SCREEN = MTR.id("packet_open_lift_track_floor_screen");
	public static final ResourceLocation PACKET_OPEN_LIFT_CUSTOMIZATION_SCREEN = MTR.id("packet_open_lift_customization_screen");
	public static final ResourceLocation PACKET_OPEN_RESOURCE_PACK_CREATOR_SCREEN = MTR.id("packet_open_resource_pack_creator_screen");

	public static final ResourceLocation PACKET_ANNOUNCE = MTR.id("packet_announce");

	public static final ResourceLocation PACKET_CREATE_RAIL = MTR.id("packet_create_rail");
	public static final ResourceLocation PACKET_CREATE_SIGNAL = MTR.id("packet_create_signal");
	public static final ResourceLocation PACKET_REMOVE_NODE = MTR.id("packet_remove_node");
	public static final ResourceLocation PACKET_REMOVE_LIFT_FLOOR_TRACK = MTR.id("packet_remove_lift_floor_track");
	public static final ResourceLocation PACKET_REMOVE_RAIL = MTR.id("packet_remove_rail");
	public static final ResourceLocation PACKET_REMOVE_SIGNALS = MTR.id("packet_remove_signals");
	public static final ResourceLocation PACKET_REMOVE_RAIL_ACTION = MTR.id("packet_remove_rail_action");

	public static final ResourceLocation PACKET_GENERATE_PATH = MTR.id("packet_generate_path");
	public static final ResourceLocation PACKET_CLEAR_TRAINS = MTR.id("packet_clear_trains");
	public static final ResourceLocation PACKET_SIGN_TYPES = MTR.id("packet_sign_types");
	public static final ResourceLocation PACKET_DRIVE_TRAIN = MTR.id("packet_drive_train");
	public static final ResourceLocation PACKET_PRESS_LIFT_BUTTON = MTR.id("packet_press_lift_button");
	public static final ResourceLocation PACKET_ADD_BALANCE = MTR.id("packet_add_balance");
	public static final ResourceLocation PACKET_PIDS_UPDATE = MTR.id("packet_pids_update");
	public static final ResourceLocation PACKET_ARRIVAL_PROJECTOR_UPDATE = MTR.id("packet_arrival_projector_update");
	public static final ResourceLocation PACKET_CHUNK_S2C = MTR.id("packet_chunk_s2c");

	public static final ResourceLocation PACKET_UPDATE_STATION = MTR.id("packet_update_station");
	public static final ResourceLocation PACKET_UPDATE_PLATFORM = MTR.id("packet_update_platform");
	public static final ResourceLocation PACKET_UPDATE_SIDING = MTR.id("packet_update_siding");
	public static final ResourceLocation PACKET_UPDATE_ROUTE = MTR.id("packet_update_route");
	public static final ResourceLocation PACKET_UPDATE_DEPOT = MTR.id("packet_update_depot");
	public static final ResourceLocation PACKET_UPDATE_LIFT = MTR.id("packet_update_lift");

	public static final ResourceLocation PACKET_DELETE_STATION = MTR.id("packet_delete_station");
	public static final ResourceLocation PACKET_DELETE_PLATFORM = MTR.id("packet_delete_platform");
	public static final ResourceLocation PACKET_DELETE_SIDING = MTR.id("packet_delete_siding");
	public static final ResourceLocation PACKET_DELETE_ROUTE = MTR.id("packet_delete_route");
	public static final ResourceLocation PACKET_DELETE_DEPOT = MTR.id("packet_delete_depot");

	public static final ResourceLocation PACKET_WRITE_RAILS = MTR.id("write_rails");
	public static final ResourceLocation PACKET_UPDATE_TRAINS = MTR.id("update_trains");
	public static final ResourceLocation PACKET_DELETE_TRAINS = MTR.id("delete_trains");
	public static final ResourceLocation PACKET_UPDATE_LIFTS = MTR.id("update_lifts");
	public static final ResourceLocation PACKET_DELETE_LIFTS = MTR.id("delete_lifts");
	public static final ResourceLocation PACKET_UPDATE_TRAIN_PASSENGERS = MTR.id("update_train_passengers");
	public static final ResourceLocation PACKET_UPDATE_TRAIN_PASSENGER_POSITION = MTR.id("update_train_passenger_position");
	public static final ResourceLocation PACKET_UPDATE_LIFT_PASSENGERS = MTR.id("update_lift_passengers");
	public static final ResourceLocation PACKET_UPDATE_LIFT_PASSENGER_POSITION = MTR.id("update_lift_passenger_position");
	public static final ResourceLocation PACKET_UPDATE_ENTITY_SEAT_POSITION = MTR.id("update_entity_seat_position");
	public static final ResourceLocation PACKET_UPDATE_RAIL_ACTIONS = MTR.id("update_rail_actions");
	public static final ResourceLocation PACKET_UPDATE_SCHEDULE = MTR.id("update_schedule");
	public static final ResourceLocation PACKET_UPDATE_TRAIN_SENSOR = MTR.id("packet_update_train_announcer");
	public static final ResourceLocation PACKET_UPDATE_LIFT_TRACK_FLOOR = MTR.id("packet_update_lift_track_floor");

	public static final int MAX_PACKET_BYTES = 1048576;

	public static void register() {
		Registry.registerNetworkReceiver(PACKET_GENERATE_PATH, PacketTrainDataGuiServer::generatePathC2S);
		Registry.registerNetworkReceiver(PACKET_CLEAR_TRAINS, PacketTrainDataGuiServer::clearTrainsC2S);
		Registry.registerNetworkReceiver(PACKET_SIGN_TYPES, PacketTrainDataGuiServer::receiveSignIdsC2S);
		Registry.registerNetworkReceiver(PACKET_ADD_BALANCE, PacketTrainDataGuiServer::receiveAddBalanceC2S);
		Registry.registerNetworkReceiver(PACKET_PIDS_UPDATE, PacketTrainDataGuiServer::receivePIDSMessageC2S);
		Registry.registerNetworkReceiver(PACKET_ARRIVAL_PROJECTOR_UPDATE, PacketTrainDataGuiServer::receiveArrivalProjectorMessageC2S);
		Registry.registerNetworkReceiver(PACKET_UPDATE_STATION, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_STATION, railwayData -> railwayData.stations, railwayData -> railwayData.dataCache.stationIdMap, (id, transportMode) -> new Station(id), false));
		Registry.registerNetworkReceiver(PACKET_UPDATE_PLATFORM, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_PLATFORM, railwayData -> railwayData.platforms, railwayData -> railwayData.dataCache.platformIdMap, null, false));
		Registry.registerNetworkReceiver(PACKET_UPDATE_SIDING, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_SIDING, railwayData -> railwayData.sidings, railwayData -> railwayData.dataCache.sidingIdMap, null, false));
		Registry.registerNetworkReceiver(PACKET_UPDATE_ROUTE, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_ROUTE, railwayData -> railwayData.routes, railwayData -> railwayData.dataCache.routeIdMap, Route::new, false));
		Registry.registerNetworkReceiver(PACKET_UPDATE_DEPOT, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_DEPOT, railwayData -> railwayData.depots, railwayData -> railwayData.dataCache.depotIdMap, Depot::new, false));
		Registry.registerNetworkReceiver(PACKET_UPDATE_LIFT, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_LIFT, railwayData -> railwayData.lifts, railwayData -> railwayData.dataCache.liftsServerIdMap, null, false));
		Registry.registerNetworkReceiver(PACKET_UPDATE_LIFT_TRACK_FLOOR, PacketTrainDataGuiServer::receiveLiftTrackFloorC2S);
		Registry.registerNetworkReceiver(PACKET_DELETE_STATION, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_DELETE_STATION, railwayData -> railwayData.stations, railwayData -> railwayData.dataCache.stationIdMap, null, true));
		Registry.registerNetworkReceiver(PACKET_DELETE_PLATFORM, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_DELETE_PLATFORM, railwayData -> railwayData.platforms, railwayData -> railwayData.dataCache.platformIdMap, null, true));
		Registry.registerNetworkReceiver(PACKET_DELETE_SIDING, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_DELETE_SIDING, railwayData -> railwayData.sidings, railwayData -> railwayData.dataCache.sidingIdMap, null, true));
		Registry.registerNetworkReceiver(PACKET_DELETE_ROUTE, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_DELETE_ROUTE, railwayData -> railwayData.routes, railwayData -> railwayData.dataCache.routeIdMap, null, true));
		Registry.registerNetworkReceiver(PACKET_DELETE_DEPOT, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_DELETE_DEPOT, railwayData -> railwayData.depots, railwayData -> railwayData.dataCache.depotIdMap, null, true));
		Registry.registerNetworkReceiver(PACKET_UPDATE_TRAIN_SENSOR, PacketTrainDataGuiServer::receiveTrainSensorC2S);
		Registry.registerNetworkReceiver(PACKET_REMOVE_RAIL_ACTION, PacketTrainDataGuiServer::receiveRemoveRailAction);
		Registry.registerNetworkReceiver(PACKET_UPDATE_TRAIN_PASSENGER_POSITION, PacketTrainDataGuiServer::receiveUpdateTrainPassengerPosition);
		Registry.registerNetworkReceiver(PACKET_UPDATE_LIFT_PASSENGER_POSITION, PacketTrainDataGuiServer::receiveUpdateLiftPassengerPosition);
		Registry.registerNetworkReceiver(PACKET_UPDATE_ENTITY_SEAT_POSITION, PacketTrainDataGuiServer::receiveUpdateEntitySeatPassengerPosition);
		Registry.registerNetworkReceiver(PACKET_DRIVE_TRAIN, PacketTrainDataGuiServer::receiveDriveTrainC2S);
		Registry.registerNetworkReceiver(PACKET_PRESS_LIFT_BUTTON, PacketTrainDataGuiServer::receivePressLiftButtonC2S);

		Registry.registerNetworkPacket(PACKET_VERSION_CHECK);
		Registry.registerNetworkPacket(PACKET_CHUNK_S2C);
		Registry.registerNetworkPacket(PACKET_OPEN_DASHBOARD_SCREEN);
		Registry.registerNetworkPacket(PACKET_OPEN_PIDS_CONFIG_SCREEN);
		Registry.registerNetworkPacket(PACKET_OPEN_ARRIVAL_PROJECTOR_CONFIG_SCREEN);
		Registry.registerNetworkPacket(PACKET_OPEN_RAILWAY_SIGN_SCREEN);
		Registry.registerNetworkPacket(PACKET_OPEN_TICKET_MACHINE_SCREEN);
		Registry.registerNetworkPacket(PACKET_OPEN_TRAIN_SENSOR_SCREEN);
		Registry.registerNetworkPacket(PACKET_OPEN_RESOURCE_PACK_CREATOR_SCREEN);
		Registry.registerNetworkPacket(PACKET_ANNOUNCE);
		Registry.registerNetworkPacket(PACKET_GENERATE_PATH);
		Registry.registerNetworkPacket(PACKET_CREATE_RAIL);
		Registry.registerNetworkPacket(PACKET_CREATE_SIGNAL);
		Registry.registerNetworkPacket(PACKET_REMOVE_NODE);
		Registry.registerNetworkPacket(PACKET_REMOVE_RAIL);
		Registry.registerNetworkPacket(PACKET_REMOVE_SIGNALS);
		Registry.registerNetworkPacket(PACKET_REMOVE_LIFT_FLOOR_TRACK);
		Registry.registerNetworkPacket(PACKET_UPDATE_STATION);
		Registry.registerNetworkPacket(PACKET_UPDATE_PLATFORM);
		Registry.registerNetworkPacket(PACKET_UPDATE_SIDING);
		Registry.registerNetworkPacket(PACKET_UPDATE_ROUTE);
		Registry.registerNetworkPacket(PACKET_UPDATE_DEPOT);
		Registry.registerNetworkPacket(PACKET_DELETE_STATION);
		Registry.registerNetworkPacket(PACKET_DELETE_PLATFORM);
		Registry.registerNetworkPacket(PACKET_DELETE_SIDING);
		Registry.registerNetworkPacket(PACKET_DELETE_ROUTE);
		Registry.registerNetworkPacket(PACKET_DELETE_DEPOT);
		Registry.registerNetworkPacket(PACKET_UPDATE_LIFT);
		Registry.registerNetworkPacket(PACKET_WRITE_RAILS);
		Registry.registerNetworkPacket(PACKET_UPDATE_TRAINS);
		Registry.registerNetworkPacket(PACKET_UPDATE_LIFTS);
		Registry.registerNetworkPacket(PACKET_DELETE_TRAINS);
		Registry.registerNetworkPacket(PACKET_DELETE_LIFTS);
		Registry.registerNetworkPacket(PACKET_UPDATE_TRAIN_PASSENGERS);
		Registry.registerNetworkPacket(PACKET_UPDATE_LIFT_PASSENGERS);
		Registry.registerNetworkPacket(PACKET_UPDATE_TRAIN_PASSENGER_POSITION);
		Registry.registerNetworkPacket(PACKET_UPDATE_LIFT_PASSENGER_POSITION);
		Registry.registerNetworkPacket(PACKET_UPDATE_RAIL_ACTIONS);
		Registry.registerNetworkPacket(PACKET_UPDATE_SCHEDULE);
		Registry.registerNetworkPacket(PACKET_OPEN_LIFT_TRACK_FLOOR_SCREEN);
		Registry.registerNetworkPacket(PACKET_OPEN_LIFT_CUSTOMIZATION_SCREEN);
	}

	public static void registerClient() {
		RegistryClient.registerNetworkReceiver(PACKET_VERSION_CHECK, packet -> PacketTrainDataGuiClient.openVersionCheckS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_CHUNK_S2C, packet -> PacketTrainDataGuiClient.receiveChunk(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_DASHBOARD_SCREEN, packet -> PacketTrainDataGuiClient.openDashboardScreenS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_PIDS_CONFIG_SCREEN, packet -> PacketTrainDataGuiClient.openPIDSConfigScreenS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_ARRIVAL_PROJECTOR_CONFIG_SCREEN, packet -> PacketTrainDataGuiClient.openArrivalProjectorConfigScreenS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_RAILWAY_SIGN_SCREEN, packet -> PacketTrainDataGuiClient.openRailwaySignScreenS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_TICKET_MACHINE_SCREEN, packet -> PacketTrainDataGuiClient.openTicketMachineScreenS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_TRAIN_SENSOR_SCREEN, packet -> PacketTrainDataGuiClient.openTrainSensorScreenS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_RESOURCE_PACK_CREATOR_SCREEN, packet -> PacketTrainDataGuiClient.openResourcePackCreatorScreen(Minecraft.getInstance()));
		RegistryClient.registerNetworkReceiver(PACKET_ANNOUNCE, packet -> PacketTrainDataGuiClient.announceS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_GENERATE_PATH, packet -> PacketTrainDataGuiClient.generatePathS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_CREATE_RAIL, packet -> PacketTrainDataGuiClient.createRailS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_CREATE_SIGNAL, packet -> PacketTrainDataGuiClient.createSignalS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_REMOVE_NODE, packet -> PacketTrainDataGuiClient.removeNodeS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_REMOVE_RAIL, packet -> PacketTrainDataGuiClient.removeRailConnectionS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_REMOVE_SIGNALS, packet -> PacketTrainDataGuiClient.removeSignalsS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_REMOVE_LIFT_FLOOR_TRACK, packet -> PacketTrainDataGuiClient.removeLiftFloorTrackS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_STATION, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.STATIONS, ClientData.DATA_CACHE.stationIdMap, (id, transportMode) -> new Station(id), false));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_PLATFORM, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.PLATFORMS, ClientData.DATA_CACHE.platformIdMap, null, false));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_SIDING, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.SIDINGS, ClientData.DATA_CACHE.sidingIdMap, null, false));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_ROUTE, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.ROUTES, ClientData.DATA_CACHE.routeIdMap, Route::new, false));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_DEPOT, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.DEPOTS, ClientData.DATA_CACHE.depotIdMap, Depot::new, false));
		RegistryClient.registerNetworkReceiver(PACKET_DELETE_STATION, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.STATIONS, ClientData.DATA_CACHE.stationIdMap, (id, transportMode) -> new Station(id), true));
		RegistryClient.registerNetworkReceiver(PACKET_DELETE_PLATFORM, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.PLATFORMS, ClientData.DATA_CACHE.platformIdMap, null, true));
		RegistryClient.registerNetworkReceiver(PACKET_DELETE_SIDING, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.SIDINGS, ClientData.DATA_CACHE.sidingIdMap, null, true));
		RegistryClient.registerNetworkReceiver(PACKET_DELETE_ROUTE, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.ROUTES, ClientData.DATA_CACHE.routeIdMap, Route::new, true));
		RegistryClient.registerNetworkReceiver(PACKET_DELETE_DEPOT, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.DEPOTS, ClientData.DATA_CACHE.depotIdMap, Depot::new, true));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_LIFT, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.LIFTS, ClientData.DATA_CACHE.liftsClientIdMap, null, false));
		RegistryClient.registerNetworkReceiver(PACKET_WRITE_RAILS, packet -> ClientData.writeRails(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_TRAINS, packet -> ClientData.updateTrains(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_LIFTS, packet -> ClientData.updateLifts(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_DELETE_TRAINS, packet -> ClientData.deleteTrains(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_DELETE_LIFTS, packet -> ClientData.deleteLifts(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_TRAIN_PASSENGERS, packet -> ClientData.updateTrainPassengers(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_LIFT_PASSENGERS, packet -> ClientData.updateLiftPassengers(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_TRAIN_PASSENGER_POSITION, packet -> ClientData.updateTrainPassengerPosition(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_LIFT_PASSENGER_POSITION, packet -> ClientData.updateLiftPassengerPosition(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_RAIL_ACTIONS, packet -> ClientData.updateRailActions(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_SCHEDULE, packet -> ClientData.updateSchedule(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_LIFT_TRACK_FLOOR_SCREEN, packet -> PacketTrainDataGuiClient.openLiftTrackFloorS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_LIFT_CUSTOMIZATION_SCREEN, packet -> PacketTrainDataGuiClient.openLiftCustomizationS2C(Minecraft.getInstance(), packet));
	}
}
