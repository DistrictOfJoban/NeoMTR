package net.londonunderground.mod.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.Entity;

public class TunnelEntityModel extends EntityModel<Entity> {

	private final ModelMapper main;

	public TunnelEntityModel() {
		final int textureWidth = 16;
		final int textureHeight = 16;
		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);
		main = new ModelMapper(modelDataWrapper);
		main.setPos(0, 0, 0);

		main.texOffs(0, 0).addBox(0, 0, 0, 16, 16, 16, 0, false);
		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		main.setModelPart();
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
		main.render(poseStack, buffer, 0, 0, 0, packedLight, packedOverlay);
	}
}
