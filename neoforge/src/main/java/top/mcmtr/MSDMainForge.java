package top.mcmtr;

import mtr.Registry;
import mtr.item.ItemWithCreativeTabBase;
import mtr.mappings.BlockEntityMapper;
import mtr.neoforge.DeferredRegisterHolder;
import mtr.neoforge.mappings.ForgeUtilities;
import mtr.registry.CreativeModeTabs;
import mtr.registry.RegistryObject;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;

public class MSDMainForge {
    private static final DeferredRegisterHolder<Item> ITEMS = new DeferredRegisterHolder<>(MSDMain.MOD_ID, ForgeUtilities.registryGetItem());
    private static final DeferredRegisterHolder<Block> BLOCKS = new DeferredRegisterHolder<>(MSDMain.MOD_ID, ForgeUtilities.registryGetBlock());
    private static final DeferredRegisterHolder<BlockEntityType<?>> BLOCK_ENTITY_TYPES = new DeferredRegisterHolder<>(MSDMain.MOD_ID, ForgeUtilities.registryGetBlockEntityType());
    private static final DeferredRegisterHolder<SoundEvent> SOUND_EVENTS = new DeferredRegisterHolder<>(MSDMain.MOD_ID, ForgeUtilities.registryGetSoundEvent());
    private static final DeferredRegisterHolder<EntityType<?>> ENTITY_TYPES = new DeferredRegisterHolder<>(MSDMain.MOD_ID, ForgeUtilities.registryGetEntityType());

    static {
        MSDMain.init(MSDMainForge::registerItem, MSDMainForge::registerBlock, MSDMainForge::registerBlock, MSDMainForge::registerBlockEntityType, MSDMainForge::registerEntityType, MSDMainForge::registerSoundEvent);
    }

    public static void registerAllDeferred(IEventBus eventBus) {
        ITEMS.register(eventBus);
        BLOCKS.register(eventBus);
        BLOCK_ENTITY_TYPES.register(eventBus);
        SOUND_EVENTS.register(eventBus);
    }

    public static void registerItem(String path, RegistryObject<Item> item) {
        ITEMS.register(path, () -> {
            final Item itemObject = item.get();
            if (itemObject instanceof ItemWithCreativeTabBase) {
                Registry.registerCreativeModeTab(((ItemWithCreativeTabBase) itemObject).creativeModeTab.resourceLocation, itemObject);
            } else if (itemObject instanceof ItemWithCreativeTabBase.ItemPlaceOnWater) {
                Registry.registerCreativeModeTab(((ItemWithCreativeTabBase.ItemPlaceOnWater) itemObject).creativeModeTab.resourceLocation, itemObject);
            }
            return itemObject;
        });
    }

    public static void registerBlock(String path, RegistryObject<Block> block) {
        BLOCKS.register(path, block::get);
    }

    public static void registerBlock(String path, RegistryObject<Block> block, CreativeModeTabs.Wrapper creativeModeTabWrapper) {
        registerBlock(path, block);
        ITEMS.register(path, () -> {
            final BlockItem blockItem = new BlockItem(block.get(), new Item.Properties());
            Registry.registerCreativeModeTab(creativeModeTabWrapper.resourceLocation, blockItem);
            return blockItem;
        });
    }

    public static void registerBlockEntityType(String path, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>> blockEntityType) {
        BLOCK_ENTITY_TYPES.register(path, blockEntityType::get);
    }

    public static void registerSoundEvent(String path, SoundEvent soundEvent) {
        SOUND_EVENTS.register(path, () -> soundEvent);
    }

    public static void registerEntityType(String path, RegistryObject<? extends EntityType<? extends Entity>> entityType) {
        ENTITY_TYPES.register(path, entityType::get);
    }
}