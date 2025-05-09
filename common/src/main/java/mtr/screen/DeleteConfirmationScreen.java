package mtr.screen;

import mtr.MTR;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;

public class DeleteConfirmationScreen extends MTRScreenBase implements IGui {

	private final Runnable deleteCallback;
	private final String name;
	private final Button buttonYes;
	private final Button buttonNo;

	private static final int BUTTON_WIDTH = 100;
	private static final int BUTTON_HALF_PADDING = 10;

	public DeleteConfirmationScreen(Runnable deleteCallback, String name) {
		super(Text.literal(""));

		this.deleteCallback = deleteCallback;
		this.name = name;

		buttonYes = UtilitiesClient.newButton(Text.translatable("gui.yes"), button -> onYes());
		buttonNo = UtilitiesClient.newButton(Text.translatable("gui.no"), button -> onNo());
	}

	@Override
	protected void init() {
		super.init();
		IDrawing.setPositionAndWidth(buttonYes, width / 2 - BUTTON_WIDTH - BUTTON_HALF_PADDING, height / 2, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonNo, width / 2 + BUTTON_HALF_PADDING, height / 2, BUTTON_WIDTH);
		addRenderableWidget(buttonYes);
		addRenderableWidget(buttonNo);
	}

	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		super.renderBackground(guiGraphics, mouseX, mouseY, delta);
		try {
			guiGraphics.drawCenteredString(font, Text.translatable("gui.mtr.delete_confirmation", IGui.formatMTRLanguageName(name)), width / 2, height / 2 - SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE);
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
		guiGraphics.pose().translate(0, 0, 100);
	}

	private void onYes() {
		deleteCallback.run();
		onClose();
	}

	private void onNo() {
		onClose();
	}
}
