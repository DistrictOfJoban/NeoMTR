package com.lx862.mtrsurveyor.util;

import net.minecraft.util.Tuple;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.Locale;

public class Util {
    public static BoundingBox getBoundingBox(Tuple<Integer, Integer> corner1, Tuple<Integer, Integer> corner2) {
        int a1 = Math.min(corner1.getA(), corner2.getA());
        int a2 = Math.min(corner1.getB(), corner2.getB());
        int b1 = Math.max(corner2.getA(), corner2.getA());
        int b2 = Math.max(corner2.getB(), corner2.getB());

        return new BoundingBox(a1, 0, a2, b1, 255, b2);
    }

    public static String getCamelCase(String s) {
        return String.valueOf(s.charAt(0)).toUpperCase(Locale.ROOT) + s.substring(1).toLowerCase(Locale.ROOT);
    }
}

