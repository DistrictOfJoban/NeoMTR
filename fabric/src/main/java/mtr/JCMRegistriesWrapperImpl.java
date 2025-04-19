package mtr;

import cn.zbx1425.mtrsteamloco.RegistriesWrapper;
import com.lx862.jcm.mod.Constants;
import mtr.item.ItemWithCreativeTabBase;
import mtr.mappings.FabricRegistryUtilities;
import mtr.registry.CreativeModeTabs;
import mtr.registry.RegistryObject;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class JCMRegistriesWrapperImpl implements RegistriesWrapper {

    @Override
    public void registerBlock(String id, RegistryObject<Block> block) {
        Registry.register(BuiltInRegistries.BLOCK, Constants.id(id), block.get());
    }

    @Override
    public void registerBlockAndItem(String id, RegistryObject<Block> block, CreativeModeTabs.Wrapper tab) {
        registerBlock(id, block);
        final BlockItem blockItem = new BlockItem(block.get(), new Item.Properties());
        Registry.register(BuiltInRegistries.ITEM, Constants.id(id), blockItem);
        FabricRegistryUtilities.registerCreativeModeTab(tab.get(), blockItem);
    }

    @Override
    public void registerItem(String id, RegistryObject<? extends Item> item) {
        Registry.register(BuiltInRegistries.ITEM, Constants.id(id), item.get());

        if(item.get() instanceof ItemWithCreativeTabBase itm) {
            FabricRegistryUtilities.registerCreativeModeTab(itm.creativeModeTab.get(), item.get());
        }
    }

    @Override
    public void registerBlockEntityType(String id, RegistryObject<? extends BlockEntityType<? extends BlockEntity>> blockEntityType) {
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Constants.id(id), blockEntityType.get());
    }

    @Override
    public void registerEntityType(String id, RegistryObject<? extends EntityType<? extends Entity>> entityType) {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, Constants.id(id), entityType.get());
    }

    @Override
    public void registerSoundEvent(String id, SoundEvent soundEvent) {
        Registry.register(BuiltInRegistries.SOUND_EVENT, Constants.id(id), soundEvent);
    }

    @Override
    public void registerParticleType(String id, ParticleType<?> particleType) {
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Constants.id(id), particleType);
    }

    @Override
    public SimpleParticleType createParticleType(boolean overrideLimiter) {
        return FabricParticleTypes.simple(overrideLimiter);
    }
}