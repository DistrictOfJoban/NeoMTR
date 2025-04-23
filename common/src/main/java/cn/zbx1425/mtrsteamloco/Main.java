package cn.zbx1425.mtrsteamloco;

import cn.zbx1425.mtrsteamloco.block.BlockEyeCandy;
import cn.zbx1425.mtrsteamloco.block.BlockOneWayGate;
import cn.zbx1425.mtrsteamloco.network.*;
import com.google.gson.JsonParser;
import mtr.loader.MTRRegistry;
import mtr.registry.CreativeModeTabs;
import mtr.registry.MTRAddonRegistry;
import mtr.registry.RegistryObject;
import mtr.item.ItemBridgeCreator;
import mtr.item.ItemWithCreativeTabBase;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Main {
	public static final String MOD_ID = "mtrsteamloco";
	public static final MTRAddonRegistry.MTRAddon ADDON = new MTRAddonRegistry.MTRAddon(MOD_ID, "MTR-NTE", BuildConfig.MOD_VERSION);

	public static final Logger LOGGER = LoggerFactory.getLogger("MTR-NTE");
	public static final JsonParser JSON_PARSER = new JsonParser();

	public static final boolean enableRegistry;
	static {
		boolean enableRegistry1;
		try {
			String jarPath = Main.class.getProtectionDomain().getCodeSource().getLocation()
					.toURI().getPath().toLowerCase(Locale.ROOT);
			enableRegistry1 = !jarPath.endsWith("-client.jar");
		} catch (URISyntaxException ignored) {
			enableRegistry1 = true;
		}
		enableRegistry = enableRegistry1;
	}

	public static final RegistryObject<Block> BLOCK_EYE_CANDY = new RegistryObject<>(BlockEyeCandy::new);
	public static final RegistryObject<BlockEntityType<BlockEyeCandy.BlockEntityEyeCandy>>
			BLOCK_ENTITY_TYPE_EYE_CANDY = new RegistryObject<>(() ->
			MTRRegistry.getBlockEntityType(
					BlockEyeCandy.BlockEntityEyeCandy::new,
					BLOCK_EYE_CANDY.get()
			));
	public static final RegistryObject<Block> BLOCK_ONE_WAY_GATE = new RegistryObject<>(BlockOneWayGate::new);

	public static final RegistryObject<ItemWithCreativeTabBase> BRIDGE_CREATOR_1 = new RegistryObject<>(() -> new ItemBridgeCreator(1));

	public static final SoundEvent SOUND_EVENT_BELL = SoundEvent.createVariableRangeEvent(Main.id("bell"));

	public static SimpleParticleType PARTICLE_STEAM_SMOKE;

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public static void init(RegistriesWrapper registries) {
		LOGGER.info("MTR-NTE " + BuildConfig.MOD_VERSION + " built at "
				+ DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.systemDefault()).format(BuildConfig.BUILD_TIME));
		if (enableRegistry) {
			registries.registerBlockAndItem("eye_candy", BLOCK_EYE_CANDY, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registries.registerBlockEntityType("eye_candy", BLOCK_ENTITY_TYPE_EYE_CANDY);
			registries.registerBlockAndItem("one_way_gate_1", BLOCK_ONE_WAY_GATE, CreativeModeTabs.RAILWAY_FACILITIES);
			registries.registerItem("bridge_creator_1", BRIDGE_CREATOR_1);
			registries.registerSoundEvent("bell", SOUND_EVENT_BELL);
			PARTICLE_STEAM_SMOKE = registries.createParticleType(true);
			registries.registerParticleType("steam_smoke", PARTICLE_STEAM_SMOKE);

			MTRRegistry.registerNetworkReceiver(PacketUpdateBlockEntity.PACKET_UPDATE_BLOCK_ENTITY,
					PacketUpdateBlockEntity::receiveUpdateC2S);
			MTRRegistry.registerNetworkReceiver(PacketUpdateRail.PACKET_UPDATE_RAIL,
					PacketUpdateRail::receiveUpdateC2S);
			MTRRegistry.registerNetworkReceiver(PacketUpdateHoldingItem.PACKET_UPDATE_HOLDING_ITEM,
					PacketUpdateHoldingItem::receiveUpdateC2S);
			MTRRegistry.registerNetworkReceiver(PacketVirtualDrive.PACKET_VIRTUAL_DRIVE,
					PacketVirtualDrive::receiveVirtualDriveC2S);

			MTRRegistry.registerNetworkPacket(PacketVersionCheck.PACKET_VERSION_CHECK);
			MTRRegistry.registerNetworkPacket(PacketScreen.PACKET_SHOW_SCREEN);
			MTRRegistry.registerNetworkPacket(PacketVirtualDrivingPlayers.PACKET_VIRTUAL_DRIVING_PLAYERS);

			MTRRegistry.registerPlayerJoinEvent(PacketVersionCheck::sendVersionCheckS2C);
		}

		MTRRegistry.registerPlayerJoinEvent(PacketVirtualDrivingPlayers::sendVirtualDrivingPlayersS2C);
		MTRAddonRegistry.registerAddon(ADDON);
	}

	@FunctionalInterface
	public interface RegisterBlockItem {
		void accept(String string, RegistryObject<Block> block, CreativeModeTabs tab);
	}
}
