package com.lx862.jcm.mod.block.behavior;

import com.lx862.jcm.mod.data.EnquiryScreenType;
import com.lx862.jcm.mod.data.TransactionEntry;
import com.lx862.jcm.mod.data.TransactionLog;
import com.lx862.jcm.mod.util.TextUtil;
import mtr.data.TicketSystem;
import mtr.registry.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

import static mtr.data.TicketSystem.BALANCE_OBJECTIVE;

public interface EnquiryMachineBehavior {
    default void enquiry(EnquiryScreenType type, BlockPos pos, Level world, Player player) {
        world.playSound(null, player.blockPosition(), SoundEvents.TICKET_PROCESSOR_ENTRY, SoundSource.BLOCKS, 1, 1);

        if(type == EnquiryScreenType.NONE) {
            int score = TicketSystem.getPlayerScore(world, player, BALANCE_OBJECTIVE).get();
            player.displayClientMessage(TextUtil.translatable("gui.mtr.balance", String.valueOf(score)), true);
        } else {
            List<TransactionEntry> entries = TransactionLog.readLog(player, player.getStringUUID());
            // TODO
//            Networking.sendPacketToClient(player, new EnquiryUpdateGUIPacket(type, pos, entries, TicketSystem.getPlayerScore(world, player, BALANCE_OBJECTIVE).get()));
        }
    }
}
