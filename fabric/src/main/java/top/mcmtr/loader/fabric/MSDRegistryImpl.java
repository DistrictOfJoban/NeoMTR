package top.mcmtr.loader.fabric;

import mtr.fabric.CompatPacketRegistry;
import mtr.item.ItemWithCreativeTabBase;
import mtr.mappings.FabricRegistryUtilities;
import mtr.mappings.NetworkUtilities;
import mtr.registry.CreativeModeTabs;
import mtr.registry.RegistryObject;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import top.mcmtr.mod.MSDMain;

import java.util.function.Consumer;

public class MSDRegistryImpl {
    public static CompatPacketRegistry PACKET_REGISTRY = new CompatPacketRegistry();

    public static void registerBlock(String id, RegistryObject<Block> block) {
        Registry.register(BuiltInRegistries.BLOCK, MSDMain.id(id), block.get());
    }

    public static void registerBlockAndItem(String id, RegistryObject<Block> block, CreativeModeTabs.Wrapper tab) {
        registerBlock(id, block);
        final BlockItem blockItem = new BlockItem(block.get(), new Item.Properties());
        Registry.register(BuiltInRegistries.ITEM, MSDMain.id(id), blockItem);
        FabricRegistryUtilities.registerCreativeModeTab(tab.get(), blockItem);
    }

    public static void registerItem(String id, RegistryObject<? extends Item> item) {
        Registry.register(BuiltInRegistries.ITEM, MSDMain.id(id), item.get());

        if(item.get() instanceof ItemWithCreativeTabBase itm) {
            FabricRegistryUtilities.registerCreativeModeTab(itm.creativeModeTab.get(), item.get());
        }
    }

    public static void registerBlockEntityType(String id, RegistryObject<? extends BlockEntityType<? extends BlockEntity>> blockEntityType) {
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, MSDMain.id(id), blockEntityType.get());
    }

    public static void registerEntityType(String id, RegistryObject<? extends EntityType<? extends Entity>> entityType) {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, MSDMain.id(id), entityType.get());
    }

    public static void registerSoundEvent(String id, SoundEvent soundEvent) {
        Registry.register(BuiltInRegistries.SOUND_EVENT, MSDMain.id(id), soundEvent);
    }

    public static void registerParticleType(String id, ParticleType<?> particleType) {
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, MSDMain.id(id), particleType);
    }

    public static SimpleParticleType createParticleType(boolean overrideLimiter) {
        return FabricParticleTypes.simple(overrideLimiter);
    }

    public static void registerNetworkPacket(ResourceLocation resourceLocation) {
        PACKET_REGISTRY.registerPacket(resourceLocation);
    }

    public static void registerNetworkReceiver(ResourceLocation resourceLocation, NetworkUtilities.PacketCallback packetCallback) {
        PACKET_REGISTRY.registerNetworkReceiverC2S(resourceLocation, packetCallback);
    }

    public static void sendToPlayer(ServerPlayer player, ResourceLocation id, FriendlyByteBuf packet) {
        PACKET_REGISTRY.sendS2C(player, id, packet);
    }

    public static void registerPlayerJoinEvent(Consumer<ServerPlayer> consumer) {
        ServerEntityEvents.ENTITY_LOAD.register((entity, serverWorld) -> {
            if (entity instanceof ServerPlayer) {
                consumer.accept((ServerPlayer) entity);
            }
        });
    }

    public static void registerPlayerQuitEvent(Consumer<ServerPlayer> consumer) {
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> consumer.accept(handler.player));
    }

    public static void registerTickEvent(Consumer<MinecraftServer> consumer) {
        ServerTickEvents.START_SERVER_TICK.register(consumer::accept);
    }
}
