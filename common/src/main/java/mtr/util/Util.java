package mtr.util;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public class Util {
    public static String prettyPrintJson(JsonElement jsonElement) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
    }

    public static float round(double value, int decimalPlaces) {
        int factor = 1;
        for (int i = 0; i < decimalPlaces; i++) {
            factor *= 10;
        }
        return (float) Math.round(value * factor) / factor;
    }

    public static boolean isBetween(double value, double value1, double value2) {
        return isBetween(value, value1, value2, 0);
    }

    public static boolean isBetween(double value, double value1, double value2, double padding) {
        return value >= Math.min(value1, value2) - padding && value <= Math.max(value1, value2) + padding;
    }
}
