package com.lx862.jcm.mod.registry;

import cn.zbx1425.mtrsteamloco.RegistriesWrapper;
import com.lx862.jcm.mod.Constants;
import com.lx862.jcm.mod.item.ItemDRLAPG;
import com.lx862.jcm.mod.util.JCMLogger;
import mtr.MTR;
import mtr.registry.RegistryObject;
import net.minecraft.world.item.Item;

public class Items {
    public static final RegistryObject<Item> APG_DOOR_DRL = new RegistryObject<>(() -> new ItemDRLAPG(ItemDRLAPG.EnumPSDAPGItem.DOOR, ItemDRLAPG.EnumPSDAPGType.APG, new Item.Properties()));
    public static final RegistryObject<Item> APG_GLASS_DRL = new RegistryObject<>(() -> new ItemDRLAPG(ItemDRLAPG.EnumPSDAPGItem.GLASS, ItemDRLAPG.EnumPSDAPGType.APG, new Item.Properties()));
    public static final RegistryObject<Item> APG_GLASS_END_DRL = new RegistryObject<>(() -> new ItemDRLAPG(ItemDRLAPG.EnumPSDAPGItem.GLASS_END, ItemDRLAPG.EnumPSDAPGType.APG, new Item.Properties()));

    public static void register(RegistriesWrapper registriesWrapper) {
        // We just load the class and it will be registered, nothing else
        JCMLogger.debug("Registering items...");
        registriesWrapper.registerItem("apg_door_drl", APG_DOOR_DRL);
        registriesWrapper.registerItem("apg_glass_drl", APG_GLASS_DRL);
        registriesWrapper.registerItem("apg_glass_end_drl", APG_GLASS_END_DRL);
    }
}
