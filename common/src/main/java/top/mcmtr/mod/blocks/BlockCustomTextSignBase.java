package top.mcmtr.mod.blocks;

import mtr.block.IBlock;
import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.EntityBlockMapper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import top.mcmtr.mod.config.Config;
import top.mcmtr.mod.packet.MSDPacketTrainDataGuiServer;

public abstract class BlockCustomTextSignBase extends BlockChangeModelBase implements EntityBlockMapper {
    public BlockCustomTextSignBase(int count) {
        super(count, Properties.of().requiresCorrectToolForDrops().strength(2.0F).lightLevel((state) -> 5));
    }

    @Override
    public InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult ctx) {
        return IBlock.checkHoldingBrush(level, player, () -> {
            final BlockEntity entity1 = level.getBlockEntity(blockPos);
            if (entity1 instanceof TileEntityBlockCustomTextSignBase) {
                ((TileEntityBlockCustomTextSignBase) entity1).syncData();
                MSDPacketTrainDataGuiServer.openCustomTextSignConfigScreenS2C((ServerPlayer) player, blockPos, ((TileEntityBlockCustomTextSignBase) entity1).getMaxArrivals());
            }
        });
    }

    public abstract static class TileEntityBlockCustomTextSignBase extends BlockEntityClientSerializableMapper {
        private final String[] messages = new String[getMaxArrivals()];
        private static final String KEY_MESSAGE = "msd_custom_message";

        public TileEntityBlockCustomTextSignBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
            super(type, pos, state);
        }

        @Environment(EnvType.CLIENT)
        public double getViewDistance() {
            return Config.getCustomTextSignMaxViewDistance();
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            for (int i = 0; i < getMaxArrivals(); i++) {
                messages[i] = compoundTag.getString(KEY_MESSAGE + i);
            }
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            for (int i = 0; i < getMaxArrivals(); i++) {
                compoundTag.putString(KEY_MESSAGE + i, messages[i] == null ? "" : messages[i]);
            }
        }

        public AABB getRenderBoundingBox() {
            return new AABB(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        }

        public void setData(String[] messages) {
            System.arraycopy(messages, 0, this.messages, 0, Math.min(messages.length, this.messages.length));
            setChanged();
            syncData();
        }

        public String getMessage(int index) {
            if (index >= 0 && index < getMaxArrivals()) {
                if (messages[index] == null) {
                    messages[index] = "";
                }
                return messages[index];
            } else {
                return "";
            }
        }

        public abstract int getMaxArrivals();
    }
}