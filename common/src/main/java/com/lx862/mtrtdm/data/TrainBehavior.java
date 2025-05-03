package com.lx862.mtrtdm.data;

import java.util.ArrayList;
import java.util.List;

public class TrainBehavior {
    public List<String> routeName;
    public List<Long> routeId = new ArrayList<>();
    public TrainSound speedDropSound;
    public TrainSound speedRaiseSound;
    public String mainBossbarContent = "";
    public String overSpeedTitle = "";
    public int pathLookup = 5;
    public boolean restrictToRailSpeed;
    public boolean overSpeedDecelToZero;
    public boolean showMainBossbar;
    public boolean showLimitBossBar;
    public boolean showDwellBossBar;
    public double decelFactor = 1.5;
    public double drag = 0;
    public double slopeSpeedChange = 0;
    public int nextPathClearance = 3;
    public boolean incorrectStopPosAlarm = false;
    public TrainSound incorrectStopPosSound = null;
    public TrainSound overspeedSound = null;
    public TrainSound needBrakeSound = null;
    public TrainSound enterSidingSound = null;

    public TrainBehavior(List<String> routeName, boolean restrictToRailSpeed, boolean overSpeedDecelToZero, boolean incorrectStopPosAlarm, String incorrectStopPosSound, String speedDropSound, String speedRaiseSound, String mainBossbarContent, boolean showLimitBossBar, boolean showDwellBossBar) {
        this.routeName = routeName;
        this.restrictToRailSpeed = restrictToRailSpeed;
        this.overSpeedDecelToZero = overSpeedDecelToZero;
        this.mainBossbarContent = mainBossbarContent;
        this.showMainBossbar = !mainBossbarContent.isEmpty();
        this.showLimitBossBar = showLimitBossBar;
        this.incorrectStopPosAlarm = incorrectStopPosAlarm;
        this.showDwellBossBar = showDwellBossBar;
    }

    public TrainBehavior() {
    }
}