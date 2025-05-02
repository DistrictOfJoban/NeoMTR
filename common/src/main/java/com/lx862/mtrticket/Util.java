package com.lx862.mtrticket;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.numbers.BlankFormat;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.ScoreAccess;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

public class Util {
    public static ScoreAccess getScore(String objName, String playerName, Level world) {
        try {
            world.getScoreboard().addObjective(objName, ObjectiveCriteria.DUMMY, Component.literal(objName), ObjectiveCriteria.RenderType.INTEGER, false, new BlankFormat());
        } catch (Exception ignored) {
        }

        return world.getScoreboard().getOrCreatePlayerScore(ScoreHolder.forNameOnly(playerName), world.getScoreboard().getObjective(objName));
    }

    public static boolean hasPerm(Player player) {
        return player.hasPermissions(2);
    }

    public static void sendPlaySoundIdS2CPacket(Level world, ServerPlayer player, ResourceLocation sound, SoundSource soundCategory, Vec3 pos, float volume, float pitch) {
        player.connection.send(new ClientboundSoundPacket(Holder.direct(SoundEvent.createVariableRangeEvent(sound)), soundCategory, pos.x, pos.y, pos.z, volume, pitch, world.getRandom().nextLong()));
    }
}
