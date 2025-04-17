package com.lx862.jcm.mod;

import com.lx862.jcm.mod.registry.BlockEntities;
import com.lx862.jcm.mod.registry.Blocks;
import com.lx862.jcm.mod.registry.Items;
import com.lx862.jcm.mod.util.JCMLogger;
import mtr.Keys;
import mtr.MTR;
import mtr.mappings.BlockEntityMapper;
import mtr.registry.RegistryObject;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class JCM {
    public static void init(
            MTR.RegisterCallback<RegistryObject<Item>> registerItem,
            MTR.RegisterCallback<RegistryObject<Block>> registerBlock,
            MTR.RegisterBlockItem registerBlockItem,
            MTR.RegisterBlockItem registerEnchantedBlockItem,
            MTR.RegisterCallback<RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>>> registerBlockEntityType,
            MTR.RegisterCallback<RegistryObject<? extends EntityType<? extends Entity>>> registerEntityType,
            MTR.RegisterCallback<SoundEvent> registerSoundEvent
    ) {
        try {
            JCMLogger.info("Joban Client Mod v{} @ NeoMTR {}", Constants.MOD_VERSION, Keys.class.getField("MOD_VERSION").get(null));
        } catch (Exception e) {
            JCMLogger.warn("Cannot obtain NeoMTR Version, countdown to disaster...");
        }
        Blocks.register(registerBlock, registerBlockItem);
        BlockEntities.register(registerBlockEntityType);
        Items.register(registerItem);
//        Networking.register();
//        Events.register();
    }
}