package net.londonunderground.mod.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.Config;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.RailwayData;
import mtr.data.Station;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import net.londonunderground.mod.LUAddon;
import net.londonunderground.mod.blocks.BlockRoundelBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Locale;

public class RenderRoundel<T extends BlockRoundelBase.TileEntityBlockRoundelBase> extends BlockEntityRendererMapper<T> implements IGui, IDrawing {

	private final float maxWidth;
	private final float maxScale;
	private final float xOffset;
	private final float yOffset;
	private final float zOffset;
	private final float xTilt;
	private final int textColor;
	private final boolean isDoubleSided;
	private final String font;

	public RenderRoundel(BlockEntityRenderDispatcher dispatcher, float maxWidth, float maxScale, float xOffset, float yOffset, float zOffset, float xTilt, int textColor, boolean isDoubleSided, String font) {
		super(dispatcher);
		this.maxWidth = maxWidth;
		this.maxScale = maxScale;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.zOffset = zOffset;
		this.xTilt = xTilt;
		this.textColor = textColor;
		this.isDoubleSided = isDoubleSided;
		this.font = font;
	}

	private void render(PoseStack poseStack, MultiBufferSource bufferSource, Font font, MutableComponent roundelText, int textWidth, int light) {
		poseStack.pushPose();
		UtilitiesClient.rotateXDegrees(poseStack, xTilt);
		poseStack.translate(-xOffset, -yOffset, -zOffset - SMALL_OFFSET * 2);

		final float scale = Math.min((maxWidth) / textWidth, maxScale);
		poseStack.scale(scale, scale, scale);
		poseStack.translate(0, -3.5, 0);
		font.drawInBatch(roundelText, -textWidth / 2, 0, textColor, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, light);

		poseStack.popPose();
	}

	@Override
	public void render(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		final Style style = Config.useMTRFont() ? Style.EMPTY.withFont(LUAddon.id("johnston")) : Style.EMPTY;

		if (!blockEntity.shouldRender()) {
			return;
		}

		final Level world = blockEntity.getLevel();
		if (world == null) {
			return;
		}

		final BlockPos pos = blockEntity.getBlockPos();
		final BlockState state = world.getBlockState(pos);
		final Direction facing = IBlock.getStatePropertySafe(state, BlockRoundelBase.FACING);

		final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
		final MutableComponent roundelText = Text.literal(IGui.textOrUntitled(IGui.formatStationName(station == null ? "" : station.name)).toUpperCase(Locale.ROOT)).setStyle(style);
		final int textWidth = Minecraft.getInstance().font.width(roundelText);

		poseStack.pushPose();
		poseStack.translate(0.5, 0.5, 0.5);
		UtilitiesClient.rotateYDegrees(poseStack, -facing.toYRot());
		UtilitiesClient.rotateZDegrees(poseStack, 180);
		render(poseStack, bufferSource, Minecraft.getInstance().font, roundelText, textWidth, packedLight);
		if (isDoubleSided) {
			UtilitiesClient.rotateYDegrees(poseStack, 180);
			render(poseStack, bufferSource, Minecraft.getInstance().font, roundelText, textWidth, packedLight);
		}
		poseStack.popPose();
	}
}
