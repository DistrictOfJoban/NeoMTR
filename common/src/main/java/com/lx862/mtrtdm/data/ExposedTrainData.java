package com.lx862.mtrtdm.data;

import mtr.data.TrainServer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.phys.Vec3;

import java.util.Set;
import java.util.UUID;

public class ExposedTrainData {
    public final TrainServer train;
    public final long routeId;
    public final Vec3[] positions;
    public final boolean isManual;
    public boolean isCurrentlyManual;
    public int accelerationSign;
    public int manualCooldown;
    public int manualToAutomaticTime;
    public Set<UUID> ridingEntities;

    public ExposedTrainData(TrainServer server, long routeId, Vec3[] positions, boolean isManual) {
        this.train = server;
        this.routeId = routeId;
        this.positions = positions;
        this.isManual = isManual;
    }
}
