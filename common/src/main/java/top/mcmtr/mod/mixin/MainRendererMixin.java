package top.mcmtr.mod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.IDrawing;
import mtr.mappings.UtilitiesClient;
import mtr.path.PathData;
import mtr.render.MainRenderer;
import mtr.util.BlockUtil;
import mtr.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.mcmtr.mod.client.MSDClientData;
import top.mcmtr.mod.data.Catenary;
import top.mcmtr.mod.data.CatenaryType;
import top.mcmtr.mod.data.RigidCatenary;
import top.mcmtr.mod.data.TransCatenary;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(MainRenderer.class)
public class MainRendererMixin {
    @Inject(method = "Lmtr/render/MainRenderer;renderRelative(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/world/level/Level;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V", at = @At(value = "INVOKE", target = "Lmtr/render/MainRenderer;renderTrains(Lnet/minecraft/world/level/Level;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V"))
    private static void renderCatenary(Minecraft minecraft, LocalPlayer player, Level level, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, CallbackInfo ci) {
        final int renderDistanceChunks = UtilitiesClient.getRenderDistance();
        final int maxCatenaryDistance = renderDistanceChunks * 16;
        final Map<UUID, Boolean> renderedCatenaryMap = new HashMap<>();
        matrices.pushPose();
        MSDClientData.CATENARIES.forEach((startPos, catenaryMap) -> catenaryMap.forEach((endPos, catenary) -> {
            if (!Util.isBetween(player.getX(), startPos.getX(), endPos.getX(), maxCatenaryDistance) || !Util.isBetween(player.getZ(), startPos.getZ(), endPos.getZ(), maxCatenaryDistance)) {
                return;
            }
            final UUID catenaryProduct = PathData.getRailProduct(startPos, endPos);
            if (renderedCatenaryMap.containsKey(catenaryProduct)) {
                if (renderedCatenaryMap.get(catenaryProduct)) {
                    return;
                }
            } else {
                renderedCatenaryMap.put(catenaryProduct, true);
            }
            if (catenary.catenaryType == CatenaryType.ELECTRIC) {
                catenary.render((x1, y1, z1, x2, y2, z2, count, i, base, sinX, sinZ, increment) ->
                    IDrawing.drawLine(matrices, vertexConsumers, (float) x1, (float) y1 + 0.5F, (float) z1, (float) x2, (float) y2 + 0.5F, (float) z2, 0, 0, 0)
                );
            } else if (catenary.catenaryType == CatenaryType.RIGID_SOFT_CATENARY) {
                renderRigidSoftCatenaryStandard(catenary);
            } else {
                renderCatenaryStandard(catenary);
            }
        }));
        matrices.popPose();
        matrices.pushPose();
        final Map<UUID, Boolean> renderedRigidCatenaryMap = new HashMap<>();
        MSDClientData.RIGID_CATENARIES.forEach((startPos, rigidCatenaryMap) -> rigidCatenaryMap.forEach((endPos, rigidCatenary) -> {
            if (!Util.isBetween(player.getX(), startPos.getX(), endPos.getX(), maxCatenaryDistance) || !Util.isBetween(player.getZ(), startPos.getZ(), endPos.getZ(), maxCatenaryDistance)) {
                return;
            }
            final UUID rigidCatenaryProduct = PathData.getRailProduct(startPos, endPos);
            if (renderedRigidCatenaryMap.containsKey(rigidCatenaryProduct)) {
                if (renderedRigidCatenaryMap.get(rigidCatenaryProduct)) {
                    return;
                }
            } else {
                renderedRigidCatenaryMap.put(rigidCatenaryProduct, true);
            }
            renderRigidCatenaryStandard(level, rigidCatenary);
        }));
        matrices.popPose();
        matrices.pushPose();
        final Map<UUID, Boolean> renderedTransCatenaryMap = new HashMap<>();
        MSDClientData.TRANS_CATENARIES.forEach((startPos, catenaryMap) -> catenaryMap.forEach((endPos, catenary) -> {
            if (!Util.isBetween(player.getX(), startPos.getX(), endPos.getX(), maxCatenaryDistance) || !Util.isBetween(player.getZ(), startPos.getZ(), endPos.getZ(), maxCatenaryDistance)) {
                return;
            }
            final UUID catenaryProduct = PathData.getRailProduct(startPos, endPos);
            if (renderedTransCatenaryMap.containsKey(catenaryProduct)) {
                if (renderedTransCatenaryMap.get(catenaryProduct)) {
                    return;
                }
            } else {
                renderedTransCatenaryMap.put(catenaryProduct, true);
            }
            if (catenary.catenaryType == CatenaryType.TRANS_ELECTRIC) {
                catenary.render((x1, y1, z1, x2, y2, z2, count, i, base, sinX, sinZ, increment) ->
                        IDrawing.drawLine(matrices, vertexConsumers, (float) x1, (float) y1 + 0.5F, (float) z1, (float) x2, (float) y2 + 0.5F, (float) z2, 0, 0, 0)
                );
            } else {
                renderTransCatenaryStandard(catenary);
            }
        }));
        matrices.popPose();
    }

    private static void renderCatenaryStandard(Catenary catenary) {
        renderCatenaryStandard(catenary, "msd:textures/block/overhead_line.png");
    }

    private static void renderRigidCatenaryStandard(Level world, RigidCatenary rigidCatenary) {
        renderRigidCatenaryStandard(world, rigidCatenary, "msd:textures/block/rigid_overhead_line.png");
    }

    private static void renderRigidSoftCatenaryStandard(Catenary catenary) {
        renderRigidSoftCatenaryStandard(catenary, "msd:textures/block/overhead_line.png");
    }

    private static void renderTransCatenaryStandard(TransCatenary catenary) {
        renderTransCatenaryStandard(catenary, "msd:textures/block/overhead_line.png");
    }

    private static void renderCatenaryStandard(Catenary catenary, String texture) {
        final int maxCatenaryDistance = UtilitiesClient.getRenderDistance() * 16;
        catenary.render((x1, y1, z1, x2, y2, z2, count, i, base, sinX, sinZ, increment) -> {
            final BlockPos pos3 = BlockUtil.newBlockPos(x1, y1, z1);
            if (MainRenderer.shouldNotRender(pos3, maxCatenaryDistance, null)) {
                return;
            }
            MainRenderer.scheduleRender(ResourceLocation.parse(texture), false, MainRenderer.QueuedRenderLayer.EXTERIOR, (matrices, vertexConsumer) -> {
                if (count < 8) {
                    IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x2, (float) y2 + 0.65F + (float) base, (float) z2, (float) x2, (float) y2, (float) z2, (float) x1, (float) y1, (float) z1, 0.0F, 0.0F, 1.0F, 1.0F, Direction.UP, -1, 0);
                    IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) y2 + 0.65F + (float) base, (float) z2, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2, 0.0F, 1.0F, 1.0F, 0.0F, Direction.UP, -1, 0);
                } else {
                    if (i < (count / 2 - increment)) {
                        IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x2, (float) y2 + 0.65F + (float) (base * 0.5), (float) z2, (float) x2, (float) y2, (float) z2, (float) x1, (float) y1, (float) z1, 0.0F, 0.0F, 1.0F, 1.0F, Direction.UP, -1, 0);
                        IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) y2 + 0.65F + (float) (base * 0.5), (float) z2, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2, 0.0F, 1.0F, 1.0F, 0.0F, Direction.UP, -1, 0);
                    } else if (i >= (count / 2)) {
                        IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x2, (float) y2 + 0.65F + (float) (base / 0.5), (float) z2, (float) x2, (float) y2, (float) z2, (float) x1, (float) y1, (float) z1, 0.0F, 0.0F, 1.0F, 1.0F, Direction.UP, -1, 0);
                        IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) y2 + 0.65F + (float) (base / 0.5), (float) z2, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2, 0.0F, 1.0F, 1.0F, 0.0F, Direction.UP, -1, 0);
                    } else {
                        IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x2, (float) y2 + 0.65F + (float) base, (float) z2, (float) x2, (float) y2, (float) z2, (float) x1, (float) y1, (float) z1, 0.0F, 0.0F, 1.0F, 1.0F, Direction.UP, -1, 0);
                        IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) y2 + 0.65F + (float) base, (float) z2, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2, 0.0F, 1.0F, 1.0F, 0.0F, Direction.UP, -1, 0);
                    }
                }
                IDrawing.drawTexture(matrices, vertexConsumer, (float) (x1 - sinX), (float) y1, (float) (z1 + sinZ), (float) (x2 - sinX), (float) y2, (float) (z2 + sinZ), (float) (x2 + sinX), (float) y2, (float) (z2 - sinZ), (float) (x1 + sinX), (float) y1, (float) (z1 - sinZ), 0.0F, 0.0F, 1.0F, 0.03125F, Direction.UP, -1, 0);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) (x2 - sinX), (float) y2, (float) (z2 + sinZ), (float) (x1 - sinX), (float) y1, (float) (z1 + sinZ), (float) (x1 + sinX), (float) y1, (float) (z1 - sinZ), (float) (x2 + sinX), (float) y2, (float) (z2 - sinZ), 0.0F, 0.03125F, 1.0F, 0.0F, Direction.UP, -1, 0);
            });
        });
    }

    private static void renderRigidCatenaryStandard(Level world, RigidCatenary rigidCatenary, String texture) {
        final int maxCatenaryDistance = UtilitiesClient.getRenderDistance() * 16;
        rigidCatenary.render((absx1, absz1, absx2, absz2, absx3, absz3, absx4, absz4, absx5, absz5, absx6, absz6, absx7, absz7, absx8, absz8, absy1, absy2) -> {
            final BlockPos pos3 = BlockUtil.newBlockPos(absx1, absy1, absz1);
            if (MainRenderer.shouldNotRender(pos3, maxCatenaryDistance, null)) {
                return;
            }

            final double x1 = 0;
            final double z1 = 0;
            final double x2 = absx2 - absx1;
            final double z2 = absz2 - absz1;
            final double x3 = absx3 - absx1;
            final double z3 = absz3 - absz1;
            final double x4 = absx4 - absx1;
            final double z4 = absz4 - absz1;
            final double x5 = absx5 - absx1;
            final double z5 = absz5 - absz1;
            final double x6 = absx6 - absx1;
            final double z6 = absz6 - absz1;
            final double x7 = absx7 - absx1;
            final double z7 = absz7 - absz1;
            final double x8 = absx8 - absx1;
            final double z8 = absz8 - absz1;
            final double y1 = 0;
            final double y2 = absy2 - absy1;

            final int light2 = LightTexture.pack(world.getBrightness(LightLayer.BLOCK, pos3), world.getBrightness(LightLayer.SKY, pos3));
            MainRenderer.scheduleRender(ResourceLocation.parse(texture), false, MainRenderer.QueuedRenderLayer.EXTERIOR, (matrices, vertexConsumer) -> {
                matrices.pushPose();
                MainRenderer.transformRelativeToCamera(matrices, absx1, absy1, absz1);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1, (float) z1, (float) x2, (float) y1, (float) z2, (float) x3, (float) y2, (float) z3, (float) x4, (float) y2, (float) z4, 0.0F, 0.0F, 1.0F, 0.03125F, Direction.UP, -1, 0);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) x3, (float) y2, (float) z3, (float) x2, (float) y1, (float) z2, (float) x1, (float) y1, (float) z1, (float) x4, (float) y2, (float) z4, 0.0F, 0.03125F, 1.0F, 0.0F, Direction.UP, -1, 0);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1, (float) z1, (float) x5, (float) y1 + 0.125F, (float) z5, (float) x8, (float) y2 + 0.125F, (float) z8, (float) x4, (float) y2, (float) z4, 0.0F, 0.5F, 1.0F, 1.0F, Direction.UP, -1, light2);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) x8, (float) y2 + 0.125F, (float) z8, (float) x5, (float) y1 + 0.125F, (float) z5, (float) x1, (float) y1, (float) z1, (float) x4, (float) y2, (float) z4, 0.0F, 1.0F, 1.0F, 0.5F, Direction.UP, -1, light2);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) y1, (float) z2, (float) x6, (float) y1 + 0.125F, (float) z6, (float) x7, (float) y2 + 0.125F, (float) z7, (float) x3, (float) y2, (float) z3, 0.0F, 0.5F, 1.0F, 1.0F, Direction.UP, -1, light2);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) x7, (float) y2 + 0.125F, (float) z7, (float) x6, (float) y1 + 0.125F, (float) z6, (float) x2, (float) y1, (float) z2, (float) x3, (float) y2, (float) z3, 0.0F, 1.0F, 1.0F, 0.5F, Direction.UP, -1, light2);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) x5, (float) y1 + 0.125F, (float) z5, (float) x6, (float) y1 + 0.125F, (float) z6, (float) x7, (float) y2 + 0.125F, (float) z7, (float) x8, (float) y2 + 0.125F, (float) z8, 0.0F, 0.5F, 1.0F, 1.0F, Direction.UP, -1, light2);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) x7, (float) y2 + 0.125F, (float) z7, (float) x6, (float) y1 + 0.125F, (float) z6, (float) x5, (float) y1 + 0.125F, (float) z5, (float) x8, (float) y2 + 0.125F, (float) z8, 0.0F, 1.0F, 1.0F, 0.5F, Direction.UP, -1, light2);
                matrices.popPose();
            });
        });
    }

    private static void renderRigidSoftCatenaryStandard(Catenary catenary, String texture) {
        final int maxCatenaryDistance = UtilitiesClient.getRenderDistance() * 16;
        catenary.render((absX1, absY1, absZ1, absX2, absY2, absZ2, count, i, base, sinX, sinZ, increment) -> {
            final BlockPos pos3 = BlockUtil.newBlockPos(absX1, absY1, absZ1);
            if (MainRenderer.shouldNotRender(pos3, maxCatenaryDistance, null)) {
                return;
            }

            final double x1 = 0;
            final double y1 = 0;
            final double z1 = 0;
            final double x2 = absX2 - absX1;
            final double y2 = absY2 - absY1;
            final double z2 = absZ2 - absZ1;

            MainRenderer.scheduleRender(ResourceLocation.parse(texture), false, MainRenderer.QueuedRenderLayer.EXTERIOR, (matrices, vertexConsumer) -> {
                matrices.pushPose();
                MainRenderer.transformRelativeToCamera(matrices, absX1, absY1, absZ1);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) (x1 - sinX), (float) y1, (float) (z1 + sinZ), (float) (x2 - sinX), (float) y2, (float) (z2 + sinZ), (float) (x2 + sinX), (float) y2, (float) (z2 - sinZ), (float) (x1 + sinX), (float) y1, (float) (z1 - sinZ), 0.0F, 0.0F, 1.0F, 0.03125F, Direction.UP, -1, 0);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) (x2 - sinX), (float) y2, (float) (z2 + sinZ), (float) (x1 - sinX), (float) y1, (float) (z1 + sinZ), (float) (x1 + sinX), (float) y1, (float) (z1 - sinZ), (float) (x2 + sinX), (float) y2, (float) (z2 - sinZ), 0.0F, 0.03125F, 1.0F, 0.0F, Direction.UP, -1, 0);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1 + 0.03125F, (float) z1, (float) x2, (float) y2 + 0.03125F, (float) z2, (float) x2, (float) y2, (float) z2, (float) x1, (float) y1, (float) z1, 0.0F, 0.0F, 1.0F, 0.03125F, Direction.UP, -1, 0);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) y2 + 0.03125F, (float) z2, (float) x1, (float) y1 + 0.03125F, (float) z1, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2, 0.0F, 0.03125F, 1.0F, 0.0F, Direction.UP, -1, 0);
                matrices.popPose();
            });
        });
    }

    private static void renderTransCatenaryStandard(TransCatenary catenary, String texture) {
        final int maxCatenaryDistance = UtilitiesClient.getRenderDistance() * 16;
        catenary.render((absX1, absY1, absZ1, absX2, absY2, absZ2, count, i, base, sinX, sinZ, increment) -> {
            final BlockPos pos3 = BlockUtil.newBlockPos(absX1, absY1, absZ1);
            if (MainRenderer.shouldNotRender(pos3, maxCatenaryDistance, null)) {
                return;
            }
            final double x1 = 0;
            final double y1 = 0;
            final double z1 = 0;
            final double x2 = absX2 - absX1;
            final double y2 = absY2 - absY1;
            final double z2 = absZ2 - absZ1;
            MainRenderer.scheduleRender(ResourceLocation.parse(texture), false, MainRenderer.QueuedRenderLayer.EXTERIOR, (matrices, vertexConsumer) -> {
                matrices.pushPose();
                MainRenderer.transformRelativeToCamera(matrices, absX1, absY1, absZ1);
                if (count < 8) {
                    IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x2, (float) y2 + 0.65F + (float) base, (float) z2, (float) x2, (float) y2, (float) z2, (float) x1, (float) y1, (float) z1, 0.0F, 0.0F, 1.0F, 1.0F, Direction.UP, -1, 0);
                    IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) y2 + 0.65F + (float) base, (float) z2, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2, 0.0F, 1.0F, 1.0F, 0.0F, Direction.UP, -1, 0);
                } else {
                    if (i < (count / 2 - increment)) {
                        IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x2, (float) y2 + 0.65F + (float) (base * 0.5), (float) z2, (float) x2, (float) y2, (float) z2, (float) x1, (float) y1, (float) z1, 0.0F, 0.0F, 1.0F, 1.0F, Direction.UP, -1, 0);
                        IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) y2 + 0.65F + (float) (base * 0.5), (float) z2, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2, 0.0F, 1.0F, 1.0F, 0.0F, Direction.UP, -1, 0);
                    } else if (i >= (count / 2)) {
                        IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x2, (float) y2 + 0.65F + (float) (base / 0.5), (float) z2, (float) x2, (float) y2, (float) z2, (float) x1, (float) y1, (float) z1, 0.0F, 0.0F, 1.0F, 1.0F, Direction.UP, -1, 0);
                        IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) y2 + 0.65F + (float) (base / 0.5), (float) z2, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2, 0.0F, 1.0F, 1.0F, 0.0F, Direction.UP, -1, 0);
                    } else {
                        IDrawing.drawTexture(matrices, vertexConsumer, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x2, (float) y2 + 0.65F + (float) base, (float) z2, (float) x2, (float) y2, (float) z2, (float) x1, (float) y1, (float) z1, 0.0F, 0.0F, 1.0F, 1.0F, Direction.UP, -1, 0);
                        IDrawing.drawTexture(matrices, vertexConsumer, (float) x2, (float) y2 + 0.65F + (float) base, (float) z2, (float) x1, (float) y1 + 0.65F + (float) base, (float) z1, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2, 0.0F, 1.0F, 1.0F, 0.0F, Direction.UP, -1, 0);
                    }
                }
                IDrawing.drawTexture(matrices, vertexConsumer, (float) (x1 - sinX), (float) y1, (float) (z1 + sinZ), (float) (x2 - sinX), (float) y2, (float) (z2 + sinZ), (float) (x2 + sinX), (float) y2, (float) (z2 - sinZ), (float) (x1 + sinX), (float) y1, (float) (z1 - sinZ), 0.0F, 0.0F, 1.0F, 0.03125F, Direction.UP, -1, 0);
                IDrawing.drawTexture(matrices, vertexConsumer, (float) (x2 - sinX), (float) y2, (float) (z2 + sinZ), (float) (x1 - sinX), (float) y1, (float) (z1 + sinZ), (float) (x1 + sinX), (float) y1, (float) (z1 - sinZ), (float) (x2 + sinX), (float) y2, (float) (z2 - sinZ), 0.0F, 0.03125F, 1.0F, 0.0F, Direction.UP, -1, 0);
                matrices.popPose();
            });
        });
    }
}