package com.lx862.mtrtdm;

import com.lx862.mtrtdm.data.ExposedTrainData;
import com.lx862.mtrtdm.mixin.SidingAccessorMixin;
import com.lx862.mtrtdm.mixin.TrainAccessorMixin;
import com.lx862.mtrtdm.mixin.TrainServerAccessorMixin;
import mtr.data.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static ExposedTrainData getNearestTrain(Level world, Vec3 playerPos) {
        List<ExposedTrainData> trainDataList = new ArrayList<>();
        RailwayData data = RailwayData.getInstance(world);

        /* Loop through each siding */
        for(Siding siding : data.sidings) {
            /* Loop through each train in each siding */
            for(TrainServer train : ((SidingAccessorMixin)siding).getTrains()) {
                final Vec3[] positions = new Vec3[train.trainCars + 1];

                /* Loop through each car in each train */
                for (int i = 0; i <= train.trainCars; i++) {
                    positions[i] = ((TrainAccessorMixin)train).getTheRoutePosition((((TrainAccessorMixin) train).getReversed() ? train.trainCars - i : i) * train.spacing);
                }

                trainDataList.add(new ExposedTrainData(train, ((TrainServerAccessorMixin)train).getRouteId(), positions, ((TrainAccessorMixin)train).getIsManualAllowed()));
            }
        }

        ExposedTrainData closestTrainCar = null;
        Vec3 closestPos = null;
        for(ExposedTrainData train : trainDataList) {
            /* Loop through every car */
            for(int i = 0; i < train.positions.length; i++) {
                /* First train found */
                if(closestTrainCar == null) {
                    closestTrainCar = train;
                    closestPos = train.positions[i];
                } else {
                    /* Compare if the train looped is closer to the player */
                    double lastTrainDistance = getManhattenDistance(closestPos, playerPos);
                    double thisTrainDistance = getManhattenDistance(train.positions[i], playerPos);
                    boolean isCloser = thisTrainDistance < lastTrainDistance;

                    if(isCloser) {
                        closestTrainCar = train;
                        closestPos = train.positions[i];
                    }
                }
            }
        }

        ExposedTrainData trainData = closestTrainCar;

        if(trainData == null) {
            return null;
        }

        if(trainData.isManual) {
            trainData.isCurrentlyManual = ((TrainAccessorMixin)trainData.train).getIsCurrentlyManual();
            if(trainData.isCurrentlyManual) {
                trainData.accelerationSign = ((TrainAccessorMixin) trainData.train).getManualNotch();
                trainData.ridingEntities = ((TrainAccessorMixin) trainData.train).getRidingEntities();
                trainData.manualCooldown = ((TrainServerAccessorMixin)trainData.train).getManualCoolDown();
                trainData.manualToAutomaticTime = ((TrainAccessorMixin) trainData.train).getManualToAutomaticTime();
            }
        }

        return trainData;
    }

    public static double getManhattenDistance(Vec3 pos1, Vec3 pos2) {
        return Math.abs(pos2.x() - pos1.x()) + Math.abs(pos2.y() - pos1.y()) + Math.abs(pos2.z() - pos1.z());
    }

    public static double getManhattenDistance(BlockPos pos1, BlockPos pos2) {
        return Math.abs(pos2.getX() - pos1.getX()) + Math.abs(pos2.getY() - pos1.getY()) + Math.abs(pos2.getZ() - pos1.getZ());
    }
}
