package mtr.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.*;
import mtr.mappings.SelectableMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.mappings.WidgetMapper;
import mtr.util.BlockUtil;
import mtr.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;


public class WidgetMap implements WidgetMapper, SelectableMapper, GuiEventListener, IGui {

	private int x;
	private int y;
	private int width;
	private int height;
	private double scale;
	private double centerX;
	private double centerY;
	private Tuple<Integer, Integer> drawArea1, drawArea2;
	private EditState editState;
	private boolean showStations;

	private final TransportMode transportMode;
	private final OnDrawCorners onDrawCorners;
	private final Runnable onDrawCornersMouseRelease;
	private final Consumer<Long> onClickAddPlatformToRoute;
	private final BiFunction<Double, Double, Boolean> isRestrictedMouseArea;
	private final ClientLevel level;
	private final LocalPlayer player;
	private final Font font;
	private final Screen previousScreen;

	private static final int ARGB_BLUE = 0xFF4285F4;
	private static final int SCALE_UPPER_LIMIT = 64;
	private static final double SCALE_LOWER_LIMIT = 1 / 128D;

	public WidgetMap(TransportMode transportMode, OnDrawCorners onDrawCorners, Runnable onDrawCornersMouseRelease, Consumer<Long> onClickAddPlatformToRoute, Consumer<SavedRailBase> onClickEditSavedRail, BiFunction<Double, Double, Boolean> isRestrictedMouseArea, Screen previousScreen) {
		this.transportMode = transportMode;
		this.onDrawCorners = onDrawCorners;
		this.onDrawCornersMouseRelease = onDrawCornersMouseRelease;
		this.onClickAddPlatformToRoute = onClickAddPlatformToRoute;
		this.isRestrictedMouseArea = isRestrictedMouseArea;
		this.previousScreen = previousScreen;

		final Minecraft minecraftClient = Minecraft.getInstance();
		level = minecraftClient.level;
		player = minecraftClient.player;
		font = minecraftClient.font;
		if (player == null) {
			centerX = 0;
			centerY = 0;
		} else {
			centerX = player.getX();
			centerY = player.getZ();
		}
		scale = 1;
		setShowStations(true);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		// Draw background
		guiGraphics.fill(x, y, x + width, y + height, ARGB_BLACK);

		guiGraphics.enableScissor(x, y, x + width, y + height);

		final Tesselator tesselator = Tesselator.getInstance();
		final BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		final Tuple<Double, Double> mouseWorldPos = coordsToWorldPos((double) mouseX - x, mouseY - y);

		UtilitiesClient.beginDrawingRectangle(buffer);

		drawWorldMap(guiGraphics, buffer, level, scale);
		drawMTRAreas(guiGraphics, buffer, mouseWorldPos, mouseX, mouseY);
		drawPlayer(guiGraphics, buffer, player);

		RenderSystem.enableBlend();
		BufferUploader.drawWithShader(buffer.buildOrThrow());
		RenderSystem.disableBlend();

		if (scale >= 8) {
			if (showStations) {
				ClientData.DATA_CACHE.getPosToPlatforms(transportMode).forEach((platformPos, platforms) -> drawSavedRail(guiGraphics, platformPos, platforms));
			} else {
				ClientData.DATA_CACHE.getPosToSidings(transportMode).forEach((sidingPos, sidings) -> drawSavedRail(guiGraphics, sidingPos, sidings));
			}
		}

		final MultiBufferSource.BufferSource immediate = Minecraft.getInstance().renderBuffers().bufferSource();
		if (showStations) {
			for (final Station station : ClientData.STATIONS) {
				if (canDrawAreaText(station)) {
					final BlockPos pos = station.getCenter();
					final String stationString = String.format("%s|(%s)", station.name, Text.translatable("gui.mtr.zone_number", station.zone).getString());
					drawFromWorldCoords(pos.getX(), pos.getZ(), (x1, y1) -> IDrawing.drawStringWithFont(guiGraphics.pose(), font, immediate, stationString, x + x1.floatValue(), y + y1.floatValue(), MAX_LIGHT_GLOWING));
				}
			}
		} else {
			for (final Depot depot : ClientData.DEPOTS) {
				if (canDrawAreaText(depot)) {
					final BlockPos pos = depot.getCenter();
					drawFromWorldCoords(pos.getX(), pos.getZ(), (x1, y1) -> IDrawing.drawStringWithFont(guiGraphics.pose(), font, immediate, depot.name, x + x1.floatValue(), y + y1.floatValue(), MAX_LIGHT_GLOWING));
				}
			}
		}
		immediate.endBatch();
		drawMouseWorldCoordinates(guiGraphics, mouseWorldPos.getA(), mouseWorldPos.getB());
		guiGraphics.disableScissor();
	}

	private void drawWorldMap(GuiGraphics guiGraphics, BufferBuilder buffer, Level level, double scale) {
		final Tuple<Integer, Integer> topLeft = coordsToWorldPos(0, 0);
		final Tuple<Integer, Integer> bottomRight = coordsToWorldPos(width, height);
		final int increment = scale >= 1 ? 1 : (int) Math.ceil(1 / scale);
		for (int i = topLeft.getA(); i <= bottomRight.getA(); i += increment) {
			for (int j = topLeft.getB(); j <= bottomRight.getB(); j += increment) {
				if (level != null) {
					final int color = divideColorRGB(level.getBlockState(BlockUtil.newBlockPos(i, this.level.getHeight(Heightmap.Types.MOTION_BLOCKING, i, j) - 1, j)).getBlock().defaultMapColor().col, 2);
					if(color != 0) drawRectangleFromWorldCoords(guiGraphics.pose(), buffer, i, j, i + increment, j + increment, ARGB_BLACK | color);
				}
			}
		}
	}

	private void drawMTRAreas(GuiGraphics guiGraphics, BufferBuilder buffer, Tuple<Double, Double> mouseWorldPos, int mouseX, int mouseY) {
		Set<AreaBase> areasToDraw = getAreas();

		for(AreaBase areaBase : areasToDraw) {
			boolean isSelected = Screen.hasShiftDown() && areaBase.inArea((int)Math.round(mouseWorldPos.getA()), (int)Math.round(mouseWorldPos.getB()));
			if(isSelected) {
				Tuple<Integer, Integer> corner1WithBorder = new Tuple<>(areaBase.corner1.getA() + 2, areaBase.corner1.getB() - 2);
				Tuple<Integer, Integer> corner2WithBorder = new Tuple<>(areaBase.corner2.getA() - 2, areaBase.corner2.getB() + 2);
				drawRectangleFromWorldCoords(guiGraphics.pose(), buffer, corner1WithBorder, corner2WithBorder, ARGB_WHITE);
			}

			drawRectangleFromWorldCoords(guiGraphics.pose(), buffer, areaBase.corner1, areaBase.corner2, (isSelected ? ARGB_BLACK : ARGB_BLACK_TRANSLUCENT) + areaBase.color);

		}

		// Editing area
		if (editState == EditState.EDITING_AREA && drawArea1 != null && drawArea2 != null) {
			drawRectangleFromWorldCoords(guiGraphics.pose(), buffer, drawArea1, drawArea2, ARGB_WHITE_TRANSLUCENT);
		}
		drawMTRRails(guiGraphics, buffer, mouseWorldPos, mouseX, mouseY);
	}

	private void drawMTRRails(GuiGraphics guiGraphics, BufferBuilder buffer, Tuple<Double, Double> mouseWorldPos, int mouseX, int mouseY) {
		if (showStations) {
			ClientData.DATA_CACHE.getPosToPlatforms(transportMode).forEach((platformPos, platforms) -> drawRectangleFromWorldCoords(guiGraphics.pose(), buffer, platformPos.getX(), platformPos.getZ(), platformPos.getX() + 1, platformPos.getZ() + 1, 0x88FFFFFF));
			mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> {
				renderPlatformTooltip((Platform) savedRail, guiGraphics, mouseX, mouseY);
				drawRectangleFromWorldCoords(guiGraphics.pose(), buffer, x1, z1, x2, z2, 0xFFFFFFFF);
			}, true);
		} else {
			ClientData.DATA_CACHE.getPosToSidings(transportMode).forEach((sidingPos, sidings) -> drawRectangleFromWorldCoords(guiGraphics.pose(), buffer, sidingPos.getX(), sidingPos.getZ(), sidingPos.getX() + 1, sidingPos.getZ() + 1, 0x88FFFFFF));
			mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> {
				renderSidingTooltip((Siding)savedRail, guiGraphics, mouseX, mouseY);
				drawRectangleFromWorldCoords(guiGraphics.pose(), buffer, x1, z1, x2, z2, ARGB_WHITE);
			}, false);
		}
	}

	private void drawMouseWorldCoordinates(GuiGraphics guiGraphics, double mouseWorldX, double mouseWorldY) {
		final String mousePosText = String.format("(%.1f, %.1f)", mouseWorldX, mouseWorldY);
		guiGraphics.drawString(font, mousePosText, x + width - TEXT_PADDING - font.width(mousePosText), y + TEXT_PADDING, ARGB_WHITE);
	}

	private void drawPlayer(GuiGraphics guiGraphics, BufferBuilder buffer, Player player) {
		drawFromWorldCoords(player.getX(), player.getZ(), (x1, y1) -> {
			drawRectangle(guiGraphics.pose(), buffer, x1 - 2, y1 - 3, x1 + 2, y1 + 3, ARGB_WHITE);
			drawRectangle(guiGraphics.pose(), buffer, x1 - 3, y1 - 2, x1 + 3, y1 + 2, ARGB_WHITE);
			drawRectangle(guiGraphics.pose(), buffer, x1 - 2, y1 - 2, x1 + 2, y1 + 2, ARGB_BLUE);
		});
	}

	private void renderPlatformTooltip(Platform platform, GuiGraphics guiGraphics, int mouseX, int mouseY) {
		final MutableComponent platformText = Component.literal("Platform " + platform.name).withStyle(ChatFormatting.RED);
		final MutableComponent keyDwell = Component.literal("Dwell: ").withStyle(ChatFormatting.GOLD);
		final MutableComponent valueDwell = Component.literal((platform.getDwellTime() / 2d) + "s").withStyle(ChatFormatting.WHITE);
		final MutableComponent keyRouteVia = Component.literal("Route Via: ").withStyle(ChatFormatting.GOLD);

		final List<Component> tooltipComponents = new ArrayList<>();
		tooltipComponents.add(platformText);
		tooltipComponents.add(keyDwell.append(valueDwell));
		tooltipComponents.add(keyRouteVia);

		for (Route route : ClientData.ROUTES) {
			if (route.platformIds.stream().anyMatch(e -> e.platformId == platform.id)) {
				MutableComponent routeText = Component.literal(route.name.replace("|", " ")).withColor(ARGB_BLACK | route.color);
				if(route.isHidden) routeText.append(Component.literal(" (Hidden)").withStyle(ChatFormatting.GRAY));
				tooltipComponents.add(routeText);
			}
		}

		if(tooltipComponents.isEmpty()) {
			tooltipComponents.add(Component.literal("(None)"));
		}

		guiGraphics.renderComponentTooltip(font, tooltipComponents, mouseX, mouseY);
	}

	private void renderSidingTooltip(Siding siding, GuiGraphics guiGraphics, int mouseX, int mouseY) {
		final MutableComponent platformText = Component.literal("Siding " + siding.name).withStyle(ChatFormatting.YELLOW);
		final MutableComponent keyTrainType = Component.literal("Train type: ").withStyle(ChatFormatting.GOLD);
		final MutableComponent valueTrainType = Component.literal(siding.getTrainId()).withStyle(ChatFormatting.WHITE);
		final MutableComponent keyManual = Component.literal("Manual: ").withStyle(ChatFormatting.GOLD);
		final MutableComponent valueManual = Component.literal(siding.getIsManual() ? "Yes" : "No").withStyle(siding.getIsManual() ? ChatFormatting.GREEN : ChatFormatting.RED);

		final List<Component> tooltipComponents = new ArrayList<>();
		tooltipComponents.add(platformText);
		tooltipComponents.add(keyTrainType.append(valueTrainType));
		tooltipComponents.add(keyManual.append(valueManual));

		guiGraphics.renderComponentTooltip(font, tooltipComponents, mouseX, mouseY);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (editState == EditState.EDITING_AREA) {
			drawArea2 = coordsToWorldPos((int) Math.round(mouseX - x), (int) Math.round(mouseY - y));
			if (drawArea1.getA().equals(drawArea2.getA())) {
				drawArea2 = new Tuple<>(drawArea2.getA() + 1, drawArea2.getB());
			}
			if (drawArea1.getB().equals(drawArea2.getB())) {
				drawArea2 = new Tuple<>(drawArea2.getA(), drawArea2.getB() + 1);
			}
			onDrawCorners.onDrawCorners(drawArea1, drawArea2);
		} else {
			centerX -= deltaX / scale;
			centerY -= deltaY / scale;
		}
		return true;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (editState == EditState.EDITING_AREA) {
			onDrawCornersMouseRelease.run();
		}
		return true;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (isMouseOver(mouseX, mouseY)) {
			if (ClientData.hasPermission()) {
				if (editState == EditState.EDITING_AREA) {
					drawArea1 = coordsToWorldPos((int) (mouseX - x), (int) (mouseY - y));
					drawArea2 = null;
				} else if (editState == EditState.EDITING_ROUTE) {
					final Tuple<Double, Double> mouseWorldPos = coordsToWorldPos(mouseX - x, mouseY - y);
					mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> onClickAddPlatformToRoute.accept(savedRail.id), true);
				} else {
					final Tuple<Double, Double> mouseWorldPos = coordsToWorldPos(mouseX - x, mouseY - y);

					for(AreaBase areaBase : getAreas()) {
						boolean isSelected = Screen.hasShiftDown() && areaBase.inArea((int)Math.round(mouseWorldPos.getA()), (int)Math.round(mouseWorldPos.getB()));
						if(isSelected) {
							if(areaBase instanceof Station station) {
								Minecraft.getInstance().setScreen(new EditStationScreen(station, previousScreen));
							} else if(areaBase instanceof Depot depot) {
								Minecraft.getInstance().setScreen(new EditDepotScreen(depot, transportMode, previousScreen));
							}
						}
					}

					mouseOnSavedRail(mouseWorldPos, (savedRail, x1, z1, x2, z2) -> editSavedRail(savedRail), showStations);
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private void editSavedRail(SavedRailBase savedRail) {
		if (savedRail instanceof Platform) {
			UtilitiesClient.setScreen(Minecraft.getInstance(), new PlatformScreen((Platform) savedRail, transportMode, previousScreen));
		} else if (savedRail instanceof Siding) {
			UtilitiesClient.setScreen(Minecraft.getInstance(), new SidingScreen((Siding) savedRail, transportMode, previousScreen));
		}
	}

	private Set<AreaBase> getAreas() {
		HashSet<AreaBase> areas = new HashSet<>();
		if (showStations) {
			for (final Station station : ClientData.STATIONS) {
				if (AreaBase.nonNullCorners(station)) {
					areas.add(station);

				}
			}
		} else {
			for (final Depot depot : ClientData.DEPOTS) {
				if (depot.isTransportMode(transportMode) && AreaBase.nonNullCorners(depot)) {
					areas.add(depot);
				}
			}
		}
		return areas;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double amount) {
		final double oldScale = scale;
		if (oldScale > SCALE_LOWER_LIMIT && amount < 0) {
			centerX -= (mouseX - x - width / 2D) / scale;
			centerY -= (mouseY - y - height / 2D) / scale;
		}
		scale(amount);
		if (oldScale < SCALE_UPPER_LIMIT && amount > 0) {
			centerX += (mouseX - x - width / 2D) / scale;
			centerY += (mouseY - y - height / 2D) / scale;
		}
		return true;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height && !(mouseX >= x + width - SQUARE_SIZE * 10 && mouseY >= y + height - SQUARE_SIZE) && !isRestrictedMouseArea.apply(mouseX, mouseY);
	}

	public void setFocused(boolean focused) {
	}

	public boolean isFocused() {
		return false;
	}

	public EditState getEditState() {
		return editState;
	}

	public void setPositionAndSize(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void scale(double amount) {
		scale *= Math.pow(2, amount);
		scale = Mth.clamp(scale, SCALE_LOWER_LIMIT, SCALE_UPPER_LIMIT);
	}

	public void find(double x1, double z1, double x2, double z2) {
		centerX = (x1 + x2) / 2;
		centerY = (z1 + z2) / 2;
		scale = Math.max(2, scale);
	}

	public void find(BlockPos pos) {
		centerX = pos.getX();
		centerY = pos.getZ();
		scale = Math.max(8, scale);
	}

	public void startEditingArea(AreaBase editingArea) {
		editState = EditState.EDITING_AREA;
		drawArea1 = editingArea.corner1;
		drawArea2 = editingArea.corner2;
	}

	public void startEditingRoute() {
		editState = EditState.EDITING_ROUTE;
	}

	public void stopEditing() {
		editState = EditState.DEFAULT;
	}

	public void setShowStations(boolean showStations) {
		this.showStations = showStations;
	}

	private void mouseOnSavedRail(Tuple<Double, Double> mouseWorldPos, MouseOnSavedRailCallback mouseOnSavedRailCallback, boolean isPlatform) {
		try {
			(isPlatform ? ClientData.DATA_CACHE.getPosToPlatforms(transportMode) : ClientData.DATA_CACHE.getPosToSidings(transportMode)).forEach((savedRailPos, savedRails) -> {
				final int savedRailCount = savedRails.size();
				for (int i = 0; i < savedRailCount; i++) {
					final float left = savedRailPos.getX();
					final float right = savedRailPos.getX() + 1;
					final float top = savedRailPos.getZ() + (float) i / savedRailCount;
					final float bottom = savedRailPos.getZ() + (i + 1F) / savedRailCount;
					if (Util.isBetween(mouseWorldPos.getA(), left, right) && Util.isBetween(mouseWorldPos.getB(), top, bottom)) {
						mouseOnSavedRailCallback.mouseOnSavedRailCallback(savedRails.get(i), left, top, right, bottom);
					}
				}
			});
		} catch (ConcurrentModificationException ignored) {
		}
	}

	private Tuple<Integer, Integer> coordsToWorldPos(int mouseX, int mouseY) {
		final Tuple<Double, Double> worldPos = coordsToWorldPos((double) mouseX, mouseY);
		return new Tuple<>((int) Math.floor(worldPos.getA()), (int) Math.floor(worldPos.getB()));
	}

	private Tuple<Double, Double> coordsToWorldPos(double mouseX, double mouseY) {
		final double left = (mouseX - width / 2D) / scale + centerX;
		final double right = (mouseY - height / 2D) / scale + centerY;
		return new Tuple<>(left, right);
	}

	private void drawFromWorldCoords(double worldX, double worldZ, BiConsumer<Double, Double> callback) {
		final double coordsX = (worldX - centerX) * scale + width / 2D;
		final double coordsY = (worldZ - centerY) * scale + height / 2D;
		if (Util.isBetween(coordsX, 0, width) && Util.isBetween(coordsY, 0, height)) {
			callback.accept(coordsX, coordsY);
		}
	}

	private void drawRectangleFromWorldCoords(PoseStack matrices, BufferBuilder buffer, Tuple<Integer, Integer> corner1, Tuple<Integer, Integer> corner2, int color) {
		drawRectangleFromWorldCoords(matrices, buffer, corner1.getA(), corner1.getB(), corner2.getA(), corner2.getB(), color);
	}

	private void drawRectangleFromWorldCoords(PoseStack matrices, BufferBuilder buffer, double posX1, double posZ1, double posX2, double posZ2, int color) {
		final double x1 = (posX1 - centerX) * scale + width / 2D;
		final double z1 = (posZ1 - centerY) * scale + height / 2D;
		final double x2 = (posX2 - centerX) * scale + width / 2D;
		final double z2 = (posZ2 - centerY) * scale + height / 2D;
		drawRectangle(matrices, buffer, x1, z1, x2, z2, color);
	}

	private void drawRectangle(PoseStack matrices, BufferBuilder buffer, double xA, double yA, double xB, double yB, int color) {
		final double x1 = Math.min(xA, xB);
		final double y1 = Math.min(yA, yB);
		final double x2 = Math.max(xA, xB);
		final double y2 = Math.max(yA, yB);
		if (x1 < width && y1 < height && x2 >= 0 && y2 >= 0) {
			IDrawing.drawRectangle(matrices, buffer, x + Math.max(0, x1), y + Math.max(0, y1), x + Math.min(width, x2), y + Math.min(height, y2), color);
		}
	}

	private boolean canDrawAreaText(AreaBase areaBase) {
		return areaBase.getCenter() != null && scale >= 80F / Math.max(Math.abs(areaBase.corner1.getA() - areaBase.corner2.getA()), Math.abs(areaBase.corner1.getB() - areaBase.corner2.getB()));
	}

	private void drawSavedRail(GuiGraphics guiGraphics, BlockPos savedRailPos, List<? extends SavedRailBase> savedRails) {
		final int savedRailCount = savedRails.size();
		for (int i = 0; i < savedRailCount; i++) {
			final int index = i;
			drawFromWorldCoords(savedRailPos.getX() + 0.5, savedRailPos.getZ() + (i + 0.5) / savedRailCount, (x1, y1) -> guiGraphics.drawCenteredString(font, savedRails.get(index).name, x + x1.intValue(), y + y1.intValue() - TEXT_HEIGHT / 2, ARGB_WHITE));
		}
	}

	private static int divideColorRGB(int color, int amount) {
		final int r = ((color >> 16) & 0xFF) / amount;
		final int g = ((color >> 8) & 0xFF) / amount;
		final int b = (color & 0xFF) / amount;
		return (r << 16) + (g << 8) + b;
	}

	@FunctionalInterface
	public interface OnDrawCorners {
		void onDrawCorners(Tuple<Integer, Integer> corner1, Tuple<Integer, Integer> corner2);
	}

	@FunctionalInterface
	private interface MouseOnSavedRailCallback {
		void mouseOnSavedRailCallback(SavedRailBase savedRail, double x1, double z1, double x2, double z2);
	}

	public enum EditState {DEFAULT, EDITING_AREA, EDITING_ROUTE}
}
