package com.lx862.jcm.mod.config;

import com.google.gson.JsonObject;
import com.lx862.jcm.mod.util.JCMLogger;
import net.minecraft.client.Minecraft;

import java.nio.file.Path;

public class JCMConfig extends Config {

    private static final Path CONFIG_PATH = Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("jsblock.json");
    public boolean debug;

    public JCMConfig() {
        super(CONFIG_PATH);
    }

    @Override
    public void fromJson(JsonObject jsonConfig) {
        JCMLogger.info("Loading config...");
        this.debug = jsonConfig.get("debug_mode").getAsBoolean();
    }

    @Override
    public JsonObject toJson() {
        JCMLogger.info("Writing config...");
        final JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty("debug_mode", debug);
        return jsonConfig;
    }
}
