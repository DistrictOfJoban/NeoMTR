package mtr.screen;

import mtr.data.Platform;
import mtr.data.TransportMode;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.registry.Networking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class PlatformScreen extends SavedRailScreenBase<Platform> {

	private static final Component DWELL_TIME_TEXT = Text.translatable("gui.mtr.dwell_time");

	public PlatformScreen(Platform savedRailBase, TransportMode transportMode, Screen dashboardScreen) {
		super(savedRailBase, transportMode, dashboardScreen, DWELL_TIME_TEXT);
	}

	@Override
	protected void init() {
		super.init();
		UtilitiesClient.setWidgetY(sliderDwellTimeMin, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING);
		UtilitiesClient.setWidgetY(sliderDwellTimeSec, SQUARE_SIZE * 5 / 2 + TEXT_FIELD_PADDING);
	}

	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		super.renderBackground(guiGraphics, mouseX, mouseY, delta);
		if (showScheduleControls) {
			guiGraphics.drawString(font, DWELL_TIME_TEXT, SQUARE_SIZE, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING + TEXT_PADDING, ARGB_WHITE);
		}
		guiGraphics.pose().translate(0, 0, 100);
	}

	@Override
	public void onClose() {
		final int minutes = sliderDwellTimeMin.getIntValue();
		final float second = sliderDwellTimeSec.getIntValue() / 2F;
		savedRailBase.setDwellTime((int) ((second + minutes * SECONDS_PER_MINUTE) * 2), packet -> PacketTrainDataGuiClient.sendUpdate(Networking.PACKET_UPDATE_PLATFORM, packet));
		super.onClose();
	}

	@Override
	protected String getNumberStringKey() {
		return "gui.mtr.platform_number";
	}

	@Override
	protected ResourceLocation getPacketIdentifier() {
		return Networking.PACKET_UPDATE_PLATFORM;
	}
}
