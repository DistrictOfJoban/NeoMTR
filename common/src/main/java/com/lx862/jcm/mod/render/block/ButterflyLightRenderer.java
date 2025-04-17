package com.lx862.jcm.mod.render.block;

import com.lx862.jcm.mod.Constants;
import com.lx862.jcm.mod.block.entity.ButterflyLightBlockEntity;
import com.lx862.jcm.mod.data.BlockProperties;
import com.lx862.jcm.mod.render.RenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.longs.LongImmutableList;
import mtr.MTRClient;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.data.RailwayData;
import mtr.data.ScheduleEntry;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ButterflyLightRenderer extends JCMBlockEntityRenderer<ButterflyLightBlockEntity> {
    private static final ResourceLocation BUTTERFLY_LIGHT_TEXTURE = Constants.id("textures/block/butterfly_light_dotmatrix.png");

    public ButterflyLightRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void renderCurated(ButterflyLightBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource bufferSource, Level world, BlockState state, BlockPos pos, float partialTick, int light, int packedOverlay) {
        int startBlinkingSeconds = blockEntity.getStartBlinkingSeconds();
        Direction facing = IBlock.getStatePropertySafe(state, BlockProperties.FACING);

        final long platformId = RailwayData.getClosePlatformId(ClientData.PLATFORMS, ClientData.DATA_CACHE, pos, 5, 3, 3);
        if (platformId == 0) {
            return;
        }

        List<ScheduleEntry> arrivals = ArrivalsCacheClient.INSTANCE.requestArrivals(LongImmutableList.of(platformId));
        ScheduleEntry firstArrival = Utilities.getElement(arrivals, 0);
        if (firstArrival == null) return;

        boolean arrived = firstArrival.getArrival() <= System.currentTimeMillis();
        long dwellLeft = firstArrival.getDeparture() - System.currentTimeMillis();
        if (!arrived) return;

        long secondsLeft = dwellLeft / 1000;

        if(secondsLeft <= startBlinkingSeconds && MTRClient.getGameTick() % 40 > 20) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.scale(1/16F, 1/16F, 1/16F);
            rotateToBlockDirection(poseStack, blockEntity);
            UtilitiesClient.rotateZDegrees(poseStack, 180);
            poseStack.translate(-8, 3, -2.05);

            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.beaconBeam(BUTTERFLY_LIGHT_TEXTURE, true));
            RenderHelper.drawTexture(poseStack, vertexConsumer, 2, 0, 4, 12, 5, facing, ARGB_WHITE, MAX_RENDER_LIGHT);
            poseStack.popPose();
        }
    }
}
