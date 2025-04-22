package net.londonunderground.registry.fabric;

import com.mojang.brigadier.CommandDispatcher;
import mtr.item.ItemWithCreativeTabBase;
import mtr.mappings.FabricRegistryUtilities;
import mtr.registry.CreativeModeTabs;
import mtr.registry.RegistryObject;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.londonunderground.LondonUnderground;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Consumer;

public class LondonUndergroundRegistryImpl {

    public static RegistryObject<Block> registerBlock(String id, RegistryObject<Block> block) {
        Registry.register(BuiltInRegistries.BLOCK, LondonUnderground.id(id), block.get());
        return block;
    }

    public static RegistryObject<Block> registerBlockAndItem(String id, RegistryObject<Block> block, CreativeModeTabs.Wrapper tab) {
        registerBlock(id, block);
        final BlockItem blockItem = new BlockItem(block.get(), new Item.Properties());
        Registry.register(BuiltInRegistries.ITEM, LondonUnderground.id(id), blockItem);
        FabricRegistryUtilities.registerCreativeModeTab(tab.get(), blockItem);
        return block;
    }

    public static RegistryObject<Block> registerBlockAndItem(String id, RegistryObject<Block> block) {
        registerBlock(id, block);
        final BlockItem blockItem = new BlockItem(block.get(), new Item.Properties());
        Registry.register(BuiltInRegistries.ITEM, LondonUnderground.id(id), blockItem);
        return block;
    }

    public static void registerItem(String id, RegistryObject<? extends Item> item) {
        Registry.register(BuiltInRegistries.ITEM, LondonUnderground.id(id), item.get());

        if(item.get() instanceof ItemWithCreativeTabBase itm) {
            FabricRegistryUtilities.registerCreativeModeTab(itm.creativeModeTab.get(), item.get());
        }
    }

    public static <T extends BlockEntityType<? extends BlockEntity>> RegistryObject<T> registerBlockEntityType(String id, RegistryObject<T> blockEntityType) {
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, LondonUnderground.id(id), blockEntityType.get());
        return blockEntityType;
    }

    public static void registerEntityType(String id, RegistryObject<? extends EntityType<? extends Entity>> entityType) {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, LondonUnderground.id(id), entityType.get());
    }

    public static SoundEvent registerSoundEvent(String id, SoundEvent soundEvent) {
        Registry.register(BuiltInRegistries.SOUND_EVENT, LondonUnderground.id(id), soundEvent);
        return soundEvent;
    }

    public static void registerCommands(Consumer<CommandDispatcher<CommandSourceStack>> commandRegisterCallback) {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, third) -> {
            commandRegisterCallback.accept(dispatcher);
        });
        throw new AssertionError();
    }
}