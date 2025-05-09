package mtr.data;

import mtr.mappings.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.ScoreAccess;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

public class TicketSystem {

	public static final String BALANCE_OBJECTIVE = "mtr_balance";
	private static final String ENTRY_ZONE_OBJECTIVE = "mtr_entry_zone";
	private static final int BASE_FARE = 2;
	private static final int ZONE_FARE = 1;
	private static final int EVASION_FINE = 500;

	public static EnumTicketBarrierOpen passThrough(Level world, BlockPos pos, Player player, boolean isEntrance, boolean isExit, SoundEvent entrySound, SoundEvent entrySoundConcessionary, SoundEvent exitSound, SoundEvent exitSoundConcessionary, SoundEvent failSound, boolean remindIfNoRecord) {
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData == null) {
			return EnumTicketBarrierOpen.CLOSED;
		}

		final Station station = RailwayData.getStation(railwayData.stations, railwayData.dataCache, pos);
		if (station == null) {
			return EnumTicketBarrierOpen.CLOSED;
		}

		addObjectivesIfMissing(world);

		final ScoreAccess balanceScore = getPlayerScore(world, player, BALANCE_OBJECTIVE);
		final ScoreAccess entryZoneScore = getPlayerScore(world, player, ENTRY_ZONE_OBJECTIVE);

		final boolean isEntering;
		if (isEntrance && isExit) {
			isEntering = entryZoneScore.get() == 0;
		} else {
			isEntering = isEntrance;
		}

		final boolean canOpen;
		if (isEntering) {
			canOpen = onEnter(station, player, balanceScore, entryZoneScore, remindIfNoRecord);
		} else {
			canOpen = onExit(station, player, balanceScore, entryZoneScore, remindIfNoRecord);
		}

		if (canOpen) {
			world.playSound(null, pos, isConcessionary(player) ? (isEntering ? entrySoundConcessionary : exitSoundConcessionary) : (isEntering ? entrySound : exitSound), SoundSource.BLOCKS, 1, 1);
		} else if (failSound != null) {
			world.playSound(null, pos, failSound, SoundSource.BLOCKS, 1, 1);
		}

		return canOpen ? isConcessionary(player) ? EnumTicketBarrierOpen.OPEN_CONCESSIONARY : EnumTicketBarrierOpen.OPEN : EnumTicketBarrierOpen.CLOSED;
	}

	public static void addObjectivesIfMissing(Level world) {
		try {
			world.getScoreboard().addObjective(BALANCE_OBJECTIVE, ObjectiveCriteria.DUMMY, Text.literal("Balance"), ObjectiveCriteria.RenderType.INTEGER, true, null);
		} catch (Exception ignored) {
		}
		try {
			world.getScoreboard().addObjective(ENTRY_ZONE_OBJECTIVE, ObjectiveCriteria.DUMMY, Text.literal("Entry Zone"), ObjectiveCriteria.RenderType.INTEGER, true, null);
		} catch (Exception ignored) {
		}
	}

	public static ScoreAccess getPlayerScore(Level world, Player player, String objectiveName) {
		Objective objective = world.getScoreboard().getObjective(objectiveName);
		if (objective == null) {
			addObjectivesIfMissing(world);
			objective = world.getScoreboard().getObjective(objectiveName);
		}
		return world.getScoreboard().getOrCreatePlayerScore(player, objective);
	}

	private static boolean onEnter(Station station, Player player, ScoreAccess balanceScore, ScoreAccess entryZoneScore, boolean remindIfNoRecord) {
		final int entryZone = entryZoneScore.get();

		if (entryZone != 0) {
			if (remindIfNoRecord) {
				player.displayClientMessage(Text.translatable("gui.mtr.already_entered"), true);
				return false;
			} else {
				entryZoneScore.set(0);
				balanceScore.add(-EVASION_FINE);
			}
		}

		if (balanceScore.get() >= 0) {
			entryZoneScore.set(encodeZone(station.zone));
			player.displayClientMessage(Text.translatable("gui.mtr.enter_barrier", String.format("%s (%s)", station.name.replace('|', ' '), station.zone), balanceScore.get()), true);
			return true;
		} else {
			player.displayClientMessage(Text.translatable("gui.mtr.insufficient_balance", balanceScore.get()), true);
			return false;
		}
	}

	private static boolean onExit(Station station, Player player, ScoreAccess balanceScore, ScoreAccess entryZoneScore, boolean remindIfNoRecord) {
		final int entryZone = entryZoneScore.get();
		final int fare = BASE_FARE + ZONE_FARE * Math.abs(station.zone - decodeZone(entryZone));
		final int finalFare = entryZone != 0 ? isConcessionary(player) ? (int) Math.ceil(fare / 2F) : fare : EVASION_FINE;

		if (entryZone == 0 && remindIfNoRecord) {
			player.displayClientMessage(Text.translatable("gui.mtr.already_exited"), true);
			return false;
		} else {
			entryZoneScore.set(0);
			balanceScore.add(-finalFare);
			player.displayClientMessage(Text.translatable("gui.mtr.exit_barrier", String.format("%s (%s)", station.name.replace('|', ' '), station.zone), finalFare, balanceScore.get()), true);
			return true;
		}
	}

	public static void addBalance(Level level, ServerPlayer player, int amount) {
		addObjectivesIfMissing(level);
		ScoreAccess balanceScore = TicketSystem.getPlayerScore(level, player, TicketSystem.BALANCE_OBJECTIVE);
		balanceScore.add(amount);
	}

	private static boolean isConcessionary(Player player) {
		return player.isCreative();
	}

	private static int encodeZone(int zone) {
		return zone >= 0 ? zone + 1 : zone;
	}

	private static int decodeZone(int zone) {
		return zone > 0 ? zone - 1 : zone;
	}

	public enum EnumTicketBarrierOpen implements StringRepresentable {

		CLOSED("closed"), OPEN("open"), OPEN_CONCESSIONARY("open_concessionary");
		private final String name;

		EnumTicketBarrierOpen(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getSerializedName() {
			return name;
		}

		public boolean isOpen() {
			return this != CLOSED;
		}
	}
}
