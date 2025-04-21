package com.lx862.jcm.mod.render.gui.screen;

import com.lx862.jcm.mod.block.entity.FareSaverBlockEntity;
import com.lx862.jcm.mod.network.block.FareSaverUpdatePacket;
import com.lx862.jcm.mod.registry.Networking;
import com.lx862.jcm.mod.render.gui.screen.base.BlockConfigScreen;
import com.lx862.jcm.mod.render.gui.widget.IntegerTextField;
import com.lx862.jcm.mod.util.TextCategory;
import com.lx862.jcm.mod.util.TextUtil;
import mtr.screen.WidgetBetterTextField;
import net.minecraft.network.chat.MutableComponent;

public class FareSaverScreen extends BlockConfigScreen {
    private final IntegerTextField discountTextField;
    private final WidgetBetterTextField prefixTextField;
    public FareSaverScreen(FareSaverBlockEntity blockEntity) {
        super(blockEntity.getBlockPos());
        this.prefixTextField = new WidgetBetterTextField("$", 4);
        this.prefixTextField.setSize(60, 20);
        this.prefixTextField.setValue(blockEntity.getPrefix());
        this.discountTextField = new IntegerTextField(0, 0, 60, 20, 0, 1000000, 2);
        this.discountTextField.setValue(blockEntity.getDiscount());
    }

    @Override
    public MutableComponent getScreenTitle() {
        return TextUtil.translatable(TextCategory.BLOCK, "faresaver");
    }

    @Override
    public void addConfigEntries() {
        addWidget(prefixTextField);
        addWidget(discountTextField);
        listViewWidget.add(TextUtil.translatable(TextCategory.GUI, "faresaver.prefix"), prefixTextField);
        listViewWidget.add(TextUtil.translatable(TextCategory.GUI, "faresaver.discount"), discountTextField);
    }
    @Override
    public void onSave() {
        Networking.sendPacketToServer(new FareSaverUpdatePacket(blockPos, prefixTextField.getValue(), (int)discountTextField.getNumber()));
    }
}
