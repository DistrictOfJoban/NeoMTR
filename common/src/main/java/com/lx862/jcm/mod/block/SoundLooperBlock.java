package com.lx862.jcm.mod.block;

import com.lx862.jcm.mod.block.base.JCMBlock;
import com.lx862.jcm.mod.block.entity.SoundLooperBlockEntity;
import com.lx862.jcm.mod.network.gui.SoundLooperGUIPacket;
import com.lx862.jcm.mod.registry.Networking;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SoundLooperBlock extends JCMBlock implements EntityBlockMapper {
    public SoundLooperBlock(Properties settings) {
        super(settings);
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SoundLooperBlockEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        return IBlock.checkHoldingBrush(world, player, () -> {
            SoundLooperBlockEntity be = (SoundLooperBlockEntity) world.getBlockEntity(pos);
            Networking.sendPacketToClient(player, new SoundLooperGUIPacket(pos, be.getCorner1(), be.getCorner2(), be.getSoundId(), be.getSoundCategory(), be.getLoopInterval(), be.getSoundVolume(), be.needRedstone(), be.rangeLimited()));
        });
    }
}
