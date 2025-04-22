package net.londonunderground.registry.neoforge;

import com.mojang.brigadier.CommandDispatcher;
import mtr.Registry;
import mtr.item.ItemWithCreativeTabBase;
import mtr.neoforge.DeferredRegisterHolder;
import mtr.neoforge.mappings.ForgeUtilities;
import mtr.registry.CreativeModeTabs;
import mtr.registry.RegistryObject;
import net.londonunderground.LondonUnderground;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;

import java.util.function.Consumer;

public class LondonUndergroundRegistryImpl {
    private static final DeferredRegisterHolder<Item> ITEMS = new DeferredRegisterHolder<>(LondonUnderground.MOD_ID, ForgeUtilities.registryGetItem());
    private static final DeferredRegisterHolder<Block> BLOCKS = new DeferredRegisterHolder<>(LondonUnderground.MOD_ID, ForgeUtilities.registryGetBlock());
    private static final DeferredRegisterHolder<BlockEntityType<?>> BLOCK_ENTITY_TYPES = new DeferredRegisterHolder<>(LondonUnderground.MOD_ID, ForgeUtilities.registryGetBlockEntityType());
    private static final DeferredRegisterHolder<EntityType<?>> ENTITY_TYPES = new DeferredRegisterHolder<>(LondonUnderground.MOD_ID, ForgeUtilities.registryGetEntityType());
    private static final DeferredRegisterHolder<SoundEvent> SOUND_EVENTS = new DeferredRegisterHolder<>(LondonUnderground.MOD_ID, ForgeUtilities.registryGetSoundEvent());
    private static Consumer<CommandDispatcher<CommandSourceStack>> commandRegistrationCallback;

    public static RegistryObject<Block> registerBlock(String id, RegistryObject<Block> block) {
        BLOCKS.register(id, block::get);
        return block;
    }

    public static RegistryObject<Block> registerBlockAndItem(String id, RegistryObject<Block> block, CreativeModeTabs.Wrapper tab) {
        BLOCKS.register(id, block::get);
        ITEMS.register(id, () -> {
            final BlockItem blockItem = new BlockItem(block.get(), new Item.Properties());
            Registry.registerCreativeModeTab(tab.resourceLocation, blockItem);
            return blockItem;
        });
        return block;
    }

    public static RegistryObject<Block> registerBlockAndItem(String id, RegistryObject<Block> block) {
        BLOCKS.register(id, block::get);
        ITEMS.register(id, () -> {
            final BlockItem blockItem = new BlockItem(block.get(), new Item.Properties());
            return blockItem;
        });
        return block;
    }

    public static void registerItem(String id, RegistryObject<? extends Item> item) {
        ITEMS.register(id, () -> {
            final Item itemObject = item.get();

            if(itemObject instanceof ItemWithCreativeTabBase itm) {
                Registry.registerCreativeModeTab(itm.creativeModeTab.resourceLocation, itemObject);
            }
            return itemObject;
        });
    }

    public static <T extends BlockEntityType<? extends BlockEntity>> RegistryObject<T> registerBlockEntityType(String id, RegistryObject<T> blockEntityType) {
        BLOCK_ENTITY_TYPES.register(id, blockEntityType::get);
        return blockEntityType;
    }

    public static void registerEntityType(String id, RegistryObject<? extends EntityType<? extends Entity>> entityType) {
        ENTITY_TYPES.register(id, entityType::get);
    }

    public static SoundEvent registerSoundEvent(String id, SoundEvent soundEvent) {
        SOUND_EVENTS.register(id, () -> soundEvent);
        return soundEvent;
    }

    public static void registerAllDeferred(IEventBus eventBus) {
        ITEMS.register(eventBus);
        BLOCKS.register(eventBus);
        BLOCK_ENTITY_TYPES.register(eventBus);
        ENTITY_TYPES.register(eventBus);
        SOUND_EVENTS.register(eventBus);
    }

    public static void onRegisterCommands(Consumer<CommandDispatcher<CommandSourceStack>> commandRegisterCallback) {
        commandRegistrationCallback = commandRegisterCallback;
    }

    public static void invokeRegisterCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        if(commandRegistrationCallback != null) {
            commandRegistrationCallback.accept(dispatcher);
        }
    }
}
