package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.block.BlockLiftButtons;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.Lift;
import mtr.item.ItemLiftButtonsLinkModifier;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.Utilities;
import mtr.mappings.UtilitiesClient;
import mtr.util.BlockUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class RenderLiftButtons extends BlockEntityRendererMapper<BlockLiftButtons.TileEntityLiftButtons> implements IGui, IBlock {

	private static final int HOVER_COLOR = 0xFFFFAAAA;
	private static final ResourceLocation BUTTON_TEXTURE = ResourceLocation.parse("mtr:textures/block/lift_button.png");

	public RenderLiftButtons(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(BlockLiftButtons.TileEntityLiftButtons entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		final Level world = entity.getLevel();
		if (world == null) {
			return;
		}

		final Player player = Minecraft.getInstance().player;
		if (player == null) {
			return;
		}

		final BlockPos pos = entity.getBlockPos();
		if (MainRenderer.shouldNotRender(pos, MainRenderer.maxTrainRenderDistance, null)) {
			return;
		}

		final BlockState state = world.getBlockState(pos);
		final Direction facing = IBlock.getStatePropertySafe(state, HorizontalDirectionalBlock.FACING);
		final boolean holdingLinker = Utilities.isHolding(player, item -> item instanceof ItemLiftButtonsLinkModifier || Block.byItem(item) instanceof BlockLiftButtons);

		matrices.pushPose();
		matrices.translate(0.5, 0, 0.5);

		final boolean[] buttonStates = {false, false, false, false};
		final Map<BlockPos, Tuple<String, Lift.LiftDirection>> liftDisplays = new HashMap<>();
		final List<BlockPos> liftPositions = new ArrayList<>();
		entity.forEachTrackPosition(world, (trackPosition, trackFloorTileEntity) -> {
			renderLiftObjectLink(matrices, vertexConsumers, world, pos, trackPosition, facing, holdingLinker);

			ClientData.LIFTS.forEach(lift -> {
				if (lift.hasFloor(trackPosition)) {
					lift.hasUpDownButtonForFloor(trackPosition.getY(), buttonStates);
					if (lift.liftInstructions.containsInstruction(trackPosition.getY(), true)) {
						buttonStates[2] = true;
					}
					if (lift.liftInstructions.containsInstruction(trackPosition.getY(), false)) {
						buttonStates[3] = true;
					}

					final BlockPos liftPos = BlockUtil.newBlockPos(lift.getPositionX(), 0, lift.getPositionZ());
					liftPositions.add(liftPos);
					liftDisplays.put(liftPos, new Tuple<>(ClientData.DATA_CACHE.requestLiftFloorText(lift.getCurrentFloorBlockPos())[0], lift.getLiftDirection()));
				}
			});
		});
		liftPositions.sort(Comparator.comparingInt(checkPos -> facing.getStepX() * (checkPos.getZ() - pos.getZ()) - facing.getStepZ() * (checkPos.getX() - pos.getX())));

		final HitResult hitResult = Minecraft.getInstance().hitResult;
		final boolean lookingAtTopHalf;
		final boolean lookingAtBottomHalf;
		if (hitResult == null || !IBlock.getStatePropertySafe(state, BlockLiftButtons.UNLOCKED)) {
			lookingAtTopHalf = false;
			lookingAtBottomHalf = false;
		} else {
			final Vec3 hitLocation = hitResult.getLocation();
			final double hitX = hitLocation.x - Math.floor(hitLocation.x);
			final double hitY = hitLocation.y - Math.floor(hitLocation.y);
			final double hitZ = hitLocation.z - Math.floor(hitLocation.z);
			final boolean inBlock = hitX > 0 && hitY > 0 && hitZ > 0 && BlockUtil.newBlockPos(hitLocation).equals(pos);
			lookingAtTopHalf = inBlock && (!buttonStates[1] || hitY > 0.25 && hitY < 0.5);
			lookingAtBottomHalf = inBlock && (!buttonStates[0] || hitY < 0.25);
		}

		UtilitiesClient.rotateYDegrees(matrices, -facing.toYRot());
		matrices.translate(0, 0, 0.4375 - SMALL_OFFSET);

		if (buttonStates[0]) {
			final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(buttonStates[2] || lookingAtTopHalf ? MoreRenderLayers.getLight(BUTTON_TEXTURE, true) : MoreRenderLayers.getExterior(BUTTON_TEXTURE));
			IDrawing.drawTexture(matrices, vertexConsumer, -1.5F / 16, (buttonStates[1] ? 4.5F : 2.5F) / 16, 3F / 16, 3F / 16, 0, 1, 1, 0, facing, buttonStates[2] ? MainRenderer.LIFT_LIGHT_COLOR : lookingAtTopHalf ? HOVER_COLOR : ARGB_GRAY, light);
		}
		if (buttonStates[1]) {
			final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(buttonStates[3] || lookingAtBottomHalf ? MoreRenderLayers.getLight(BUTTON_TEXTURE, true) : MoreRenderLayers.getExterior(BUTTON_TEXTURE));
			IDrawing.drawTexture(matrices, vertexConsumer, -1.5F / 16, (buttonStates[0] ? 0.5F : 2.5F) / 16, 3F / 16, 3F / 16, 0, 0, 1, 1, facing, buttonStates[3] ? MainRenderer.LIFT_LIGHT_COLOR : lookingAtBottomHalf ? HOVER_COLOR : ARGB_GRAY, light);
		}

		final float maxWidth = Math.min(0.25F, 0.375F / liftPositions.size());
		UtilitiesClient.rotateZDegrees(matrices, 180);
		matrices.translate(maxWidth * (0.5 - liftPositions.size() / 2F), 0, 0);
		IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(ResourceLocation.parse("mtr:textures/block/black.png"))), -maxWidth / 2, -0.9375F, maxWidth * liftPositions.size(), 0.40625F, Direction.UP, light);
		matrices.translate(0, -0.875, -SMALL_OFFSET);

		liftPositions.forEach(liftPosition -> {
			final Tuple<String, Lift.LiftDirection> liftDisplay = liftDisplays.get(liftPosition);
			if (liftDisplay != null) {
				MainRenderer.renderLiftDisplay(matrices, vertexConsumers, pos, liftDisplay.getA(), liftDisplay.getB(), maxWidth, 0.3125F);
			}
			matrices.translate(maxWidth, 0, 0);
		});

		matrices.popPose();
	}

	public static void renderLiftObjectLink(PoseStack matrices, MultiBufferSource vertexConsumers, Level world, BlockPos pos, BlockPos trackPosition, Direction facing, boolean holdingLinker) {
		if (holdingLinker) {
			final Direction trackFacing = IBlock.getStatePropertySafe(world, trackPosition, HorizontalDirectionalBlock.FACING);
			IDrawing.drawLine(matrices, vertexConsumers, trackPosition.getX() - pos.getX() + trackFacing.getStepX() / 2F, trackPosition.getY() - pos.getY() + 0.5F, trackPosition.getZ() - pos.getZ() + trackFacing.getStepZ() / 2F, facing.getStepX() / 2F, 0.25F, facing.getStepZ() / 2F, 0xFF, 0xFF, 0xFF);
		}
	}
}
