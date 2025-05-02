package com.lx862.mtrticket;

import net.minecraft.server.MinecraftServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TickManager {
    public static int ticks = 0;
    public static ConcurrentHashMap<Integer, Runnable> scheduleList = new ConcurrentHashMap<>();

    public static void schedule(int ticksAfter, Runnable callback) {
        scheduleList.put(ticks + ticksAfter, callback);
    }

    public static void onTick(MinecraftServer minecraftServer) {
        ticks++;
        for(Map.Entry<Integer, Runnable> entry : scheduleList.entrySet()) {
            if(ticks >= entry.getKey()) {
                scheduleList.remove(entry.getKey());
                entry.getValue().run();
            }
        }
    }
}