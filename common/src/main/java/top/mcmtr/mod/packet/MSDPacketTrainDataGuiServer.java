package top.mcmtr.mod.packet;

import io.netty.buffer.Unpooled;
import mtr.block.BlockRouteSignBase;
import mtr.data.SerializedDataBase;
import mtr.mappings.BlockEntityMapper;
import mtr.packet.PacketTrainDataBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import top.mcmtr.loader.MSDRegistry;
import top.mcmtr.mod.blocks.BlockCustomTextSignBase;
import top.mcmtr.mod.blocks.BlockNodeBase;
import top.mcmtr.mod.blocks.BlockYamanoteRailwaySign;
import top.mcmtr.mod.data.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static top.mcmtr.mod.packet.MSDPacket.*;

public class MSDPacketTrainDataGuiServer extends PacketTrainDataBase {
    public static void openYamanoteRailwaySignScreenS2C(ServerPlayer player, BlockPos signPos) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(signPos);
        MSDRegistry.sendToPlayer(player, PACKET_OPEN_YAMANOTE_RAILWAY_SIGN_SCREEN, packet);
    }

    public static void receiveMSDSignIdsC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
        final BlockPos signPos = packet.readBlockPos();
        final int selectedIdsLength = packet.readInt();
        final Set<Long> selectedIds = new HashSet<>();
        for (int i = 0; i < selectedIdsLength; i++) {
            selectedIds.add(packet.readLong());
        }
        final int signLength = packet.readInt();
        final String[] signIds = new String[signLength];
        for (int i = 0; i < signLength; i++) {
            final String signId = packet.readUtf(SerializedDataBase.PACKET_STRING_READ_LENGTH);
            signIds[i] = signId.isEmpty() ? null : signId;
        }
        minecraftServer.execute(() -> {
            final BlockEntity entity = player.level().getBlockEntity(signPos);
            if (entity instanceof BlockYamanoteRailwaySign.TileEntityRailwaySign) {
                setTileEntityDataAndWriteUpdate(player, entity2 -> entity2.setData(selectedIds, signIds), (BlockYamanoteRailwaySign.TileEntityRailwaySign) entity);
            } else if (entity instanceof BlockRouteSignBase.TileEntityRouteSignBase) {
                final long platformId = selectedIds.isEmpty() ? 0 : (long) selectedIds.toArray()[0];
                final BlockEntity entityAbove = player.level().getBlockEntity(signPos.above());
                if (entityAbove instanceof BlockRouteSignBase.TileEntityRouteSignBase) {
                    setTileEntityDataAndWriteUpdate(player, entity2 -> entity2.setPlatformId(platformId), ((BlockRouteSignBase.TileEntityRouteSignBase) entityAbove), (BlockRouteSignBase.TileEntityRouteSignBase) entity);
                } else {
                    setTileEntityDataAndWriteUpdate(player, entity2 -> entity2.setPlatformId(platformId), (BlockRouteSignBase.TileEntityRouteSignBase) entity);
                }
            }
        });
    }

    @SafeVarargs
    private static <T extends BlockEntityMapper> void setTileEntityDataAndWriteUpdate(ServerPlayer player, Consumer<T> setData, T... entities) {
        final CatenaryData catenaryData = CatenaryData.getInstance(player.level());
        if (catenaryData != null && entities.length > 0) {
            final CompoundTag compoundTagOld = new CompoundTag();
            entities[0].writeCompoundTag(compoundTagOld);
            BlockPos blockPos = null;
            long posLong = 0;
            for (final T entity : entities) {
                setData.accept(entity);
                final BlockPos entityPos = entity.getBlockPos();
                if (blockPos == null || entityPos.asLong() > posLong) {
                    blockPos = entityPos;
                    posLong = entityPos.asLong();
                }
            }
            final CompoundTag compoundTagNew = new CompoundTag();
            entities[0].writeCompoundTag(compoundTagNew);
        }
    }

    public static void createCatenaryS2C(Level world, BlockPos pos1, BlockPos pos2, Catenary catenary1, Catenary catenary2) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(pos1);
        packet.writeBlockPos(pos2);
        catenary1.writePacket(packet);
        catenary2.writePacket(packet);
        world.players().forEach(worldPlayer -> MSDRegistry.sendToPlayer((ServerPlayer) worldPlayer, PACKET_CREATE_CATENARY, packet));
    }

    public static void removeCatenaryNodeS2C(Level world, BlockPos pos) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(pos);
        world.players().forEach(worldPlayer -> MSDRegistry.sendToPlayer((ServerPlayer) worldPlayer, PACKET_REMOVE_CATENARY_NODE, packet));
    }

    public static void removeCatenaryConnectionS2C(Level world, BlockPos pos1, BlockPos pos2) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(pos1);
        packet.writeBlockPos(pos2);
        world.players().forEach(worldPlayer -> MSDRegistry.sendToPlayer((ServerPlayer) worldPlayer, PACKET_REMOVE_CATENARY, packet));
    }

    public static void createRigidCatenaryS2C(Level world, BlockPos pos1, BlockPos pos2, RigidCatenary catenary1, RigidCatenary catenary2) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(pos1);
        packet.writeBlockPos(pos2);
        catenary1.writePacket(packet);
        catenary2.writePacket(packet);
        world.players().forEach(worldPlayer -> MSDRegistry.sendToPlayer((ServerPlayer) worldPlayer, PACKET_CREATE_RIGID_CATENARY, packet));
    }

    public static void removeRigidCatenaryNodeS2C(Level world, BlockPos pos) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(pos);
        world.players().forEach(worldPlayer -> MSDRegistry.sendToPlayer((ServerPlayer) worldPlayer, PACKET_REMOVE_RIGID_CATENARY_NODE, packet));
    }

    public static void removeRigidCatenaryConnectionS2C(Level world, BlockPos pos1, BlockPos pos2) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(pos1);
        packet.writeBlockPos(pos2);
        world.players().forEach(worldPlayer -> MSDRegistry.sendToPlayer((ServerPlayer) worldPlayer, PACKET_REMOVE_RIGID_CATENARY, packet));
    }

    public static void openCustomTextSignConfigScreenS2C(ServerPlayer player, BlockPos pos1, int maxArrivals) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(pos1);
        packet.writeInt(maxArrivals);
        MSDRegistry.sendToPlayer(player, PACKET_OPEN_CUSTOM_TEXT_SIGN_CONFIG_SCREEN, packet);
    }

    public static void receiveCustomTextSignMessageC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
        final BlockPos pos = packet.readBlockPos();
        final int maxArrivals = packet.readInt();
        final String[] messages = new String[maxArrivals];
        for (int i = 0; i < maxArrivals; i++) {
            messages[i] = packet.readUtf(SerializedDataBase.PACKET_STRING_READ_LENGTH);
        }
        minecraftServer.execute(() -> {
            final List<BlockCustomTextSignBase.TileEntityBlockCustomTextSignBase> entities = new ArrayList<>();
            final BlockEntity entity1 = player.level().getBlockEntity(pos);
            if (entity1 instanceof BlockCustomTextSignBase.TileEntityBlockCustomTextSignBase) {
                entities.add((BlockCustomTextSignBase.TileEntityBlockCustomTextSignBase) entity1);
            }
            setTileEntityDataAndWriteUpdate(player, entity -> entity.setData(messages), entities.toArray(new BlockCustomTextSignBase.TileEntityBlockCustomTextSignBase[0]));
        });
    }

    public static void openBlockNodeScreenC2S(ServerPlayer player, BlockLocation location, BlockPos pos) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(pos);
        packet.writeDouble(location.getX());
        packet.writeDouble(location.getY());
        packet.writeDouble(location.getZ());
        MSDRegistry.sendToPlayer(player, PACKET_OPEN_BLOCK_NODE_SCREEN, packet);
    }

    public static void receiveBlockNodeLocationC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
        final BlockPos pos = packet.readBlockPos();
        final double locationX = packet.readDouble();
        final double locationY = packet.readDouble();
        final double locationZ = packet.readDouble();
        final BlockLocation location = new BlockLocation(locationX, locationY, locationZ);
        minecraftServer.execute(() -> {
            final List<BlockNodeBase.BlockNodeBaseEntity> entities = new ArrayList<>();
            final BlockEntity entity1 = player.level().getBlockEntity(pos);
            if (entity1 instanceof BlockNodeBase.BlockNodeBaseEntity) {
                entities.add((BlockNodeBase.BlockNodeBaseEntity) entity1);
            }
            setTileEntityDataAndWriteUpdate(player, entity -> entity.setData(location), entities.toArray(new BlockNodeBase.BlockNodeBaseEntity[0]));
        });
    }

    public static void createTransCatenaryS2C(Level world, BlockPos pos1, BlockPos pos2, TransCatenary catenary1, TransCatenary catenary2) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(pos1);
        packet.writeBlockPos(pos2);
        catenary1.writePacket(packet);
        catenary2.writePacket(packet);
        world.players().forEach(worldPlayer -> MSDRegistry.sendToPlayer((ServerPlayer) worldPlayer, PACKET_CREATE_TRANS_CATENARY, packet));
    }

    public static void removeTransCatenaryNodeS2C(Level world, BlockPos pos) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(pos);
        world.players().forEach(worldPlayer -> MSDRegistry.sendToPlayer((ServerPlayer) worldPlayer, PACKET_REMOVE_TRANS_CATENARY_NODE, packet));
    }

    public static void removeTransCatenaryConnectionS2C(Level world, BlockPos pos1, BlockPos pos2) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(pos1);
        packet.writeBlockPos(pos2);
        world.players().forEach(worldPlayer -> MSDRegistry.sendToPlayer((ServerPlayer) worldPlayer, PACKET_REMOVE_TRANS_CATENARY, packet));
    }
}