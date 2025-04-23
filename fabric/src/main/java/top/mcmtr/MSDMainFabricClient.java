package top.mcmtr;

import net.fabricmc.api.ClientModInitializer;
import top.mcmtr.loader.fabric.MSDRegistryImpl;
import top.mcmtr.mod.MSDMainClient;

public class MSDMainFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MSDMainClient.init();
        MSDMainClient.registerItemModelPredicates();
        MSDRegistryImpl.PACKET_REGISTRY.commitClient();
    }
}