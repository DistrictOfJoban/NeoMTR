package cn.zbx1425.mtrsteamloco.network;

import cn.zbx1425.mtrsteamloco.Main;
import io.netty.buffer.Unpooled;
import mtr.RegistryClient;
import mtr.data.RailwayData;
import mtr.data.RailwayDataMountModule;
import mtr.data.Siding;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class PacketVirtualDrive {

    public static final ResourceLocation PACKET_VIRTUAL_DRIVE = Main.id("virtual_drive");

    public static void sendVirtualDriveC2S(boolean isDriving) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBoolean(isDriving);
        RegistryClient.sendToServer(PACKET_VIRTUAL_DRIVE, packet);
    }

    public static void receiveVirtualDriveC2S(MinecraftServer server, ServerPlayer player, FriendlyByteBuf packet) {
        final boolean isDriving = packet.readBoolean();
        server.execute(() -> {
            RailwayDataMountModule railwayDataMountModule = RailwayData.getInstance(player.level()).getModule(RailwayDataMountModule.NAME);
            if (isDriving) {
                railwayDataMountModule.updatePlayerInVirtualDrive(player, true);
                // Unmount the player from the train they're currently riding
                // Mounting cooldown is still applied by playerInVirtualDrive
                // And thus the player wouldn't get mounted onto another real train
                for (Siding siding : RailwayData.getInstance(player.level()).sidings) {
                    siding.unmountPlayer(player);
                }
            } else {
                railwayDataMountModule.updatePlayerInVirtualDrive(player, false);
            }
            for (ServerPlayer target : player.serverLevel().players()) {
                PacketVirtualDrivingPlayers.sendVirtualDrivingPlayersS2C(target);
            }
        });
    }
}
