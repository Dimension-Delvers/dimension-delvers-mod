package com.wanderersoftherift.wotr.gui.config;

import com.mojang.math.Transformation;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector2i;
import org.joml.Vector3f;

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
        if (config.hasOrientation() && config.getOrientation() != config.getDefaultOrientation()) {
            return height;
        }
        return width;
    }

    @Override
    public int getConfigHeight() {
        if (config.hasOrientation() && config.getOrientation() != config.getDefaultOrientation()) {
            return width;
        }
        return height;
    }

    @Override
    public void preRender(GuiGraphics guiGraphics) {
        Vector2i defaultPos = config.getDefaultAnchor()
                .getPos(config.getDefaultX(), config.getDefaultY(), width, height, guiGraphics.guiWidth(),
                        guiGraphics.guiHeight());
        Vector2i targetPos = config.getPosition(getConfigWidth(), getConfigHeight(), guiGraphics.guiWidth(),
                guiGraphics.guiHeight());

        if (config.hasOrientation() && config.getOrientation() != config.getDefaultOrientation()) {
            Quaternionf quaternionf = new Quaternionf();
            quaternionf.rotateAxis(0.5f * Math.PI_f, new Vector3f(0, 0, 1));
            guiGraphics.pose()
                    .pushTransformation(new Transformation(new Vector3f(targetPos.x + height, targetPos.y, 0),
                            new Quaternionf(), new Vector3f(1, 1, 1), quaternionf).compose(
                                    new Transformation(new Vector3f(-defaultPos.x, -defaultPos.y, 0), new Quaternionf(),
                                            new Vector3f(1, 1, 1), new Quaternionf())
                            ));
        } else {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(targetPos.x - defaultPos.x, targetPos.y - defaultPos.y, 0);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
    }

    @Override
    public void postRender(GuiGraphics guiGraphics) {
        guiGraphics.pose().popPose();
    }

}
