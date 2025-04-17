package com.lx862.jcm.mod.render.gui.screen;

import com.lx862.jcm.mod.render.GuiHelper;
import com.lx862.jcm.mod.render.RenderHelper;
import com.lx862.jcm.mod.render.gui.screen.base.TitledScreen;
import com.lx862.jcm.mod.util.TextCategory;
import com.lx862.jcm.mod.util.TextUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;

public class TextureTextAtlasScreen extends TitledScreen implements RenderHelper, GuiHelper {
    public TextureTextAtlasScreen() {
        super(true);
    }

    @Override
    public MutableComponent getScreenTitle() {
        return TextUtil.literal("New Text Renderer");
    }

    @Override
    public MutableComponent getScreenSubtitle() {
        return TextUtil.literal("Viewing texture atlas");
    }

    @Override
    protected void init() {
        super.init();
        // 1.16 button texture will glitch out if the button is too wide, we need a clamp
        int btnWidth = GuiHelper.getButtonWidth(width * 0.6);

        ButtonWidgetExtension btn = new ButtonWidgetExtension(
                (width - btnWidth) / 2,
                height - 30,
                btnWidth,
                20,
                TextUtil.translatable("gui.done"),
                (b) -> onClose2()
        );

        addWidget(btn);
    }

    @Override
    public void drawBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        super.drawBackground(guiGraphics, mouseX, mouseY, tickDelta);
        drawTextureAtlas(guiGraphics);
    }

    private void drawTextureAtlas(GuiGraphics guiGraphics) {
        final PoseStack poseStack = guiGraphics.pose();
        int maxWidth = (int)(width * 0.75);
        int maxHeight = height - 90;

//        if(TextureTextRenderer.initialized()) {
//            double imgRatio = (double) TextureTextRenderer.getAtlasWidth() / TextureTextRenderer.getAtlasHeight();
//            double finalWidth = maxWidth;
//            double finalHeight = maxWidth / imgRatio;
//
//            if(finalHeight > maxHeight) {
//                finalWidth = maxHeight * imgRatio;
//                finalHeight = maxHeight;
//            }
//
//            finalWidth *= animationProgress;
//            finalHeight *= animationProgress;
//
//            int startX = (width - (int)finalWidth) / 2;
//            int startY = (height - (int)finalHeight) / 2;
//
//            GuiHelper.drawTexture(guiGraphics, TextureTextRenderer.getAtlasIdentifier(), startX, startY, (int)finalWidth, (int)finalHeight);
//        } else {
            poseStack.pushPose();
            poseStack.translate(width / 2.0, height / 2.0, 0);
            poseStack.scale((float)animationProgress, (float)animationProgress, (float)animationProgress);
            RenderHelper.drawCenteredText(guiGraphics, TextUtil.translatable(TextCategory.GUI, "atlas_config.not_initialized"), 0, 0, ARGB_WHITE);
            poseStack.popPose();
//        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
