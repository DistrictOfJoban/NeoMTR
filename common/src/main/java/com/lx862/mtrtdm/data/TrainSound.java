package com.lx862.mtrtdm.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import mtr.registry.Items;
import mtr.MTR;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

import java.util.Set;
import java.util.UUID;

public class TrainSound {
    public String id;
    public int loopTick;

    public TrainSound(JsonElement jsonElement) {
        try {
            id = jsonElement.getAsString();
            loopTick = -1;
        } catch (Exception e) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            loopTick = jsonArray.get(0).getAsInt();
            id = jsonArray.get(1).getAsString();
        }
    }

    public boolean loopable() {
        return loopTick > 0;
    }

    public void play(Set<UUID> players, boolean playIfNotLoopable, Level world) {
        boolean shouldPlay = loopable() ? true : playIfNotLoopable;
        int loopInterval = loopable() ? loopTick : 1;

        if(shouldPlay && MTR.isGameTickInterval(loopInterval)) {
            for (UUID uuid : players) {
                ServerPlayer player = (ServerPlayer) world.getPlayerByUUID(uuid);
                if(player == null) continue;

                if (player.isHolding(Items.DRIVER_KEY.get())) {
                    player.connection.send(new ClientboundSoundPacket(Holder.direct(SoundEvent.createVariableRangeEvent(ResourceLocation.tryParse(id))), SoundSource.MASTER, player.position().x, player.position().y, player.position().z, 10000000, 1, world.getRandom().nextLong()));
                }
            }
        }
    }
}
