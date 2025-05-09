package com.lx862.jcm.mod.data.pids.preset.components;

import com.google.gson.JsonObject;
import com.lx862.jcm.mod.data.KVPair;
import com.lx862.jcm.mod.data.pids.preset.PIDSContext;
import com.lx862.jcm.mod.data.pids.preset.components.base.PIDSComponent;
import com.lx862.jcm.mod.data.pids.preset.components.base.TextComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;

public class ClockComponent extends TextComponent {
    private final boolean use24h;
    private final boolean showHour;
    private final boolean showMin;
    private final boolean showAMPM;

    public ClockComponent(double x, double y, double width, double height, KVPair additionalParam) {
        super(x, y, width, height, additionalParam);
        this.use24h = additionalParam.get("use24h", true);
        this.showHour = additionalParam.get("showHour", true);
        this.showMin = additionalParam.get("showMin", true);
        this.showAMPM = additionalParam.get("showAMPM", false);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, Direction facing, PIDSContext context) {
        long timeNow = context.level.getDayTime() + 6000;
        long hours = timeNow / 1000;
        long minutes = (long)Math.floor((timeNow - (hours * 1000)) / 16.8);
        String str = "";
        if(showHour) {
            if(use24h) str += String.format("%02d", hours % 24);
            else str += hours % 12;
        }
        if(showHour && showMin) str += (":");
        if(showMin) str += String.format("%02d", minutes % 60);
        if(showMin && showAMPM) str += (" ");
        if(showAMPM) str += ((hours % 24 >= 12) ? "PM" : "AM");
        drawText(poseStack, bufferSource, str);
    }

    public static PIDSComponent parseComponent(double x, double y, double width, double height, JsonObject jsonObject) {
        return new ClockComponent(x, y, width, height, new KVPair(jsonObject));
    }
}
