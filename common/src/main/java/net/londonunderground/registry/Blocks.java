package net.londonunderground.registry;

import mtr.registry.RegistryObject;
import net.londonunderground.LondonUnderground;
import net.londonunderground.blocks.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public final class Blocks {

	static {
		PLATFORM_TFL_1 = LondonUndergroundRegistry.registerBlockAndItem("platform_tfl_1", new RegistryObject<>(() -> new LUPlatform1(BlockBehaviour.Properties.of(), true)), CreativeModeTabs.TFL_BLOCKS);
		PLATFORM_TFL_ISLAND = LondonUndergroundRegistry.registerBlockAndItem("platform_tfl_island", new RegistryObject<>(() -> new LUPlatform1(BlockBehaviour.Properties.of(), true)), CreativeModeTabs.TFL_BLOCKS);
		PLATFORM_TFL_GAP = LondonUndergroundRegistry.registerBlockAndItem("platform_tfl_gap", new RegistryObject<>(() -> new LUPlatform1(BlockBehaviour.Properties.of(), true)), CreativeModeTabs.TFL_BLOCKS);

		PLATFORM_BLOCK = LondonUndergroundRegistry.registerBlockAndItem("platform_block", new RegistryObject<>(() -> new Block(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_BLOCKS);
		PLATFORM_VARIANT = LondonUndergroundRegistry.registerBlockAndItem("platform_variant", new RegistryObject<>(() -> new Block(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_BLOCKS);
		TUNNEL_DARKNESS = LondonUndergroundRegistry.registerBlockAndItem("tunnel_darkness", new RegistryObject<>(() -> new TunnelDarknessBlock(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_BLOCKS);
		TFL_BLOCK = LondonUndergroundRegistry.registerBlockAndItem("tfl_block", new RegistryObject<>(() -> new Block(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_BLOCKS);
		SOUND_OUTSIDE_AMBIENCE = LondonUndergroundRegistry.registerBlockAndItem("sounds/outside_ambience", new RegistryObject<>(() -> new SoundOutsideAmbience(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_BLOCKS);
		TUBE_STATION_AMBIENCE1 = LondonUndergroundRegistry.registerBlockAndItem("sounds/tube_station_ambience1", new RegistryObject<>(() -> new SoundTubeStationAmbience1(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_BLOCKS);
		SOUND_SEE_IT_SAY_IT_SORTED = LondonUndergroundRegistry.registerBlockAndItem("sounds/see_it_say_it_sorted", new RegistryObject<>(() -> new SoundSeeItSayItSorted(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_BLOCKS);
		STATION_LIGHT = LondonUndergroundRegistry.registerBlockAndItem("station_light", new RegistryObject<>(() -> new StationLight(BlockBehaviour.Properties.of().lightLevel(state -> 15))), CreativeModeTabs.TFL_BLOCKS);
		EXIT_SIGN = LondonUndergroundRegistry.registerBlockAndItem("exit_sign", new RegistryObject<>(() -> new ExitSign(BlockBehaviour.Properties.of().lightLevel(state -> 15))), CreativeModeTabs.TFL_BLOCKS);

		TUNNEL_ENTRANCE_1 = LondonUndergroundRegistry.registerBlockAndItem("tunnel_entrance_1", new RegistryObject<>(() -> new Block(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		TUNNEL_A0 = LondonUndergroundRegistry.registerBlockAndItem("tunnel_a0", new RegistryObject<>(() -> new TunnelA0(BlockBehaviour.Properties.of().noCollission())), CreativeModeTabs.TFL_STATION);
		TUNNEL_A0_MORDEN = LondonUndergroundRegistry.registerBlockAndItem("tunnel_a0_morden", new RegistryObject<>(() -> new TunnelA0(BlockBehaviour.Properties.of().noCollission())), CreativeModeTabs.TFL_STATION);
		TUNNEL_A1 = LondonUndergroundRegistry.registerBlockAndItem("tunnel_a1", new RegistryObject<>(() -> new TunnelA1(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_STATION);
		TUNNEL_A1_MORDEN = LondonUndergroundRegistry.registerBlockAndItem("tunnel_a1_morden", new RegistryObject<>(() -> new TunnelA1(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_STATION);
		TUNNEL_A1_DUEL = LondonUndergroundRegistry.registerBlockAndItem("tunnel_a1_duel", new RegistryObject<>(() -> new TunnelA1Duel(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_STATION);
		TUNNEL_A2 = LondonUndergroundRegistry.registerBlockAndItem("tunnel_a2", new RegistryObject<>(() -> new TunnelA2(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		TUNNEL_A2_NOWIRES = LondonUndergroundRegistry.registerBlockAndItem("tunnel_a2_nowires", new RegistryObject<>(() -> new TunnelA2NoWires(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		TUNNEL_A2_LIGHT = LondonUndergroundRegistry.registerBlockAndItem("tunnel_a2_light", new RegistryObject<>(() -> new TunnelA2Light(BlockBehaviour.Properties.of().lightLevel(state -> 15))), CreativeModeTabs.TFL_STATION);
		TUNNEL_A2_LIGHT_WHITE = LondonUndergroundRegistry.registerBlockAndItem("tunnel_a2_light_white", new RegistryObject<>(() -> new TunnelA2Light(BlockBehaviour.Properties.of().lightLevel(state -> 15))), CreativeModeTabs.TFL_STATION);

		TUNNEL_A2_NOWIRES_MORDEN = LondonUndergroundRegistry.registerBlockAndItem("tunnel_a2_nowires_morden", new RegistryObject<>(() -> new TunnelA2NoWires(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		TUNNEL_A2_SIGNAL = LondonUndergroundRegistry.registerBlockAndItem("tunnel_a2_signal", new RegistryObject<>(() -> new TunnelA2Signal(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		TUNNEL_A2_DUEL = LondonUndergroundRegistry.registerBlockAndItem("tunnel_a2_duel", new RegistryObject<>(() -> new TunnelA2Duel(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		TUNNEL_A3 = LondonUndergroundRegistry.registerBlockAndItem("tunnel_a3", new RegistryObject<>(() -> new TunnelA3(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_STATION);
		TUNNEL_A3_NOWIRES = LondonUndergroundRegistry.registerBlockAndItem("tunnel_a3_nowires", new RegistryObject<>(() -> new TunnelA3NoWires(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_STATION);
		TUNNEL_A3_NOWIRES_MORDEN = LondonUndergroundRegistry.registerBlockAndItem("tunnel_a3_nowires_morden", new RegistryObject<>(() -> new TunnelA3NoWires(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_STATION);
		TUNNEL_WIRES = LondonUndergroundRegistry.registerBlockAndItem("tunnel_wires", new RegistryObject<>(() -> new TunnelWires(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_STATION);
		TUNNEL_A3_DUEL = LondonUndergroundRegistry.registerBlockAndItem("tunnel_a3_duel", new RegistryObject<>(() -> new TunnelA3Duel(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_STATION);
		TUNNEL_A4 = LondonUndergroundRegistry.registerBlockAndItem("tunnel_a4", new RegistryObject<>(() -> new TunnelA4(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_STATION);
		TUNNEL_A5 = LondonUndergroundRegistry.registerBlockAndItem("tunnel_a5", new RegistryObject<>(() -> new TunnelA5(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);


		STATION_A1 = LondonUndergroundRegistry.registerBlockAndItem("station_a1", new RegistryObject<>(() -> new StationA1(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		STATION_A2 = LondonUndergroundRegistry.registerBlockAndItem("station_a2", new RegistryObject<>(() -> new StationA2(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		STATION_A3_GREEN = LondonUndergroundRegistry.registerBlockAndItem("station_a3_green", new RegistryObject<>(() -> new StationA3StopMarker(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		STATION_A3 = LondonUndergroundRegistry.registerBlockAndItem("station_a3", new RegistryObject<>(() -> new StationA3(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		STATION_A4 = LondonUndergroundRegistry.registerBlockAndItem("station_a4", new RegistryObject<>(() -> new StationA4(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		STATION_A5 = LondonUndergroundRegistry.registerBlockAndItem("station_a5", new RegistryObject<>(() -> new StationA5(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		STATION_A6 = LondonUndergroundRegistry.registerBlockAndItem("station_a6", new RegistryObject<>(() -> new StationA6(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		STATION_A7 = LondonUndergroundRegistry.registerBlockAndItem("station_a7", new RegistryObject<>(() -> new StationA7(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		STATION_A8 = LondonUndergroundRegistry.registerBlockAndItem("station_a8", new RegistryObject<>(() -> new StationA8(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		STATION_A9 = LondonUndergroundRegistry.registerBlockAndItem("station_a9", new RegistryObject<>(() -> new StationA9(BlockBehaviour.Properties.of().lightLevel(state -> 2))), CreativeModeTabs.TFL_STATION);
		STATION_A9_PIDPOLE = LondonUndergroundRegistry.registerBlockAndItem("station_a9_pidpole", new RegistryObject<>(() -> new BlockPIDSPoleStation(BlockBehaviour.Properties.of().lightLevel(state -> 2))), CreativeModeTabs.TFL_STATION);
		STATION_A1b = LondonUndergroundRegistry.registerBlockAndItem("station_a1b", new RegistryObject<>(() -> new StationA1(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		STATION_A2b = LondonUndergroundRegistry.registerBlockAndItem("station_a2b", new RegistryObject<>(() -> new StationA2(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		STATION_A3b_GREEN = LondonUndergroundRegistry.registerBlockAndItem("station_a3b_green", new RegistryObject<>(() -> new StationA3StopMarker(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		STATION_A3b = LondonUndergroundRegistry.registerBlockAndItem("station_a3b", new RegistryObject<>(() -> new StationA3(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		STATION_A4b = LondonUndergroundRegistry.registerBlockAndItem("station_a4b", new RegistryObject<>(() -> new StationA4(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		STATION_A5b = LondonUndergroundRegistry.registerBlockAndItem("station_a5b", new RegistryObject<>(() -> new StationA5(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		STATION_A6b = LondonUndergroundRegistry.registerBlockAndItem("station_a6b", new RegistryObject<>(() -> new StationA6(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		STATION_A7b = LondonUndergroundRegistry.registerBlockAndItem("station_a7b", new RegistryObject<>(() -> new StationA7(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		STATION_A8b = LondonUndergroundRegistry.registerBlockAndItem("station_a8b", new RegistryObject<>(() -> new StationA8(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		STATION_A9b = LondonUndergroundRegistry.registerBlockAndItem("station_a9b", new RegistryObject<>(() -> new StationA9(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_STATION);
		STATION_A9b_PIDPOLE = LondonUndergroundRegistry.registerBlockAndItem("station_a9b_pidpole", new RegistryObject<>(() -> new BlockPIDSPoleStation(BlockBehaviour.Properties.of().lightLevel(state -> 2))), CreativeModeTabs.TFL_STATION);

		TUBE_0 = LondonUndergroundRegistry.registerBlockAndItem("tube_0", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_STATION);
		TUBE_0_SLAB = LondonUndergroundRegistry.registerBlockAndItem("tube_0_slab", new RegistryObject<>(() -> new SlabBlock(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_STATION);
		TUBE_0b = LondonUndergroundRegistry.registerBlockAndItem("tube_0b", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_STATION);
		TUBE_0b_SLAB = LondonUndergroundRegistry.registerBlockAndItem("tube_0b_slab", new RegistryObject<>(() -> new SlabBlock(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_STATION);


		PIDS_POLE = LondonUndergroundRegistry.registerBlockAndItem("pids_pole", new RegistryObject<>(() -> new BlockPIDSPole(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_BLOCKS);

		ROUNDEL_POLE = LondonUndergroundRegistry.registerBlockAndItem("roundel_pole", new RegistryObject<>(() -> new LUPoleRoundel(BlockBehaviour.Properties.of().lightLevel(state -> 15).noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		ROUNDEL_POLE_DLR = LondonUndergroundRegistry.registerBlockAndItem("roundel_pole_dlr", new RegistryObject<>(() -> new LUPoleRoundel(BlockBehaviour.Properties.of().lightLevel(state -> 15).noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		ROUNDEL_POLE_LIZ = LondonUndergroundRegistry.registerBlockAndItem("roundel_pole_liz", new RegistryObject<>(() -> new LUPoleRoundel(BlockBehaviour.Properties.of().lightLevel(state -> 15).noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		ROUNDEL_POLE_OVERGROUND = LondonUndergroundRegistry.registerBlockAndItem("roundel_pole_overground", new RegistryObject<>(() -> new LUPoleRoundel(BlockBehaviour.Properties.of().lightLevel(state -> 15).noOcclusion())), CreativeModeTabs.TFL_SIGNS);

		NORTHERN_PIDS = LondonUndergroundRegistry.registerBlockAndItem("pids_northern", new RegistryObject<>(() -> new NorthernLinePIDS()), CreativeModeTabs.TFL_BLOCKS);

		MORDEN_STAIRS = LondonUndergroundRegistry.registerBlockAndItem("morden_stairs", new RegistryObject<>(() -> new StairBlockExtends(getGenericState(), BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_BLOCKS);
		MORDEN_STONE = LondonUndergroundRegistry.registerBlockAndItem("morden_stone", new RegistryObject<>(() -> new MordenBlock(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_BLOCKS);
		MORDEN_SLAB = LondonUndergroundRegistry.registerBlockAndItem("morden_slab", new RegistryObject<>(() -> new SlabBlock(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_BLOCKS);
		MORDEN_COBBLE_SLAB = LondonUndergroundRegistry.registerBlockAndItem("morden_cobble_slab", new RegistryObject<>(() -> new SlabBlock(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_BLOCKS);
		MORDEN_COBBLESTONE = LondonUndergroundRegistry.registerBlockAndItem("morden_cobblestone", new RegistryObject<>(() -> new MordenBlock(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_BLOCKS);
		MORDEN_ARCH_NEW = LondonUndergroundRegistry.registerBlockAndItem("morden_arch_new", new RegistryObject<>(() -> new TunnelA4(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_STATION);
		MORDEN_ARCH_ROOF = LondonUndergroundRegistry.registerBlockAndItem("morden_arch_roof", new RegistryObject<>(() -> new TunnelA5(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_STATION);
		MORDEN_SIGN = LondonUndergroundRegistry.registerBlockAndItem("morden_sign", new RegistryObject<>(() -> new MordenSign(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		METROPOLITAN_SIGN = LondonUndergroundRegistry.registerBlockAndItem("metropolitan_sign", new RegistryObject<>(() -> new MetropolitanSign(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		ELIZABETH_SIGN = LondonUndergroundRegistry.registerBlockAndItem("elizabeth_sign", new RegistryObject<>(() -> new ElizabethSign(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_SIGNS);

		SIGN_RIVER = LondonUndergroundRegistry.registerBlockAndItem("sign_river", new RegistryObject<>(() -> new SignRiver(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		SIGN_OVERGROUND = LondonUndergroundRegistry.registerBlockAndItem("sign_overground", new RegistryObject<>(() -> new SignOverground(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		SIGN_DLR = LondonUndergroundRegistry.registerBlockAndItem("sign_dlr", new RegistryObject<>(() -> new SignDlr(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		SIGN_TRAMS = LondonUndergroundRegistry.registerBlockAndItem("sign_trams", new RegistryObject<>(() -> new SignTrams(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		SIGN_POPPY = LondonUndergroundRegistry.registerBlockAndItem("sign_poppy", new RegistryObject<>(() -> new SignPoppy(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		SIGN_METRO = LondonUndergroundRegistry.registerBlockAndItem("sign_metro", new RegistryObject<>(() -> new SignMetro(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		SIGN_LIZZY = LondonUndergroundRegistry.registerBlockAndItem("sign_lizzy", new RegistryObject<>(() -> new SignLizzy(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		SIGN_UNDERGROUND = LondonUndergroundRegistry.registerBlockAndItem("sign_underground", new RegistryObject<>(() -> new SignUnderground(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		SIGN_PRIDE = LondonUndergroundRegistry.registerBlockAndItem("sign_pride", new RegistryObject<>(() -> new SignPride(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);

		LU_POLE = LondonUndergroundRegistry.registerBlockAndItem("lu_pole", new RegistryObject<>(() -> new BlockLUPole(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_BLOCKS);
		LU_CROSSBAR = LondonUndergroundRegistry.registerBlockAndItem("lu_crossbar", new RegistryObject<>(() -> new BlockLUCrossbar(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_BLOCKS);
		BLOCK_ROUNDEL_1 = LondonUndergroundRegistry.registerBlockAndItem("block_roundel_1", new RegistryObject<>(() -> new BlockRoundel(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		BLOCK_ROUNDEL_NLE = LondonUndergroundRegistry.registerBlockAndItem("block_roundel_nle", new RegistryObject<>(() -> new BlockRoundelNLE(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		BRITISH_RAIL_UNDERGROUND = LondonUndergroundRegistry.registerBlockAndItem("british_rail_underground", new RegistryObject<>(() -> new BritishRailUnderground(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		BLOCK_ROUNDEL_1_BIG = LondonUndergroundRegistry.registerBlockAndItem("block_roundel_1_big", new RegistryObject<>(() -> new BlockRoundelBig(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		BLOCK_ROUNDEL_1_BIG_EVEN = LondonUndergroundRegistry.registerBlockAndItem("block_roundel_1_big_even", new RegistryObject<>(() -> new BlockRoundelBigEven(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		BLOCK_ROUNDEL_2 = LondonUndergroundRegistry.registerBlockAndItem("block_roundel_2", new RegistryObject<>(() -> new BlockRoundel2(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		BLOCK_ROUNDEL_2_BIG = LondonUndergroundRegistry.registerBlockAndItem("block_roundel_2_big", new RegistryObject<>(() -> new BlockRoundel2Big(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		BLOCK_ROUNDEL_2_BIG_EVEN = LondonUndergroundRegistry.registerBlockAndItem("block_roundel_2_big_even", new RegistryObject<>(() -> new BlockRoundel2BigEven(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		BLOCK_ROUNDEL_3 = LondonUndergroundRegistry.registerBlockAndItem("block_roundel_3", new RegistryObject<>(() -> new BlockRoundel3(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		BLOCK_ROUNDEL_3_BIG = LondonUndergroundRegistry.registerBlockAndItem("block_roundel_3_big", new RegistryObject<>(() -> new BlockRoundel3Big(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		BLOCK_ROUNDEL_3_BIG_EVEN = LondonUndergroundRegistry.registerBlockAndItem("block_roundel_3_big_even", new RegistryObject<>(() -> new BlockRoundel3BigEven(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		BLOCK_ROUNDEL_4 = LondonUndergroundRegistry.registerBlockAndItem("block_roundel_4", new RegistryObject<>(() -> new BlockRoundel4(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		BLOCK_ROUNDEL_4_BIG = LondonUndergroundRegistry.registerBlockAndItem("block_roundel_4_big", new RegistryObject<>(() -> new BlockRoundel4Big(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		BLOCK_ROUNDEL_4_BIG_EVEN = LondonUndergroundRegistry.registerBlockAndItem("block_roundel_4_big_even", new RegistryObject<>(() -> new BlockRoundel4BigEven(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		BLOCK_ROUNDEL_5 = LondonUndergroundRegistry.registerBlockAndItem("block_roundel_5", new RegistryObject<>(() -> new BlockRoundel5(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		BLOCK_ROUNDEL_5_BIG = LondonUndergroundRegistry.registerBlockAndItem("block_roundel_5_big", new RegistryObject<>(() -> new BlockRoundel5Big(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);
		BLOCK_ROUNDEL_5_BIG_EVEN = LondonUndergroundRegistry.registerBlockAndItem("block_roundel_5_big_even", new RegistryObject<>(() -> new BlockRoundel5BigEven(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_SIGNS);

		MORDEN_STEPS_LEFT = LondonUndergroundRegistry.registerBlockAndItem("morden_steps_left", new RegistryObject<>(() -> new MordenSteps(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_BLOCKS);
		MORDEN_STEPS_MIDDLE = LondonUndergroundRegistry.registerBlockAndItem("morden_steps_middle", new RegistryObject<>(() -> new MordenSteps(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_BLOCKS);
		MORDEN_STEPS_RIGHT = LondonUndergroundRegistry.registerBlockAndItem("morden_steps_right", new RegistryObject<>(() -> new MordenSteps(BlockBehaviour.Properties.of())), CreativeModeTabs.TFL_BLOCKS);

		NAME_PROJECTOR = LondonUndergroundRegistry.registerBlockAndItem("name_projector", new RegistryObject<>(() -> new NameProjector(BlockBehaviour.Properties.of().noOcclusion())), CreativeModeTabs.TFL_BLOCKS);

		//
		// Legacy Items
		//

		TRACK_1 = LondonUndergroundRegistry.registerBlockAndItem("track_1", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));
		TRACK_2 = LondonUndergroundRegistry.registerBlockAndItem("track_2", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));
		TUBE_1 = LondonUndergroundRegistry.registerBlockAndItem("tube_1", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));
		TUBE_1_STOP = LondonUndergroundRegistry.registerBlockAndItem("tube_1_stop", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));
		TUBE_1_DUEL = LondonUndergroundRegistry.registerBlockAndItem("tube_1_duel", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));
		TUBE_2 = LondonUndergroundRegistry.registerBlockAndItem("tube_2", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));
		TUBE_2_DUEL = LondonUndergroundRegistry.registerBlockAndItem("tube_2_duel", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));
		TUBE_3 = LondonUndergroundRegistry.registerBlockAndItem("tube_3", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));
		TUBE_1b = LondonUndergroundRegistry.registerBlockAndItem("tube_1b", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));
		TUBE_1b_STOP = LondonUndergroundRegistry.registerBlockAndItem("tube_1b_stop", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));
		TUBE_1b_DUEL = LondonUndergroundRegistry.registerBlockAndItem("tube_1b_duel", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));
		TUBE_2b = LondonUndergroundRegistry.registerBlockAndItem("tube_2b", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));
		TUBE_2b_DUEL = LondonUndergroundRegistry.registerBlockAndItem("tube_2b_duel", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));
		TUBE_3b = LondonUndergroundRegistry.registerBlockAndItem("tube_3b", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));

		MORDEN_STEPS = LondonUndergroundRegistry.registerBlockAndItem("morden_arch", new RegistryObject<>(() -> new StairBlockExtends(getGenericState(), BlockBehaviour.Properties.of().noOcclusion())));
		MORDEN_ARCH = LondonUndergroundRegistry.registerBlockAndItem("morden_steps", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));

		TUNNEL_BLOCK_0 = LondonUndergroundRegistry.registerBlockAndItem("tunnel_block_0", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion())));
		TUNNEL_BLOCK_1 = LondonUndergroundRegistry.registerBlockAndItem("tunnel_block_1", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));
		TUNNEL_BLOCK_1_DUEL = LondonUndergroundRegistry.registerBlockAndItem("tunnel_block_1_duel", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));
		TUNNEL_BLOCK_2 = LondonUndergroundRegistry.registerBlockAndItem("tunnel_block_2", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));
		TUNNEL_BLOCK_2_SIGNAL = LondonUndergroundRegistry.registerBlockAndItem("tunnel_block_2_duel", new RegistryObject<>(() -> new BlockTunnelSignal(BlockBehaviour.Properties.of().noOcclusion())));
		TUNNEL_BLOCK_2_DUEL = LondonUndergroundRegistry.registerBlockAndItem("tunnel_block_2_signal", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));
		TUNNEL_BLOCK_3 = LondonUndergroundRegistry.registerBlockAndItem("tunnel_block_3", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));
		TUNNEL_BLOCK_3_DUEL = LondonUndergroundRegistry.registerBlockAndItem("tunnel_block_3_duel", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));
		TUNNEL_BLOCK_4 = LondonUndergroundRegistry.registerBlockAndItem("tunnel_block_4", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));
		TUNNEL_BLOCK_5 = LondonUndergroundRegistry.registerBlockAndItem("tunnel_block_5", new RegistryObject<>(() -> new LUDirectionalBlock(BlockBehaviour.Properties.of().noOcclusion())));

		BLOCK_ROUNDEL_STATION = LondonUndergroundRegistry.registerBlockAndItem("block_roundel_station", new RegistryObject<>(() -> new BlockRoundelStation(BlockBehaviour.Properties.of().noOcclusion())));
		BLOCK_ROUNDEL_STATION_TYPE_B = LondonUndergroundRegistry.registerBlockAndItem("block_roundel_station_type_b", new RegistryObject<>(() -> new BlockRoundelStationTypeB(BlockBehaviour.Properties.of().noOcclusion())));
		BLOCK_ROUNDEL_STATION_TYPE_C = LondonUndergroundRegistry.registerBlockAndItem("block_roundel_station_type_c", new RegistryObject<>(() -> new BlockRoundelStationTypeC(BlockBehaviour.Properties.of().noOcclusion())));
		BLOCK_ROUNDEL_STATION_TOP = LondonUndergroundRegistry.registerBlockAndItem("block_roundel_station_top", new RegistryObject<>(() -> new BlockRoundelStationTop(BlockBehaviour.Properties.of().noOcclusion())));
	}

	public static final RegistryObject<Block> PLATFORM_TFL_1;
	public static final RegistryObject<Block> PLATFORM_TFL_ISLAND;
	public static final RegistryObject<Block> PLATFORM_TFL_GAP;
	public static final RegistryObject<Block> PLATFORM_BLOCK;
	public static final RegistryObject<Block> PLATFORM_VARIANT;
	public static final RegistryObject<Block> TUNNEL_DARKNESS;
	public static final RegistryObject<Block> TFL_BLOCK;
	public static final RegistryObject<Block> SOUND_OUTSIDE_AMBIENCE;
	public static final RegistryObject<Block> TUBE_STATION_AMBIENCE1;
	public static final RegistryObject<Block> SOUND_SEE_IT_SAY_IT_SORTED;
	public static final RegistryObject<Block> STATION_LIGHT;
	public static final RegistryObject<Block> EXIT_SIGN;
	public static final RegistryObject<Block> TUNNEL_ENTRANCE_1;
	public static final RegistryObject<Block> TUNNEL_A0;
	public static final RegistryObject<Block> TUNNEL_A0_MORDEN;
	public static final RegistryObject<Block> TUNNEL_A1;
	public static final RegistryObject<Block> TUNNEL_A1_MORDEN;
	public static final RegistryObject<Block> TUNNEL_A1_DUEL;
	public static final RegistryObject<Block> TUNNEL_A2;
	public static final RegistryObject<Block> TUNNEL_A2_NOWIRES;
	public static final RegistryObject<Block> TUNNEL_A2_LIGHT;
	public static final RegistryObject<Block> TUNNEL_A2_LIGHT_WHITE;
	public static final RegistryObject<Block> TUNNEL_A2_NOWIRES_MORDEN;
	public static final RegistryObject<Block> TUNNEL_A2_SIGNAL;
	public static final RegistryObject<Block> TUNNEL_A2_DUEL;
	public static final RegistryObject<Block> TUNNEL_A3;
	public static final RegistryObject<Block> TUNNEL_A3_NOWIRES;
	public static final RegistryObject<Block> TUNNEL_A3_NOWIRES_MORDEN;
	public static final RegistryObject<Block> TUNNEL_WIRES;
	public static final RegistryObject<Block> TUNNEL_A3_DUEL;
	public static final RegistryObject<Block> TUNNEL_A4;
	public static final RegistryObject<Block> TUNNEL_A5;
	public static final RegistryObject<Block> STATION_A1;
	public static final RegistryObject<Block> STATION_A2;
	public static final RegistryObject<Block> STATION_A3_GREEN;
	public static final RegistryObject<Block> STATION_A3;
	public static final RegistryObject<Block> STATION_A4;
	public static final RegistryObject<Block> STATION_A5;
	public static final RegistryObject<Block> STATION_A6;
	public static final RegistryObject<Block> STATION_A7;
	public static final RegistryObject<Block> STATION_A8;
	public static final RegistryObject<Block> STATION_A9;
	public static final RegistryObject<Block> STATION_A9_PIDPOLE;
	public static final RegistryObject<Block> STATION_A1b;
	public static final RegistryObject<Block> STATION_A2b;
	public static final RegistryObject<Block> STATION_A3b_GREEN;
	public static final RegistryObject<Block> STATION_A3b;
	public static final RegistryObject<Block> STATION_A4b;
	public static final RegistryObject<Block> STATION_A5b;
	public static final RegistryObject<Block> STATION_A6b;
	public static final RegistryObject<Block> STATION_A7b;
	public static final RegistryObject<Block> STATION_A8b;
	public static final RegistryObject<Block> STATION_A9b;
	public static final RegistryObject<Block> STATION_A9b_PIDPOLE;
	public static final RegistryObject<Block> TUBE_0;
	public static final RegistryObject<Block> TUBE_0_SLAB;
	public static final RegistryObject<Block> TUBE_0b;
	public static final RegistryObject<Block> TUBE_0b_SLAB;
	public static final RegistryObject<Block> PIDS_POLE;
	public static final RegistryObject<Block> ROUNDEL_POLE;
	public static final RegistryObject<Block> ROUNDEL_POLE_DLR;
	public static final RegistryObject<Block> ROUNDEL_POLE_LIZ;
	public static final RegistryObject<Block> ROUNDEL_POLE_OVERGROUND;
	public static final RegistryObject<Block> NORTHERN_PIDS;
	public static final RegistryObject<Block> MORDEN_STAIRS;
	public static final RegistryObject<Block> MORDEN_STONE;
	public static final RegistryObject<Block> MORDEN_SLAB;
	public static final RegistryObject<Block> MORDEN_COBBLE_SLAB;
	public static final RegistryObject<Block> MORDEN_COBBLESTONE;
	public static final RegistryObject<Block> MORDEN_ARCH_NEW;
	public static final RegistryObject<Block> MORDEN_ARCH_ROOF;
	public static final RegistryObject<Block> MORDEN_SIGN;
	public static final RegistryObject<Block> METROPOLITAN_SIGN;
	public static final RegistryObject<Block> ELIZABETH_SIGN;
	public static final RegistryObject<Block> SIGN_RIVER;
	public static final RegistryObject<Block> SIGN_OVERGROUND;
	public static final RegistryObject<Block> SIGN_DLR;
	public static final RegistryObject<Block> SIGN_TRAMS;
	public static final RegistryObject<Block> SIGN_POPPY;
	public static final RegistryObject<Block> SIGN_METRO;
	public static final RegistryObject<Block> SIGN_LIZZY;
	public static final RegistryObject<Block> SIGN_UNDERGROUND;
	public static final RegistryObject<Block> SIGN_PRIDE;
	public static final RegistryObject<Block> LU_POLE;
	public static final RegistryObject<Block> BLOCK_ROUNDEL_1;
	public static final RegistryObject<Block> BLOCK_ROUNDEL_NLE;
	public static final RegistryObject<Block> BRITISH_RAIL_UNDERGROUND;
	public static final RegistryObject<Block> BLOCK_ROUNDEL_1_BIG;
	public static final RegistryObject<Block> BLOCK_ROUNDEL_1_BIG_EVEN;
	public static final RegistryObject<Block> BLOCK_ROUNDEL_2;
	public static final RegistryObject<Block> BLOCK_ROUNDEL_2_BIG;
	public static final RegistryObject<Block> BLOCK_ROUNDEL_2_BIG_EVEN;
	public static final RegistryObject<Block> BLOCK_ROUNDEL_3;
	public static final RegistryObject<Block> BLOCK_ROUNDEL_3_BIG;
	public static final RegistryObject<Block> BLOCK_ROUNDEL_3_BIG_EVEN;
	public static final RegistryObject<Block> BLOCK_ROUNDEL_4;
	public static final RegistryObject<Block> BLOCK_ROUNDEL_4_BIG;
	public static final RegistryObject<Block> BLOCK_ROUNDEL_4_BIG_EVEN;
	public static final RegistryObject<Block> BLOCK_ROUNDEL_5;
	public static final RegistryObject<Block> BLOCK_ROUNDEL_5_BIG;
	public static final RegistryObject<Block> BLOCK_ROUNDEL_5_BIG_EVEN;
	public static final RegistryObject<Block> LU_CROSSBAR;
	public static final RegistryObject<Block> MORDEN_STEPS_LEFT;
	public static final RegistryObject<Block> MORDEN_STEPS_MIDDLE;
	public static final RegistryObject<Block> MORDEN_STEPS_RIGHT;
	public static final RegistryObject<Block> NAME_PROJECTOR;
	public static final RegistryObject<Block> TRACK_1;
	public static final RegistryObject<Block> TRACK_2;
	public static final RegistryObject<Block> TUBE_1;
	public static final RegistryObject<Block> TUBE_1_STOP;
	public static final RegistryObject<Block> TUBE_1_DUEL;
	public static final RegistryObject<Block> TUBE_2;
	public static final RegistryObject<Block> TUBE_2_DUEL;
	public static final RegistryObject<Block> TUBE_3;
	public static final RegistryObject<Block> TUBE_1b;
	public static final RegistryObject<Block> TUBE_1b_STOP;
	public static final RegistryObject<Block> TUBE_1b_DUEL;
	public static final RegistryObject<Block> TUBE_2b;
	public static final RegistryObject<Block> TUBE_2b_DUEL;
	public static final RegistryObject<Block> TUBE_3b;
	public static final RegistryObject<Block> MORDEN_STEPS;
	public static final RegistryObject<Block> MORDEN_ARCH;
	public static final RegistryObject<Block> TUNNEL_BLOCK_0;
	public static final RegistryObject<Block> TUNNEL_BLOCK_1;
	public static final RegistryObject<Block> TUNNEL_BLOCK_1_DUEL;
	public static final RegistryObject<Block> TUNNEL_BLOCK_2;
	public static final RegistryObject<Block> TUNNEL_BLOCK_2_SIGNAL;
	public static final RegistryObject<Block> TUNNEL_BLOCK_2_DUEL;
	public static final RegistryObject<Block> TUNNEL_BLOCK_3;
	public static final RegistryObject<Block> TUNNEL_BLOCK_3_DUEL;
	public static final RegistryObject<Block> TUNNEL_BLOCK_4;
	public static final RegistryObject<Block> TUNNEL_BLOCK_5;
	public static final RegistryObject<Block> BLOCK_ROUNDEL_STATION;
	public static final RegistryObject<Block> BLOCK_ROUNDEL_STATION_TYPE_B;
	public static final RegistryObject<Block> BLOCK_ROUNDEL_STATION_TYPE_C;
	public static final RegistryObject<Block> BLOCK_ROUNDEL_STATION_TOP;

	public static void init() {
		LondonUnderground.LOGGER.info("Registering MTR London Underground Addon blocks");
	}

	private static BlockState getGenericState() {
		return net.minecraft.world.level.block.Blocks.DIRT.defaultBlockState();
	}
}
