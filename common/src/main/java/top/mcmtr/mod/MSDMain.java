package top.mcmtr.mod;

import mtr.registry.MTRAddonRegistry;
import net.minecraft.resources.ResourceLocation;
import top.mcmtr.loader.MSDRegistry;
import top.mcmtr.mod.data.CatenaryData;
import top.mcmtr.mod.data.RigidCatenaryData;
import top.mcmtr.mod.data.TransCatenaryData;
import top.mcmtr.mod.packet.MSDPacket;
import top.mcmtr.mod.packet.MSDPacketTrainDataGuiServer;

import static top.mcmtr.mod.packet.MSDPacket.*;

public class MSDMain {
    public static final String MOD_ID = "msd";
    public static final MTRAddonRegistry.MTRAddon ADDON = new MTRAddonRegistry.MTRAddon(MOD_ID, "Station Decoration", "1.3.4-enhancement-1");

    public static void init() {
        MSDRegistry.registerItem("catenary_connector", MSDItems.CATENARY_CONNECTOR);
        MSDRegistry.registerItem("electric_connector", MSDItems.ELECTRIC_CONNECTOR);
        MSDRegistry.registerItem("catenary_remover", MSDItems.CATENARY_REMOVER);
        MSDRegistry.registerItem("rigid_catenary_connector", MSDItems.RIGID_CATENARY_CONNECTOR);
        MSDRegistry.registerItem("rigid_catenary_remover", MSDItems.RIGID_CATENARY_REMOVER);
        MSDRegistry.registerItem("rigid_soft_catenary_connector", MSDItems.RIGID_SOFT_CATENARY_CONNECTOR);
        MSDRegistry.registerItem("model_change_stick", MSDItems.MODEL_CHANGE_STICK);
        MSDRegistry.registerItem("trans_catenary_connector", MSDItems.TRANS_CATENARY_CONNECTOR);
        MSDRegistry.registerItem("trans_catenary_remover", MSDItems.TRANS_CATENARY_REMOVER);
        MSDRegistry.registerItem("trans_electric_connector", MSDItems.TRANS_ELECTRIC_CONNECTOR);

        MSDRegistry.registerBlockAndItem("decoration_stair", MSDBlocks.DECORATION_STAIR, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("decoration_floor", MSDBlocks.DECORATION_FLOOR, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("railing_stair_start", MSDBlocks.RAILING_STAIR_START, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("railing_stair", MSDBlocks.RAILING_STAIR, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("railing_stair_end", MSDBlocks.RAILING_STAIR_END, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("railing_stair_start_mirror", MSDBlocks.RAILING_STAIR_START_MIRROR, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("railing_stair_mirror", MSDBlocks.RAILING_STAIR_MIRROR, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("railing_stair_end_mirror", MSDBlocks.RAILING_STAIR_END_MIRROR, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("railing_stair_flat", MSDBlocks.RAILING_STAIR_FLAT, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yuuni_pids", MSDBlocks.YUUNI_PIDS, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yuuni_pids_pole", MSDBlocks.YUUNI_PIDS_POLE, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yuuni_2_pids", MSDBlocks.YUUNI_2_PIDS, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yamanote_4_pids", MSDBlocks.YAMANOTE_4_PIDS, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yamanote_5_pids", MSDBlocks.YAMANOTE_5_PIDS, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yamanote_6_pids", MSDBlocks.YAMANOTE_6_PIDS, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yamanote_7_pids", MSDBlocks.YAMANOTE_7_PIDS, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yamanote_railway_sign_2_even", MSDBlocks.YAMANOTE_RAILWAY_SIGN_2_EVEN, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yamanote_railway_sign_2_odd", MSDBlocks.YAMANOTE_RAILWAY_SIGN_2_ODD, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yamanote_railway_sign_3_even", MSDBlocks.YAMANOTE_RAILWAY_SIGN_3_EVEN, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yamanote_railway_sign_3_odd", MSDBlocks.YAMANOTE_RAILWAY_SIGN_3_ODD, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yamanote_railway_sign_4_even", MSDBlocks.YAMANOTE_RAILWAY_SIGN_4_EVEN, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yamanote_railway_sign_4_odd", MSDBlocks.YAMANOTE_RAILWAY_SIGN_4_ODD, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yamanote_railway_sign_5_even", MSDBlocks.YAMANOTE_RAILWAY_SIGN_5_EVEN, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yamanote_railway_sign_5_odd", MSDBlocks.YAMANOTE_RAILWAY_SIGN_5_ODD, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yamanote_railway_sign_6_even", MSDBlocks.YAMANOTE_RAILWAY_SIGN_6_EVEN, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yamanote_railway_sign_6_odd", MSDBlocks.YAMANOTE_RAILWAY_SIGN_6_ODD, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yamanote_railway_sign_7_even", MSDBlocks.YAMANOTE_RAILWAY_SIGN_7_EVEN, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yamanote_railway_sign_7_odd", MSDBlocks.YAMANOTE_RAILWAY_SIGN_7_ODD, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yamanote_railway_sign_pole", MSDBlocks.YAMANOTE_RAILWAY_SIGN_POLE, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("surveillance_cameras", MSDBlocks.SURVEILLANCE_CAMERAS, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("surveillance_cameras_wall", MSDBlocks.SURVEILLANCE_CAMERAS_WALL, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("hall_seat_side", MSDBlocks.HALL_SEAT_SIDE, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("hall_seat_middle", MSDBlocks.HALL_SEAT_MIDDLE, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("hall_seat_side_mirror", MSDBlocks.HALL_SEAT_SIDE_MIRROR, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("catenary_pole", MSDBlocks.CATENARY_POLE, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("catenary_pole_top_side", MSDBlocks.CATENARY_POLE_TOP_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("catenary_pole_top_middle", MSDBlocks.CATENARY_POLE_TOP_MIDDLE, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("catenary_rack_pole", MSDBlocks.CATENARY_RACK_POLE, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("catenary_rack_pole_both_side", MSDBlocks.CATENARY_RACK_POLE_BOTH_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("catenary_rack_side", MSDBlocks.CATENARY_RACK_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("catenary_rack_both_side", MSDBlocks.CATENARY_RACK_BOTH_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("catenary_rack_2", MSDBlocks.CATENARY_RACK_2, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("catenary_rack_1", MSDBlocks.CATENARY_RACK_1, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("catenary_node", MSDBlocks.CATENARY_NODE, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("catenary_node_style_2", MSDBlocks.CATENARY_NODE_STYLE_2, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("short_catenary_rack_pole", MSDBlocks.SHORT_CATENARY_RACK_POLE, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("short_catenary_rack_pole_both_side", MSDBlocks.SHORT_CATENARY_RACK_POLE_BOTH_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("short_catenary_rack_side", MSDBlocks.SHORT_CATENARY_RACK_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("short_catenary_rack_both_side", MSDBlocks.SHORT_CATENARY_RACK_BOTH_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("short_catenary_rack", MSDBlocks.SHORT_CATENARY_RACK, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("short_catenary_node", MSDBlocks.SHORT_CATENARY_NODE, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("short_catenary_node_style_2", MSDBlocks.SHORT_CATENARY_NODE_STYLE_2, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("electric_pole_top_side", MSDBlocks.ELECTRIC_POLE_TOP_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("electric_pole_top_both_side", MSDBlocks.ELECTRIC_POLE_TOP_BOTH_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("electric_pole_side", MSDBlocks.ELECTRIC_POLE_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("electric_pole_another_side", MSDBlocks.ELECTRIC_POLE_ANOTHER_SIDE, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("electric_node", MSDBlocks.ELECTRIC_NODE, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("rigid_catenary_node", MSDBlocks.RIGID_CATENARY_NODE, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("laptop", MSDBlocks.LAPTOP, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("decoration_book", MSDBlocks.DECORATION_BOOK, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("display_board_horizontal", MSDBlocks.DISPLAY_BOARD_HORIZONTAL, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("display_board_vertically", MSDBlocks.DISPLAY_BOARD_VERTICALLY, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yuuni_ticket", MSDBlocks.YUUNI_TICKET, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("ceiling_double", MSDBlocks.CEILING_DOUBLE, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("ceiling_double_light", MSDBlocks.CEILING_DOUBLE_LIGHT, MSDCreativeModeTabs.MSD_Station_Decoration);
        MSDRegistry.registerBlockAndItem("yuuni_standing_sign", MSDBlocks.STANDING_SIGN, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("yuuni_standing_sign_pole", MSDBlocks.STANDING_SIGN_POLE, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("yuuni_standing_sign_1", MSDBlocks.STANDING_SIGN_1, MSDCreativeModeTabs.MSD_BLOCKS);
        MSDRegistry.registerBlockAndItem("trans_catenary_node", MSDBlocks.TRANS_CATENARY_NODE, MSDCreativeModeTabs.MSD_BLOCKS);

        MSDRegistry.registerBlock("yamanote_railway_sign_middle", MSDBlocks.YAMANOTE_RAILWAY_SIGN_MIDDLE);

        MSDRegistry.registerBlockEntityType("yuuni_pids", MSDBlockEntityTypes.YUUNI_PIDS_TILE_ENTITY);
        MSDRegistry.registerBlockEntityType("yuuni_2_pids", MSDBlockEntityTypes.YUUNI_PIDS_2_TILE_ENTITY);
        MSDRegistry.registerBlockEntityType("yamanote_4_pids", MSDBlockEntityTypes.YAMANOTE_4_PIDS_TILE_ENTITY);
        MSDRegistry.registerBlockEntityType("yamanote_5_pids", MSDBlockEntityTypes.YAMANOTE_5_PIDS_TILE_ENTITY);
        MSDRegistry.registerBlockEntityType("yamanote_6_pids", MSDBlockEntityTypes.YAMANOTE_6_PIDS_TILE_ENTITY);
        MSDRegistry.registerBlockEntityType("yamanote_7_pids", MSDBlockEntityTypes.YAMANOTE_7_PIDS_TILE_ENTITY);
        MSDRegistry.registerBlockEntityType("yamanote_railway_sign_2_even", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_2_EVEN_TILE_ENTITY);
        MSDRegistry.registerBlockEntityType("yamanote_railway_sign_2_odd", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_2_ODD_TILE_ENTITY);
        MSDRegistry.registerBlockEntityType("yamanote_railway_sign_3_even", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_3_EVEN_TILE_ENTITY);
        MSDRegistry.registerBlockEntityType("yamanote_railway_sign_3_odd", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_3_ODD_TILE_ENTITY);
        MSDRegistry.registerBlockEntityType("yamanote_railway_sign_4_even", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_4_EVEN_TILE_ENTITY);
        MSDRegistry.registerBlockEntityType("yamanote_railway_sign_4_odd", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_4_ODD_TILE_ENTITY);
        MSDRegistry.registerBlockEntityType("yamanote_railway_sign_5_even", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_5_EVEN_TILE_ENTITY);
        MSDRegistry.registerBlockEntityType("yamanote_railway_sign_5_odd", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_5_ODD_TILE_ENTITY);
        MSDRegistry.registerBlockEntityType("yamanote_railway_sign_6_even", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_6_EVEN_TILE_ENTITY);
        MSDRegistry.registerBlockEntityType("yamanote_railway_sign_6_odd", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_6_ODD_TILE_ENTITY);
        MSDRegistry.registerBlockEntityType("yamanote_railway_sign_7_even", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_7_EVEN_TILE_ENTITY);
        MSDRegistry.registerBlockEntityType("yamanote_railway_sign_7_odd", MSDBlockEntityTypes.YAMANOTE_RAILWAY_SIGN_7_ODD_TILE_ENTITY);
        MSDRegistry.registerBlockEntityType("yuuni_standing_sign", MSDBlockEntityTypes.STANDING_SIGN_TILE_ENTITY);
        MSDRegistry.registerBlockEntityType("yuuni_standing_sign_1", MSDBlockEntityTypes.STANDING_SIGN_1_TILE_ENTITY);
        MSDRegistry.registerBlockEntityType("trans_catenary_node", MSDBlockEntityTypes.BLOCK_TRANS_CATENARY_NODE_ENTITY);
        MSDRegistry.registerBlockEntityType("catenary_node", MSDBlockEntityTypes.BLOCK_CATENARY_NODE_ENTITY);
        MSDRegistry.registerBlockEntityType("catenary_node_style_2", MSDBlockEntityTypes.BLOCK_CATENARY_NODE_STYLE_2_ENTITY);
        MSDRegistry.registerBlockEntityType("short_catenary_node", MSDBlockEntityTypes.BLOCK_SHORT_CATENARY_NODE_ENTITY);
        MSDRegistry.registerBlockEntityType("short_catenary_node_style_2", MSDBlockEntityTypes.BLOCK_SHORT_CATENARY_NODE_STYLE_2_ENTITY);
        MSDRegistry.registerBlockEntityType("electric_node", MSDBlockEntityTypes.BLOCK_ELECTRIC_NODE_ENTITY);

        MSDRegistry.registerNetworkPacket(PACKET_YAMANOTE_SIGN_TYPES);
        MSDRegistry.registerNetworkPacket(PACKET_CUSTOM_TEXT_SIGN_UPDATE);
        MSDRegistry.registerNetworkPacket(PACKET_BLOCK_NODE_POS_UPDATE);

        MSDRegistry.registerNetworkPacket(MSDPacket.PACKET_OPEN_YAMANOTE_RAILWAY_SIGN_SCREEN);
        MSDRegistry.registerNetworkPacket(MSDPacket.PACKET_WRITE_CATENARY);
        MSDRegistry.registerNetworkPacket(MSDPacket.PACKET_REMOVE_CATENARY_NODE);
        MSDRegistry.registerNetworkPacket(MSDPacket.PACKET_CREATE_CATENARY);
        MSDRegistry.registerNetworkPacket(MSDPacket.PACKET_REMOVE_CATENARY);
        MSDRegistry.registerNetworkPacket(MSDPacket.PACKET_WRITE_RIGID_CATENARY);
        MSDRegistry.registerNetworkPacket(MSDPacket.PACKET_REMOVE_RIGID_CATENARY_NODE);
        MSDRegistry.registerNetworkPacket(MSDPacket.PACKET_CREATE_RIGID_CATENARY);
        MSDRegistry.registerNetworkPacket(MSDPacket.PACKET_REMOVE_RIGID_CATENARY);
        MSDRegistry.registerNetworkPacket(MSDPacket.PACKET_OPEN_CUSTOM_TEXT_SIGN_CONFIG_SCREEN);
        MSDRegistry.registerNetworkPacket(MSDPacket.PACKET_OPEN_BLOCK_NODE_SCREEN);
        MSDRegistry.registerNetworkPacket(MSDPacket.PACKET_WRITE_TRANS_CATENARY);
        MSDRegistry.registerNetworkPacket(MSDPacket.PACKET_CREATE_TRANS_CATENARY);
        MSDRegistry.registerNetworkPacket(MSDPacket.PACKET_REMOVE_TRANS_CATENARY);
        MSDRegistry.registerNetworkPacket(MSDPacket.PACKET_REMOVE_TRANS_CATENARY_NODE);

        MSDRegistry.registerNetworkReceiver(PACKET_YAMANOTE_SIGN_TYPES, MSDPacketTrainDataGuiServer::receiveMSDSignIdsC2S);
        MSDRegistry.registerNetworkReceiver(PACKET_CUSTOM_TEXT_SIGN_UPDATE, MSDPacketTrainDataGuiServer::receiveCustomTextSignMessageC2S);
        MSDRegistry.registerNetworkReceiver(PACKET_BLOCK_NODE_POS_UPDATE, MSDPacketTrainDataGuiServer::receiveBlockNodeLocationC2S);

        MSDRegistry.registerTickEvent(minecraftServer -> minecraftServer.getAllLevels().forEach(serverLevel -> {
            final CatenaryData catenaryData = CatenaryData.getInstance(serverLevel);
            final RigidCatenaryData rigidCatenaryData = RigidCatenaryData.getInstance(serverLevel);
            final TransCatenaryData transCatenaryData = TransCatenaryData.getInstance(serverLevel);
            if (catenaryData != null) {
                catenaryData.simulateCatenaries();
            }
            if (rigidCatenaryData != null) {
                rigidCatenaryData.simulateRigidCatenaries();
            }
            if (transCatenaryData != null) {
                transCatenaryData.simulateTransCatenaries();
            }
        }));
        MSDRegistry.registerPlayerJoinEvent(player -> {
            final CatenaryData catenaryData = CatenaryData.getInstance(player.level());
            final RigidCatenaryData rigidCatenaryData = RigidCatenaryData.getInstance(player.level());
        });
        MSDRegistry.registerPlayerQuitEvent(player -> {
            final CatenaryData catenaryData = CatenaryData.getInstance(player.level());
            final RigidCatenaryData rigidCatenaryData = RigidCatenaryData.getInstance(player.level());
            final TransCatenaryData transCatenaryData = TransCatenaryData.getInstance(player.level());
            if (catenaryData != null) {
                catenaryData.disconnectPlayer(player);
            }
            if (rigidCatenaryData != null) {
                rigidCatenaryData.disconnectPlayer(player);
            }
            if(transCatenaryData != null) {
                transCatenaryData.disconnectPlayer(player);
            }
        });
        MTRAddonRegistry.registerAddon(ADDON);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}