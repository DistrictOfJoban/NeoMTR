package mtr.mixin.qol;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mtr.client.ClientData;
import mtr.data.TrainClient;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
public class RainMixin {

    @WrapOperation(
            method = "renderSnowAndRain(Lnet/minecraft/client/renderer/LightTexture;FDDD)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getHeight(Lnet/minecraft/world/level/levelgen/Heightmap$Types;II)I")
    )
    public int modifyRain(Level level, Heightmap.Types heightmapType, int x, int z, Operation<Integer> original) {
        for(TrainClient trainClient : ClientData.TRAINS) {
            int trainRoofAtCurrentPos = trainClient.getRoofAt(x, z);
            if(trainRoofAtCurrentPos != Integer.MIN_VALUE) {
                return trainRoofAtCurrentPos;
            }
        }

        return original.call(level, heightmapType, x, z);
    }

    @WrapOperation(
            method = "Lnet/minecraft/client/renderer/LevelRenderer;tickRain(Lnet/minecraft/client/Camera;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelReader;getHeightmapPos(Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/BlockPos;")
    )
    public BlockPos modifyRainSound(LevelReader levelReader, Heightmap.Types heightmapType, BlockPos pos, Operation<BlockPos> original) {
        for(TrainClient trainClient : ClientData.TRAINS) {
            int trainRoofAtCurrentPos = trainClient.getRoofAt(pos.getX(), pos.getZ());
            if(trainRoofAtCurrentPos != Integer.MIN_VALUE) {
                return new BlockPos(pos.getX(), trainRoofAtCurrentPos + 1, pos.getZ());
            }
        }

        return original.call(levelReader, heightmapType, pos);
    }
}
