package top.mcmtr.loader;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mtr.mappings.NetworkUtilities;
import mtr.registry.CreativeModeTabs;
import mtr.registry.RegistryObject;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Consumer;

public class MSDRegistry {
    @ExpectPlatform
    public static void registerBlock(String id, RegistryObject<Block> block) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerBlockAndItem(String id, RegistryObject<Block> block, CreativeModeTabs.Wrapper tab) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerItem(String id, RegistryObject<? extends Item> item) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerBlockEntityType(String id, RegistryObject<? extends BlockEntityType<? extends BlockEntity>> blockEntityType) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerEntityType(String id, RegistryObject<? extends EntityType<? extends Entity>> entityType) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerSoundEvent(String id, SoundEvent soundEvent) {
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
    public static void sendToPlayer(ServerPlayer player, ResourceLocation id, FriendlyByteBuf packet) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerParticleType(String id, ParticleType<?> particleType) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static SimpleParticleType createParticleType(boolean overrideLimiter) {
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
    public static void registerTickEvent(Consumer<MinecraftServer> consumer) {
        throw new AssertionError();
    }
}
