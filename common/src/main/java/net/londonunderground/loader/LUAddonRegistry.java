package net.londonunderground.loader;

import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.injectables.annotations.ExpectPlatform;
import mtr.registry.CreativeModeTabs;
import mtr.registry.RegistryObject;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Consumer;

public class LUAddonRegistry {
    @ExpectPlatform
    public static RegistryObject<Block> registerBlock(String id, RegistryObject<Block> block) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static RegistryObject<Block> registerBlockAndItem(String id, RegistryObject<Block> block, CreativeModeTabs.Wrapper tab) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static RegistryObject<Block> registerBlockAndItem(String id, RegistryObject<Block> block) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerItem(String id, RegistryObject<? extends Item> item) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends BlockEntityType<? extends BlockEntity>> RegistryObject<T> registerBlockEntityType(String id, RegistryObject<T> blockEntityType) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerEntityType(String id, RegistryObject<? extends EntityType<? extends Entity>> entityType) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static SoundEvent registerSoundEvent(String id, SoundEvent soundEvent) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerCommands(Consumer<CommandDispatcher<CommandSourceStack>> commandRegisterCallback) {
        throw new AssertionError();
    }
}
