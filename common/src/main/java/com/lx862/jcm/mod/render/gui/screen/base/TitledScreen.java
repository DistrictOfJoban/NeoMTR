package com.lx862.jcm.mod.render.gui.screen.base;

import com.lx862.jcm.mod.Constants;
import com.lx862.jcm.mod.render.gui.GuiHelper;
import com.lx862.jcm.mod.render.RenderHelper;
import com.lx862.jcm.mod.util.TextUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.net.URI;

public abstract class TitledScreen extends AnimatedScreen {
    public static final int TEXT_PADDING = 10;
    public static final int TITLE_SCALE = 2;
    protected double elapsed = 0;
    protected boolean showJCMWarning;

    public TitledScreen(boolean animatable) {
        this(animatable, true);
    }

    public TitledScreen(boolean animatable, boolean showJCMWarning) {
        super(animatable);
        this.showJCMWarning = showJCMWarning;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        drawCustomBackground(guiGraphics, mouseX, mouseY, tickDelta);
        drawTitle(guiGraphics);
        drawSubtitle(guiGraphics);

        // Draw darkened header for non-animated screen.
        if(!shouldAnimate) {
            guiGraphics.fill(0, 0, width, getStartY(), 0x66000000);

            RenderSystem.enableBlend();
            GuiHelper.drawTexture(guiGraphics, HEADER_SEPARATOR, 0, getStartY(), width, 2);
            RenderSystem.disableBlend();
        }

        if(showJCMWarning) {
            // TODO: Remove this on release
            guiGraphics.drawString(font, TextUtil.literal("NeoJCM Beta Release").setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)), width - font.width("NeoJCM Beta release") - 6, 6, 0xFFFFFFFF, true);
            guiGraphics.drawString(font, TextUtil.literal("Report issues here!").setStyle(Style.EMPTY.withUnderlined(true)), width - font.width("Report issues here!") - 6, 18, 0xFFFFFFFF, true);
        }

        elapsed += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() / Constants.MC_TICK_PER_SECOND;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean clickedResult = super.mouseClicked(mouseX, mouseY, button);
        int x1 = width - font.width("Report issues here!") - 6;
        int x2 = width - 6;
        int y1 = 18;
        int y2 = 18 + 8;
        if(button == 0 && mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2) {
            ConfirmLinkScreen.confirmLinkNow(this, URI.create("https://github.com/DistrictOfJoban/NeoMTR/issues"), true);
            return true;
        }
        return clickedResult;
    }

    public void drawCustomBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        super.renderBackground(guiGraphics, mouseX, mouseY, tickDelta);
    }

    private void drawTitle(GuiGraphics guiGraphics) {
        int titleHeight = (RenderHelper.lineHeight * TITLE_SCALE);
        MutableComponent titleText = getScreenTitle();
        final PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(width / 2.0, TEXT_PADDING, 0);
        poseStack.translate(0, -((titleHeight + TEXT_PADDING) * (1 - animationProgress)), 0);
        poseStack.scale(TITLE_SCALE, TITLE_SCALE, TITLE_SCALE);
        RenderHelper.scaleToFit(poseStack, font.width(titleText), width / (float)TITLE_SCALE, true);
        guiGraphics.drawCenteredString(font, titleText, 0, 0, 0xFFFFFFFF);
        poseStack.popPose();
    }

    private void drawSubtitle(GuiGraphics guiGraphics) {
        double titleHeight = (RenderHelper.lineHeight * TITLE_SCALE);
        MutableComponent subtitleText = getScreenSubtitle();
        final PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(width / 2.0, titleHeight * animationProgress, 0);
        poseStack.translate(0, TEXT_PADDING * 1.5, 0);
        RenderHelper.scaleToFit(poseStack, font.width(subtitleText), width, true);
        guiGraphics.drawCenteredString(font, subtitleText, 0, 0, 0xFFFFFFFF);
        poseStack.popPose();
    }

    /**
     * @return Return the Y coordinate that is below the title and subtitle
     */
    protected int getStartY() {
        double titleHeight = RenderHelper.lineHeight * TITLE_SCALE;
        double subtitleHeight = font.lineHeight + (TEXT_PADDING);
        return TEXT_PADDING + (int)(titleHeight + subtitleHeight);
    }

    public abstract MutableComponent getScreenTitle();
    public abstract MutableComponent getScreenSubtitle();
}
