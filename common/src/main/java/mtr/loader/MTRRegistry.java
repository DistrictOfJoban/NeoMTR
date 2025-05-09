package mtr.loader;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mtr.MTR;
import mtr.item.ItemBlockEnchanted;
import mtr.item.ItemWithCreativeTabBase;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.NetworkUtilities;
import mtr.mappings.Utilities;
import mtr.registry.CreativeModeTabs;
import mtr.registry.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
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

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MTRRegistry {

	@ExpectPlatform
	public static void registerItem(String path, RegistryObject<Item> item) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerBlock(String path, RegistryObject<Block> block) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerBlock(String path, RegistryObject<Block> block, CreativeModeTabs.Wrapper creativeModeTab) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerEnchantedBlock(String path, RegistryObject<Block> block, CreativeModeTabs.Wrapper creativeModeTab) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerBlockEntityType(String path, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>> blockEntityType) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerEntityType(String path, RegistryObject<? extends EntityType<? extends Entity>> entityType) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerSoundEvent(String path, SoundEvent soundEvent) {
		throw new AssertionError();
	}

	//

	@ExpectPlatform
	public static boolean isFabric() {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static boolean isClientEnvironment() {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <T extends BlockEntityMapper> BlockEntityType<T> getBlockEntityType(Utilities.TileEntitySupplier<T> supplier, Block block) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static Supplier<CreativeModeTab> getCreativeModeTab(ResourceLocation id, Supplier<ItemStack> supplier) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerCreativeModeTab(ResourceLocation resourceLocation, Item item) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static Packet<?> createAddEntityPacket(Entity entity) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerNetworkPacket(ResourceLocation resourceLocation) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerNetworkReceiver(ResourceLocation resourceLocation, NetworkUtilities.PacketCallback packetCallback) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerPlayerJoinEvent(Consumer<ServerPlayer> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerPlayerQuitEvent(Consumer<ServerPlayer> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerServerStartingEvent(Consumer<MinecraftServer> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerServerStoppingEvent(Consumer<MinecraftServer> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerTickEvent(Consumer<MinecraftServer> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void sendToPlayer(ServerPlayer player, ResourceLocation id, FriendlyByteBuf packet) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void setInTeleportationState(Player player, boolean isRiding) {
		throw new AssertionError();
	}
}
