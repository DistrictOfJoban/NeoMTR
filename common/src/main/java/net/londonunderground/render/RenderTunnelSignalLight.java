package net.londonunderground.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.BlockEntityMapper;
import mtr.render.RenderSignalBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.Direction;

public class RenderTunnelSignalLight<T extends BlockEntityMapper> extends RenderSignalBase<T> implements IGui {

	private final boolean redOnTop;
	private final int proceedColor;

	public RenderTunnelSignalLight(BlockEntityRenderDispatcher dispatcher, boolean redOnTop, int proceedColor) {
		super(dispatcher, true, 2);
		this.redOnTop = redOnTop;
		this.proceedColor = proceedColor;
	}

	@Override
	protected void render(PoseStack matrices, MultiBufferSource vertexConsumers, VertexConsumer vertexConsumer, T entity, float tickDelta, Direction facing, int occupiedAspect, boolean isBackSide) {
		final float y = occupiedAspect > 0 == redOnTop ? 0.25F : 0.4375F;
		IDrawing.drawTexture(matrices, vertexConsumer, -0.25F, y, 0.3125F, -0.0625F, y + 0.1875F, 0.3125F, facing.getOpposite(), occupiedAspect > 0 ? 0xFFFF0000 : proceedColor, MAX_LIGHT_GLOWING);
	}
}
