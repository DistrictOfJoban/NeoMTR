package top.mcmtr;

import net.fabricmc.api.ClientModInitializer;

// TODO: Entry point not used atm
public class MSDMainFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MSDMainClient.init();
        MSDMainClient.registerItemModelPredicates();
    }
}