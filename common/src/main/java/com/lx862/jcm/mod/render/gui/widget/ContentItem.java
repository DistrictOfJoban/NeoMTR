package com.lx862.jcm.mod.render.gui.widget;

import com.lx862.jcm.mod.render.GuiHelper;
import com.lx862.jcm.mod.render.RenderHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

import static com.lx862.jcm.mod.render.gui.widget.ListViewWidget.ENTRY_PADDING;

/**
 * Represent a row in {@link ListViewWidget}
 */
public class ContentItem extends AbstractListItem {
    public DrawIconCallback drawIconCallback = null;
    public final MutableComponent title;
    public final AbstractWidget widget;

    public ContentItem(MutableComponent title, AbstractWidget widget, int height) {
        super(height);
        this.title = title;
        this.widget = widget;
    }

    public ContentItem(MutableComponent title, AbstractWidget widget) {
        this(title, widget, 22);
    }

    public ContentItem setIcon(ResourceLocation textureId) {
        this.drawIconCallback = (guiDrawing, startX, startY, width, height) -> GuiHelper.drawTexture(guiDrawing, textureId, startX, startY, width, height);
        return this;
    }

    public ContentItem setIconCallback(DrawIconCallback drawIconCallback) {
        this.drawIconCallback = drawIconCallback;
        return this;
    }

    public boolean hasIcon() {
        return this.drawIconCallback != null;
    }

    @FunctionalInterface
    public interface DrawIconCallback extends RenderHelper {
        void accept(GuiGraphics guiGraphics, int startX, int startY, int width, int height);
    }

    /* */
    public void draw(GuiGraphics guiGraphics, int entryX, int entryY, int width, int height, int mouseX, int mouseY, boolean widgetVisible, double elapsed, float tickDelta) {
        super.draw(guiGraphics, entryX, entryY, width, height, mouseX, mouseY, widgetVisible, elapsed, tickDelta);
        drawListEntry(guiGraphics, entryX, entryY, width, mouseX, mouseY, widgetVisible, elapsed, tickDelta);
    }

    @Override
    public boolean matchQuery(String searchTerm) {
        return Objects.equals(searchTerm, "") || (title != null && title.getString().contains(searchTerm));
    }

    @Override
    public void positionChanged(int entryX, int entryY) {
        if(widget != null) {
            int offsetY = (height - widget.getHeight()) / 2;
            widget.setX(entryX - widget.getWidth());
            widget.setY(entryY + offsetY);
        }
    }

    @Override
    public void hidden() {
        if(widget != null) {
            widget.visible = false;
        }
    }

    @Override
    public void shown() {
        if(widget != null) {
            widget.visible = true;
        }
    }

    private void drawListEntry(GuiGraphics guiGraphics, int entryX, int entryY, int width, int mouseX, int mouseY, boolean widgetVisible, double elapsed, float tickDelta) {
        if(title != null) drawListEntryDescription(guiGraphics, entryX, entryY, width, elapsed);

        if(widget != null) {
            widget.visible = widgetVisible;
            // We have to draw our widget (Right side) again after rendering the highlight so it doesn't get covered.
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, 0, 100);
            widget.render(guiGraphics, mouseX, mouseY, tickDelta);
            guiGraphics.pose().popPose();
        }
    }

    private void drawListEntryDescription(GuiGraphics guiGraphics, int entryX, int entryY, int width, double elapsed) {
        int textHeight = 9;
        int iconSize = hasIcon() ? height - ENTRY_PADDING : 0;
        int widgetWidth = widget == null ? 0 : widget.getWidth();
        int availableTextWidth = width - widgetWidth - ENTRY_PADDING - iconSize;
        int textY = (height / 2) - (textHeight / 2);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(entryX, entryY, 0);
        guiGraphics.pose().translate(ENTRY_PADDING, 0, 0);

        if(hasIcon()) {
            drawIconCallback.accept(guiGraphics, 0, ((height - iconSize) / 2), iconSize, iconSize);
            guiGraphics.pose().translate(iconSize + ENTRY_PADDING, 0, 0); // Shift the text to the right
        }

        GuiHelper.drawScrollableText(guiGraphics, title, elapsed, entryX + ENTRY_PADDING + iconSize, 0, textY, availableTextWidth - iconSize - ENTRY_PADDING - ENTRY_PADDING - ENTRY_PADDING, ARGB_WHITE, true);
        guiGraphics.pose().popPose();
    }
}
