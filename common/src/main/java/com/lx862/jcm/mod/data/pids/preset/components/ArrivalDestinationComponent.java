package com.lx862.jcm.mod.data.pids.preset.components;

import com.google.gson.JsonObject;
import com.lx862.jcm.mod.data.KVPair;
import com.lx862.jcm.mod.data.pids.preset.PIDSContext;
import com.lx862.jcm.mod.data.pids.preset.components.base.PIDSComponent;
import com.lx862.jcm.mod.data.pids.preset.components.base.TextComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import mtr.data.Route;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;

public class ArrivalDestinationComponent extends TextComponent {
    private final int arrivalIndex;
    public ArrivalDestinationComponent(double x, double y, double width, double height, KVPair additionalParam) {
        super(x, y, width, height, additionalParam);
        this.arrivalIndex = additionalParam.getInt("arrivalIndex", 0);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, Direction facing, PIDSContext context) {
        ObjectArrayList<ArrivalResponse> arrivals = context.arrivals;
        if(arrivalIndex >= arrivals.size()) return;

        ArrivalResponse arrival = arrivals.get(arrivalIndex);
        String routeNo = arrival.getRouteNumber().isEmpty() ? "" : arrival.getRouteNumber() + " ";
        String destinationString = cycleString(routeNo) + cycleString(arrival.getDestination());
        if(arrival.getCircularState() == Route.CircularState.CLOCKWISE) {
            destinationString = (isCjk(destinationString, false) ? TranslationProvider.GUI_MTR_CLOCKWISE_VIA_CJK : TranslationProvider.GUI_MTR_CLOCKWISE_VIA).getString(destinationString);
        } else if(arrival.getCircularState() == Route.CircularState.ANTICLOCKWISE) {
            destinationString = (isCjk(destinationString, false) ? TranslationProvider.GUI_MTR_ANTICLOCKWISE_VIA_CJK : TranslationProvider.GUI_MTR_ANTICLOCKWISE_VIA).getString(destinationString);
        }

        drawText(graphicsHolder, guiDrawing, facing, destinationString);
    }

    public static PIDSComponent parseComponent(double x, double y, double width, double height, JsonObject jsonObject) {
        return new ArrivalDestinationComponent(x, y, width, height, new KVPair(jsonObject));
    }
}
