package com.lx862.jcm.mod.registry;

import mtr.RegistryClient;
import mtr.mapping.mapper.BlockEntityExtension;
import mtr.mapping.mapper.BlockEntityRenderer;
import mtr.mapping.registry.BlockEntityTypeRegistryObject;
import mtr.registry.RegistryObject;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public class JCMRegistryClient {
    public static <T extends BlockEntityTypeRegistryObject<U>, U extends BlockEntityExtension> void registerBlockEntityRenderer(T blockEntityType, Function<BlockEntityRenderer.Argument, BlockEntityRenderer<U>> rendererInstance) {
        REGISTRY_CLIENT.registerBlockEntityRenderer(blockEntityType, rendererInstance);
    }

    public static void registerBlockRenderType(RenderType renderLayer, RegistryObject<Block>... blocks) {
        for(RegistryObject<Block> block : blocks) {
            RegistryClient.registerBlockRenderType(renderLayer, block.get());
        }
    }

    public static void registerStationColoredBlock(RegistryObject<Block>... blocks) {
        for(RegistryObject<Block> block : blocks) {
            RegistryClient.registerBlockColors(block.get());
        }
    }
}
