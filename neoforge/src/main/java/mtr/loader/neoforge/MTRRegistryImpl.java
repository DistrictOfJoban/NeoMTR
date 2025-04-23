package mtr.loader.neoforge;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import mtr.MTR;
import mtr.item.ItemBlockEnchanted;
import mtr.item.ItemWithCreativeTabBase;
import mtr.loader.MTRRegistry;
import mtr.neoforge.CompatPacketRegistry;
import mtr.neoforge.DeferredRegisterHolder;
import mtr.neoforge.mappings.ArchitecturyUtilities;
import mtr.neoforge.mappings.ForgeUtilities;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.NetworkUtilities;
import mtr.mappings.Utilities;
import mtr.mixin.PlayerTeleportationStateAccessor;
import mtr.registry.CreativeModeTabs;
import mtr.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MTRRegistryImpl {

	private static final DeferredRegisterHolder<Item> ITEMS = new DeferredRegisterHolder<>(MTR.MOD_ID, ForgeUtilities.registryGetItem());
	private static final DeferredRegisterHolder<Block> BLOCKS = new DeferredRegisterHolder<>(MTR.MOD_ID, ForgeUtilities.registryGetBlock());
	private static final DeferredRegisterHolder<BlockEntityType<?>> BLOCK_ENTITY_TYPES = new DeferredRegisterHolder<>(MTR.MOD_ID, ForgeUtilities.registryGetBlockEntityType());
	private static final DeferredRegisterHolder<EntityType<?>> ENTITY_TYPES = new DeferredRegisterHolder<>(MTR.MOD_ID, ForgeUtilities.registryGetEntityType());
	private static final DeferredRegisterHolder<SoundEvent> SOUND_EVENTS = new DeferredRegisterHolder<>(MTR.MOD_ID, ForgeUtilities.registryGetSoundEvent());
	private static final DeferredRegisterHolder<CreativeModeTab> CREATIVE_MODE_TABS = new DeferredRegisterHolder<>(MTR.MOD_ID, Registries.CREATIVE_MODE_TAB);

	public static final CompatPacketRegistry PACKET_REGISTRY = new CompatPacketRegistry();

	public static void registerItem(String path, RegistryObject<Item> item) {
		ITEMS.register(path, () -> {
			final Item itemObject = item.get();
			if (itemObject instanceof ItemWithCreativeTabBase) {
				MTRRegistry.registerCreativeModeTab(((ItemWithCreativeTabBase) itemObject).creativeModeTab.resourceLocation, itemObject);
			} else if (itemObject instanceof ItemWithCreativeTabBase.ItemPlaceOnWater) {
				MTRRegistry.registerCreativeModeTab(((ItemWithCreativeTabBase.ItemPlaceOnWater) itemObject).creativeModeTab.resourceLocation, itemObject);
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
			MTRRegistry.registerCreativeModeTab(creativeModeTabWrapper.resourceLocation, blockItem);
			return blockItem;
		});
	}

	public static void registerEnchantedBlock(String path, RegistryObject<Block> block, CreativeModeTabs.Wrapper creativeModeTab) {
		registerBlock(path, block);
		ITEMS.register(path, () -> {
			final ItemBlockEnchanted itemBlockEnchanted = new ItemBlockEnchanted(block.get(), new Item.Properties());
			MTRRegistry.registerCreativeModeTab(creativeModeTab.resourceLocation, itemBlockEnchanted);
			return itemBlockEnchanted;
		});
	}

	public static void registerBlockEntityType(String path, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>> blockEntityType) {
		BLOCK_ENTITY_TYPES.register(path, blockEntityType::get);
	}

	public static void registerEntityType(String path, RegistryObject<? extends EntityType<? extends Entity>> entityType) {
		ENTITY_TYPES.register(path, entityType::get);
	}

	public static void registerSoundEvent(String path, SoundEvent soundEvent) {
		SOUND_EVENTS.register(path, () -> soundEvent);
	}

	///

	public static boolean isFabric() {
		return false;
	}

	public static boolean isClientEnvironment() {
		return Platform.getEnvironment() == Env.CLIENT;
	}

	public static <T extends BlockEntityMapper> BlockEntityType<T> getBlockEntityType(Utilities.TileEntitySupplier<T> supplier, Block block) {
		return BlockEntityType.Builder.of(supplier::supplier, block).build(null);
	}

	public static Supplier<CreativeModeTab> getCreativeModeTab(ResourceLocation id, Supplier<ItemStack> supplier) {
		String normalizedPath = id.getPath().startsWith(id.getNamespace() + "_")
				? id.getPath().substring(id.getNamespace().length() + 1) : id.getPath();
		return ForgeUtilities.createCreativeModeTab(id, supplier,
				String.format("itemGroup.%s.%s", id.getNamespace(), normalizedPath));
	}

	public static void registerCreativeModeTab(ResourceLocation resourceLocation, Item item) {
		ForgeUtilities.registerCreativeModeTab(resourceLocation, item);
	}

	public static void registerNetworkPacket(ResourceLocation resourceLocation) {
		PACKET_REGISTRY.registerPacket(resourceLocation);
	}

	public static void registerNetworkReceiver(ResourceLocation resourceLocation, NetworkUtilities.PacketCallback packetCallback) {
		PACKET_REGISTRY.registerNetworkReceiverC2S(resourceLocation, packetCallback);
	}

	public static void registerPlayerJoinEvent(Consumer<ServerPlayer> consumer) {
		ArchitecturyUtilities.registerPlayerJoinEvent(consumer);
		ArchitecturyUtilities.registerPlayerChangeDimensionEvent(consumer);
	}

	public static void registerPlayerQuitEvent(Consumer<ServerPlayer> consumer) {
		ArchitecturyUtilities.registerPlayerQuitEvent(consumer);
	}

	public static void registerServerStartingEvent(Consumer<MinecraftServer> consumer) {
		ArchitecturyUtilities.registerServerStartingEvent(consumer);
	}

	public static void registerServerStoppingEvent(Consumer<MinecraftServer> consumer) {
		ArchitecturyUtilities.registerServerStoppingEvent(consumer);
	}

	public static void registerTickEvent(Consumer<MinecraftServer> consumer) {
		ArchitecturyUtilities.registerTickEvent(consumer);
	}

	public static void sendToPlayer(ServerPlayer player, ResourceLocation id, FriendlyByteBuf packet) {
		packet.resetReaderIndex();
		PACKET_REGISTRY.sendS2C(player, id, packet);
	}

	public static void setInTeleportationState(Player player, boolean isRiding) {
		((PlayerTeleportationStateAccessor) player).setInTeleportationState(isRiding);
	}

	public static void registeredAllDeferred(IEventBus eventBus) {
		ITEMS.register(eventBus);
		BLOCKS.register(eventBus);
		BLOCK_ENTITY_TYPES.register(eventBus);
		ENTITY_TYPES.register(eventBus);
		SOUND_EVENTS.register(eventBus);

		ForgeUtilities.registerCreativeModeTabsToDeferredRegistry(CREATIVE_MODE_TABS);
		CREATIVE_MODE_TABS.register(eventBus);
	}
}

