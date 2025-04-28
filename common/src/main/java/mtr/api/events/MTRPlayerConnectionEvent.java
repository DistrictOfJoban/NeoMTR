package mtr.api.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public interface MTRPlayerConnectionEvent {
    void onPlayerConnect(ServerPlayer player);
    void onPlayerDisconnect(Player player);
}
