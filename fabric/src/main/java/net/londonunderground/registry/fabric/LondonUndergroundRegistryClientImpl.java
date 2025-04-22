package net.londonunderground.registry.fabric;

import mtr.data.RailwayData;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.EntityRendererMapper;
import mtr.mappings.FabricRegistryUtilities;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Function;

public class LondonUndergroundRegistryClientImpl {
    public static void registerBlockRenderType(RenderType type, Block block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, type);
    }

    public static void registerItemModelPredicate(String id, Item item, String tag) {
        FabricRegistryUtilities.registerItemModelPredicate(id, item, tag);
    }

    public static <T extends BlockEntityMapper> void registerBlockEntityRenderer(BlockEntityType<T> type, Function<BlockEntityRenderDispatcher, BlockEntityRendererMapper<T>> function) {
        BlockEntityRendererRegistry.register(type, context -> function.apply(null));
    }

    public static <T extends Entity> void registerEntityRenderer(EntityType<T> type, Function<Object, EntityRendererMapper<T>> function) {
        EntityRendererRegistry.register(type, function::apply);
    }

    public static void registerKeyBinding(KeyMapping keyMapping) {
        KeyBindingHelper.registerKeyBinding(keyMapping);
    }

    public static void registerBlockColors(Block block) {
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> RailwayData.getStationColor(pos), block);
    }
}
