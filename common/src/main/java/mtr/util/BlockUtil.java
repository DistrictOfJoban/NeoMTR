package mtr.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BlockUtil {
    public static BlockPos newBlockPos(int x, int y, int z) {
        return new BlockPos(x, y, z);
    }

    public static BlockPos newBlockPos(double x, double y, double z) {
        return newBlockPos(Mth.floor(x), Mth.floor(y), Mth.floor(z));
    }

    public static boolean chunkLoaded(Level world, BlockPos pos) {
        return world.getChunkSource().getChunkNow(pos.getX() / 16, pos.getZ() / 16) != null && world.hasChunk(pos.getX() / 16, pos.getZ() / 16);
    }

    public static BlockPos newBlockPos(Vec3 vec3) {
        return newBlockPos(vec3.x, vec3.y, vec3.z);
    }

    public static BlockPos offsetBlockPos(BlockPos pos, double x, double y, double z) {
        return x == 0 && y == 0 && z == 0 ? pos : newBlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
    }
}
