package net.londonunderground.mod.registry;

import mtr.loader.MTRRegistry;
import mtr.registry.RegistryObject;
import net.londonunderground.mod.LUAddon;
import net.londonunderground.mod.blocks.*;
import net.londonunderground.loader.LUAddonRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;

@SuppressWarnings("unchecked")
public final class BlockEntityTypes {

	static {
		DARK_TILE = LUAddonRegistry.registerBlockEntityType("tunnel_darkness", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(TunnelDarknessBlock.TileEntityTunnelDarkness::new, Blocks.TUNNEL_DARKNESS.get())));
		PIDS_NORTHERN_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("pids_northern", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(NorthernLinePIDS.TileEntityNorthernLinePIDS::new, Blocks.NORTHERN_PIDS.get())));
		TUNNEL_BLOCK_2_SIGNAL = LUAddonRegistry.registerBlockEntityType("tunnel_block_2_signal", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BlockTunnelSignal.TileEntityTunnelSignalLight1::new, Blocks.TUNNEL_BLOCK_2_SIGNAL.get())));
		TUNNEL_A2_SIGNAL = LUAddonRegistry.registerBlockEntityType("tunnel_a2_signal", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(TunnelA2Signal.TileEntityTunnelSignalLight1::new, Blocks.TUNNEL_A2_SIGNAL.get())));
		BRITISH_RAIL_UNDERGROUND_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("british_rail_underground", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BritishRailUnderground.TileEntityBritishRailUnderground::new, Blocks.BRITISH_RAIL_UNDERGROUND.get())));

		BLOCK_ROUNDEL_NLE_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("block_roundel_nle", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BlockRoundelNLE.TileEntityBlockRoundelNLE::new, Blocks.BLOCK_ROUNDEL_NLE.get())));
		BLOCK_ROUNDEL_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("block_roundel_1", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BlockRoundel.TileEntityBlockRoundel::new, Blocks.BLOCK_ROUNDEL_1.get())));
		BLOCK_ROUNDEL_BIG_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("block_roundel_1_big", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BlockRoundelBig.TileEntityBlockRoundelBig::new, Blocks.BLOCK_ROUNDEL_1_BIG.get())));
		BLOCK_ROUNDEL_BIG_EVEN_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("block_roundel_1_big_even", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BlockRoundelBigEven.TileEntityBlockRoundelBigEven::new, Blocks.BLOCK_ROUNDEL_1_BIG_EVEN.get())));

		BLOCK_ROUNDEL2_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("block_roundel_2", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BlockRoundel2.TileEntityBlockRoundel2::new, Blocks.BLOCK_ROUNDEL_2.get())));
		BLOCK_ROUNDEL2_BIG_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("block_roundel_2_big", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BlockRoundel2Big.TileEntityBlockRoundel2Big::new, Blocks.BLOCK_ROUNDEL_2_BIG.get())));
		BLOCK_ROUNDEL2_BIG_EVEN_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("block_roundel_2_big_even", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BlockRoundel2BigEven.TileEntityBlockRoundel2BigEven::new, Blocks.BLOCK_ROUNDEL_2_BIG_EVEN.get())));

		BLOCK_ROUNDEL3_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("block_roundel_3", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BlockRoundel3.TileEntityBlockRoundel3::new, Blocks.BLOCK_ROUNDEL_3.get())));
		BLOCK_ROUNDEL3_BIG_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("block_roundel_3_big", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BlockRoundel3Big.TileEntityBlockRoundel3Big::new, Blocks.BLOCK_ROUNDEL_3_BIG.get())));
		BLOCK_ROUNDEL3_BIG_EVEN_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("block_roundel_3_big_even", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BlockRoundel3BigEven.TileEntityBlockRoundel3BigEven::new, Blocks.BLOCK_ROUNDEL_3_BIG_EVEN.get())));

		BLOCK_ROUNDEL4_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("block_roundel_4", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BlockRoundel4.TileEntityBlockRoundel4::new, Blocks.BLOCK_ROUNDEL_4.get())));
		BLOCK_ROUNDEL4_BIG_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("block_roundel_4_big", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BlockRoundel4Big.TileEntityBlockRoundel4Big::new, Blocks.BLOCK_ROUNDEL_4_BIG.get())));
		BLOCK_ROUNDEL4_BIG_EVEN_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("block_roundel_4_big_even", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BlockRoundel4BigEven.TileEntityBlockRoundel4BigEven::new, Blocks.BLOCK_ROUNDEL_4_BIG_EVEN.get())));

		BLOCK_ROUNDEL5_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("block_roundel_5", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BlockRoundel5.TileEntityBlockRoundel5::new, Blocks.BLOCK_ROUNDEL_5.get())));
		BLOCK_ROUNDEL5_BIG_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("block_roundel_5_big", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BlockRoundel5Big.TileEntityBlockRoundel5Big::new, Blocks.BLOCK_ROUNDEL_5_BIG.get())));
		BLOCK_ROUNDEL5_BIG_EVEN_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("block_roundel_5_big_even", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BlockRoundel5BigEven.TileEntityBlockRoundel5BigEven::new, Blocks.BLOCK_ROUNDEL_5_BIG_EVEN.get())));

		BLOCK_ROUNDEL_STATION_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("block_roundel_station", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BlockRoundelStation.TileEntityBlockRoundelStation::new, Blocks.BLOCK_ROUNDEL_STATION.get())));
		BLOCK_ROUNDEL_STATION_TYPE_B_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("block_roundel_station_type_b", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BlockRoundelStationTypeB.TileEntityBlockRoundelStationTypeB::new, Blocks.BLOCK_ROUNDEL_STATION_TYPE_B.get())));
		BLOCK_ROUNDEL_STATION_TYPE_C_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("block_roundel_station_type_c", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BlockRoundelStationTypeC.TileEntityBlockRoundelStationTypeC::new, Blocks.BLOCK_ROUNDEL_STATION_TYPE_C.get())));
		BLOCK_ROUNDEL_STATION_TOP_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("block_roundel_station_top", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(BlockRoundelStationTop.TileEntityBlockRoundelStationTop::new, Blocks.BLOCK_ROUNDEL_STATION_TOP.get())));
		MORDEN_SIGN_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("morden_sign", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(MordenSign.TileEntityMordenSign::new, Blocks.MORDEN_SIGN.get())));
		METROPOLITAN_SIGN_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("metropolitan_sign", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(MetropolitanSign.TileEntityMetropolitanSign::new, Blocks.METROPOLITAN_SIGN.get())));
		ELIZABETH_SIGN_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("elizabeth_sign", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(ElizabethSign.TileEntityElizabethSign::new, Blocks.ELIZABETH_SIGN.get())));

		SIGN_RIVER_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("sign_river", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(SignRiver.TileEntitySignRiver::new, Blocks.SIGN_RIVER.get())));
		SIGN_OVERGROUND_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("sign_overground", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(SignOverground.TileEntitySignOverground::new, Blocks.SIGN_OVERGROUND.get())));
		SIGN_DLR_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("sign_dlr", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(SignDlr.TileEntitySignDlr::new, Blocks.SIGN_DLR.get())));
		SIGN_TRAMS_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("sign_trams", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(SignTrams.TileEntitySignTrams::new, Blocks.SIGN_TRAMS.get())));
		SIGN_POPPY_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("sign_poppy", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(SignPoppy.TileEntitySignPoppy::new, Blocks.SIGN_POPPY.get())));
		SIGN_METRO_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("sign_metro", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(SignMetro.TileEntitySignMetro::new, Blocks.SIGN_METRO.get())));
		SIGN_LIZZY_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("sign_lizzy", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(SignLizzy.TileEntitySignLizzy::new, Blocks.SIGN_LIZZY.get())));
		SIGN_UNDERGROUND_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("sign_underground", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(SignUnderground.TileEntitySignUnderground::new, Blocks.SIGN_UNDERGROUND.get())));
		SIGN_PRIDE_TILE_ENTITY = LUAddonRegistry.registerBlockEntityType("sign_pride", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(SignPride.TileEntitySignPride::new, Blocks.SIGN_PRIDE.get())));

		NAME_PROJECTOR = LUAddonRegistry.registerBlockEntityType("name_projector", new RegistryObject<>(() -> MTRRegistry.getBlockEntityType(NameProjector.TileEntityNameProjector::new, Blocks.NAME_PROJECTOR.get())));
	}

	public static final RegistryObject<BlockEntityType<TunnelDarknessBlock.TileEntityTunnelDarkness>> DARK_TILE;
	public static final RegistryObject<BlockEntityType<NorthernLinePIDS.TileEntityNorthernLinePIDS>> PIDS_NORTHERN_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<BlockTunnelSignal.TileEntityTunnelSignalLight1>> TUNNEL_BLOCK_2_SIGNAL;
	public static final RegistryObject<BlockEntityType<TunnelA2Signal.TileEntityTunnelSignalLight1>> TUNNEL_A2_SIGNAL;
	public static final RegistryObject<BlockEntityType<BritishRailUnderground.TileEntityBritishRailUnderground>> BRITISH_RAIL_UNDERGROUND_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<BlockRoundelNLE.TileEntityBlockRoundelNLE>> BLOCK_ROUNDEL_NLE_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<BlockRoundel.TileEntityBlockRoundel>> BLOCK_ROUNDEL_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<BlockRoundelBig.TileEntityBlockRoundelBig>> BLOCK_ROUNDEL_BIG_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<BlockRoundelBigEven.TileEntityBlockRoundelBigEven>> BLOCK_ROUNDEL_BIG_EVEN_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<BlockRoundel2.TileEntityBlockRoundel2>> BLOCK_ROUNDEL2_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<BlockRoundel2Big.TileEntityBlockRoundel2Big>> BLOCK_ROUNDEL2_BIG_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<BlockRoundel2BigEven.TileEntityBlockRoundel2BigEven>> BLOCK_ROUNDEL2_BIG_EVEN_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<BlockRoundel3.TileEntityBlockRoundel3>> BLOCK_ROUNDEL3_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<BlockRoundel3Big.TileEntityBlockRoundel3Big>> BLOCK_ROUNDEL3_BIG_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<BlockRoundel3BigEven.TileEntityBlockRoundel3BigEven>> BLOCK_ROUNDEL3_BIG_EVEN_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<BlockRoundel4.TileEntityBlockRoundel4>> BLOCK_ROUNDEL4_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<BlockRoundel4Big.TileEntityBlockRoundel4Big>> BLOCK_ROUNDEL4_BIG_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<BlockRoundel4BigEven.TileEntityBlockRoundel4BigEven>> BLOCK_ROUNDEL4_BIG_EVEN_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<BlockRoundel5.TileEntityBlockRoundel5>> BLOCK_ROUNDEL5_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<BlockRoundel5Big.TileEntityBlockRoundel5Big>> BLOCK_ROUNDEL5_BIG_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<BlockRoundel5BigEven.TileEntityBlockRoundel5BigEven>> BLOCK_ROUNDEL5_BIG_EVEN_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<BlockRoundelStation.TileEntityBlockRoundelStation>> BLOCK_ROUNDEL_STATION_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<BlockRoundelStationTypeB.TileEntityBlockRoundelStationTypeB>> BLOCK_ROUNDEL_STATION_TYPE_B_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<BlockRoundelStationTypeC.TileEntityBlockRoundelStationTypeC>> BLOCK_ROUNDEL_STATION_TYPE_C_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<BlockRoundelStationTop.TileEntityBlockRoundelStationTop>> BLOCK_ROUNDEL_STATION_TOP_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<MordenSign.TileEntityMordenSign>> MORDEN_SIGN_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<MetropolitanSign.TileEntityMetropolitanSign>> METROPOLITAN_SIGN_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<ElizabethSign.TileEntityElizabethSign>> ELIZABETH_SIGN_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<SignRiver.TileEntitySignRiver>> SIGN_RIVER_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<SignOverground.TileEntitySignOverground>> SIGN_OVERGROUND_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<SignDlr.TileEntitySignDlr>> SIGN_DLR_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<SignTrams.TileEntitySignTrams>> SIGN_TRAMS_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<SignPoppy.TileEntitySignPoppy>> SIGN_POPPY_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<SignMetro.TileEntitySignMetro>> SIGN_METRO_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<SignLizzy.TileEntitySignLizzy>> SIGN_LIZZY_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<SignUnderground.TileEntitySignUnderground>> SIGN_UNDERGROUND_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<SignPride.TileEntitySignPride>> SIGN_PRIDE_TILE_ENTITY;
	public static final RegistryObject<BlockEntityType<NameProjector.TileEntityNameProjector>> NAME_PROJECTOR;

	public static void init() {
		LUAddon.LOGGER.info("Registering MTR London Underground Addon block entity types");
	}
}
