package com.lx862.jcm.mod.render.gui.screen.base;

import com.lx862.jcm.mod.render.GuiHelper;
import com.lx862.jcm.mod.render.gui.widget.ListViewWidget;
import com.lx862.jcm.mod.render.gui.widget.WidgetSet;
import com.lx862.jcm.mod.util.TextCategory;
import com.lx862.jcm.mod.util.TextUtil;
import mtr.data.IGui;
import mtr.data.Station;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;

/**
 * GUI Screen for configuring block settings, you should extend this class for your own block config screen
 */
public abstract class BlockConfigScreen extends TitledScreen implements GuiHelper {
    protected final BlockPos blockPos;
    protected final ListViewWidget listViewWidget;
    protected final WidgetSet bottomEntryWidget;
    private final ButtonWidgetExtension saveButton;
    private final ButtonWidgetExtension discardButton;
    private boolean discardConfig = false;
    public BlockConfigScreen(BlockPos blockPos) {
        super(false);
        this.blockPos = blockPos;

        this.saveButton = new ButtonWidgetExtension(0, 0, 0, 20, TextUtil.translatable(TextCategory.GUI, "block_config.save"), (btn) -> {
            onClose2();
        });

        this.discardButton = new ButtonWidgetExtension(0, 0, 0, 20, TextUtil.translatable(TextCategory.GUI, "block_config.discard"), (btn) -> {
            discardConfig = true;
            onClose2();
        });

        this.listViewWidget = new ListViewWidget();
        this.bottomEntryWidget = new WidgetSet(20);
    }

    @Override
    protected void init() {
        super.init();
        int contentWidth = (int)Math.min((width * 0.75), MAX_CONTENT_WIDTH);
        int listViewHeight = Math.max(160, (int)((height - 60) * 0.75));
        int startX = (width - contentWidth) / 2;
        int startY = TEXT_PADDING * 5;
        int bottomEntryHeight = (height - startY - listViewHeight - (BOTTOM_ROW_MARGIN * 2));

        listViewWidget.reset();
        bottomEntryWidget.reset();

        listViewWidget.setXYSize(startX, startY, contentWidth, listViewHeight);
        addConfigEntries();
        addBottomRowButtons();
        addWidget(new ClickableWidget(listViewWidget));
        addWidget(new ClickableWidget(bottomEntryWidget));
        listViewWidget.positionWidgets();
        bottomEntryWidget.setXYSize(startX, startY + listViewHeight + BOTTOM_ROW_MARGIN, contentWidth, bottomEntryHeight);
    }

    public abstract void addConfigEntries();
    public abstract void onSave();

    protected void addBottomRowButtons() {
        addWidget(saveButton);
        addWidget(discardButton);

        bottomEntryWidget.addWidget(saveButton);
        bottomEntryWidget.addWidget(discardButton);
    }

    @Override
    public MutableComponent getScreenSubtitle() {
        Station atStation = InitClient.findStation(blockPos);

        if(atStation != null) {
            return TextUtil.translatable(TextCategory.GUI,
                    "block_config.subtitle_with_station",
                    blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                    IGui.formatStationName(atStation.name)
            );
        } else {
            return TextUtil.translatable(TextCategory.GUI,
                    "block_config.subtitle",
                    blockPos.getX(), blockPos.getY(), blockPos.getZ()
            );
        }
    }

    @Override
    public void onClose2() {
        // Save config by default, unless explicitly requested not to
        if(!discardConfig) {
            onSave();
        }
        super.onClose2();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
