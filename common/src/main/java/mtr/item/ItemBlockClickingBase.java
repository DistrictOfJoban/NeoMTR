package mtr.item;

import mtr.registry.CreativeModeTabs;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;
import java.util.function.Function;

public abstract class ItemBlockClickingBase extends ItemWithCreativeTabBase {

	public static final String TAG_POS = "pos";

	public ItemBlockClickingBase(CreativeModeTabs.Wrapper creativeModeTab, Function<Properties, Properties> propertiesConsumer) {
		super(creativeModeTab, propertiesConsumer);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (!context.getLevel().isClientSide) {
			if (clickCondition(context)) {
				final CustomData customData = context.getItemInHand()
						.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
				CompoundTag compoundTag = customData.copyTag();
				if (compoundTag.contains(TAG_POS)) {
					final BlockPos posEnd = BlockPos.of(compoundTag.getLong(TAG_POS));
					onEndClick(context, posEnd, compoundTag);
					compoundTag.remove(TAG_POS);
				} else {
					compoundTag.putLong(TAG_POS, context.getClickedPos().asLong());
					onStartClick(context, compoundTag);
				}
				context.getItemInHand().set(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
				return InteractionResult.SUCCESS;
			} else {
				return InteractionResult.FAIL;
			}
		} else {
			return super.useOn(context);
		}
	}

	public BlockPos getFirstBlockPos(ItemStack itemStack) {
		CustomData tag = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
		final long posLong = tag.copyTag().getLong(TAG_POS);
		if(posLong == 0) return null;
		return BlockPos.of(posLong);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		BlockPos clickedPos = getFirstBlockPos(stack);
		if (clickedPos != null) {
			tooltip.add(Text.translatable("tooltip.mtr.selected_block", clickedPos.toShortString()).setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
		}
	}

	protected abstract void onStartClick(UseOnContext context, CompoundTag compoundTag);

	protected abstract void onEndClick(UseOnContext context, BlockPos posEnd, CompoundTag compoundTag);

	protected abstract boolean clickCondition(UseOnContext context);
}
