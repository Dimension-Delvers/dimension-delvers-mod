package com.wanderersoftherift.wotr.gui.configuration;

import com.wanderersoftherift.wotr.config.HudElementConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.joml.Vector2i;

public class ConfigurableLayerProxy implements ConfigurableLayer {
    private final Component name;
    private final HudElementConfig config;
    private final int width;
    private final int height;

    public ConfigurableLayerProxy(Component name, HudElementConfig config, int width, int height) {
        this.name = name;
        this.config = config;
        this.width = width;
        this.height = height;
    }

    @Override
    public Component getName() {
        return name;
    }

    @Override
    public HudElementConfig getConfig() {
        return config;
    }

    public Vector2i getTranslation(int screenWidth, int screenHeight) {
        Vector2i defaultPos = config.getDefaultAnchor()
                .getPos(config.getDefaultX(), config.getDefaultY(), width, height, screenWidth, screenHeight);
        Vector2i targetPos = config.getPosition(width, height, screenWidth, screenHeight);
        return targetPos.sub(defaultPos);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void preRender(GuiGraphics guiGraphics) {
        Vector2i pos = getTranslation(guiGraphics.guiWidth(), guiGraphics.guiHeight());
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(pos.x, pos.y, 0);
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
    }

    @Override
    public void postRender(GuiGraphics guiGraphics) {
        guiGraphics.pose().popPose();
    }
}
