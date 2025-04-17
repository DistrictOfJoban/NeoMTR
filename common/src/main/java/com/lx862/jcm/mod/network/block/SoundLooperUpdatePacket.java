package com.lx862.jcm.mod.network.block;

import com.lx862.jcm.mod.block.base.JCMBlock;
import com.lx862.jcm.mod.block.entity.SoundLooperBlockEntity;
import com.lx862.jcm.mod.util.BlockUtil;
import mtr.mapping.holder.*;
import mtr.mapping.registry.PacketHandler;
import mtr.mapping.tool.PacketBufferReceiver;
import mtr.mapping.tool.PacketBufferSender;

public class SoundLooperUpdatePacket extends PacketHandler {
    private final BlockPos blockPos;
    private final BlockPos corner1;
    private final BlockPos corner2;
    private final String soundId;
    private final int soundCategory;
    private final int interval;
    private final float soundVolume;
    private final boolean needRedstone;
    private final boolean limitRange;

    public SoundLooperUpdatePacket(PacketBufferReceiver packetBufferReceiver) {
        this.blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
        this.corner1 = BlockPos.fromLong(packetBufferReceiver.readLong());
        this.corner2 = BlockPos.fromLong(packetBufferReceiver.readLong());
        this.soundId = packetBufferReceiver.readString();
        this.soundCategory = packetBufferReceiver.readInt();
        this.interval = packetBufferReceiver.readInt();
        this.soundVolume = packetBufferReceiver.readFloat();
        this.needRedstone = packetBufferReceiver.readBoolean();
        this.limitRange = packetBufferReceiver.readBoolean();
    }

    public SoundLooperUpdatePacket(BlockPos blockPos, BlockPos corner1, BlockPos corner2, String soundId, int soundCategory, int interval, float soundVolume, boolean needRedstone, boolean limitRange) {
        this.blockPos = blockPos;
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.soundId = soundId;
        this.soundCategory = soundCategory;
        this.interval = interval;
        this.soundVolume = soundVolume;
        this.needRedstone = needRedstone;
        this.limitRange = limitRange;
    }

    @Override
    public void runServer(MinecraftServer minecraftServer, ServerPlayer serverPlayer) {
        Level world = serverPlayer.getEntityWorld();
        BlockState state = BlockUtil.getBlockState(world, blockPos);
        if(state == null || !(state.getBlock().data instanceof JCMBlock)) return;

        ((JCMBlock)state.getBlock()).loopStructure(state, world, blockPos, (bs, be) -> {
            if(be.data instanceof SoundLooperBlockEntity) {
                ((SoundLooperBlockEntity)be).setData(soundId, soundCategory, interval, soundVolume, needRedstone, limitRange, corner1, corner2);
            }
        });
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeLong(blockPos.asLong());
        packetBufferSender.writeLong(corner1.asLong());
        packetBufferSender.writeLong(corner2.asLong());
        packetBufferSender.writeString(soundId);
        packetBufferSender.writeInt(soundCategory);
        packetBufferSender.writeInt(interval);
        packetBufferSender.writeFloat(soundVolume);
        packetBufferSender.writeBoolean(needRedstone);
        packetBufferSender.writeBoolean(limitRange);
    }
}
