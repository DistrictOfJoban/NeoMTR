package com.lx862.jcm.mixin.modded.mtr;

import mtr.data.TicketSystem;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(value = TicketSystem.class, remap = false)
public class MixinTicketSystem {
    private static final int BASE_FARE = 2;
    private static final int ZONE_FARE = 1;

    @Inject(method = "onExit", at = @At("HEAD"))
    private static void a() {

    }

//    @Inject(method = "onExit", at = @At("HEAD"))
//    private static void onExit(Station station, Player player, boolean remindIfNoRecord, CallbackInfoReturnable<Boolean> cir, int entryZone1, int entryZone2, int entryZone3, boolean entered, long fare, long finalFare) {
//        long finalDeductedAmount = -finalFare;
//
//        if(entered && FareSaverBlock.discountList.containsKey(player.getUUID())) {
//            long subsidyAmount = Math.min(finalFare, FareSaverBlock.discountList.get(player.getUUID()));
//            incrementPlayerScore(world, player, "mtr_balance", "Balance", (int) subsidyAmount);
//            finalDeductedAmount += subsidyAmount;
//
//            if (subsidyAmount < 0 && finalFare > 0) {
//                player.displayClientMessage(TextUtil.translatable(TextCategory.HUD, "faresaver.saved_sarcasm", -subsidyAmount), false);
//            } else if (subsidyAmount > 0) {
//                player.displayClientMessage(TextUtil.translatable(TextCategory.HUD, "faresaver.saved", subsidyAmount), false);
//            }
//
//            FareSaverBlock.discountList.remove(player.getUUID());
//        }
//
//        TransactionLog.writeLog(player, new TransactionEntry(station.name, finalDeductedAmount, System.currentTimeMillis()));
//    }
//
//    @Inject(method = "addBalance", at = @At("TAIL"))
//    private static void addBalance(Level world, Player player, int amount, CallbackInfo ci) {
//        if (!world.isClientSide()) {
//            TransactionLog.writeLog(player, new TransactionEntry("Add Value", amount, System.currentTimeMillis()));
//        }
//    }

    private static int decodeZone(int zone) {
        return zone > 0 ? zone - 1 : zone;
    }

    private static boolean isConcessionary(Player player) {
        return player.isCreative();
    }
}