package com.lx862.jcm.mod.render.gui.screen;

import com.lx862.jcm.mod.Constants;
import com.lx862.jcm.mod.JCMClient;
import com.lx862.jcm.mod.render.GuiHelper;
import com.lx862.jcm.mod.render.gui.screen.base.TitledScreen;
import com.lx862.jcm.mod.render.gui.widget.ListViewWidget;
import com.lx862.jcm.mod.render.gui.widget.MappedWidget;
import com.lx862.jcm.mod.render.gui.widget.WidgetSet;
import com.lx862.jcm.mod.util.JCMLogger;
import com.lx862.jcm.mod.util.TextCategory;
import com.lx862.jcm.mod.util.TextUtil;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class ClientConfigScreen extends TitledScreen implements GuiHelper {
    private static final ResourceLocation TEXTURE_BACKGROUND = Constants.id("textures/gui/config_screen/bg.png");
    private static final ResourceLocation TEXTURE_STAR = Constants.id("textures/gui/config_screen/stars.png");
    private static final ResourceLocation TEXTURE_TERRAIN = Constants.id("textures/gui/config_screen/terrain.png");
    private static final int STAR_ROTATION_LENGTH = 260 * 1000;

    private final WidgetSet bottomRowWidget;
    private final ListViewWidget listViewWidget;
    private final CheckboxWidgetExtension disableRenderingButton;
    private final CheckboxWidgetExtension debugModeButton;
    private final ButtonWidgetExtension textAtlasButton;

    private boolean discardConfig = false;


    public ClientConfigScreen() {
        super(true);
        bottomRowWidget = new WidgetSet(20);
        listViewWidget = new ListViewWidget();

        this.disableRenderingButton = new CheckboxWidgetExtension(0, 0, 20, 20, false, bool -> {
            JCMClient.getConfig().disableRendering = bool;
        });

        this.debugModeButton = new CheckboxWidgetExtension(0, 0, 20, 20, false, bool -> {
            JCMClient.getConfig().debug = bool;
        });

        this.textAtlasButton = new ButtonWidgetExtension(0, 0, 60, 20, TextUtil.translatable(TextCategory.GUI, "config.listview.widget.open"), (buttonWidget -> {
            Minecraft.getInstance().setScreen(new TextureTextAtlasScreen().withPreviousScreen(this));
        }));
    }

    @Override
    public MutableComponent getScreenTitle() {
        return TextUtil.translatable(TextCategory.GUI, "brand");
    }

    @Override
    public MutableComponent getScreenSubtitle() {
        return TextUtil.translatable(TextCategory.GUI, "config.version", Constants.MOD_VERSION);
    }

    @Override
    protected void init() {
        super.init();
        listViewWidget.reset();
        bottomRowWidget.reset();

        int contentWidth = (int)Math.min((width * 0.75), MAX_CONTENT_WIDTH);
        int listViewHeight = (int)((height - 60) * 0.75);
        int startX = (width - contentWidth) / 2;
        int startY = TEXT_PADDING * 5;

        int bottomEntryHeight = (height - startY - listViewHeight - (BOTTOM_ROW_MARGIN * 2));

        addConfigEntries();
        addBottomButtons();
        addWidget(listViewWidget);
        addWidget(bottomRowWidget);
        listViewWidget.setXYSize(startX, startY, contentWidth, listViewHeight);
        bottomRowWidget.setXYSize(startX, startY + listViewHeight + BOTTOM_ROW_MARGIN, contentWidth, bottomEntryHeight);
    }

    private void setEntryStateFromClientConfig() {
        disableRenderingButton.setChecked(JCMClient.getConfig().disableRendering);
        debugModeButton.setChecked(JCMClient.getConfig().debug);
    }

    private void addConfigEntries() {
        setEntryStateFromClientConfig();

        // General
        listViewWidget.addCategory(TextUtil.translatable(TextCategory.GUI, "config.listview.category.general"));

        listViewWidget.add(TextUtil.translatable(TextCategory.GUI, "config.listview.title.disable_rendering"), new MappedWidget(disableRenderingButton));
        addWidget(disableRenderingButton);

        // Debug
        listViewWidget.addCategory(TextUtil.translatable(TextCategory.GUI, "config.listview.category.debug"));
        listViewWidget.add(TextUtil.translatable(TextCategory.GUI, "config.listview.title.debug_mode"), new MappedWidget(debugModeButton));
        addWidget(debugModeButton);
    }


    private void addBottomButtons() {
        ButtonWidgetExtension latestLogButton = new ButtonWidgetExtension(0, 0, 0, 20, TextUtil.translatable(TextCategory.GUI, "config.latest_log"), (btn) -> {
            openLatestLog();
        });

        ButtonWidgetExtension crashLogButton = new ButtonWidgetExtension(0, 0, 0, 20, TextUtil.translatable(TextCategory.GUI, "config.crash_log"), (btn) -> {
            openLatestCrashReport();
        });

        ButtonWidgetExtension saveButton = new ButtonWidgetExtension(0, 0, 0, 20, TextUtil.translatable(TextCategory.GUI, "config.save"), (btn) -> {
            onClose2();
        });

        ButtonWidgetExtension discardButton = new ButtonWidgetExtension(0, 0, 0, 20, TextUtil.translatable(TextCategory.GUI, "config.discard"), (btn) -> {
            discardConfig = true;
            onClose2();
        });

        ButtonWidgetExtension resetButton = new ButtonWidgetExtension(0, 0, 0, 20, TextUtil.translatable(TextCategory.GUI, "config.reset"), (btn) -> {
            JCMClient.getConfig().reset();
            setEntryStateFromClientConfig();
        });

        addWidget(latestLogButton);
        addWidget(crashLogButton);

        addWidget(saveButton);
        addWidget(discardButton);
        addWidget(resetButton);

        bottomRowWidget.addWidget(latestLogButton);
        bottomRowWidget.addWidget(crashLogButton);
        bottomRowWidget.insertRow();
        bottomRowWidget.addWidget(saveButton);
        bottomRowWidget.addWidget(discardButton);
        bottomRowWidget.addWidget(resetButton);
    }

    @Override
    public void drawBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        double terrainHeight = (width / 3.75);
        double starSize = Math.max(width, height) * 4;
        float starUVSize = (float) (starSize / 384F);
        double translateY = height * (1 - animationProgress);
        GuiHelper.drawTexture(guiGraphics, TEXTURE_BACKGROUND, 0, 0, width, height);
        graphicsHolder.push();
        graphicsHolder.translate(0, translateY * 0.2f, 0);
        graphicsHolder.translate(width / 2.0, height / 2.0, 0);

        float starRot = (System.currentTimeMillis() % STAR_ROTATION_LENGTH) / (float)STAR_ROTATION_LENGTH;
        graphicsHolder.rotateZDegrees(starRot * 360);
        graphicsHolder.translate(-width / 2.0, -height / 2.0, 0);
        GuiHelper.drawTexture(guiDrawing, TEXTURE_STAR, 0, 0, starSize, starSize, 0, 0, starUVSize, starUVSize);
        graphicsHolder.pop();

        GuiHelper.drawTexture(guiDrawing, TEXTURE_TERRAIN, 0, translateY + height - terrainHeight, width, terrainHeight);
    }

    @Override
    public void onClose2() {
        if(!closing) {
            if (!discardConfig) {
                JCMClient.getConfig().write();
            } else {
                // Don't save our change to disk, and read it from disk, effectively discarding the config
                JCMClient.getConfig().read();
            }
        }

        super.onClose2();
    }

    public static void openLatestLog() {
        File latestLog = Paths.get(Minecraft.getInstance().gameDirectory.toString(), "logs", "latest.log").toFile();
        if(latestLog.exists()) {
            Util.getPlatform().openFile(latestLog);
        }
    }

    public static void openLatestCrashReport() {
        File crashReportDir = Paths.get(Minecraft.getInstance().gameDirectory.toString(), "crash-reports").toFile();

        if(crashReportDir.exists()) {
            File[] crashReports = crashReportDir.listFiles();
            if(crashReports != null && crashReportDir.length() > 0) {
                SimpleDateFormat crashReportFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

                Arrays.sort(crashReports, (o1, o2) -> {
                    String filename1 = o1.getName().replace("crash-", "").replace("-client", "");
                    String filename2 = o2.getName().replace("crash-", "").replace("-client", "");
                    if(o1 == o2) return 0;

                    try {
                        long ts1 = crashReportFormat.parse(filename1).getTime();
                        long ts2 = crashReportFormat.parse(filename2).getTime();
                        return ts1 > ts2 ? -1 : 1;
                    } catch (ParseException e) {
                        JCMLogger.debug("Cannot compare crash report file name " + filename1 + " <-> " + filename2);
                    }
                    return 1;
                });

                JCMLogger.debug("Latest crash report is: " + crashReports[0].getName());
                Util.getPlatform().openFile(crashReports[0]);
            }
        }
    }
}
