package mtr;

import cn.zbx1425.mtrsteamloco.Main;
import mtr.loader.fabric.MTRRegistryImpl;
import net.fabricmc.api.ModInitializer;

public class MTRFabric implements ModInitializer {

	private final RegistriesWrapperImpl REGISTRIES = new RegistriesWrapperImpl();

	@Override
	public void onInitialize() {
		MTR.init();
		Main.init(REGISTRIES);
		MTRRegistryImpl.PACKET_REGISTRY.commitCommon();
	}
}
