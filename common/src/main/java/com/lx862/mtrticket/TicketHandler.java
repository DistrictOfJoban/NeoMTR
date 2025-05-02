package com.lx862.mtrticket;

import com.lx862.mtrticket.config.TicketConfig;
import com.lx862.mtrticket.data.TicketBarriers;
import com.lx862.mtrticket.data.Tickets;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mtr.MTR;
import mtr.data.RailwayData;
import mtr.data.Station;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.scores.ScoreAccess;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class TicketHandler {
    public static HashMap<String, Tickets> ticketList = new HashMap<>();
    public static ArrayList<TicketBarriers> barrierList = new ArrayList<>();

    public static InteractionResult tap(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
        if(world.isClientSide) return InteractionResult.PASS;

        ScoreAccess entryZone = Util.getScore("mtr_entry_zone", player.getGameProfile().getName(), world);
        String blockId = BuiltInRegistries.BLOCK.getKey(world.getBlockState(hitResult.getBlockPos()).getBlock()).toString();
        ItemStack item = player.getItemInHand(hand);

        if (item.getItem() == Items.ARROW) {
            for(TicketBarriers barriers : barrierList) {
                if(!blockId.equals(barriers.blockId())) {
                    continue;
                }

                if (barriers.isEntrance()) {
                    onEnter(item, (ServerPlayer) player, hitResult.getBlockPos(), world);
                } else {
                    onExit(item, (ServerPlayer) player, hitResult.getBlockPos(), world, entryZone);
                }
            }
        }
        return InteractionResult.PASS;
    }

    private static void onEnter(ItemStack itemHolding, ServerPlayer player, BlockPos pos, Level world) {
        CustomData customData = itemHolding.get(DataComponents.CUSTOM_DATA);
        if(customData == null) {
            Util.sendPlaySoundIdS2CPacket(player.level(), player, MTR.id("ticket_processor_fail"), SoundSource.BLOCKS, player.position(), 1.0f, 1.0f);
            player.sendSystemMessage(Component.literal("This ticket is invalid. Please buy a new ticket from customer service centre.").withStyle(ChatFormatting.RED), true);
            return;
        }

        CompoundTag itemTag = customData.copyTag();
        if(itemTag.getBoolean("entered")) {
            Util.sendPlaySoundIdS2CPacket(player.level(), player, MTR.id("ticket_processor_fail"), SoundSource.BLOCKS, player.position(), 1.0f, 1.0f);
            player.sendSystemMessage(Component.literal("You have already entered the gate. Please contact the customer service centre.").withStyle(ChatFormatting.RED), true);
            return;
        }

        if (!itemTag.getBoolean("SingleRide") && itemTag.getLong("expireTime") < System.currentTimeMillis()) {
            Util.sendPlaySoundIdS2CPacket(player.level(), player, MTR.id("ticket_processor_fail"), SoundSource.BLOCKS, player.position(), 1.0f, 1.0f);
            player.sendSystemMessage(Component.literal("Ticket is expired or invalid, please buy a new one using the /buyTicket command.").withStyle(ChatFormatting.RED), false);
            return;
        }

        if (itemTag.getBoolean("exitOnly")) {
            Util.sendPlaySoundIdS2CPacket(player.level(), player, MTR.id("ticket_processor_fail"), SoundSource.BLOCKS, player.position(), 1.0f, 1.0f);
            player.sendSystemMessage(Component.literal("This ticket is for exit only.").withStyle(ChatFormatting.RED), false);
            return;
        }

        if (!itemTag.getString("filterZone").isEmpty()) {
            boolean accept = false;
            RailwayData data = RailwayData.getInstance(world);
            for(String stnZone : itemTag.getString("filterZone").split(",")) {
                int zone = Integer.parseInt(stnZone);

                for(Station stn : data.dataCache.stationIdMap.values()) {
                    int playerX = pos.getX();
                    int playerZ = pos.getZ();

                    if (stn.zone == zone && stn.inArea(playerX, playerZ)) {
                        accept = true;
                        break;
                    }
                }
            }

            if(!accept) {
                Util.sendPlaySoundIdS2CPacket(player.level(), player, MTR.id("ticket_processor_fail"), SoundSource.BLOCKS, player.position(), 1.0f, 1.0f);
                player.sendSystemMessage(Component.literal("This ticket is not applicable to this station's fare zone.").withStyle(ChatFormatting.RED), false);
                return;
            }
        }

        itemTag.putBoolean("entered", true);
        itemHolding.set(DataComponents.CUSTOM_DATA, CustomData.of(itemTag));
        BlockState state = world.getBlockState(pos);
        BlockState blockState = setProperties(state, TicketConfig.getEntranceState());
        world.setBlockAndUpdate(pos, blockState);
        Util.sendPlaySoundIdS2CPacket(player.level(), player, MTR.id("ticket_barrier_concessionary"), SoundSource.BLOCKS, player.position(), 1.0f, 1.0f);
        player.sendSystemMessage(Component.literal("Ticket retrieved, please proceed.").withStyle(ChatFormatting.GREEN), true);
    }

    private static void onExit(ItemStack itemHolding, ServerPlayer player, BlockPos pos, Level world, ScoreAccess entryZone) {
        CustomData customData = itemHolding.get(DataComponents.CUSTOM_DATA);
        if(customData == null) {
            Util.sendPlaySoundIdS2CPacket(player.level(), player, MTR.id("ticket_processor_fail"), SoundSource.BLOCKS, player.position(), 1.0f, 1.0f);
            player.sendSystemMessage(Component.literal("This ticket is invalid. Please buy a new ticket from customer service centre.").withStyle(ChatFormatting.RED), true);
            return;
        }

        CompoundTag itemTag = customData.copyTag();

        if(!itemTag.getBoolean("entered") && !itemTag.getBoolean("exitOnly")) {
            Util.sendPlaySoundIdS2CPacket(player.level(), player, MTR.id("ticket_processor_fail"), SoundSource.BLOCKS, player.position(), 1.0f, 1.0f);
            player.sendSystemMessage(Component.literal("No entry record found on the ticket. Please contact the customer service centre.").withStyle(ChatFormatting.RED), true);
            return;
        }

        itemTag.putBoolean("entered", false);

        if ((!itemTag.getBoolean("SingleRide") || itemTag.getLong("expireTime") < System.currentTimeMillis())) {
            entryZone.set(0);
            itemHolding.shrink(1);
        }

        BlockState state = world.getBlockState(pos);
        BlockState blockState = setProperties(state, TicketConfig.getExitState());
        world.setBlockAndUpdate(pos, blockState);
        Util.sendPlaySoundIdS2CPacket(player.level(), player, MTR.id("ticket_barrier_concessionary"), SoundSource.BLOCKS, player.position(), 1.0f, 1.0f);
        player.sendSystemMessage(Component.literal("Ticket received, please proceed.").withStyle(ChatFormatting.GREEN), true);
    }

    public static void giveTicket(Player player, Tickets ticket) {
        ItemStack itm = new ItemStack(BuiltInRegistries.ITEM.get(TicketConfig.getBaseItem()));
        CompoundTag itemTag = new CompoundTag();
        CompoundTag itemLore;

        if (!ticket.singleRide()) {
            itemTag.putLong("expireTime", System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(ticket.expireDays(), TimeUnit.DAYS));
            String date = new SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis() + 10000L);
            try {
                itemLore = TagParser.parseTag(String.format("{Lore:['[{\"text\":\"Expires on %s \",\"italic\":false}]']}", date));
                itemTag.put("display", itemLore);
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
        }

        itemTag.putBoolean("SingleRide", ticket.singleRide());
        itemTag.putBoolean("exitOnly", ticket.exitOnly());
        itemTag.putBoolean("entered", false);
        itemTag.putString("filterZone", ticket.filteredZone());
        itemTag.putInt("CustomModelData", ticket.predicateID());

        itm.set(DataComponents.CUSTOM_DATA, CustomData.of(itemTag));
        itm.set(DataComponents.CUSTOM_NAME, Component.literal(ticket.name()).setStyle(Style.EMPTY.withItalic(false)).withStyle(ChatFormatting.AQUA));
        player.addItem(itm);
    }

    private static <P extends Comparable<P>> BlockState setState(BlockState state, Property<P> property, String value) {
        if (property.getValue(value).isPresent()) {
            return state.setValue(property, property.getValue(value).get());
        }
        return state;
    }

    private static BlockState setProperties(BlockState state, String stateValue) {
        String[] split = stateValue.split("=");
        if (split.length != 2) return state;

        String property = split[0].trim();
        String value = split[1].trim();

        /* Find the appropriate state properties */
        for (Property<?> p : state.getProperties()) {
            if (p.getName().equals(property)) {
                state = setState(state, p, value);
            }
        }
        return state;
    }
}
