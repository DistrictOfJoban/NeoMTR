package com.lx862.jcm.mod.block;

import com.lx862.jcm.mod.block.base.Horizontal2MirroredBlock;
import com.lx862.jcm.mod.block.entity.PIDSBlockEntity;
import com.lx862.jcm.mod.network.gui.PIDSGUIPacket;
import com.lx862.jcm.mod.registry.Networking;
import mtr.block.IBlock;
import mtr.mappings.EntityBlockMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class JCMPIDSBlock extends Horizontal2MirroredBlock implements EntityBlockMapper {
    public JCMPIDSBlock(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        return IBlock.checkHoldingBrush(world, player, () -> {
            PIDSBlockEntity be = (PIDSBlockEntity) world.getBlockEntity(pos);
            Networking.sendPacketToClient(player, new PIDSGUIPacket(pos, be.getCustomMessages(), be.getRowHidden(), be.platformNumberHidden(), be.getPresetId()));
        });
    }
}
