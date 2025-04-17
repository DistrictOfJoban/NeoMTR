package com.lx862.jcm.mod.block;

import com.lx862.jcm.mod.block.base.Vertical3Block;
import com.lx862.jcm.mod.block.entity.FareSaverBlockEntity;
import com.lx862.jcm.mod.network.gui.FareSaverGUIPacket;
import com.lx862.jcm.mod.registry.Networking;
import com.lx862.jcm.mod.util.JCMUtil;
import com.lx862.jcm.mod.util.TextCategory;
import com.lx862.jcm.mod.util.TextUtil;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashMap;
import java.util.UUID;

public class FareSaverBlock extends Vertical3Block implements EntityBlockMapper {
    public static final HashMap<UUID, Integer> discountList = new HashMap<>();

    public FareSaverBlock(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        return IBlock.getVoxelShapeByDirection(3, 0, 6.5, 13, 16, 9.5, IBlock.getStatePropertySafe(state, FACING));
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if(world.isClientSide()) return InteractionResult.SUCCESS;

        UUID playerUuid = player.getUUID();
        FareSaverBlockEntity be = (FareSaverBlockEntity)world.getBlockEntity(pos);
        int discount = be.getDiscount();

        if (JCMUtil.playerHoldingBrush(player)) {
            Networking.sendPacketToClient(player, new FareSaverGUIPacket(pos, be.getPrefix(), discount));
            return InteractionResult.SUCCESS;
        }

        if(discountList.containsKey(playerUuid)) {
            player.displayClientMessage(TextUtil.translatable(TextCategory.HUD, "faresaver.fail", discountList.get(playerUuid)), true);
        } else {
            discountList.put(playerUuid, discount);

            if(discount > 0) {
                player.displayClientMessage(TextUtil.translatable(TextCategory.HUD, "faresaver.success", discount), true);
            } else {
                player.displayClientMessage(TextUtil.translatable(TextCategory.HUD, "faresaver.success.sarcasm", discount), true);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FareSaverBlockEntity(blockPos, blockState);
    }
}