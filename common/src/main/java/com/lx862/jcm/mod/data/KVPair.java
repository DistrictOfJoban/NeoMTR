package com.lx862.jcm.mod.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.HashMap;

public class KVPair {
    private final HashMap<String, Object> map;

    public KVPair() {
        this.map = new HashMap<>();
    }

    public KVPair(JsonObject jsonObject)  {
        this();
        jsonObject.entrySet().forEach(entry -> {
            Object val = null;
            if(entry.getValue().isJsonPrimitive()) {
                JsonPrimitive jsonPrimitive = entry.getValue().getAsJsonPrimitive();
                if(jsonPrimitive.isBoolean()) {
                    val = jsonPrimitive.getAsBoolean();
                }
                if(jsonPrimitive.isNumber()) {
                    val = jsonPrimitive.getAsDouble();
                }
                if(jsonPrimitive.isString()) {
                    val = jsonPrimitive.getAsString();
                }
            } else {
                if(entry.getValue().isJsonArray()) {
                    val = entry.getValue().getAsJsonArray();
                } else if(entry.getValue().isJsonObject()) {
                    val = entry.getValue().getAsJsonObject();
                }
            }

            if(val != null) {
                with(entry.getKey(), val);
            }
        });
    }

    public KVPair with(String str, Object obj) {
        map.put(str, obj);
        return this;
    }

    public <T> T get(String key, T fallback) {
        return (T) map.getOrDefault(key, fallback);
    }

    public <T> T get(String key) {
        return (T) map.get(key);
    }

    public int getInt(String key, int fallback) {
        Object value = get(key);
        if(Integer.class.isInstance(value)) {
            return (Integer)value;
        } else {
            return ((Double)value).intValue();
        }
    }

    public double getDouble(String key, double fallback) {
        return (double)get(key, fallback);
    }

    public int getColor(String key, int defaultColor) {
        if(!map.containsKey(key)) return defaultColor;

        Object obj = map.get(key);
        if(obj instanceof String) {
            return Color.decode((String)obj).getRGB();
        } else {
            return getInt(key, defaultColor);
        }
    }

    /**
     * Get an optional identifier
     * @param str Key
     * @return The identifier, or null if the key does not exist
     */
    public ResourceLocation getIdentifier(String str) {
        return map.containsKey(str) ? ResourceLocation.parse((String)map.get(str)) : null;
    }
}
