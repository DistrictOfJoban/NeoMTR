package com.lx862.jcm.mod.network.gui;

import mtr.mapping.holder.BlockPos;
import mtr.mapping.registry.PacketHandler;
import mtr.mapping.tool.PacketBufferReceiver;
import mtr.mapping.tool.PacketBufferSender;

public class SubsidyMachineGUIPacket extends PacketHandler {
    private final BlockPos blockPos;
    private final int pricePerUse;
    private final int cooldown;

    public SubsidyMachineGUIPacket(PacketBufferReceiver packetBufferReceiver) {
        this.blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
        this.pricePerUse = packetBufferReceiver.readInt();
        this.cooldown = packetBufferReceiver.readInt();
    }

    public SubsidyMachineGUIPacket(BlockPos blockPos, int pricePerUse, int cooldown) {
        this.blockPos = blockPos;
        this.pricePerUse = pricePerUse;
        this.cooldown = cooldown;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeLong(blockPos.asLong());
        packetBufferSender.writeInt(pricePerUse);
        packetBufferSender.writeInt(cooldown);
    }

    @Override
    public void runClient() {
        ClientHelper.openSubsidyMachineGUIScreen(blockPos, pricePerUse, cooldown);
    }
}
