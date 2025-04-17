package com.lx862.jcm.mod.render;

import com.lx862.jcm.mod.JCMClient;
import com.lx862.jcm.mod.data.Pair;
import com.mojang.blaze3d.vertex.PoseStack;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public interface GuiHelper {
    int MAX_CONTENT_WIDTH = 400;
    int BOTTOM_ROW_MARGIN = 6;
    int MAX_BUTTON_WIDTH = 375;

    static void drawTexture(GuiGraphics guiDrawing, ResourceLocation identifier, double x, double y, double width, double height) {
        Pair<Float, Float> uv = JCMClient.getMcMetaManager().getUV(identifier);
        drawTexture(guiDrawing, identifier, x, y, width, height, 0, uv.getLeft(), 1, uv.getRight());
    }

    static void drawTexture(GuiGraphics guiDrawing, ResourceLocation identifier, double x, double y, double width, double height, float u1, float v1, float u2, float v2) {
        UtilitiesClient.beginDrawingTexture(identifier);
        guiDrawing.drawTexture(x, y, x + width, y + height, u1, v1, u2, v2);
        guiDrawing.finishDrawingTexture();
    }

    static void drawRectangle(GuiGraphics guiGraphics, double x, double y, double width, double height, int color) {
        guiGraphics.beginDrawingRectangle();
        guiGraphics.drawRectangle(x, y, x + width, y + height, color);
        guiGraphics.finishDrawingRectangle();
    }

    /**
     * Draw text that would shift back and fourth if there's not enough space to display
     * Similar to the scrollable text added in Minecraft 1.19.4
     * @param graphicsHolder Graphics holder
     * @param text The text to display
     * @param elapsed The time elapsed, this would dictate the scrolling animation speed
     * @param startX The start X where your text should be clipped. (Measure from the left edge of your window)
     * @param textX The text X that would be rendered
     * @param textY The text Y that would be rendered
     * @param maxW The maximum width allowed for your text
     * @param color Color of the text
     * @param shadow Whether text should be rendered with shadow
     */
    static void drawScrollableText(GuiGraphics graphicsHolder, MutableComponent text, double elapsed, int startX, int textX, int textY, int maxW, int color, boolean shadow) {
        Font font = Minecraft.getInstance().font;
        int textWidth = font.width(text);
        PoseStack ps = graphicsHolder.pose();

        if(textWidth > maxW) {
            double slideProgress = ((Math.sin(elapsed / 4)) / 2) + 0.5;
            ps.translate(-slideProgress * (textWidth - maxW), 0, 0);
            ClipStack.add(startX, 0, maxW, 500);
            graphicsHolder.drawString(font, text, textX, textY, color, shadow);
            ClipStack.pop();
        } else {
            graphicsHolder.drawString(font, text, textX, textY, color, shadow);
        }
    }

    /**
     * This clamps the inputted button width to {@link GuiHelper#MAX_BUTTON_WIDTH } to prevent button texture glitch on 1.16 if the button is too wide
     * @param width The desired button width
     * @return The clamped button width
     */
    static int getButtonWidth(double width) {
        return (int)Math.min(MAX_BUTTON_WIDTH, width);
    }
}
