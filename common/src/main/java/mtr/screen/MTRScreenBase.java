package mtr.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MTRScreenBase extends Screen {
    protected Screen previousScreen = null;

    protected MTRScreenBase(Component title) {
        super(title);
    }

    public MTRScreenBase withPreviousScreen(Screen previousScreen) {
        this.previousScreen = previousScreen;
        return this;
    }

    @Override
    public void onClose() {
        super.onClose();
        minecraft.setScreen(previousScreen);
    }
}
