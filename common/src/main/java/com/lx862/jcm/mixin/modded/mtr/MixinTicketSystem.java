package com.lx862.jcm.mixin.modded.mtr;

import com.lx862.jcm.mod.block.FareSaverBlock;
import com.lx862.jcm.mod.data.TransactionLog;
import com.lx862.jcm.mod.data.TransactionEntry;
import com.lx862.jcm.mod.util.TextCategory;
import com.lx862.jcm.mod.util.TextUtil;
import mtr.data.Station;
import mtr.data.TicketSystem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = TicketSystem.class, remap = false)
public abstract class MixinTicketSystem {
    @Shadow
    private static void incrementPlayerScore(Level world, Player player, String objective, String title, int value) {
    }

    @Inject(method = "onExit", at = @At(value = "INVOKE", target = "Lorg/mtr/mod/data/TicketSystem;incrementPlayerScore(Lorg/mtr/mapping/holder/World;Lorg/mtr/mapping/holder/Player;Ljava/lang/String;Ljava/lang/String;I)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void onExit(Level world, Station station, Player player, boolean remindIfNoRecord, CallbackInfoReturnable<Boolean> cir, int entryZone1, int entryZone2, int entryZone3, boolean entered, long fare, long finalFare) {
        long finalDeductedAmount = -finalFare;

        if(entered && FareSaverBlock.discountList.containsKey(player.getUUID())) {
            long subsidyAmount = Math.min(finalFare, FareSaverBlock.discountList.get(player.getUUID()));
            incrementPlayerScore(world, player, "mtr_balance", "Balance", (int) subsidyAmount);
            finalDeductedAmount += subsidyAmount;

            if (subsidyAmount < 0 && finalFare > 0) {
                player.displayClientMessage(TextUtil.translatable(TextCategory.HUD, "faresaver.saved_sarcasm", -subsidyAmount), false);
            } else if (subsidyAmount > 0) {
                player.displayClientMessage(TextUtil.translatable(TextCategory.HUD, "faresaver.saved", subsidyAmount), false);
            }

            FareSaverBlock.discountList.remove(player.getUUID());
        }

        TransactionLog.writeLog(player, new TransactionEntry(station.name, finalDeductedAmount, System.currentTimeMillis()));
    }

    @Inject(method = "addBalance", at = @At("TAIL"))
    private static void addBalance(Level world, Player player, int amount, CallbackInfo ci) {
        if (!world.isClientSide()) {
            TransactionLog.writeLog(player, new TransactionEntry("Add Value", amount, System.currentTimeMillis()));
        }
    }
}