package cn.zbx1425.mtrsteamloco;

import cn.zbx1425.mtrsteamloco.game.TrainVirtualDrive;
import cn.zbx1425.mtrsteamloco.game.VirtualDriveClientData;
import cn.zbx1425.mtrsteamloco.gui.ConfigScreen;
import cn.zbx1425.mtrsteamloco.network.PacketScreen;
import cn.zbx1425.mtrsteamloco.network.PacketVersionCheck;
import cn.zbx1425.mtrsteamloco.network.PacketVirtualDrivingPlayers;
import cn.zbx1425.mtrsteamloco.render.ShadersModHandler;
import cn.zbx1425.mtrsteamloco.render.block.BlockEntityEyeCandyRenderer;
import cn.zbx1425.mtrsteamloco.render.rail.RailRenderDispatcher;
import cn.zbx1425.sowcer.util.DrawContext;
import cn.zbx1425.sowcerext.reuse.AtlasManager;
import cn.zbx1425.sowcerext.reuse.DrawScheduler;
import cn.zbx1425.sowcerext.reuse.ModelManager;
import mtr.MTRClient;
import mtr.loader.MTRRegistryClient;
import mtr.item.ItemBlockClickingBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;

public class MainClient {

	public static DrawScheduler drawScheduler = new DrawScheduler();
	public static ModelManager modelManager = new ModelManager();
	public static AtlasManager atlasManager = new AtlasManager();

	public static RailRenderDispatcher railRenderDispatcher = new RailRenderDispatcher();

	public static DrawContext drawContext = new DrawContext();

	public static void init() {
		ClientConfig.load();
		ShadersModHandler.init();

		mtr.client.CustomResources.registerReloadListener(CustomResources::init);

		if (Main.enableRegistry) {
			MTRRegistryClient.registerTileEntityRenderer(Main.BLOCK_ENTITY_TYPE_EYE_CANDY.get(), BlockEntityEyeCandyRenderer::new);
			MTRRegistryClient.registerBlockRenderType(RenderType.cutout(), Main.BLOCK_ONE_WAY_GATE.get());

			MTRRegistryClient.registerNetworkReceiver(PacketVersionCheck.PACKET_VERSION_CHECK, PacketVersionCheck::receiveVersionCheckS2C);
			MTRRegistryClient.registerNetworkReceiver(PacketScreen.PACKET_SHOW_SCREEN, PacketScreen::receiveScreenS2C);
			MTRRegistryClient.registerNetworkReceiver(PacketVirtualDrivingPlayers.PACKET_VIRTUAL_DRIVING_PLAYERS,
					PacketVirtualDrivingPlayers.Client::receiveVirtualDrivingPlayersS2C);

			MTRRegistryClient.registerItemModelPredicate("mtr:selected", Main.BRIDGE_CREATOR_1.get(), ItemBlockClickingBase.TAG_POS);
		}

		MTRRegistryClient.registerPlayerJoinEvent(localPlayer -> {
			railRenderDispatcher.clearRail();

			VirtualDriveClientData.drivingPlayers.clear();
			TrainVirtualDrive.activeTrain = null;
		});

		MTRClient.registerAddonConfigGUICallback(Main.ADDON, (prevScreen) -> Minecraft.getInstance().setScreen(ConfigScreen.createScreen(prevScreen)));
	}

}
