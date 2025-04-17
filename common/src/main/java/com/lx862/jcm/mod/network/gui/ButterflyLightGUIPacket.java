package com.lx862.jcm.mod.network.gui;

import mtr.mapping.holder.BlockPos;
import mtr.mapping.registry.PacketHandler;
import mtr.mapping.tool.PacketBufferReceiver;
import mtr.mapping.tool.PacketBufferSender;

public class ButterflyLightGUIPacket extends PacketHandler {
    private final BlockPos blockPos;
    private final int secondsToBlink;

    public ButterflyLightGUIPacket(PacketBufferReceiver packetBufferReceiver) {
        this.blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
        this.secondsToBlink = packetBufferReceiver.readInt();
    }

    public ButterflyLightGUIPacket(BlockPos blockPos, int secondsToBlink) {
        this.blockPos = blockPos;
        this.secondsToBlink = secondsToBlink;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeLong(blockPos.asLong());
        packetBufferSender.writeInt(secondsToBlink);
    }

    @Override
    public void runClient() {
        ClientHelper.openButterflyLightScreen(blockPos, secondsToBlink);
    }
}
