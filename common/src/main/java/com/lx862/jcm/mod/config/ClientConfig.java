package com.lx862.jcm.mod.config;

import com.google.gson.JsonObject;
import com.lx862.jcm.mod.util.JCMLogger;
import net.minecraft.client.Minecraft;

import java.nio.file.Path;

public class ClientConfig extends Config {

    private static final Path CONFIG_PATH = Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("jsblock_client.json");
    public boolean disableRendering;
    public boolean debug;

    public ClientConfig() {
        super(CONFIG_PATH);
    }

    @Override
    public void fromJson(JsonObject jsonConfig) {
        JCMLogger.info("Loading client config...");
        this.disableRendering = jsonConfig.get("disable_rendering").getAsBoolean();
        this.debug = jsonConfig.get("debug_mode").getAsBoolean();
    }

    @Override
    public JsonObject toJson() {
        JCMLogger.info("Writing client config...");
        final JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty("disable_rendering", disableRendering);
        jsonConfig.addProperty("debug_mode", debug);
        return jsonConfig;
    }
}
