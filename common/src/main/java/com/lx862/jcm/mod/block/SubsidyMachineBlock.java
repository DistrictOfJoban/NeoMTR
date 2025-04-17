package com.lx862.jcm.mod.block;

import com.lx862.jcm.mod.Constants;
import com.lx862.jcm.mod.block.base.WallAttachedBlock;
import com.lx862.jcm.mod.block.entity.SubsidyMachineBlockEntity;
import com.lx862.jcm.mod.data.JCMServerStats;
import com.lx862.jcm.mod.network.gui.SubsidyMachineGUIPacket;
import com.lx862.jcm.mod.registry.Networking;
import com.lx862.jcm.mod.util.TextCategory;
import com.lx862.jcm.mod.util.TextUtil;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import mtr.block.IBlock;
import mtr.data.TicketSystem;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.UUID;

public class SubsidyMachineBlock extends WallAttachedBlock implements EntityBlockMapper {
    private static final Object2LongOpenHashMap<UUID> cooldownMap = new Object2LongOpenHashMap<>();
    public SubsidyMachineBlock(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        return IBlock.getVoxelShapeByDirection(4, 1.25, 0, 12, 14.75, 2, IBlock.getStatePropertySafe(state, FACING));
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        BlockEntity be = world.getBlockEntity(pos);
        if(be == null) return InteractionResult.FAIL;

        SubsidyMachineBlockEntity sbe = (SubsidyMachineBlockEntity)be;

        return IBlock.checkHoldingBrush(world, player, () -> {
            Networking.sendPacketToClient(player, new SubsidyMachineGUIPacket(pos, sbe.getSubsidyAmount(), sbe.getCooldown()));
        }, () -> {
            if(cooldownExpired(player, sbe.getCooldown())) {
                updateCooldown(player);
                int finalBalance = addMTRBalanceToPlayer(world, player, sbe.getSubsidyAmount());
                player.displayClientMessage(TextUtil.translatable(TextCategory.HUD, "subsidy_machine.success", sbe.getSubsidyAmount(), finalBalance), true);
            } else {
                int remainingSec = Math.round(sbe.getCooldown() - getCooldown(player));
                player.displayClientMessage(TextUtil.translatable(TextCategory.HUD, "subsidy_machine.fail", remainingSec).withStyle(ChatFormatting.RED), true);
            }
        });
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SubsidyMachineBlockEntity(blockPos, blockState);
    }

    private static int addMTRBalanceToPlayer(Level world, Player player, int amount) {
        return TicketSystem.getPlayerScore(world, player, TicketSystem.BALANCE_OBJECTIVE).add(amount);
    }

    private static boolean cooldownExpired(Player player, int cooldown) {
        return getCooldown(player) >= cooldown;
    }

    private static long getCooldown(Player player) {
        return (JCMServerStats.getGameTick() - cooldownMap.getOrDefault(player.getUUID(), 0)) / Constants.MC_TICK_PER_SECOND;
    }

    private static void updateCooldown(Player player) {
        cooldownMap.put(player.getUUID(), JCMServerStats.getGameTick());
    }
}