package top.mcmtr.mod.blocks;

import mtr.mappings.BlockDirectionalMapper;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.List;

public abstract class BlockChangeModelBase extends BlockDirectionalMapper {
    public static final IntegerProperty TEXTURE_TYPE = IntegerProperty.create("type", 0, 5);
    private final int count;

    public BlockChangeModelBase(int count, Properties properties) {
        super(properties);
        this.count = count;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
        tooltip.add(Text.translatable("tooltip.msd.model_count", count).setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TEXTURE_TYPE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection()).setValue(TEXTURE_TYPE, 0);
    }

    public int getCount() {
        return count;
    }
}