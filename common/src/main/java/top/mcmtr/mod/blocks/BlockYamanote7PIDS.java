package top.mcmtr.mod.blocks;

import mtr.block.BlockPIDSBaseHorizontal;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.Text;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import top.mcmtr.mod.MSDBlockEntityTypes;
import top.mcmtr.mod.config.Config;

import java.util.List;

public class BlockYamanote7PIDS extends BlockPIDSBaseHorizontal {
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape shape1 = IBlock.getVoxelShapeByDirection(7, 8, 0, 9, 16, 29, IBlock.getStatePropertySafe(blockState, FACING));
        VoxelShape shape2 = IBlock.getVoxelShapeByDirection(7.5, 8, 29, 8.5, 16, 30, IBlock.getStatePropertySafe(blockState, FACING));
        return Shapes.or(shape1, shape2);
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TileEntityPIDS(blockPos, blockState);
    }

    @Override
    public String getDescriptionId() {
        return "block.msd.yamanote_pids";
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
        tooltip.add(Text.translatable("tooltip.msd.yamanote_7_pids_length").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
    }

    public static class TileEntityPIDS extends TileEntityBlockPIDSBaseHorizontal {
        public TileEntityPIDS(BlockPos pos, BlockState state) {
            super(MSDBlockEntityTypes.YAMANOTE_7_PIDS_TILE_ENTITY.get(), pos, state);
        }

        @Environment(EnvType.CLIENT)
        public double getViewDistance() {
            return Config.getYuuniPIDSMaxViewDistance();
        }

        @Override
        public int getMaxArrivals() {
            return 3;
        }
    }
}