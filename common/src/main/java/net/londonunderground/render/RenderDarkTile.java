package net.londonunderground.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.Utilities;
import net.londonunderground.LondonUnderground;
import net.londonunderground.blocks.TunnelDarknessBlock;
import net.londonunderground.registry.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;

public class RenderDarkTile<T extends TunnelDarknessBlock.TileEntityTunnelDarkness> extends BlockEntityRendererMapper<T> {

	private static final TunnelEntityModel TUNNEL_ENTITY_MODEL = new TunnelEntityModel();

	public RenderDarkTile(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(T entity, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
		final LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) {
			return;
		}
		if (!Utilities.isHolding(player, item -> item == Blocks.TUNNEL_DARKNESS.get().asItem())) {
			if (entity.getBlockPos().distSqr(player.blockPosition()) < 8196) {
				return;
			}
		}


		poseStack.pushPose();
		poseStack.translate(0, 1.5, 0);
		final VertexConsumer vertexConsumer = bufferSource.getBuffer(TUNNEL_ENTITY_MODEL.renderType(LondonUnderground.id("textures/block/tunnel_darkness.png")));
		TUNNEL_ENTITY_MODEL.renderToBuffer(poseStack, vertexConsumer, light, overlay, 0xFFFFFFFF);
		poseStack.popPose();
	}

	@Override
	public boolean shouldRenderOffScreen(T blockEntity) {
		return true;
	}

	public int getViewDistance() {
		return 512;
	}
}
