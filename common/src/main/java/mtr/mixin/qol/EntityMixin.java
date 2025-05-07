package mtr.mixin.qol;

import mtr.client.ClientData;
import mtr.data.TrainClient;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "collideBoundingBox(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Lnet/minecraft/world/level/Level;Ljava/util/List;)Lnet/minecraft/world/phys/Vec3;", at = @At("HEAD"), cancellable = true)
    private static void mtrTrainCollision(@Nullable Entity entity, Vec3 vec, AABB collisionBox, Level level, List<VoxelShape> potentialHits, CallbackInfoReturnable<Vec3> cir) {
        if(level.isClientSide()) {
            Vec3 beforeMove = collisionBox.getBottomCenter();
            Vec3 afterMove = beforeMove.add(vec);

            double alteredMovementX = vec.x();
            double alteredMovementY = vec.y();
            double alteredMovementZ = vec.z();

            for(TrainClient trainClient : ClientData.TRAINS) {
                Double trainRoofY = trainClient.getTopY(afterMove.x(), afterMove.z());
                if(trainRoofY != null) {
                    if(beforeMove.y() >= trainRoofY && afterMove.y() < trainRoofY) {
                        alteredMovementY = 0;
                        break;
                    }
                }
            }

            if(alteredMovementX != vec.x() || alteredMovementY != vec.y() || alteredMovementZ != vec.z()) {
                cir.setReturnValue(new Vec3(alteredMovementX, alteredMovementY, alteredMovementZ));
            }
        }
    }
}
