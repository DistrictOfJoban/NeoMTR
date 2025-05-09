package net.londonunderground.loader.neoforge;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import mtr.data.RailwayData;
import mtr.loader.neoforge.MTRRegistryClientImpl;
import mtr.neoforge.mappings.ForgeUtilities;
import mtr.mappings.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class LUAddonRegistryClientImpl {

    public static void registerBlockRenderType(RenderType type, Block block) {
        MTRRegistryClientImpl.RegistryUtilitiesClient.registerRenderType(type, block);
    }

    public static void registerItemModelPredicate(String id, Item item, String tag) {
        MTRRegistryClientImpl.RegistryUtilitiesClient.registerItemModelPredicate(id, item, tag);
    }

    public static <T extends BlockEntityMapper> void registerBlockEntityRenderer(BlockEntityType<T> type, Function<BlockEntityRenderDispatcher, BlockEntityRendererMapper<T>> function) {
        MTRRegistryClientImpl.RegistryUtilitiesClient.registerBlockEntityRenderer(type, function);
    }

    public static <T extends Entity> void registerEntityRenderer(EntityType<T> type, Function<Object, EntityRendererMapper<T>> function) {
        MTRRegistryClientImpl.RegistryUtilitiesClient.registerEntityRenderer(type, function::apply);
    }

    public static void registerKeyBinding(KeyMapping keyMapping) {
        ForgeUtilities.registerKeyBinding(keyMapping);
    }

    public static void registerBlockColors(Block block) {
        MTRRegistryClientImpl.RegistryUtilitiesClient.registerBlockColors(new StationColor(), block);
    }

    private static class StationColor implements BlockColor {

        @Override
        public int getColor(BlockState blockState, BlockAndTintGetter blockAndTintGetter, BlockPos pos, int i) {
            return RailwayData.getStationColor(pos);
        }
    }


    public interface RegistryUtilitiesClient {

        static void registerItemModelPredicate(String id, Item item, String tag) {
            ItemPropertiesRegistry.register(item, ResourceLocation.parse(id), (itemStack, clientWorld, livingEntity, i) ->
                    itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).contains(tag) ? 1 : 0);
        }

        static <T extends BlockEntityMapper> void registerTileEntityRenderer(BlockEntityType<T> type, Function<BlockEntityRenderDispatcher, BlockEntityRendererMapper<T>> factory) {
            BlockEntityRendererRegistry.register(type, context -> factory.apply(null));
        }

        static <T extends Entity> void registerEntityRenderer(EntityType<T> type, Function<EntityRendererProvider.Context, EntityRendererMapper<T>> factory) {
        }

        static void registerRenderType(RenderType renderType, Block block) {
            RenderTypeRegistry.register(renderType, block);
        }

        static void registerBlockColors(BlockColor blockColor, Block block) {
            ColorHandlerRegistry.registerBlockColors(blockColor, block);
        }
    }
}
