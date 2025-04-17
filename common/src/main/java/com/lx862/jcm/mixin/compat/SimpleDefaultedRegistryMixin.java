package com.lx862.jcm.mixin.compat;

import com.lx862.jcm.mod.util.JCMUtil;
import net.minecraft.core.DefaultedMappedRegistry;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Block renamed in JCM 2.0.0, unfortunately there's no easy way to migrate ids.
 * This class helps migrate these ids
 * See <a href="https://github.com/Noaaan/MythicMetals/blob/1.20/src/main/java/nourl/mythicmetals/mixin/DefaultedRegistryMixin.java">here</a>
 * And <a href="https://github.com/orgs/FabricMC/discussions/2361">https://github.com/orgs/FabricMC/discussions/2361</a>
 */
@Mixin(DefaultedMappedRegistry.class)
public class SimpleDefaultedRegistryMixin {
    @ModifyVariable(at = @At("HEAD"), method = "get(Lnet/minecraft/resources/ResourceLocation;)Ljava/lang/Object;", ordinal = 0, argsOnly = true)
    ResourceLocation dataFixerRegistry(ResourceLocation id) {
        if(id == null) return null;
        return JCMUtil.getMigrationId(id);
    }
}