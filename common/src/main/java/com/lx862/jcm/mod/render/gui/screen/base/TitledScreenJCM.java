package com.lx862.jcm.mod.render.gui.screen.base;

import com.lx862.jcm.mod.util.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.net.URI;

public abstract class TitledScreenJCM extends TitledScreen {

    public TitledScreenJCM(Component title, boolean animatable) {
        super(title, animatable);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        super.renderBackground(guiGraphics, mouseX, mouseY, tickDelta);

        // TODO: Remove this on release
        guiGraphics.drawString(font, TextUtil.literal("NeoJCM Beta Release").setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)), width - font.width("NeoJCM Beta release") - 6, 6, 0xFFFFFFFF, true);
        guiGraphics.drawString(font, TextUtil.literal("Report issues here!").setStyle(Style.EMPTY.withUnderlined(true)), width - font.width("Report issues here!") - 6, 18, 0xFFFFFFFF, true);
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
}
