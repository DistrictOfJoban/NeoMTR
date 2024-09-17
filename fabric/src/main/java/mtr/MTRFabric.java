package mtr;

import mtr.fabric.CompatPacketRegistry;
import mtr.item.ItemBlockEnchanted;
import mtr.item.ItemWithCreativeTabBase;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.FabricRegistryUtilities;
import mtr.mappings.RegistryUtilities;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class MTRFabric implements ModInitializer {

	public static CompatPacketRegistry PACKET_REGISTRY = new CompatPacketRegistry();

	@Override
	public void onInitialize() {
		MTR.init(MTRFabric::registerItem, MTRFabric::registerBlock, MTRFabric::registerBlock, MTRFabric::registerEnchantedBlock, MTRFabric::registerBlockEntityType, MTRFabric::registerEntityType, MTRFabric::registerSoundEvent);
		PACKET_REGISTRY.commitCommon();
	}

	private static void registerItem(String path, RegistryObject<Item> item) {
		final Item itemObject = item.get();
		Registry.register(RegistryUtilities.registryGetItem(), MTR.id(path), itemObject);
		if (itemObject instanceof ItemWithCreativeTabBase) {
			FabricRegistryUtilities.registerCreativeModeTab(((ItemWithCreativeTabBase) itemObject).creativeModeTab.get(), itemObject);
		} else if (itemObject instanceof ItemWithCreativeTabBase.ItemPlaceOnWater) {
			FabricRegistryUtilities.registerCreativeModeTab(((ItemWithCreativeTabBase.ItemPlaceOnWater) itemObject).creativeModeTab.get(), itemObject);
		}
	}

	private static void registerBlock(String path, RegistryObject<Block> block) {
		Registry.register(RegistryUtilities.registryGetBlock(), MTR.id(path), block.get());
	}

	private static void registerBlock(String path, RegistryObject<Block> block, CreativeModeTabs.Wrapper creativeModeTab) {
		registerBlock(path, block);
		final BlockItem blockItem = new BlockItem(block.get(), RegistryUtilities.createItemProperties(creativeModeTab::get));
		Registry.register(RegistryUtilities.registryGetItem(), MTR.id(path), blockItem);
		FabricRegistryUtilities.registerCreativeModeTab(creativeModeTab.get(), blockItem);
	}

	private static void registerEnchantedBlock(String path, RegistryObject<Block> block, CreativeModeTabs.Wrapper creativeModeTab) {
		registerBlock(path, block);
		final ItemBlockEnchanted itemBlockEnchanted = new ItemBlockEnchanted(block.get(), RegistryUtilities.createItemProperties(creativeModeTab::get));
		Registry.register(RegistryUtilities.registryGetItem(), MTR.id(path), itemBlockEnchanted);
		FabricRegistryUtilities.registerCreativeModeTab(creativeModeTab.get(), itemBlockEnchanted);
	}

	private static void registerBlockEntityType(String path, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>> blockEntityType) {
		Registry.register(RegistryUtilities.registryGetBlockEntityType(), MTR.id(path), blockEntityType.get());
	}

	private static void registerEntityType(String path, RegistryObject<? extends EntityType<? extends Entity>> entityType) {
		Registry.register(RegistryUtilities.registryGetEntityType(), MTR.id(path), entityType.get());
	}

	private static void registerSoundEvent(String path, SoundEvent soundEvent) {
		Registry.register(RegistryUtilities.registryGetSoundEvent(), MTR.id(path), soundEvent);
	}
}
