package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.MTR;
import mtr.MTRClient;
import mtr.client.TrainClientRegistry;
import mtr.client.TrainProperties;
import mtr.data.IGui;
import mtr.data.Train;
import mtr.data.TrainClient;
import mtr.data.TransportMode;
import mtr.mappings.UtilitiesClient;
import mtr.model.ModelBogie;
import mtr.model.ModelCableCarGrip;
import mtr.model.ModelTrainBase;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public class JonModelTrainRenderer extends TrainRendererBase implements IGui {

	private final TrainClient train;

	public final ModelTrainBase model;
	public final String textureId;
	public final String gangwayConnectionId;
	public final String trainBarrierId;

	private static final EntityModel<Minecart> MODEL_MINECART = UtilitiesClient.getMinecartModel();
	private static final EntityModel<Boat> MODEL_BOAT = UtilitiesClient.getBoatModel();
	private static final Map<Long, FakeBoat> BOATS = new HashMap<>();
	private static final ModelCableCarGrip MODEL_CABLE_CAR_GRIP = new ModelCableCarGrip();
	private static final ModelBogie MODEL_BOGIE = new ModelBogie();

	private JonModelTrainRenderer(ModelTrainBase model, String textureId, String gangwayConnectionId, String trainBarrierId, TrainClient train) {
		this.model = model;
		this.textureId = textureId;
		this.gangwayConnectionId = gangwayConnectionId;
		this.trainBarrierId = trainBarrierId;
		this.train = train;
	}

	public JonModelTrainRenderer(ModelTrainBase model, String textureId, String gangwayConnectionId, String trainBarrierId) {
		this(model, resolvePath(textureId), resolvePath(gangwayConnectionId), resolvePath(trainBarrierId), null);
	}

	@Override
	public TrainRendererBase createTrainInstance(TrainClient train) {
		return new JonModelTrainRenderer(model, textureId, gangwayConnectionId, trainBarrierId, train);
	}

	@Override
	public void renderCar(int carIndex, double x, double y, double z, float yaw, float pitch, float roll, boolean doorLeftOpen, boolean doorRightOpen) {
		final float doorLeftValue = doorLeftOpen ? train.getDoorValue() : 0;
		final float doorRightValue = doorRightOpen ? train.getDoorValue() : 0;
		final boolean atPlatform = train.path.get(train.getIndex(0, train.spacing, true)).dwellTime > 0;
		final boolean hasPitch = pitch < 0 ? train.transportMode.hasPitchAscending : train.transportMode.hasPitchDescending;

		final String trainId = train.trainId;
		final TrainProperties trainProperties = TrainClientRegistry.getTrainProperties(trainId);

		if (model == null && isTranslucentBatch) {
			return;
		}

		final BlockPos posAverage = applyAverageTransform(x, y, z);
		if (posAverage == null) {
			return;
		}

		matrices.pushPose();
		applyTransform(train, x, y, z, yaw, pitch, roll, true);

		final int light = LightTexture.pack(world.getBrightness(LightLayer.BLOCK, posAverage), world.getBrightness(LightLayer.SKY, posAverage));

		if (model == null || textureId == null) {
			final boolean isBoat = train.transportMode == TransportMode.BOAT;

			matrices.translate(0, isBoat ? 0.875 : 0.5, 0);
			UtilitiesClient.rotateYDegrees(matrices, 90);

			final EntityModel<? extends Entity> model = isBoat ? MODEL_BOAT : MODEL_MINECART;
			final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.renderType(resolveTexture(textureId, textureId -> textureId + ".png")));

			if (isBoat) {
				if (!BOATS.containsKey(train.id)) {
					BOATS.put(train.id, new FakeBoat());
				}
				MODEL_BOAT.setupAnim(BOATS.get(train.id), (train.getSpeed() + Train.ACCELERATION_DEFAULT) * (doorLeftValue == 0 && doorRightValue == 0 ? lastFrameDuration : 0), 0, -0.1F, 0, 0);
			} else {
				model.setupAnim(null, 0, 0, -0.1F, 0, 0);
			}

			model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
		} else if (!textureId.isEmpty()) {
			final boolean renderDetails = MTRClient.isReplayMod() || posAverage.distSqr(camera.getBlockPosition()) <= MainRenderer.DETAIL_RADIUS_SQUARED;
			model.render(matrices, vertexConsumers, train, resolveTexture(textureId, textureId -> textureId + ".png"), light, doorLeftValue, doorRightValue, train.isDoorOpening(), carIndex, train.trainCars, !train.isReversed(), train.getIsOnRoute(), isTranslucentBatch, renderDetails, atPlatform);

			if (trainProperties.bogiePosition != 0 && !isTranslucentBatch) {
				if (trainProperties.isJacobsBogie) {
					if (carIndex == 0) {
						MODEL_BOGIE.render(matrices, vertexConsumers, light, train.trainCars == 1 ? 0 : -(int) (trainProperties.bogiePosition * 16));
					} else if (carIndex == train.trainCars - 1) {
						MODEL_BOGIE.render(matrices, vertexConsumers, light, (int) (trainProperties.bogiePosition * 16));
					}
				} else {
					MODEL_BOGIE.render(matrices, vertexConsumers, light, (int) (trainProperties.bogiePosition * 16));
					MODEL_BOGIE.render(matrices, vertexConsumers, light, -(int) (trainProperties.bogiePosition * 16));
				}
			}
		}

		if (train.transportMode == TransportMode.CABLE_CAR && !isTranslucentBatch) {
			matrices.translate(0, TransportMode.CABLE_CAR.railOffset + 0.5, 0);
			if (!hasPitch) {
				UtilitiesClient.rotateX(matrices, pitch);
			}
			if (trainId.endsWith("_rht")) {
				UtilitiesClient.rotateYDegrees(matrices, 180);
			}
			MODEL_CABLE_CAR_GRIP.render(matrices, vertexConsumers, light);
		}

		matrices.popPose();
		matrices.popPose();
	}

	@Override
	public void renderConnection(Vec3 prevPos1, Vec3 prevPos2, Vec3 prevPos3, Vec3 prevPos4, Vec3 thisPos1, Vec3 thisPos2, Vec3 thisPos3, Vec3 thisPos4, double x, double y, double z, float yaw, float pitch, float roll) {
		final BlockPos posAverage = applyAverageTransformLocal(x, y, z);
		if (posAverage == null) {
			return;
		}

		final String trainId = train.trainId;
		final TrainProperties trainProperties = TrainClientRegistry.getTrainProperties(trainId);

		final int light = LightTexture.pack(world.getBrightness(LightLayer.BLOCK, posAverage), world.getBrightness(LightLayer.SKY, posAverage));

		if (!gangwayConnectionId.isEmpty()) {
			matrices.pushPose();
//			MainRenderer.transformRelativeToCamera(matrices, x, y, z);
			final VertexConsumer vertexConsumerExterior = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(getConnectorTextureString(true, "exterior")));
			drawTexture(matrices, vertexConsumerExterior, thisPos2, prevPos3, prevPos4, thisPos1, light);
			drawTexture(matrices, vertexConsumerExterior, prevPos2, thisPos3, thisPos4, prevPos1, light);
			drawTexture(matrices, vertexConsumerExterior, prevPos3, thisPos2, thisPos3, prevPos2, light);
			drawTexture(matrices, vertexConsumerExterior, prevPos1, thisPos4, thisPos1, prevPos4, light);

			final int lightOnLevel = train.getIsOnRoute() ? MainRenderer.MAX_LIGHT_INTERIOR : light;
			final VertexConsumer vertexConsumerSide = vertexConsumers.getBuffer(MoreRenderLayers.getInterior(getConnectorTextureString(true, "side")));
			drawTexture(matrices, vertexConsumerSide, thisPos3, prevPos2, prevPos1, thisPos4, lightOnLevel);
			drawTexture(matrices, vertexConsumerSide, prevPos3, thisPos2, thisPos1, prevPos4, lightOnLevel);
			drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getInterior(getConnectorTextureString(true, "roof"))), prevPos2, thisPos3, thisPos2, prevPos3, lightOnLevel);
			drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getInterior(getConnectorTextureString(true, "floor"))), prevPos4, thisPos1, thisPos4, prevPos1, lightOnLevel);
			matrices.popPose();
		}

		if (trainProperties.isJacobsBogie) {
			matrices.pushPose();
			applyTransform(train, x, y, z, yaw, pitch, roll, true);
			MODEL_BOGIE.render(matrices, vertexConsumers, light, 0);
			matrices.popPose();
		}

		matrices.popPose();
	}

	@Override
	public void renderBarrier(Vec3 prevPos1, Vec3 prevPos2, Vec3 prevPos3, Vec3 prevPos4, Vec3 thisPos1, Vec3 thisPos2, Vec3 thisPos3, Vec3 thisPos4, double x, double y, double z, float yaw, float pitch, float roll) {
		if (StringUtils.isEmpty(trainBarrierId)) {
			return;
		}

		final BlockPos posAverage = applyAverageTransformLocal(x, y, z);
		if (posAverage == null) {
			return;
		}

		final int light = LightTexture.pack(world.getBrightness(LightLayer.BLOCK, posAverage), world.getBrightness(LightLayer.SKY, posAverage));

		final VertexConsumer vertexConsumerExterior = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(getConnectorTextureString(false, "exterior")));
		drawTexture(matrices, vertexConsumerExterior, thisPos2, prevPos3, prevPos4, thisPos1, light);
		drawTexture(matrices, vertexConsumerExterior, prevPos2, thisPos3, thisPos4, prevPos1, light);
		drawTexture(matrices, vertexConsumerExterior, thisPos3, prevPos2, prevPos1, thisPos4, light);
		drawTexture(matrices, vertexConsumerExterior, prevPos3, thisPos2, thisPos1, prevPos4, light);

		matrices.popPose();
	}

	public ResourceLocation resolveTexture(String textureId, Function<String, String> formatter) {
		final String textureString = formatter.apply(textureId);
		final ResourceLocation id = ResourceLocation.parse(textureString);
		final boolean available;

		if (!MainRenderer.AVAILABLE_TEXTURES.contains(textureString) && !MainRenderer.UNAVAILABLE_TEXTURES.contains(textureString)) {
			available = UtilitiesClient.hasResource(id);
			(available ? MainRenderer.AVAILABLE_TEXTURES : MainRenderer.UNAVAILABLE_TEXTURES).add(textureString);
			if (!available) {
				MTR.LOGGER.warn("[NeoMTR] Texture {} not found, using default", textureString);
			}
		} else {
			available = MainRenderer.AVAILABLE_TEXTURES.contains(textureString);
		}

		if (available) {
			return id;
		} else {
			final TrainRendererBase baseRenderer = TrainClientRegistry.getTrainProperties(train.baseTrainType).renderer;
			return ResourceLocation.parse((!(baseRenderer instanceof JonModelTrainRenderer) ? "mtr:textures/block/transparent.png" : formatter.apply(((JonModelTrainRenderer) baseRenderer).textureId)));
		}
	}

	private ResourceLocation getConnectorTextureString(boolean isConnector, String partName) {
		return resolveTexture(isConnector ? gangwayConnectionId : trainBarrierId, textureId -> String.format("%s_%s_%s.png", textureId, isConnector ? "connector" : "barrier", partName));
	}

	private static void drawTexture(PoseStack matrices, VertexConsumer vertexConsumer, Vec3 pos1, Vec3 pos2, Vec3 pos3, Vec3 pos4, int light) {
		mtr.client.IDrawing.drawTexture(matrices, vertexConsumer, (float) pos1.x, (float) pos1.y, (float) pos1.z, (float) pos2.x, (float) pos2.y, (float) pos2.z, (float) pos3.x, (float) pos3.y, (float) pos3.z, (float) pos4.x, (float) pos4.y, (float) pos4.z, 0, 0, 1, 1, Direction.UP, -1, light);
	}

	private static String resolvePath(String path) {
		return path == null ? null : path.toLowerCase(Locale.ENGLISH).split("\\.png")[0];
	}

	private static class FakeBoat extends Boat {

		private float progress;

		public FakeBoat() {
			super(EntityType.BOAT, null);
		}

		@Override
		public float getRowingTime(int paddle, float newProgress) {
			progress += newProgress;
			return progress;
		}
	}
}
