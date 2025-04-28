package mtr.api;

import mtr.data.Rail;
import mtr.data.RailwayData;
import net.minecraft.core.BlockPos;
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

	public void earlyInit() {

	}

	public void init() {

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

	public String getName() {
		return this.name;
	}

	public String toString() {
		return "RailwayDataModule[name=" + this.name + "]";
	}
}
