package com.lx862.jcm.mod.render.block;

import com.lx862.jcm.mod.Constants;
import com.lx862.jcm.mod.block.entity.APGDoorDRLBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.MTR;
import mtr.MTRClient;
import mtr.block.BlockAPGGlass;
import mtr.block.BlockAPGGlassEnd;
import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.IBlock;
import mtr.data.IGui;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;
import mtr.mappings.UtilitiesClient;
import mtr.render.RenderTrains;
import mtr.render.StoredMatrixTransformations;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

/**
 * Copied from MTR, PSD/APG is a pain
 */
public class RenderDRLAPGDoor<T extends APGDoorDRLBlockEntity> extends BlockEntityRendererMapper<T> implements IGui, IBlock {
    private final int type;
    private static final ModelSingleCube MODEL_APG_TOP = new ModelSingleCube(34, 9, 0, 15, 1, 16, 8, 1);
    private static final ModelAPGDoorBottom MODEL_APG_BOTTOM = new ModelAPGDoorBottom();
    private static final ModelAPGDoorLight MODEL_APG_LIGHT = new ModelAPGDoorLight();
    private static final ModelSingleCube MODEL_APG_DOOR_LOCKED = new ModelSingleCube(6, 6, 5, 17, 1, 6, 6, 0);
    public RenderDRLAPGDoor(BlockEntityRenderDispatcher dispatcher, int type) {
        super(dispatcher);
        this.type = type;
    }

    @Override
    public boolean shouldRenderOffScreen(T blockEntity) {
        return true;
    }

    @Override
    public void render(T entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        final Level level = entity.getLevel();
        if (level == null) {
            return;
        }

        final BlockPos blockPos = entity.getBlockPos();
        final Direction facing = IBlock.getStatePropertySafe(level, blockPos, BlockPSDAPGDoorBase.FACING);
        final boolean side = IBlock.getStatePropertySafe(level, blockPos, BlockPSDAPGDoorBase.SIDE) == EnumSide.RIGHT;
        final boolean half = IBlock.getStatePropertySafe(level, blockPos, BlockPSDAPGDoorBase.HALF) == DoubleBlockHalf.UPPER;
        final boolean end = IBlock.getStatePropertySafe(level, blockPos, BlockPSDAPGDoorBase.END);
        final boolean unlocked = IBlock.getStatePropertySafe(level, blockPos, BlockPSDAPGDoorBase.UNLOCKED);
        final double open = Math.min(entity.getOpen(MTRClient.getLastFrameDuration()), type >= 3 ? 0.75F : 1);

        final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + blockPos.getX(), blockPos.getY(), 0.5 + blockPos.getZ());
        storedMatrixTransformations.add(matricesNew -> {
            matricesNew.translate(0.5 + entity.getBlockPos().getX(), entity.getBlockPos().getY(), 0.5 + entity.getBlockPos().getZ());
            UtilitiesClient.rotateYDegrees(matricesNew, -facing.toYRot());
            UtilitiesClient.rotateXDegrees(matricesNew, 180);
        });
        final StoredMatrixTransformations storedMatrixTransformationsLight = storedMatrixTransformations.copy();

        switch (type) {
            case 2:
                if (half) {
                    final Block block = level.getBlockState(blockPos.relative(side ? facing.getClockWise() : facing.getCounterClockWise())).getBlock();
                    if (block instanceof BlockAPGGlass || block instanceof BlockAPGGlassEnd) {
                        RenderTrains.scheduleRender(ResourceLocation.parse(String.format("mtr:textures/block/apg_door_light_%s.png", open > 0 ? "on" : "off")), false, open > 0 ? RenderTrains.QueuedRenderLayer.LIGHT_TRANSLUCENT : RenderTrains.QueuedRenderLayer.EXTERIOR, (poseStack1, vertexConsumer) -> {
                            storedMatrixTransformationsLight.transform(poseStack1);
                            poseStack1.translate(side ? -0.515625 : 0.515625, 0, 0);
                            poseStack1.scale(0.5F, 1, 1);
                            MODEL_APG_LIGHT.render(poseStack1, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
                            poseStack1.popPose(); // TODO: Really?
                        });
                    }
                }
                break;
        }

        storedMatrixTransformations.add(matricesNew -> matricesNew.translate(open * (side ? -1 : 1), 0, 0));

        switch (type) {
            case 2:
                RenderTrains.scheduleRender(Constants.id(String.format("textures/block/psdapg/drlapg/apg_door_%s_%s.png", half ? "top" : "bottom", side ? "right" : "left")), false, RenderTrains.QueuedRenderLayer.EXTERIOR, (poseStack1, vertexConsumer) -> {
                    storedMatrixTransformations.transform(poseStack1);
                    (half ? MODEL_APG_TOP : MODEL_APG_BOTTOM).render(graphicsHolderNew, light, overlay, 1, 1, 1, 1);
                    poseStack1.popPose(); // TODO: Needed?
                });
                if (half && !unlocked) {
                    RenderTrains.scheduleRender(MTR.id("textures/block/sign/door_not_in_use.png"), false, RenderTrains.QueuedRenderLayer.EXTERIOR, (poseStack1, vertexConsumer) -> {
                        storedMatrixTransformations.transform(poseStack1);
                        MODEL_APG_DOOR_LOCKED.render(graphicsHolderNew, light, overlay, 1, 1, 1, 1);
                        poseStack1.popPose(); // TODO: Needed?
                    });
                }
                break;
            case 4:
                if (IBlock.getStatePropertySafe(level, blockPos, TripleHorizontalBlock.CENTER)) {
                    break;
                }
                storedMatrixTransformations.add(matricesNew -> matricesNew.translate(side ? 0.5 : -0.5, 0, 0));
        }
    }

    private static class ModelSingleCube extends EntityModel<Entity> {

        private final ModelMapper cube;

        private ModelSingleCube(int textureWidth, int textureHeight, int x, int y, int z, int length, int height, int depth) {
            final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);
            cube = new ModelMapper(modelDataWrapper);
            cube.texOffs(0, 0).addBox(x - 8, y - 16, z - 8, length, height, depth, 0, false);
            cube.setModelPart();
        }

        @Override
        public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
            cube.render(poseStack, buffer, 0, 0, 0, packedLight, packedOverlay);
        }

        @Override
        public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        }
    }

    private static class ModelAPGDoorBottom extends EntityModel<Entity> {

        private final ModelMapper bone;

        private ModelAPGDoorBottom() {
            final int textureWidth = 34;
            final int textureHeight = 27;

            final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

            bone = new ModelMapper(modelDataWrapper);;
            bone.texOffs(0, 0).addBox(-8, -16, -7, 16, 16, 1, 0, false);
            bone.texOffs(0, 17).addBox(-8, -6, -8, 16, 6, 1, 0, false);

            final ModelMapper cube_r1 = new ModelMapper(modelDataWrapper);
            cube_r1.setPos(0, -6, -8);
            cube_r1.setRotationAngle(-0.7854F, 0, 0);
            cube_r1.texOffs(0, 24).addBox(-8, -2, 0, 16, 2, 1, 0, false);

            bone.setModelPart();
        }


        @Override
        public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
            bone.render(poseStack, buffer, 0, 0, 0, packedLight, packedOverlay);
        }

        @Override
        public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        }
    }

    private static class ModelAPGDoorLight extends EntityModel<Entity> {

        private final ModelMapper bone;

        private ModelAPGDoorLight() {
            super(8, 8);

            bone = createModelPart();
            bone.setTextureUVOffset(0, 4).addCuboid(-0.5F, -2, -7, 1, 1, 3, 0.05F, false);

            final ModelPartExtension cube_r1 = bone.addWidget();
            cube_r1.setPivot(0, -2.05F, -4.95F);
            cube_r1.setRotation(0.3927F, 0, 0);
            cube_r1.setTextureUVOffset(0, 0).addCuboid(-0.5F, 0.05F, -3.05F, 1, 1, 3, 0.05F, false);

            bone.setModelPart();
        }

        @Override
        public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        }

        @Override
        public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
            bone.render(poseStack, buffer, 0, 0, 0, packedLight, packedOverlay);
        }
    }
}