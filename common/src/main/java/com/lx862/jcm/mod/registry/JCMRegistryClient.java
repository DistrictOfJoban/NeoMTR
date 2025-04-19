package com.lx862.jcm.mod.registry;

import mtr.RegistryClient;
import mtr.registry.RegistryObject;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public class JCMRegistryClient {
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
