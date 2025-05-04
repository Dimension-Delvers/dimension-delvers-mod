package com.wanderersoftherift.wotr.gui.config;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.joml.Vector2i;

/**
 * A proxy layer for adding configuration support for existing, non-configurable layers (e.g. vanilla or other mod
 * layers)
 * <p>
 * Modifies the position of the linked layer before it is rendered
 * </p>
 */
public class ConfigurableLayerProxy implements ConfigurableLayer {
    private final Component name;
    private final HudElementConfig config;
    private final int width;
    private final int height;

    /**
     * @param name   Display name for the layer
     * @param config The config that controls the positioning of the layer. Should be defaulted to the location the
     *               layer renders by default
     * @param width  The width of the layer
     * @param height The height of the layer
     */
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

    @Override
    public int getConfigWidth() {
        return width;
    }

    @Override
    public int getConfigHeight() {
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

    /**
     * @param screenWidth
     * @param screenHeight
     * @return The translation to move the layer from its default location to its configured location
     */
    private Vector2i getTranslation(int screenWidth, int screenHeight) {
        Vector2i defaultPos = config.getDefaultAnchor()
                .getPos(config.getDefaultX(), config.getDefaultY(), width, height, screenWidth, screenHeight);
        Vector2i targetPos = config.getPosition(width, height, screenWidth, screenHeight);
        return targetPos.sub(defaultPos);
    }
}
