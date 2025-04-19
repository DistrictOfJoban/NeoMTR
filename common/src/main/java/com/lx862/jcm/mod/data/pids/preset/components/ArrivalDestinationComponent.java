package com.lx862.jcm.mod.data.pids.preset.components;

import com.google.gson.JsonObject;
import com.lx862.jcm.mod.data.KVPair;
import com.lx862.jcm.mod.data.pids.preset.PIDSContext;
import com.lx862.jcm.mod.data.pids.preset.components.base.PIDSComponent;
import com.lx862.jcm.mod.data.pids.preset.components.base.TextComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.ClientData;
import mtr.data.MultipartName;
import mtr.data.Route;
import mtr.data.ScheduleEntry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;

import java.util.List;

public class ArrivalDestinationComponent extends TextComponent {
    private final int arrivalIndex;
    public ArrivalDestinationComponent(double x, double y, double width, double height, KVPair additionalParam) {
        super(x, y, width, height, additionalParam);
        this.arrivalIndex = additionalParam.getInt("arrivalIndex", 0);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, Direction facing, PIDSContext context) {
        List<ScheduleEntry> arrivals = context.arrivals;
        if(arrivalIndex >= arrivals.size()) return;

        ScheduleEntry arrival = arrivals.get(arrivalIndex);
        final Route route = ClientData.DATA_CACHE.routeIdMap.get(arrival.routeId);
        if(route == null) return;

        final String destination = ClientData.DATA_CACHE.getFormattedRouteDestination(route, arrival.currentStationIndex, "", MultipartName.Usage.PIDS_DEST);

        String routeNo = route.lightRailRouteNumber.isEmpty() ? "" : route.lightRailRouteNumber + " ";
        String destinationString = cycleString(routeNo) + cycleString(destination);
//        if(arrival.getCircularState() == Route.CircularState.CLOCKWISE) {
//            destinationString = (isCjk(destinationString, false) ? TranslationProvider.GUI_MTR_CLOCKWISE_VIA_CJK : TranslationProvider.GUI_MTR_CLOCKWISE_VIA).getString(destinationString);
//        } else if(arrival.getCircularState() == Route.CircularState.ANTICLOCKWISE) {
//            destinationString = (isCjk(destinationString, false) ? TranslationProvider.GUI_MTR_ANTICLOCKWISE_VIA_CJK : TranslationProvider.GUI_MTR_ANTICLOCKWISE_VIA).getString(destinationString);
//        }

        drawText(poseStack, bufferSource, destinationString);
    }

    public static PIDSComponent parseComponent(double x, double y, double width, double height, JsonObject jsonObject) {
        return new ArrivalDestinationComponent(x, y, width, height, new KVPair(jsonObject));
    }
}
