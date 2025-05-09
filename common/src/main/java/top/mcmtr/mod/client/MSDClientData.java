package top.mcmtr.mod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import top.mcmtr.mod.data.Catenary;
import top.mcmtr.mod.data.RigidCatenary;
import top.mcmtr.mod.data.TransCatenary;

import java.util.HashMap;
import java.util.Map;

public class MSDClientData {
    public static final Map<BlockPos, Map<BlockPos, Catenary>> CATENARIES = new HashMap<>();
    public static final Map<BlockPos, Map<BlockPos, RigidCatenary>> RIGID_CATENARIES = new HashMap<>();
    public static final Map<BlockPos, Map<BlockPos, TransCatenary>> TRANS_CATENARIES = new HashMap<>();

    /**
     * 上文:此前会检测玩家位置，之后将玩家视野范围之内的接触网信息通过此方法写入到常量Map，用于渲染器循环遍历读取。
     */
    public static void writeCatenaries(Minecraft client, FriendlyByteBuf packet) {
        final Map<BlockPos, Map<BlockPos, Catenary>> catenariesTemp = new HashMap<>();
        final int catenariesCount = packet.readInt();
        for (int i = 0; i < catenariesCount; i++) {
            final BlockPos startPos = packet.readBlockPos();
            final Map<BlockPos, Catenary> catenaryMap = new HashMap<>();
            final int catenaryCount = packet.readInt();
            for (int j = 0; j < catenaryCount; j++) {
                catenaryMap.put(packet.readBlockPos(), new Catenary(packet));
            }
            catenariesTemp.put(startPos, catenaryMap);
        }
        client.execute(() -> clearAndAddAll(CATENARIES, catenariesTemp));
    }

    public static void writeRigidCatenaries(Minecraft client, FriendlyByteBuf packet) {
        final Map<BlockPos, Map<BlockPos, RigidCatenary>> rigidCatenariesTemp = new HashMap<>();
        final int rigidCatenariesCount = packet.readInt();
        for (int i = 0; i < rigidCatenariesCount; i++) {
            final BlockPos startPos = packet.readBlockPos();
            final Map<BlockPos, RigidCatenary> rigidCatenaryMap = new HashMap<>();
            final int rigidCatenaryCount = packet.readInt();
            for (int j = 0; j < rigidCatenaryCount; j++) {
                rigidCatenaryMap.put(packet.readBlockPos(), new RigidCatenary(packet));
            }
            rigidCatenariesTemp.put(startPos, rigidCatenaryMap);
        }
        client.execute(() -> clearAndAddAll(RIGID_CATENARIES, rigidCatenariesTemp));
    }

    public static void writeTransCatenaries(Minecraft client, FriendlyByteBuf packet) {
        final Map<BlockPos, Map<BlockPos, TransCatenary>> transCatenariesTemp = new HashMap<>();
        final int transCatenariesCount = packet.readInt();
        for (int i = 0; i < transCatenariesCount; i++) {
            final BlockPos startPos = packet.readBlockPos();
            final Map<BlockPos, TransCatenary> transCatenaryMap = new HashMap<>();
            final int transCatenaryCount = packet.readInt();
            for (int j = 0; j < transCatenaryCount; j++) {
                transCatenaryMap.put(packet.readBlockPos(), new TransCatenary(packet));
            }
            transCatenariesTemp.put(startPos, transCatenaryMap);
        }
        client.execute(() -> clearAndAddAll(TRANS_CATENARIES, transCatenariesTemp));
    }

    private static <U, V> void clearAndAddAll(Map<U, V> target, Map<U, V> source) {
        target.clear();
        target.putAll(source);
    }
}