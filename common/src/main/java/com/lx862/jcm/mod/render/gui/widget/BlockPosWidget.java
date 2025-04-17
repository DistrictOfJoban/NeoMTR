package com.lx862.jcm.mod.render.gui.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.core.BlockPos;

import java.util.function.Consumer;

public class BlockPosWidget extends AbstractWidget implements WidgetsWrapper {
    private final CoordTextField posXTextField;
    private final CoordTextField posYTextField;
    private final CoordTextField posZTextField;
    public BlockPosWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.posXTextField = new CoordTextField(0, 0, width / 3, height, -29999999, 29999999, 0);
        this.posYTextField = new CoordTextField(width / 3, 0, width / 3, height, -29999999, 29999999, 0);
        this.posZTextField = new CoordTextField((width / 3) * 2, 0, width / 3, height, -29999999, 29999999, 0);

        this.posXTextField.setChangedListener2(this::setPosition);
        this.posYTextField.setChangedListener2(this::setPosition);
        this.posZTextField.setChangedListener2(this::setPosition);
    }

    @Override
    public void setAllX(int newX) {
        super.setX(newX);
        positionWidgets();
    }

    @Override
    public void setAllY(int newY) {
        super.setY(newY);
        positionWidgets();
    }

    @Override
    public void setActiveMapped(boolean active) {
        this.posXTextField.setEditable2(active);
        this.posYTextField.setEditable2(active);
        this.posZTextField.setEditable2(active);
        this.posXTextField.active = active;
        this.posYTextField.active = active;
        this.posZTextField.active = active;
        super.active = active;
    }

    @Override
    public void setVisibleMapped(boolean visible) {
        this.posXTextField.setVisible(visible);
        this.posYTextField.setVisible(visible);
        this.posZTextField.setVisible(visible);
        super.active = visible;
    }

    public void positionWidgets() {
        int perWidth = getWidth() / 3;
        this.posXTextField.setX(getX());
        this.posYTextField.setX(getX() + perWidth);
        this.posZTextField.setX(getX() + perWidth + perWidth);
        this.posXTextField.setY(getY());
        this.posYTextField.setY(getY());
        this.posZTextField.setY(getY());
        this.posXTextField.setWidth(perWidth);
        this.posYTextField.setWidth(perWidth);
        this.posZTextField.setWidth(perWidth);
    }

    public void addWidget(Consumer<ClickableWidget> callback) {
        callback.accept(new ClickableWidget(posXTextField));
        callback.accept(new ClickableWidget(posYTextField));
        callback.accept(new ClickableWidget(posZTextField));
    }

    public void setBlockPos(BlockPos newPos) {
        this.posXTextField.setValue(newPos.getX());
        this.posYTextField.setValue(newPos.getY());
        this.posZTextField.setValue(newPos.getZ());
    }

    public BlockPos getBlockPos() {
        return new BlockPos(posXTextField.getNumber(), posYTextField.getNumber(), posZTextField.getNumber());
    }

    private void setPosition(String str) {
        str = str.trim();
        String[] strSplit = str.split("\\s+");

        if(!str.isEmpty() && strSplit.length > 1) {
            for(int i = 0; i < strSplit.length; i++) {
                CoordTextField field = null;
                if(i == 0) field = posXTextField;
                if(i == 1) field = posYTextField;
                if(i == 2) field = posZTextField;

                try {
                    int parsed = Integer.parseInt(strSplit[i]);
                    if(field != null) field.setValue(parsed);
                } catch (NumberFormatException ignored) {
                }
            }
        }
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.posXTextField.render(guiGraphics, mouseX, mouseY, partialTick);
        this.posYTextField.render(guiGraphics, mouseX, mouseY, partialTick);
        this.posZTextField.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
