package com.lx862.jcm.mod.render.gui.screen;

import com.lx862.jcm.mod.network.block.PIDSProjectorUpdatePacket;
import com.lx862.jcm.mod.registry.Networking;
import com.lx862.jcm.mod.render.gui.widget.CategoryItem;
import com.lx862.jcm.mod.render.gui.widget.DoubleTextField;
import com.lx862.jcm.mod.render.gui.widget.MappedWidget;
import com.lx862.jcm.mod.render.gui.widget.WidgetSet;
import com.lx862.jcm.mod.util.TextCategory;
import com.lx862.jcm.mod.util.TextUtil;
import mtr.mapping.holder.BlockPos;
import mtr.mapping.holder.ClickableWidget;
import mtr.mapping.holder.MutableComponent;

public class PIDSProjectorScreen extends PIDSScreen {

    private final DoubleTextField xField;
    private final DoubleTextField yField;
    private final DoubleTextField zField;
    private final DoubleTextField scaleField;
    private final DoubleTextField rotateXField;
    private final DoubleTextField rotateYField;
    private final DoubleTextField rotateZField;

    public PIDSProjectorScreen(BlockPos blockPos, String[] customMessages, boolean[] rowHidden, boolean hidePlatformNumber, String presetId, double x, double y, double z, double rotateX, double rotateY, double rotateZ, double scale) {
        super(blockPos, customMessages, rowHidden, hidePlatformNumber, presetId);

        this.xField = new DoubleTextField(0, 0, 40, 20, -1000, 1000, 0);
        this.yField = new DoubleTextField(0, 0, 40, 20, -1000, 1000, 0);
        this.zField = new DoubleTextField(0, 0, 40, 20, -1000, 1000, 0);
        this.rotateXField = new DoubleTextField(0, 0, 40, 20, -359, 360, 0);
        this.rotateYField = new DoubleTextField(0, 0, 40, 20, -359, 360, 0);
        this.rotateZField = new DoubleTextField(0, 0, 40, 20, -359, 360, 0);
        this.scaleField = new DoubleTextField(0, 0, 40, 20, 0, 30, 0);

        xField.setText2(String.valueOf(x));
        yField.setText2(String.valueOf(y));
        zField.setText2(String.valueOf(z));
        rotateXField.setText2(String.valueOf(rotateX));
        rotateYField.setText2(String.valueOf(rotateY));
        rotateZField.setText2(String.valueOf(rotateZ));
        scaleField.setText2(String.valueOf(scale));
    }

    @Override
    public MutableComponent getScreenTitle() {
        return TextUtil.translatable(TextCategory.BLOCK, "pids_projector");
    }

    @Override
    public void addConfigEntries() {
        WidgetSet positionFields = new WidgetSet(20, 0);
        WidgetSet rotationFields = new WidgetSet(20, 0);

        addWidget(new ClickableWidget(xField));
        addWidget(new ClickableWidget(yField));
        addWidget(new ClickableWidget(zField));
        addWidget(new ClickableWidget(rotateXField));
        addWidget(new ClickableWidget(rotateYField));
        addWidget(new ClickableWidget(rotateZField));
        addWidget(new ClickableWidget(scaleField));
        addWidget(new ClickableWidget(positionFields));
        addWidget(new ClickableWidget(rotationFields));

        positionFields.addWidget(new MappedWidget(xField));
        positionFields.addWidget(new MappedWidget(yField));
        positionFields.addWidget(new MappedWidget(zField));

        rotationFields.addWidget(new MappedWidget(rotateXField));
        rotationFields.addWidget(new MappedWidget(rotateYField));
        rotationFields.addWidget(new MappedWidget(rotateZField));

        rotationFields.setXYSize(0, 0, 140, 20);
        positionFields.setXYSize(0, 0, 140, 20);
        listViewWidget.add(new CategoryItem(TextUtil.translatable(TextCategory.GUI, "pids.listview.category.projection")));
        listViewWidget.add(TextUtil.translatable(TextCategory.GUI, "pids.listview.widget.position"), new MappedWidget(positionFields));
        listViewWidget.add(TextUtil.translatable(TextCategory.GUI, "pids.listview.widget.rotate"), new MappedWidget(rotationFields));
        listViewWidget.add(TextUtil.translatable(TextCategory.GUI, "pids.listview.widget.scale"), new MappedWidget(scaleField));
        listViewWidget.add(new CategoryItem(TextUtil.translatable(TextCategory.GUI, "pids.listview.category.pids")));
        super.addConfigEntries();
    }

    @Override
    public void onSave() {
        String[] customMessages = new String[customMessagesWidgets.length];
        boolean[] rowHidden = new boolean[rowHiddenWidgets.length];

            for(int i = 0; i < customMessagesWidgets.length; i++) {
            customMessages[i] = this.customMessagesWidgets[i].getText2();
        }

            for(int i = 0; i < rowHiddenWidgets.length; i++) {
            rowHidden[i] = this.rowHiddenWidgets[i].isChecked2();
        }

        Networking.sendPacketToServer(new PIDSProjectorUpdatePacket(blockPos, filteredPlatforms, customMessages, rowHidden, hidePlatformNumber.isChecked2(), presetId, xField.getNumber(), yField.getNumber(), zField.getNumber(), rotateXField.getNumber(), rotateYField.getNumber(), rotateZField.getNumber(), scaleField.getNumber()));
    }
}
