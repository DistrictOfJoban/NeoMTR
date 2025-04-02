package mtr;

import mtr.data.Depot;
import mtr.data.Route;
import mtr.data.Station;
import mtr.mappings.BlockEntityMapper;
import mtr.registry.Networking;
import mtr.packet.PacketTrainDataGuiServer;
import mtr.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MTR {

	private static int gameTick = 0;

	public static final String MOD_ID = "mtr";
	public static final Logger LOGGER = LoggerFactory.getLogger("NeoMTR");

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public static void init(
			RegisterCallback<RegistryObject<Item>> registerItem,
			RegisterCallback<RegistryObject<Block>> registerBlock,
			RegisterBlockItem registerBlockItem,
			RegisterBlockItem registerEnchantedBlockItem,
			RegisterCallback<RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>>> registerBlockEntityType,
			RegisterCallback<RegistryObject<? extends EntityType<? extends Entity>>> registerEntityType,
			RegisterCallback<SoundEvent> registerSoundEvent
	) {
		Blocks.register(registerBlock, registerBlockItem, registerEnchantedBlockItem);
		BlockEntityTypes.register(registerBlockEntityType);
		Items.register(registerItem);
		Networking.register();
		SoundEvents.register(registerSoundEvent);
		Events.register();
	}

	public static void incrementGameTick() {
		gameTick++;
	}

	public static boolean isGameTickInterval(int interval) {
		return gameTick % interval == 0;
	}

	public static boolean isGameTickInterval(int interval, int offset) {
		return (gameTick + offset) % interval == 0;
	}

	@FunctionalInterface
	public interface RegisterBlockItem {
		void accept(String string, RegistryObject<Block> block, CreativeModeTabs.Wrapper tab);
	}

	@FunctionalInterface
	public interface RegisterCallback<T> {
		void register(String id, T data);
	}
}
