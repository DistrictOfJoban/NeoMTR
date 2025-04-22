package net.londonunderground.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.mappings.BlockEntityRendererMapper;
import net.londonunderground.blocks.BlockRoundelBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;

public class RenderNameProjector<T extends BlockRoundelBase.TileEntityBlockRoundelBase> extends BlockEntityRendererMapper<T> {

	public RenderNameProjector(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		// TODO
	}
}
