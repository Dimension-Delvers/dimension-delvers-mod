package com.wanderersoftherift.wotr.gui.layer;

import com.wanderersoftherift.wotr.config.HudElementConfig;
import com.wanderersoftherift.wotr.gui.configuration.ScreenAnchor;
import net.minecraft.network.chat.Component;
import org.joml.Vector2i;

public class ConfigurableLayerProxy implements ConfigurableLayer {
    private final Component name;
    private final HudElementConfig config;
    private final ScreenAnchor defaultAnchor;
    private final int defaultX;
    private final int defaultY;
    private final int width;
    private final int height;

    public ConfigurableLayerProxy(Component name, HudElementConfig config, ScreenAnchor defaultAnchor, int defaultX,
            int defaultY, int width, int height) {
        this.name = name;
        this.config = config;
        this.defaultAnchor = defaultAnchor;
        this.defaultX = defaultX;
        this.defaultY = defaultY;
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
        Vector2i defaultPos = defaultAnchor.getPos(defaultX, defaultY, width, height, screenWidth, screenHeight);
        Vector2i targetPos = config.getPosition(width, height, screenWidth, screenHeight);
        return targetPos.sub(defaultPos);
    }

    public int getDefaultX() {
        return defaultX;
    }

    public int getDefaultY() {
        return defaultY;
    }

    public ScreenAnchor getDefaultAnchor() {
        return defaultAnchor;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

}
