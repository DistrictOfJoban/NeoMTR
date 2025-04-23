package mtr.neoforge;

import cn.zbx1425.mtrsteamloco.neoforge.ClientProxy;
import com.lx862.jcm.JCMForge;
import com.lx862.jcm.loader.neoforge.JCMRegistryImpl;
import com.lx862.jcm.mod.JCM;
import com.lx862.jcm.mod.JCMClient;
import com.lx862.mtrtm.mod.TransitManager;
import com.lx862.mtrtm.loader.neoforge.LoaderImpl;
import mtr.*;
import mtr.client.CustomResources;
import mtr.loader.neoforge.MTRRegistryImpl;
import mtr.neoforge.mappings.ForgeUtilities;
import mtr.registry.Items;
import mtr.render.RenderDrivingOverlay;
import net.londonunderground.mod.LUAddon;
import net.londonunderground.mod.LUAddonClient;
import net.londonunderground.loader.neoforge.LUAddonRegistryImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import top.mcmtr.loader.neoforge.MSDRegistryImpl;
import top.mcmtr.mod.MSDMain;
import top.mcmtr.mod.MSDMainClient;

@Mod(MTR.MOD_ID)
public class MTRForge {
	private static final RegistriesWrapperImpl nteRegistries = new RegistriesWrapperImpl();

	static {
		MTR.init();
		cn.zbx1425.mtrsteamloco.Main.init(nteRegistries);
		JCM.init();
		LUAddon.init();
		TransitManager.init();
		MSDMain.init();
	}

	public MTRForge(IEventBus eventBus) {
		MTRRegistryImpl.registeredAllDeferred(eventBus);
		nteRegistries.registerAllDeferred(eventBus);
		JCMForge.init(eventBus);

		LUAddonRegistryImpl.registerAllDeferred(eventBus);
		MSDRegistryImpl.registerAllDeferred(eventBus);

		eventBus.register(MTRModEventBus.class);
		eventBus.register(ForgeUtilities.RegisterCreativeTabs.class);
		if (FMLEnvironment.dist.isClient()) {
			ForgeUtilities.renderGameOverlayAction((guiGraphics) -> {
				RenderDrivingOverlay.render((GuiGraphics) guiGraphics);
			});
			NeoForge.EVENT_BUS.register(ForgeUtilities.Events.class);
			eventBus.register(ForgeUtilities.ClientsideEvents.class);

			// NTE
			NeoForge.EVENT_BUS.register(ClientProxy.ForgeEventBusListener.class);
			eventBus.register(ClientProxy.ModEventBusListener.class);

			ClientProxy.registerConfigScreen();
		}
	}

	private static class MTRModEventBus {

		@SubscribeEvent
		public static void onClientSetupEvent(FMLClientSetupEvent event) {
			MTRClient.init();
			event.enqueueWork(Items::initItemModelPredicate);

			JCMClient.initializeClient();
			LUAddonClient.init();

			// MSD
			MSDMainClient.init();
			event.enqueueWork(MSDMainClient::registerItemModelPredicates);

			ForgeUtilities.registerTextureStitchEvent(textureAtlas -> {
				if (((TextureAtlas) textureAtlas).location().getPath().equals("textures/atlas/blocks.png")) {
					CustomResources.reload(Minecraft.getInstance().getResourceManager());
				}
			});
		}

		@SubscribeEvent
		public static void registerCommand(RegisterCommandsEvent registerCommandsEvent) {
			LoaderImpl.invokeRegisterCommands(registerCommandsEvent.getDispatcher());
			LUAddonRegistryImpl.invokeRegisterCommands(registerCommandsEvent.getDispatcher());
		}

		@SubscribeEvent
		public static void registerPayloadHandlers(final RegisterPayloadHandlersEvent event) {
			PayloadRegistrar registrar = event.registrar("1");
			MTRRegistryImpl.PACKET_REGISTRY.commit(registrar);
			JCMRegistryImpl.PACKET_REGISTRY.commit(registrar);
			MSDRegistryImpl.PACKET_REGISTRY.commit(registrar);
		}
	}
}
