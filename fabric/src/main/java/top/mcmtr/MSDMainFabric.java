package top.mcmtr;

import mtr.mappings.FabricRegistryUtilities;
import mtr.registry.CreativeModeTabs;
import mtr.registry.RegistryObject;
import mtr.item.ItemWithCreativeTabBase;
import mtr.mappings.BlockEntityMapper;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

// TODO: Entry point not used atm
public class MSDMainFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        MSDMain.init(MSDMainFabric::registerItem, MSDMainFabric::registerBlock, MSDMainFabric::registerBlock, MSDMainFabric::registerBlockEntityType, MSDMainFabric::registerEntityType, MSDMainFabric::registerSoundEvent);
    }

    public static void registerItem(String path, RegistryObject<Item> item) {
        final Item itemObject = item.get();
        Registry.register(BuiltInRegistries.ITEM, MSDMain.id(path), itemObject);
        if (itemObject instanceof ItemWithCreativeTabBase) {
            FabricRegistryUtilities.registerCreativeModeTab(((ItemWithCreativeTabBase) itemObject).creativeModeTab.get(), itemObject);
        } else if (itemObject instanceof ItemWithCreativeTabBase.ItemPlaceOnWater) {
            FabricRegistryUtilities.registerCreativeModeTab(((ItemWithCreativeTabBase.ItemPlaceOnWater) itemObject).creativeModeTab.get(), itemObject);
        }
    }

    public static void registerBlock(String path, RegistryObject<Block> block) {
        Registry.register(BuiltInRegistries.BLOCK, MSDMain.id(path), block.get());
    }

    public static void registerBlock(String path, RegistryObject<Block> block, CreativeModeTabs.Wrapper creativeModeTab) {
        registerBlock(path, block);
        final BlockItem blockItem = new BlockItem(block.get(), new Item.Properties());
        Registry.register(BuiltInRegistries.ITEM, MSDMain.id(path), blockItem);
        FabricRegistryUtilities.registerCreativeModeTab(creativeModeTab.get(), blockItem);
    }

    public static void registerBlockEntityType(String path, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>> blockEntityType) {
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, MSDMain.id(path), blockEntityType.get());
    }

    public static void registerSoundEvent(String path, SoundEvent soundEvent) {
        Registry.register(BuiltInRegistries.SOUND_EVENT, MSDMain.id(path), soundEvent);
    }

    public static void registerEntityType(String path, RegistryObject<? extends EntityType<? extends Entity>> entityType) {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, MSDMain.id(path), entityType.get());
    }
}