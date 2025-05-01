package mtr.screen;

import com.lx862.jcm.mod.render.gui.screen.base.TitledScreen;
import io.netty.buffer.Unpooled;
import mtr.MTR;
import mtr.loader.MTRRegistryClient;
import mtr.client.ClientData;
import mtr.data.IGui;
import mtr.data.NameColorDataBase;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.registry.Networking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public class RailActionsScreen extends TitledScreen implements IGui {

	final DashboardList railActionsList;
	final int MAX_WIDTH = 360;

	public RailActionsScreen() {
		super(Text.translatable("gui.mtr.rail_actions"), false);
		railActionsList = new DashboardList(null, null, null, null, null, this::onDelete, null, () -> "", text -> {
		});
	}

	@Override
	protected void init() {
		super.init();
		railActionsList.width = Math.min(MAX_WIDTH, width - SQUARE_SIZE * 2);
		railActionsList.height = height - getStartY() - SQUARE_SIZE * 3;
		railActionsList.x = (width - railActionsList.width) / 2;
		railActionsList.y = getStartY() + (SQUARE_SIZE / 2);

		railActionsList.init(this::addRenderableWidget);

		Button closeButton = Button.builder(Component.translatable("gui.done"), (btn) -> onClose())
				.pos((width - 235) / 2, height - 30)
				.size(235, 20).build();

		addRenderableWidget(closeButton);
	}

	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		super.renderBackground(guiGraphics, mouseX, mouseY, delta);
		railActionsList.render(guiGraphics, font);
		guiGraphics.pose().translate(0, 0, 100);
	}

	@Override
	public Component getScreenSubtitle() {
		return Component.translatable("gui.mtr.rail_actions.subtitle", ClientData.RAIL_ACTIONS.size());
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		railActionsList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double amount) {
		railActionsList.mouseScrolled(mouseX, mouseY, scrollX, amount);
		return super.mouseScrolled(mouseX, mouseY, scrollX, amount);
	}

	@Override
	public void tick() {
		railActionsList.tick();
		railActionsList.setData(ClientData.RAIL_ACTIONS, false, false, false, false, false, true);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private void onDelete(NameColorDataBase data, int index) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeLong(data.id);
		MTRRegistryClient.sendToServer(Networking.PACKET_REMOVE_RAIL_ACTION, packet);
	}
}
