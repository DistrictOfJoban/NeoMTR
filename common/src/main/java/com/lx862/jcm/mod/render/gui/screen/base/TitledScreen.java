package com.lx862.jcm.mod.render.gui.screen.base;

import com.lx862.jcm.mod.Constants;
import com.lx862.jcm.mod.render.RenderHelper;
import com.lx862.jcm.mod.util.TextUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.net.URI;

public abstract class TitledScreen extends AnimatedScreen {
    public static final int TEXT_PADDING = 10;
    public static final int TITLE_SCALE = 2;
    protected double elapsed = 0;
    public TitledScreen(boolean animatable) {
        super(animatable);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        drawBackground(guiGraphics, mouseX, mouseY, tickDelta);
        drawTitle(guiGraphics);
        drawSubtitle(guiGraphics);

        // TODO: Remove this on release
        guiGraphics.drawString(font, TextUtil.literal("JCM Beta Release").setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)), width - font.width("JCM Beta release") - 6, 6, 0xFFFFFFFF, true);
        guiGraphics.drawString(font, TextUtil.literal("Report issues here!").setStyle(Style.EMPTY.withColor(ChatFormatting.UNDERLINE)), width - font.width("Report issues here!") - 6, 18, 0xFFFFFFFF, true);
        elapsed += tickDelta / Constants.MC_TICK_PER_SECOND;
        super.render(guiGraphics, mouseX, mouseY, tickDelta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x1 = width - font.width("Report issues here!") - 6;
        int x2 = width - 6;
        int y1 = 18;
        int y2 = 18 + 8;
        if(button == 0 && mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2) {
            ConfirmLinkScreen.confirmLinkNow(this, URI.create("https://github.com/DistrictOfJoban/Joban-Client-Mod/issues"), true);
//            new ClickableWidgetExtension(0, 0, 0, 0).playDownSound2(Minecraft.getInstance().getSoundManager());
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void drawBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
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

    public abstract MutableComponent getScreenTitle();
    public abstract MutableComponent getScreenSubtitle();
}
