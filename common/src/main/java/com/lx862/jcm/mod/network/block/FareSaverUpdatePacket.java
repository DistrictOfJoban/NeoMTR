//package com.lx862.jcm.mod.network.block;
//
//import com.lx862.jcm.mod.block.base.JCMBlock;
//import com.lx862.jcm.mod.block.entity.FareSaverBlockEntity;
//import com.lx862.jcm.mod.util.BlockUtil;
//import mtr.mapping.holder.*;
//import mtr.mapping.registry.PacketHandler;
//import mtr.mapping.tool.PacketBufferReceiver;
//import mtr.mapping.tool.PacketBufferSender;
//
//public class FareSaverUpdatePacket extends PacketHandler {
//    private final BlockPos blockPos;
//    private final String prefix;
//    private final int discount;
//
//    public FareSaverUpdatePacket(PacketBufferReceiver packetBufferReceiver) {
//        this.blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
//        this.prefix = packetBufferReceiver.readString();
//        this.discount = packetBufferReceiver.readInt();
//    }
//
//    public FareSaverUpdatePacket(BlockPos blockPos, String prefix, int discount) {
//        this.blockPos = blockPos;
//        this.prefix = prefix;
//        this.discount = discount;
//    }
//
//    @Override
//    public void write(PacketBufferSender packetBufferSender) {
//        packetBufferSender.writeLong(blockPos.asLong());
//        packetBufferSender.writeString(prefix);
//        packetBufferSender.writeInt(discount);
//    }
//
//    @Override
//    public void runServer(MinecraftServer minecraftServer, ServerPlayer serverPlayer) {
//        Level world = serverPlayer.getEntityWorld();
//        BlockState state = BlockUtil.getBlockState(world, blockPos);
//        if(state == null || !(state.getBlock().data instanceof JCMBlock)) return;
//
//        ((JCMBlock)state.getBlock()).loopStructure(state, world, blockPos, (bs, be) -> {
//            if(be.data instanceof FareSaverBlockEntity) {
//                ((FareSaverBlockEntity)be).setData(prefix, discount);
//            }
//        });
//    }
//}
