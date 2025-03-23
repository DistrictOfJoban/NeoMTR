package cn.zbx1425.mtrsteamloco;

import mtr.registry.CreativeModeTabs;
import mtr.registry.RegistryObject;
import mtr.item.ItemWithCreativeTabBase;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface RegistriesWrapper {

    void registerBlock(String id, RegistryObject<Block> block);

    void registerItem(String id, RegistryObject<ItemWithCreativeTabBase> item);

    void registerBlockAndItem(String id, RegistryObject<Block> block, CreativeModeTabs.Wrapper tab);

    void registerBlockEntityType(String id, RegistryObject<? extends BlockEntityType<? extends BlockEntity>> blockEntityType);

    void registerEntityType(String id, RegistryObject<? extends EntityType<? extends Entity>> entityType);

    void registerSoundEvent(String id, SoundEvent soundEvent);

    void registerParticleType(String id, ParticleType<?> particleType);

    SimpleParticleType createParticleType(boolean overrideLimiter);

}
