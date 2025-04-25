package mtr.screen;

import mtr.MTR;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.gui.GuiGraphics;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public class FileUploaderScreen extends MTRScreenBase implements IGui {

	private final Consumer<List<Path>> filesCallback;

	public FileUploaderScreen(Consumer<List<Path>> filesCallback) {
		super(Text.literal(""));
		this.filesCallback = filesCallback;
	}

	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		super.renderBackground(guiGraphics, mouseX, mouseY, delta);
		try {
			guiGraphics.drawCenteredString(font, Text.translatable("gui.mtr.drag_file_to_upload"), width / 2, (height - TEXT_HEIGHT) / 2, ARGB_WHITE);
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
		guiGraphics.pose().translate(0, 0, 100);
	}

	@Override
	public void onFilesDrop(List<Path> paths) {
		filesCallback.accept(paths);
		onClose();
	}
}
