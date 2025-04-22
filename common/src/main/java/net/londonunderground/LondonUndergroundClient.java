package net.londonunderground;

import net.londonunderground.registry.BlockEntityTypes;
import net.londonunderground.registry.Blocks;
import net.londonunderground.registry.LondonUndergroundRegistryClient;
import net.londonunderground.render.*;
import net.minecraft.client.renderer.RenderType;

public final class LondonUndergroundClient {

	public static void init() {
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.DARK_TILE.get(), RenderDarkTile::new);
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.TUNNEL_BLOCK_2_SIGNAL.get(), dispatcher -> new RenderTunnelSignalLight<>(dispatcher, false, 0xFF00FF00));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.TUNNEL_A2_SIGNAL.get(), dispatcher -> new RenderTunnelSignalLight<>(dispatcher, false, 0xFF00FF00));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.PIDS_NORTHERN_TILE_ENTITY.get(), dispatcher -> new RenderNorthernLinePIDS<>(dispatcher, 3, 1.5F, 7.5F, 6, 6.5F, 29, true, true, 0xFF9900, 0xFF9900));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BLOCK_ROUNDEL_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 14 / 16F, 0.2F / 16, 0, 0, -0.5F, 0, 0xFFB3B3B3, false, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BLOCK_ROUNDEL_NLE_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 14 / 16F, 0.2F / 16, 0, 0, -0.495F, 0, 0xFFB3B3B3, false, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BRITISH_RAIL_UNDERGROUND_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 14 / 16F, 0.2F / 16, 0, 0.09F, -0.480F, 0, 0xFFB3B3B3, false, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BLOCK_ROUNDEL_BIG_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 28 / 16F, 0.4F / 16, 0, 0.5F, -0.5F, 0, 0xFFB3B3B3, false, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BLOCK_ROUNDEL_BIG_EVEN_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 28 / 16F, 0.4F / 16, -0.5F, 0.5F, -0.5F, 0, 0xFFB3B3B3, false, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BLOCK_ROUNDEL2_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 14 / 16F, 0.2F / 16, 0, 0, -0.5F, 0, 0xFFB3B3B3, false, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BLOCK_ROUNDEL2_BIG_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 28 / 16F, 0.4F / 16, 0, 0.5F, -0.5F, 0, 0xFFB3B3B3, false, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BLOCK_ROUNDEL2_BIG_EVEN_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 28 / 16F, 0.4F / 16, -0.5F, 0.5F, -0.5F, 0, 0xFFB3B3B3, false, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BLOCK_ROUNDEL3_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 14 / 16F, 0.2F / 16, 0, 0, -0.5F, 0, 0xFFB3B3B3, false, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BLOCK_ROUNDEL3_BIG_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 28 / 16F, 0.4F / 16, 0, 0.5F, -0.5F, 0, 0xFFB3B3B3, false, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BLOCK_ROUNDEL3_BIG_EVEN_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 28 / 16F, 0.4F / 16, -0.5F, 0.5F, -0.5F, 0, 0xFFB3B3B3, false, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BLOCK_ROUNDEL4_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 14 / 16F, 0.2F / 16, 0, 0, -0.5F, 0, 0xFFB3B3B3, false, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BLOCK_ROUNDEL4_BIG_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 28 / 16F, 0.4F / 16, 0, 0.5F, -0.5F, 0, 0xFFB3B3B3, false, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BLOCK_ROUNDEL4_BIG_EVEN_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 28 / 16F, 0.4F / 16, -0.5F, 0.5F, -0.5F, 0, 0xFFB3B3B3, false, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BLOCK_ROUNDEL5_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 14 / 16F, 0.2F / 16, 0, 0, -0.5F, 0, 0xFFB3B3B3, false, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BLOCK_ROUNDEL5_BIG_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 28 / 16F, 0.4F / 16, 0, 0.5F, -0.5F, 0, 0xFFB3B3B3, false, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BLOCK_ROUNDEL5_BIG_EVEN_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 28 / 16F, 0.4F / 16, -0.5F, 0.5F, -0.5F, 0, 0xFFB3B3B3, false, "johnston"));

		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BLOCK_ROUNDEL_STATION_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 22 / 13F, 0.22F / 13, 0, 0.21F, 1.046F, 0, 0xFFB3B3B3, false, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BLOCK_ROUNDEL_STATION_TOP_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 22 / 13F, 0.22F / 13, 0.40F, 0.250F, 1.577F, 30, 0xFF000D3D, false, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BLOCK_ROUNDEL_STATION_TYPE_B_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 22 / 13F, 0.22F / 13, 0.01F, -0.039F, 1.296F, 0, 0xFFB3B3B3, false, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.BLOCK_ROUNDEL_STATION_TYPE_C_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 32 / 5F, 1.32F / 13, 0.01F, 1.739F, 0.596F, 30, 0xFF000000, false, "beeching"));

		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.NAME_PROJECTOR.get(), RenderNameProjector::new);

		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.MORDEN_SIGN_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 15 / 16F, 0.2F / 16, 0, 10 / 16F, 0.425F / 16, 0, 0xFFB3B3B3, true, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.METROPOLITAN_SIGN_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 15 / 16F, 0.2F / 16, 0, 10 / 16F, 0.425F / 16, 0, 0xFFB3B3B3, true, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.ELIZABETH_SIGN_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 15 / 16F, 0.2F / 16, 0, 10 / 16F, 0.425F / 16, 0, 0xFFB3B3B3, true, "johnston"));

		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.SIGN_RIVER_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 15 / 16F, 0.2F / 16, 0, 10.2F / 16, 0.425F / 16, 0, 0xFFB3B3B3, true, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.SIGN_OVERGROUND_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 15 / 16F, 0.2F / 16, 0, 10.2F / 16, 0.425F / 16, 0, 0xFFB3B3B3, true, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.SIGN_DLR_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 15 / 16F, 0.2F / 16, 0, 10.2F / 16, 0.425F / 16, 0, 0xFFB3B3B3, true, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.SIGN_TRAMS_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 15 / 16F, 0.2F / 16, 0, 10.2F / 16, 0.425F / 16, 0, 0xFFB3B3B3, true, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.SIGN_POPPY_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 15 / 16F, 0.2F / 16, 0, 10.2F / 16, 0.425F / 16, 0, 0xFFB3B3B3, true, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.SIGN_METRO_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 15 / 16F, 0.2F / 16, 0, 10.2F / 16, 0.425F / 16, 0, 0xFFB3B3B3, true, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.SIGN_LIZZY_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 15 / 16F, 0.2F / 16, 0, 10.2F / 16, 0.425F / 16, 0, 0xFFB3B3B3, true, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.SIGN_UNDERGROUND_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 15 / 16F, 0.2F / 16, 0, 10.2F / 16, 0.425F / 16, 0, 0xFFB3B3B3, true, "johnston"));
		LondonUndergroundRegistryClient.registerBlockEntityRenderer(BlockEntityTypes.SIGN_PRIDE_TILE_ENTITY.get(), dispatcher -> new RenderRoundel<>(dispatcher, 15 / 16F, 0.2F / 16, 0, 10.2F / 16, 0.425F / 16, 0, 0xFFB3B3B3, true, "johnston"));

		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TUNNEL_BLOCK_2_SIGNAL.get());
		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.TUNNEL_A2_SIGNAL.get());
		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.BRITISH_RAIL_UNDERGROUND.get());

		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.BLOCK_ROUNDEL_1.get());
		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.BLOCK_ROUNDEL_NLE.get());
		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.BLOCK_ROUNDEL_1_BIG.get());
		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.BLOCK_ROUNDEL_1_BIG_EVEN.get());
		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.BLOCK_ROUNDEL_2.get());
		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.BLOCK_ROUNDEL_2_BIG.get());
		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.BLOCK_ROUNDEL_2_BIG_EVEN.get());
		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.BLOCK_ROUNDEL_3.get());
		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.BLOCK_ROUNDEL_3_BIG.get());
		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.BLOCK_ROUNDEL_3_BIG_EVEN.get());
		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.BLOCK_ROUNDEL_4.get());
		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.BLOCK_ROUNDEL_4_BIG.get());
		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.BLOCK_ROUNDEL_4_BIG_EVEN.get());
		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.BLOCK_ROUNDEL_5.get());
		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.BLOCK_ROUNDEL_5_BIG.get());
		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.BLOCK_ROUNDEL_5_BIG_EVEN.get());
		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.ROUNDEL_POLE.get());
		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.ROUNDEL_POLE_DLR.get());
		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.ROUNDEL_POLE_LIZ.get());
		LondonUndergroundRegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.ROUNDEL_POLE_OVERGROUND.get());
	}
}
