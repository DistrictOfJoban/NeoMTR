//package com.lx862.jcm.mod.network.block;
//
//import com.lx862.jcm.mod.block.base.JCMBlock;
//import com.lx862.jcm.mod.block.entity.ButterflyLightBlockEntity;
//import com.lx862.jcm.mod.util.BlockUtil;
//import mtr.mapping.holder.*;
//import mtr.mapping.registry.PacketHandler;
//import mtr.mapping.tool.PacketBufferReceiver;
//import mtr.mapping.tool.PacketBufferSender;
//
//public class ButterflyLightUpdatePacket extends PacketHandler {
//    private final BlockPos blockPos;
//    private final int startBlinkingSeconds;
//
//    public ButterflyLightUpdatePacket(PacketBufferReceiver packetBufferReceiver) {
//        this.blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
//        this.startBlinkingSeconds = packetBufferReceiver.readInt();
//    }
//
//    public ButterflyLightUpdatePacket(BlockPos blockPos, int startBlinkingSeconds) {
//        this.blockPos = blockPos;
//        this.startBlinkingSeconds = startBlinkingSeconds;
//    }
//
//    @Override
//    public void runServer(MinecraftServer minecraftServer, ServerPlayer serverPlayer) {
//        Level world = serverPlayer.getEntityWorld();
//        BlockState state = BlockUtil.getBlockState(world, blockPos);
//        if(state == null || !(state.getBlock().data instanceof JCMBlock)) return;
//
//        ((JCMBlock)state.getBlock()).loopStructure(state, world, blockPos, (bs, be) -> {
//            if(be.data instanceof ButterflyLightBlockEntity) {
//                ((ButterflyLightBlockEntity)be).setData(startBlinkingSeconds);
//            }
//        });
//    }
//
//    @Override
//    public void write(PacketBufferSender packetBufferSender) {
//        packetBufferSender.writeLong(blockPos.asLong());
//        packetBufferSender.writeInt(startBlinkingSeconds);
//    }
//}
