package mtr.data;

import mtr.MTR;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class RailwayDataPathGenerationModule extends RailwayDataModule {

	public static final String NAME = "path_generation";
	private final Map<Long, Thread> generatingPathThreads = new HashMap<>();

	public RailwayDataPathGenerationModule(RailwayData railwayData, Level world, Map<BlockPos, Map<BlockPos, Rail>> rails) {
		super(NAME, railwayData, world, rails);
	}

	public void generatePath(MinecraftServer minecraftServer, long depotId) {
		generatingPathThreads.keySet().removeIf(id -> !generatingPathThreads.get(id).isAlive());
		final Depot depot = railwayData.dataCache.depotIdMap.get(depotId);
		if (depot != null) {
			if (generatingPathThreads.containsKey(depotId)) {
				generatingPathThreads.get(depotId).interrupt();
				MTR.LOGGER.info("[NeoMTR] Restarting path generation {}", (depot.name.isEmpty() ? "" : " for " + depot.name));
			} else {
				MTR.LOGGER.info("[NeoMTR] Starting path generation {}", (depot.name.isEmpty() ? "" : " for " + depot.name));
			}
			depot.generateMainRoute(minecraftServer, level, railwayData.dataCache, rails, railwayData.sidings, thread -> generatingPathThreads.put(depotId, thread));
			railwayData.resetTrainDelays(depot);
		} else {
			PacketTrainDataGuiServer.generatePathS2C(level, depotId, 0);
			MTR.LOGGER.info("[NeoMTR] Failed to generate path, depot is null");
		}
	}
}
