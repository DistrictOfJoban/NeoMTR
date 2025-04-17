package com.lx862.jcm.mod.data.pids.preset.components;

import com.google.gson.JsonObject;
import com.lx862.jcm.mod.data.KVPair;
import com.lx862.jcm.mod.data.pids.preset.PIDSContext;
import com.lx862.jcm.mod.data.pids.preset.components.base.PIDSComponent;
import com.lx862.jcm.mod.data.pids.preset.components.base.TextComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import mtr.data.IGui;
import mtr.data.Station;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;

public class StationNameComponent extends TextComponent {
    public StationNameComponent(double x, double y, double width, double height, KVPair additionalParam) {
        super(x, y, width, height, additionalParam);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, Direction facing, PIDSContext context) {
        if(context.pos == null) {
            drawText(graphicsHolder, guiDrawing, facing, "車站|Station");
        } else {
            final Station station = InitClient.findStation(context.pos);
            if(station == null) {
                drawText(graphicsHolder, guiDrawing, facing, IGui.textOrUntitled(""));
            } else {
                drawText(graphicsHolder, guiDrawing, facing, IGui.textOrUntitled(station.getName()));
            }
        }
    }

    public static PIDSComponent parseComponent(double x, double y, double width, double height, JsonObject jsonObject) {
        return new StationNameComponent(x, y, width, height, new KVPair(jsonObject));
    }
}
