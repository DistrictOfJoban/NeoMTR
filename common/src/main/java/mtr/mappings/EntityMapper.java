package mtr.mappings;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class EntityMapper extends Entity {

	public EntityMapper(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

}
