package top.mcmtr.mod.blocks;

import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import top.mcmtr.mod.MSDBlockEntityTypes;
import top.mcmtr.mod.data.CatenaryData;
import top.mcmtr.mod.packet.MSDPacketTrainDataGuiServer;

public class BlockShortCatenaryNodeStyle2 extends BlockNodeBase {
    public BlockShortCatenaryNodeStyle2(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide) {
            final CatenaryData catenaryData = CatenaryData.getInstance(world);
            if (catenaryData != null) {
                catenaryData.removeCatenaryNode(pos);
                MSDPacketTrainDataGuiServer.removeCatenaryNodeS2C(world, pos);
            }
        }
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockShortCatenaryNodeStyle2Entity(blockPos, blockState);
    }

    public static class BlockShortCatenaryNodeStyle2Entity extends BlockNodeBaseEntity {
        public BlockShortCatenaryNodeStyle2Entity(BlockPos pos, BlockState state) {
            super(MSDBlockEntityTypes.BLOCK_SHORT_CATENARY_NODE_STYLE_2_ENTITY.get(), pos, state);
        }
    }
}