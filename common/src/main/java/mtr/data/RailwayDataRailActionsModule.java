package mtr.data;

import mtr.api.RailwayDataModule;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RailwayDataRailActionsModule extends RailwayDataModule {

	public static final String NAME = "rail_action";
	private final List<Rail.RailActions> railActions = new ArrayList<>();

	public RailwayDataRailActionsModule(RailwayData railwayData, Level world, Map<BlockPos, Map<BlockPos, Rail>> rails) {
		super(NAME, railwayData, world, rails);
	}

	public void tick() {
		if (!railActions.isEmpty() && railActions.get(0).build()) {
			railActions.remove(0);
			PacketTrainDataGuiServer.updateRailActionsS2C(level, railActions);
		}
	}

	public boolean markRailForBridge(Player player, BlockPos pos1, BlockPos pos2, int radius, BlockState state) {
		if (railwayData.containsRail(pos1, pos2)) {
			railActions.add(new Rail.RailActions(level, player, Rail.RailActionType.BRIDGE, rails.get(pos1).get(pos2), radius, 0, state));
			PacketTrainDataGuiServer.updateRailActionsS2C(level, railActions);
			return true;
		} else {
			return false;
		}
	}

	public boolean markRailForTunnel(Player player, BlockPos pos1, BlockPos pos2, int radius, int height) {
		if (railwayData.containsRail(pos1, pos2)) {
			railActions.add(new Rail.RailActions(level, player, Rail.RailActionType.TUNNEL, rails.get(pos1).get(pos2), radius, height, null));
			PacketTrainDataGuiServer.updateRailActionsS2C(level, railActions);
			return true;
		} else {
			return false;
		}
	}

	public boolean markRailForTunnelWall(Player player, BlockPos pos1, BlockPos pos2, int radius, int height, BlockState state) {
		if (railwayData.containsRail(pos1, pos2)) {
			railActions.add(new Rail.RailActions(level, player, Rail.RailActionType.TUNNEL_WALL, rails.get(pos1).get(pos2), radius + 1, height + 1, state));
			PacketTrainDataGuiServer.updateRailActionsS2C(level, railActions);
			return true;
		} else {
			return false;
		}
	}

	public void removeRailAction(long id) {
		railActions.removeIf(railAction -> railAction.id == id);
		PacketTrainDataGuiServer.updateRailActionsS2C(level, railActions);
	}
}
