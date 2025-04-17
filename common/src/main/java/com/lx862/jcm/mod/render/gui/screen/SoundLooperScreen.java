package com.lx862.jcm.mod.render.gui.screen;

import com.lx862.jcm.mod.block.entity.SoundLooperBlockEntity;
import com.lx862.jcm.mod.network.block.SoundLooperUpdatePacket;
import com.lx862.jcm.mod.registry.Networking;
import com.lx862.jcm.mod.render.gui.screen.base.BlockConfigScreen;
import com.lx862.jcm.mod.render.gui.widget.BlockPosWidget;
import com.lx862.jcm.mod.render.gui.widget.IntegerTextField;
import com.lx862.jcm.mod.render.gui.widget.MappedWidget;
import com.lx862.jcm.mod.util.TextCategory;
import com.lx862.jcm.mod.util.TextUtil;
import mtr.mapping.holder.BlockPos;
import mtr.mapping.holder.ClickableWidget;
import mtr.mapping.holder.MutableComponent;
import mtr.mapping.holder.Text;
import mtr.mapping.mapper.ButtonWidgetExtension;
import mtr.mapping.mapper.CheckboxWidgetExtension;
import mtr.mapping.mapper.TextFieldWidgetExtension;
import mtr.mapping.tool.TextCase;

public class SoundLooperScreen extends BlockConfigScreen {
    private final ButtonWidgetExtension soundCategoryButton;
    private final TextFieldWidgetExtension soundIdTextField;
    private final IntegerTextField repeatTickTextField;
    private final IntegerTextField soundVolumeTextField;
    private final CheckboxWidgetExtension needRedstonePowerCheckbox;
    private final CheckboxWidgetExtension limitSoundRangeCheckbox;
    private final BlockPosWidget corner1Widget;
    private final BlockPosWidget corner2Widget;
    private int soundCategory;
    public SoundLooperScreen(BlockPos blockPos, BlockPos corner1, BlockPos corner2, String soundId, int soundCategory, float volume, int repeatTick, boolean needRedstonePower, boolean limitRange) {
        super(blockPos);
        this.soundCategoryButton = new ButtonWidgetExtension(0, 0, 60, 20, (btn) -> {
            setSoundCategory((this.soundCategory + 1) % SoundLooperBlockEntity.SOURCE_LIST.length);
        });
        setSoundCategory(soundCategory);
        this.corner1Widget = new BlockPosWidget(0, 0, 120, 20);
        this.corner1Widget.setBlockPos(corner1);
        this.corner2Widget = new BlockPosWidget(0, 0, 120, 20);
        this.corner2Widget.setBlockPos(corner2);

        this.soundIdTextField = new TextFieldWidgetExtension(0, 0, 100, 20, 100, TextCase.LOWER, null, "mtr:ticket_barrier");
        this.soundIdTextField.setText2(soundId);
        this.soundVolumeTextField = new IntegerTextField(0, 0, 60, 20, 1, 1000, 100);
        this.soundVolumeTextField.setValue((int)(volume * 100));
        this.repeatTickTextField = new IntegerTextField(0, 0, 60, 20, 1, 99999, 20);
        this.repeatTickTextField.setValue(repeatTick);
        this.needRedstonePowerCheckbox = new CheckboxWidgetExtension(0, 0, 20, 20, false, (checked) -> {});
        this.needRedstonePowerCheckbox.setChecked(needRedstonePower);
        this.limitSoundRangeCheckbox = new CheckboxWidgetExtension(0, 0, 20, 20, false, (checked) -> {
            if(checked) {
                this.corner1Widget.setActiveMapped(true);
                this.corner2Widget.setActiveMapped(true);
            } else {
                this.corner1Widget.setActiveMapped(false);
                this.corner2Widget.setActiveMapped(false);
            }
        });
        this.limitSoundRangeCheckbox.setChecked(limitRange);
        this.corner1Widget.setActiveMapped(limitRange);
        this.corner2Widget.setActiveMapped(limitRange);
    }

    @Override
    public MutableComponent getScreenTitle() {
        return TextUtil.translatable(TextCategory.BLOCK, "sound_looper");
    }

    @Override
    public void addConfigEntries() {
        corner1Widget.addWidget(this::addWidget);
        corner2Widget.addWidget(this::addWidget);
        addWidget(new ClickableWidget(soundCategoryButton));
        addWidget(new ClickableWidget(soundIdTextField));
        addWidget(new ClickableWidget(soundVolumeTextField));
        addWidget(new ClickableWidget(repeatTickTextField));
        addWidget(new ClickableWidget(needRedstonePowerCheckbox));
        addWidget(new ClickableWidget(limitSoundRangeCheckbox));
        addWidget(new ClickableWidget(corner1Widget));
        addWidget(new ClickableWidget(corner2Widget));
        listViewWidget.add(TextUtil.translatable(TextCategory.GUI, "sound_looper.listview.title.sound_category"), new MappedWidget(soundCategoryButton));
        listViewWidget.add(TextUtil.translatable(TextCategory.GUI, "sound_looper.listview.title.sound_id"), new MappedWidget(soundIdTextField));
        listViewWidget.add(TextUtil.translatable(TextCategory.GUI, "sound_looper.listview.title.sound_volume"), new MappedWidget(soundVolumeTextField));
        listViewWidget.add(TextUtil.translatable(TextCategory.GUI, "sound_looper.listview.title.repeat_tick"), new MappedWidget(repeatTickTextField));
        listViewWidget.add(TextUtil.translatable(TextCategory.GUI, "sound_looper.listview.title.need_redstone"), new MappedWidget(needRedstonePowerCheckbox));
        listViewWidget.add(TextUtil.translatable(TextCategory.GUI, "sound_looper.listview.title.limit_range"), new MappedWidget(limitSoundRangeCheckbox));
        listViewWidget.add(TextUtil.translatable(TextCategory.GUI, "sound_looper.listview.title.pos1"), new MappedWidget(corner1Widget));
        listViewWidget.add(TextUtil.translatable(TextCategory.GUI, "sound_looper.listview.title.pos2"), new MappedWidget(corner2Widget));
    }

    @Override
    public void onSave() {
        Networking.sendPacketToServer(new SoundLooperUpdatePacket(blockPos, corner1Widget.getBlockPos(), corner2Widget.getBlockPos(), soundIdTextField.getText2(), soundCategory, (int)repeatTickTextField.getNumber(), soundVolumeTextField.getNumber() / 100f, needRedstonePowerCheckbox.isChecked2(), limitSoundRangeCheckbox.isChecked2()));
    }

    private void setSoundCategory(int category) {
        if(category >= SoundLooperBlockEntity.SOURCE_LIST.length) {
            this.soundCategory = 0;
        } else {
            this.soundCategory = category;
        }
        this.soundCategoryButton.setMessage2(Text.cast(TextUtil.literal(SoundLooperBlockEntity.SOURCE_LIST[category].getName())));
    }
}
