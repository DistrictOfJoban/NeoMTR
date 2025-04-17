package com.lx862.jcm.mod.data.pids.preset.components;

import com.lx862.jcm.mod.data.KVPair;
import com.lx862.jcm.mod.data.pids.preset.PIDSContext;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;

/**
 * A CustomTextureComponent that automatically tints the color to the route's color of the arriving entry.
 * Will not render if there's no arrival for the speified arrivalIndex.
 */
public class ArrivalTextureComponent extends CustomTextureComponent {
    private final int arrivalIndex;
    public ArrivalTextureComponent(double x, double y, double width, double height, KVPair additionalParam) {
        super(x, y, width, height, additionalParam);
        this.arrivalIndex = additionalParam.getInt("arrivalIndex", 0);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, Direction facing, PIDSContext context) {
        ObjectArrayList<ArrivalResponse> arrivals = context.arrivals;
        if(arrivalIndex >= arrivals.size()) return;

        ArrivalResponse arrival = arrivals.get(arrivalIndex);
        drawTexture(graphicsHolder, guiDrawing, facing, texture, 0, 0, width, height, arrival.getRouteColor() + ARGB_BLACK);
    }
}
