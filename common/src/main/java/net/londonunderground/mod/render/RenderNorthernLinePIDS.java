package net.londonunderground.mod.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.MTRClient;
import mtr.block.BlockArrivalProjectorBase;
import mtr.block.BlockPIDSBaseHorizontal;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.Config;
import mtr.data.*;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.render.MainRenderer;
import net.londonunderground.mod.LUAddon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;

import java.util.*;

public class RenderNorthernLinePIDS<T extends BlockEntityMapper> extends BlockEntityRendererMapper<T> implements IGui {

	private final float scale;
	private final float totalScaledWidth;
	private final float destinationStart;
	private final float destinationMaxWidth;
	private final float platformMaxWidth;
	private final float arrivalMaxWidth;
	private final int maxArrivals;
	private final float maxHeight;
	private final float startX;
	private final float startY;
	private final float startZ;
	private final boolean rotate90;
	private final boolean renderArrivalNumber;
	private final int textColor;
	private final int firstTrainColor;
	private final boolean appendDotAfterMin;

	private static final int SWITCH_LANGUAGE_TICKS = 60;
	private static final int CAR_TEXT_COLOR = 0xFF0000;
	private static final int MAX_VIEW_DISTANCE = 64;


	public RenderNorthernLinePIDS(BlockEntityRenderDispatcher dispatcher, int maxArrivals, float startX, float startY, float startZ, float maxHeight, int maxWidth, boolean rotate90, boolean renderArrivalNumber, int textColor, int firstTrainColor, float textPadding, boolean appendDotAfterMin) {
		super(dispatcher);
		scale = 160 * maxArrivals / maxHeight * textPadding;
		totalScaledWidth = scale * maxWidth / 16;
		destinationStart = renderArrivalNumber ? scale * 2 / 16 : 0;
		destinationMaxWidth = totalScaledWidth * 0.7F;
		platformMaxWidth = 0;
		arrivalMaxWidth = totalScaledWidth - destinationStart - destinationMaxWidth - platformMaxWidth;
		this.maxArrivals = maxArrivals;
		this.maxHeight = maxHeight;
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		this.rotate90 = rotate90;
		this.renderArrivalNumber = renderArrivalNumber;
		this.textColor = textColor;
		this.firstTrainColor = firstTrainColor;
		this.appendDotAfterMin = appendDotAfterMin;
	}

	public RenderNorthernLinePIDS(BlockEntityRenderDispatcher dispatcher, int maxArrivals, float startX, float startY, float startZ, float maxHeight, int maxWidth, boolean rotate90, boolean renderArrivalNumber, int textColor, int firstTrainColor) {
		this(dispatcher, maxArrivals, startX, startY, startZ, maxHeight, maxWidth, rotate90, renderArrivalNumber, textColor, firstTrainColor, 1, false);
	}

	@Override
	public void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource bufferSource, int light, int overlay) {

		final Style style;
		if (Config.useMTRFont()) {
			style = Style.EMPTY.withFont(LUAddon.id("londonunderground"));
		} else {
			style = Style.EMPTY;
		}

		final BlockGetter world = entity.getLevel();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getBlockPos();
		final Direction facing = IBlock.getStatePropertySafe(world, pos, HorizontalDirectionalBlock.FACING);
		if (MainRenderer.shouldNotRender(pos, Math.min(MAX_VIEW_DISTANCE, MainRenderer.maxTrainRenderDistance), rotate90 ? null : facing)) {
			return;
		}

		final String[] customMessages = new String[maxArrivals];
		final boolean[] hideArrival = new boolean[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			if (entity instanceof BlockPIDSBaseHorizontal.TileEntityBlockPIDSBaseHorizontal) {
				customMessages[i] = ((BlockPIDSBaseHorizontal.TileEntityBlockPIDSBaseHorizontal) entity).getMessage(i);
				hideArrival[i] = ((BlockPIDSBaseHorizontal.TileEntityBlockPIDSBaseHorizontal) entity).getHideArrival(i);
			} else {
				customMessages[i] = "";
			}
		}

		try {
			final Set<ScheduleEntry> schedules;
			final Map<Long, String> platformIdToName = new HashMap<>();

			final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
			if (station == null) {
				return;
			}

			final Map<Long, Platform> platforms = ClientData.DATA_CACHE.requestStationIdToPlatforms(station.id);
			if (platforms.isEmpty()) {
				return;
			}

			final Set<Long> platformIds;
			final PIDSType renderType = PIDSType.PIDS;
			switch (renderType) {
				case ARRIVAL_PROJECTOR:
					if (entity instanceof BlockArrivalProjectorBase.TileEntityArrivalProjectorBase) {
						platformIds = ((BlockArrivalProjectorBase.TileEntityArrivalProjectorBase) entity).getPlatformIds();
					} else {
						platformIds = new HashSet<>();
					}
					break;
				case PIDS:
					final Set<Long> tempPlatformIds;
					if (entity instanceof BlockPIDSBaseHorizontal.TileEntityBlockPIDSBaseHorizontal) {
						tempPlatformIds = ((BlockPIDSBaseHorizontal.TileEntityBlockPIDSBaseHorizontal) entity).getPlatformIds();
					} else {
						tempPlatformIds = new HashSet<>();
					}
					platformIds = tempPlatformIds.isEmpty() ? Collections.singleton(entity instanceof BlockPIDSBaseHorizontal.TileEntityBlockPIDSBaseHorizontal ? ((BlockPIDSBaseHorizontal.TileEntityBlockPIDSBaseHorizontal) entity).getPlatformId(ClientData.PLATFORMS, ClientData.DATA_CACHE) : 0) : tempPlatformIds;
					break;
				default:
					platformIds = new HashSet<>();
			}

			schedules = new HashSet<>();
			platforms.values().forEach(platform -> {
				if (platformIds.isEmpty() || platformIds.contains(platform.id)) {
					final Set<ScheduleEntry> scheduleForPlatform = ClientData.SCHEDULES_FOR_PLATFORM.get(platform.id);
					if (scheduleForPlatform != null) {
						scheduleForPlatform.forEach(scheduleEntry -> {
							final Route route = ClientData.DATA_CACHE.routeIdMap.get(scheduleEntry.routeId);
							if (route != null && (renderType.showTerminatingPlatforms || scheduleEntry.currentStationIndex < route.platformIds.size() - 1)) {
								schedules.add(scheduleEntry);
								platformIdToName.put(platform.id, platform.name);
							}
						});
					}
				}
			});

			final List<ScheduleEntry> scheduleList = new ArrayList<>(schedules);
			Collections.sort(scheduleList);

			int maxCars = 0;
			int minCars = Integer.MAX_VALUE;
			for (final ScheduleEntry scheduleEntry : scheduleList) {
				final int trainCars = scheduleEntry.trainCars;
				if (trainCars > maxCars) {
					maxCars = trainCars;
				}
				if (trainCars < minCars) {
					minCars = trainCars;
				}
			}
			final boolean showCarLength = minCars != maxCars;
			final float carLengthMaxWidth = showCarLength ? scale * 6 / 16 : 0;

			for (int i = 0; i < maxArrivals; i++) {
				final int languageTicks = (int) Math.floor(MTRClient.getGameTick()) / SWITCH_LANGUAGE_TICKS;
				final String destinationString;
				final boolean useCustomMessage;
				final ScheduleEntry currentSchedule = i < scheduleList.size() ? scheduleList.get(i) : null;
				final Route route = currentSchedule == null ? null : ClientData.DATA_CACHE.routeIdMap.get(currentSchedule.routeId);

				if (i < scheduleList.size() && !hideArrival[i] && route != null) {
					final String[] destinationSplit = ClientData.DATA_CACHE.getFormattedRouteDestination(route, currentSchedule.currentStationIndex, "", MultipartName.Usage.PIDS_DEST).split("\\|");
					final boolean isLightRailRoute = route.isLightRailRoute;
					final String[] routeNumberSplit = route.lightRailRouteNumber.split("\\|");

					if (customMessages[i].isEmpty()) {
						destinationString = (isLightRailRoute ? routeNumberSplit[languageTicks % routeNumberSplit.length] + " " : "") + IGui.textOrUntitled(destinationSplit[languageTicks % destinationSplit.length]);
						useCustomMessage = false;
					} else {
						final String[] customMessageSplit = customMessages[i].split("\\|");
						final int destinationMaxIndex = Math.max(routeNumberSplit.length, destinationSplit.length);
						final int indexToUse = languageTicks % (destinationMaxIndex + customMessageSplit.length);

						if (indexToUse < destinationMaxIndex) {
							destinationString = (isLightRailRoute ? routeNumberSplit[languageTicks % routeNumberSplit.length] + " " : "") + IGui.textOrUntitled(destinationSplit[languageTicks % destinationSplit.length]);
							useCustomMessage = false;
						} else {
							destinationString = customMessageSplit[indexToUse - destinationMaxIndex];
							useCustomMessage = true;
						}
					}
				} else {
					final String[] destinationSplit = customMessages[i].split("\\|");
					destinationString = destinationSplit[languageTicks % destinationSplit.length];
					useCustomMessage = true;
				}

				matrices.pushPose();
				matrices.translate(0.5, 0, 0.5);
				UtilitiesClient.rotateYDegrees(matrices, (rotate90 ? 90 : 0) - facing.toYRot());
				UtilitiesClient.rotateZDegrees(matrices, 180);
				matrices.translate((startX - 8) / 16, -startY / 16 + i * maxHeight / maxArrivals / 16, (startZ - 8) / 16 - SMALL_OFFSET * 2);
				matrices.scale(1F / scale, 1F / scale, 1F / scale);

				final Font textRenderer = Minecraft.getInstance().font;

				if (useCustomMessage) {
					final FormattedCharSequence text4 = Text.literal(destinationString).setStyle(style).getVisualOrderText();
					final int destinationWidth = textRenderer.width(text4);
					if (destinationWidth > totalScaledWidth) {
						matrices.scale(totalScaledWidth / destinationWidth, 1, 1);
					}
					textRenderer.drawInBatch(text4, 0, 0, textColor, false, matrices.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, MAX_LIGHT_GLOWING);
				} else {
					final Component arrivalText;
					final int seconds = (int) ((currentSchedule.arrivalMillis - System.currentTimeMillis()) / 1000);
					final boolean isCJK = IGui.isCjk(destinationString);
					if (seconds >= 60) {
						arrivalText = Text.translatable(isCJK ? "gui.mtr.arrival_min_cjk" : "gui.mtr.arrival_min", seconds / 60).append(appendDotAfterMin && !isCJK ? "." : "");
					} else {
						arrivalText = seconds > 0 ? Text.translatable(isCJK ? "gui.mtr.arrival_sec_cjk" : "gui.londonunderground.arrival_sec", seconds).append(appendDotAfterMin && !isCJK ? "." : "") : null;
					}
					final Component carText = Text.translatable(isCJK ? "gui.mtr.arrival_car_cjk" : "gui.mtr.arrival_car", currentSchedule.trainCars);


					if (renderArrivalNumber) {
						final FormattedCharSequence text1 = Text.literal(String.valueOf(i + 1)).setStyle(style).getVisualOrderText();
						textRenderer.drawInBatch(text1, 0, 0, textColor, false, matrices.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, MAX_LIGHT_GLOWING);
					}

					final float newDestinationMaxWidth = destinationMaxWidth - carLengthMaxWidth;

					if (showCarLength) {
						final FormattedCharSequence text3 = Text.literal(carText.getString()).setStyle(style).getVisualOrderText();

						matrices.pushPose();
						matrices.translate(destinationStart + newDestinationMaxWidth + platformMaxWidth, 0, 0);
						final int carTextWidth = textRenderer.width(text3);
						if (carTextWidth > carLengthMaxWidth) {
							matrices.scale(carLengthMaxWidth / carTextWidth, 1, 1);
						}
						textRenderer.drawInBatch(text3, 0, 0, CAR_TEXT_COLOR, false, matrices.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, MAX_LIGHT_GLOWING);
						matrices.popPose();
					}

					final String destinationString2;
					if (currentSchedule.currentStationIndex == route.platformIds.size() - 1) {
						if (isCJK) {
							destinationString2 = "終止服務";
						} else {
							destinationString2 = "Terminating Here";
						}
					} else {
						destinationString2 = destinationString;
					}

					final FormattedCharSequence text4 = Text.literal(destinationString2).setStyle(style).getVisualOrderText();

					matrices.pushPose();
					matrices.translate(destinationStart, 0, 0);
					final int destinationWidth = textRenderer.width(text4);
					if (destinationWidth > newDestinationMaxWidth) {
						matrices.scale(newDestinationMaxWidth / destinationWidth, 1, 1);
					}

					textRenderer.drawInBatch(text4, 0, 0, seconds > 0 ? textColor : firstTrainColor, false, matrices.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, MAX_LIGHT_GLOWING);
					matrices.popPose();

					if (arrivalText != null) {
						final FormattedCharSequence text5 = Text.literal(arrivalText.getString()).setStyle(style).getVisualOrderText();


						matrices.pushPose();
						final int arrivalWidth = textRenderer.width(text5);
						if (arrivalWidth > arrivalMaxWidth) {
							matrices.translate(destinationStart + newDestinationMaxWidth + platformMaxWidth + carLengthMaxWidth, 0, 0);
							matrices.scale(arrivalMaxWidth / arrivalWidth, 1, 1);
						} else {
							matrices.translate(totalScaledWidth - arrivalWidth, 0, 0);
						}
						textRenderer.drawInBatch(text5, 0, 0, textColor, false, matrices.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, MAX_LIGHT_GLOWING);
						matrices.popPose();
					}
				}

				matrices.popPose();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}