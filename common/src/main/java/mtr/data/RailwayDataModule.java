package mtr.data;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Map;

public abstract class RailwayDataModule {

	protected final String name;
	protected final RailwayData railwayData;
	protected final Level level;
	protected final Map<BlockPos, Map<BlockPos, Rail>> rails;

	public RailwayDataModule(String name, RailwayData railwayData, Level level, Map<BlockPos, Map<BlockPos, Rail>> rails) {
		this.name = name;
		this.railwayData = railwayData;
		this.level = level;
		this.rails = rails;
	}

	public void load() {

	}

	public void tick() {

	}

	public void lateTick() {

	}

	protected void save() {

	}

	public void fullSave() {
		save();
	}

	public void autoSave() {
		save();
	}

	public void onPlayerJoin(ServerPlayer player) {

	}

	public void onPlayerDisconnect(Player player) {
	}

	public String getName() {
		return this.name;
	}

	public String toString() {
		return "RailwayDataModule[name=" + this.name + "]";
	}
}
