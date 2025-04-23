package cn.zbx1425.mtrsteamloco.network;

import cn.zbx1425.mtrsteamloco.Main;
import cn.zbx1425.mtrsteamloco.game.VirtualDriveClientData;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import mtr.loader.MTRRegistry;
import mtr.data.RailwayData;
import mtr.data.RailwayDataMountModule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Set;
import java.util.UUID;

public class PacketVirtualDrivingPlayers {

    public static final ResourceLocation PACKET_VIRTUAL_DRIVING_PLAYERS = Main.id("virtual_driving_players");

    public static void sendVirtualDrivingPlayersS2C(ServerPlayer player, Set<Player> playerInVirtualDrive) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeVarInt(playerInVirtualDrive.size());
        for (Player drivingPlayer : playerInVirtualDrive) {
            packet.writeUUID(drivingPlayer.getUUID());
        }
        MTRRegistry.sendToPlayer(player, PACKET_VIRTUAL_DRIVING_PLAYERS, packet);
    }

    public static void sendVirtualDrivingPlayersS2C(ServerPlayer player) {
        RailwayDataMountModule railwayDataMountModule = RailwayData.getInstance(player.level()).getModule(RailwayDataMountModule.NAME);
        sendVirtualDrivingPlayersS2C(player, railwayDataMountModule.playerInVirtualDrive);
    }

    public static class Client {

        public static void receiveVirtualDrivingPlayersS2C(FriendlyByteBuf packet) {
            int size = packet.readVarInt();
            Set<UUID> drivingPlayers = new ObjectArraySet<>();
            for (int i = 0; i < size; i++) {
                drivingPlayers.add(packet.readUUID());
            }
            VirtualDriveClientData.drivingPlayers = drivingPlayers;
        }
    }
}
