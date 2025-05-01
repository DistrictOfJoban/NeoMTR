package mtr.screen;

import com.lx862.jcm.mod.render.gui.screen.base.TitledScreen;
import com.lx862.jcm.mod.render.gui.widget.CategoryItem;
import com.lx862.jcm.mod.render.gui.widget.ContentItem;
import com.lx862.jcm.mod.render.gui.widget.ListViewWidget;
import mtr.Keys;
import mtr.MTRClient;
import mtr.client.ClientData;
import mtr.client.Config;
import mtr.mappings.Text;
import mtr.registry.MTRAddonRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;
import java.util.function.Consumer;

public class NewConfigScreen extends TitledScreen {

    private final List<MTRAddonRegistry.MTRAddon> registeredAddons;
    private WidgetShorterSlider trackTextureOffsetSlider;
    private WidgetShorterSlider dynamicTextureResolutionSlider;
    private WidgetShorterSlider trainRenderDistanceRatioSlider;

    public NewConfigScreen() {
        super(false, false);
        Config.readConfig();
        registeredAddons = MTRAddonRegistry.getRegisteredAddons();
    }

    @Override
    public MutableComponent getScreenTitle() {
        return Component.translatable("gui.mtr.mtr_options");
    }

    @Override
    public MutableComponent getScreenSubtitle() {
        return Component.literal("v" + Keys.MOD_VERSION);
    }

    @Override
    public void init() {
        super.init();

        int listViewSize = (int)Math.min(width * 0.75, 360);
        int startX = (width - listViewSize) / 2;
        ListViewWidget listViewWidget = new ListViewWidget();
        listViewWidget.setXYSize(startX, getStartY() + TEXT_PADDING, listViewSize, height - 95);
        listViewWidget.add(new CategoryItem(Component.literal("General")));

        Button useMtrFontButton = Button.builder(getBooleanText(Config.useMTRFont()), button -> {
            boolean toggled = Config.setUseMTRFont(!Config.useMTRFont());
            button.setMessage(getBooleanText(toggled));
        }).size(60, 18).build();
        addWidget(useMtrFontButton);
        listViewWidget.add(Text.translatable("options.mtr.use_mtr_font"), useMtrFontButton);

        Button showAnnouncementMessageButton = Button.builder(getBooleanText(Config.showAnnouncementMessages()), button -> {
            boolean toggled = Config.setShowAnnouncementMessages(!Config.showAnnouncementMessages());
            button.setMessage(getBooleanText(toggled));
        }).size(60, 18).build();
        addWidget(showAnnouncementMessageButton);
        listViewWidget.add(Text.translatable("options.mtr.show_announcement_messages"), showAnnouncementMessageButton);

        Button useTTSButton = Button.builder(getBooleanText(Config.useTTSAnnouncements()), button -> {
            boolean toggled = Config.setUseTTSAnnouncements(!Config.useTTSAnnouncements());
            button.setMessage(getBooleanText(toggled));
        }).size(60, 18).build();
        addWidget(useTTSButton);
        listViewWidget.add(Text.translatable("options.mtr.use_tts_announcements"), useTTSButton);

        Button buttonHideSpecialRailColors = Button.builder(getBooleanText(Config.hideSpecialRailColors()), button -> {
            boolean hideSpecialRailColors = Config.setHideSpecialRailColors(!Config.hideSpecialRailColors());
            button.setMessage(getBooleanText(hideSpecialRailColors));
        }).size(60, 18).build();
        addWidget(buttonHideSpecialRailColors);
        listViewWidget.add(Text.translatable("options.mtr.hide_special_rail_colors"), buttonHideSpecialRailColors);

        Button hideTranslucentPartsButton = Button.builder(getBooleanText(Config.hideTranslucentParts()), button -> {
            boolean hideTranslucentParts = Config.setHideTranslucentParts(!Config.hideTranslucentParts());
            button.setMessage(getBooleanText(hideTranslucentParts));
        }).size(60, 18).build();
        addWidget(hideTranslucentPartsButton);
        listViewWidget.add(Text.translatable("options.mtr.hide_translucent_parts"), hideTranslucentPartsButton);

        Button shiftToToggleSittingButton = Button.builder(getBooleanText(Config.shiftToToggleSitting()), button -> {
            boolean shiftToToggleSitting = Config.setShiftToToggleSitting(!Config.shiftToToggleSitting());
            button.setMessage(getBooleanText(shiftToToggleSitting));
        }).size(60, 18).build();
        addWidget(shiftToToggleSittingButton);
        listViewWidget.add(Text.translatable("options.mtr.shift_to_toggle_sitting", minecraft == null ? "" : minecraft.options.keyShift.getTranslatedKeyMessage()), shiftToToggleSittingButton);

        Button buttonLanguageOptions = Button.builder(Text.translatable("options.mtr.language_options_" + Config.languageOptions()), button -> {
            int languageOptions = Config.setLanguageOptions(Config.languageOptions() + 1);
            button.setMessage(Text.translatable("options.mtr.language_options_" + languageOptions));
        }).size(60, 18).build();
        addWidget(buttonLanguageOptions);
        listViewWidget.add(Text.translatable("options.mtr.language_options"), buttonLanguageOptions);

        trackTextureOffsetSlider = new WidgetShorterSlider(0, 60, Config.TRACK_OFFSET_COUNT - 1, Object::toString, null);
        trackTextureOffsetSlider.setDrawTextCentered(true);
        trackTextureOffsetSlider.setHeight(20);
        trackTextureOffsetSlider.setValue(Config.trackTextureOffset());
        addWidget(trackTextureOffsetSlider);
        listViewWidget.add(Text.translatable("options.mtr.track_texture_offset"), trackTextureOffsetSlider);

        dynamicTextureResolutionSlider = new WidgetShorterSlider(0, 60, Config.DYNAMIC_RESOLUTION_COUNT - 1, Object::toString, null);
        dynamicTextureResolutionSlider.setDrawTextCentered(true);
        dynamicTextureResolutionSlider.setHeight(20);
        dynamicTextureResolutionSlider.setValue(Config.dynamicTextureResolution());
        addWidget(dynamicTextureResolutionSlider);
        listViewWidget.add(Text.translatable("options.mtr.dynamic_texture_resolution"), dynamicTextureResolutionSlider);

        trainRenderDistanceRatioSlider = new WidgetShorterSlider(0, 60, Config.TRAIN_RENDER_DISTANCE_RATIO_COUNT - 1, num -> String.format("%d%%", (num + 1) * 100 / Config.TRAIN_RENDER_DISTANCE_RATIO_COUNT), null);
        trainRenderDistanceRatioSlider.setHeight(20);
        trainRenderDistanceRatioSlider.setDrawTextCentered(true);
        trainRenderDistanceRatioSlider.setValue(Config.trainRenderDistanceRatio());
        addWidget(trainRenderDistanceRatioSlider);
        listViewWidget.add(Text.translatable("options.mtr.vehicle_render_distance_ratio"), trainRenderDistanceRatioSlider);

        Button supportButton = Button.builder(Text.translatable("gui.mtr.support"), (btn) -> {
            Util.getPlatform().openUri("https://www.patreon.com/minecraft_transit_railway");
        }).size(60, 18).build();
        addWidget(supportButton);
        listViewWidget.add(Text.translatable("options.mtr.support_patreon"), supportButton);

        if(!registeredAddons.isEmpty()) {
            listViewWidget.add(new CategoryItem(Component.literal("Registered addons")));
            for(MTRAddonRegistry.MTRAddon addon : registeredAddons) {
                final Consumer<Screen> openScreenCallback = MTRClient.getAddonConfigScreen(addon);

                Button configureButton = Button.builder(Component.literal("Configure..."), (btn) -> {
                    openScreenCallback.accept(this);
                }).size(80, 20).build();

                if(openScreenCallback == null) {
                    configureButton.active = false;
                    configureButton.setTooltip(Tooltip.create(Component.literal(String.format("%s does not provide a way to be configured.", addon.name()))));
                }
                addWidget(configureButton);

                final MutableComponent nameComponent = Component.literal(addon.name());
                final MutableComponent versionComponent = Component.literal(addon.version()).withStyle(ChatFormatting.GRAY);

                listViewWidget.add(new ContentItem(nameComponent.append(Component.literal(" ")).append(versionComponent), configureButton));
            }
        }


        int buttonWidth = (int)(listViewSize * 0.75);
        Button doneButton = Button.builder(Component.literal("Done"), (btn) -> {
            onClose();
        }).pos((width - buttonWidth) / 2, height - 30).size(buttonWidth, 20).build();

        addRenderableWidget(doneButton);
        addRenderableWidget(listViewWidget);
    }

    @Override
    public void onClose() {
        super.onClose();
        Config.setTrackTextureOffset(trackTextureOffsetSlider.getIntValue());
        Config.setDynamicTextureResolution(dynamicTextureResolutionSlider.getIntValue());
        Config.setTrainRenderDistanceRatio(trainRenderDistanceRatioSlider.getIntValue());
        ClientData.DATA_CACHE.sync();
        ClientData.DATA_CACHE.refreshDynamicResources();
        ClientData.SIGNAL_BLOCKS.writeCache();
    }

    private static Component getBooleanText(boolean state) {
        return Component.translatable(state ? "options.mtr.on" : "options.mtr.off").withStyle(state ? ChatFormatting.DARK_GREEN : ChatFormatting.RED);
    }
}
