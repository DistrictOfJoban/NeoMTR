package net.londonunderground.loader;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.EntityRendererMapper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Function;

public class LUAddonRegistryClient {
    @ExpectPlatform
    public static void registerBlockRenderType(RenderType type, Block block) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerItemModelPredicate(String id, Item item, String tag) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends BlockEntityMapper> void registerBlockEntityRenderer(BlockEntityType<T> type, Function<BlockEntityRenderDispatcher, BlockEntityRendererMapper<T>> function) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends Entity> void registerEntityRenderer(EntityType<T> type, Function<Object, EntityRendererMapper<T>> function) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerKeyBinding(KeyMapping keyMapping) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerBlockColors(Block block) {
        throw new AssertionError();
    }
}
