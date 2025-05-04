package com.wanderersoftherift.wotr.gui.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.joml.Vector2i;

/**
 * Configuration data for HUD element positioning
 */
public class HudElementConfig {
    private final ModConfigSpec.EnumValue<ScreenAnchor> anchor;
    private final ModConfigSpec.IntValue x;
    private final ModConfigSpec.IntValue y;

    public HudElementConfig(ModConfigSpec.Builder builder, String elementName, String elementPrefix,
            ScreenAnchor defaultAnchor, int defaultX, int defaultY) {
        builder.push(" == " + elementName + " Location == ");
        anchor = builder.comment(" Where to position the " + elementName + " relative to")
                .defineEnum(elementPrefix + "Anchor", defaultAnchor);
        x = builder.comment(" Relative horizontal position of the " + elementName)
                .defineInRange(elementPrefix + "X", defaultX, Integer.MIN_VALUE, Integer.MAX_VALUE);
        y = builder.comment(" Relative vertical position of the " + elementName)
                .defineInRange(elementPrefix + "Y", defaultY, Integer.MIN_VALUE, Integer.MAX_VALUE);
        builder.pop();
    }

    /**
     * Restore the config to its default settings
     */
    public void reset() {
        anchor.set(anchor.getDefault());
        x.set(x.getDefault());
        y.set(y.getDefault());
    }

    /**
     * @param width        Current width of the element
     * @param height       Current height of the element
     * @param screenWidth  Width of the screen
     * @param screenHeight Height of the screen
     * @return The upper-left corner to render the element, given its current width/height and the screen width/height
     */
    public Vector2i getPosition(int width, int height, int screenWidth, int screenHeight) {
        return getAnchor().getPos(getX(), getY(), width, height, screenWidth, screenHeight);
    }

    public ScreenAnchor getAnchor() {
        return anchor.get();
    }

    public int getX() {
        return x.get();
    }

    public int getY() {
        return y.get();
    }

    public void setAnchor(ScreenAnchor anchor) {
        this.anchor.set(anchor);
    }

    public void setX(int x) {
        this.x.set(x);
    }

    public void setY(int y) {
        this.y.set(y);
    }

    /**
     * Shifts anchor to the nearest one to the element, and adjusts the relative positioning such that the element
     * maintains the same position
     * 
     * @param width
     * @param height
     * @param screenWidth
     * @param screenHeight
     */
    public void reanchor(int width, int height, int screenWidth, int screenHeight) {
        Vector2i pos = getPosition(width, height, screenWidth, screenHeight);
        HorizontalAnchor newHorizontalAnchor = HorizontalAnchor.getClosest(pos.x(), width, screenWidth);
        VerticalAnchor newVerticalAnchor = VerticalAnchor.getClosest(pos.y(), height, screenHeight);
        this.anchor.set(ScreenAnchor.get(newHorizontalAnchor, newVerticalAnchor));
        pos.sub(anchor.get().getPos(0, 0, width, height, screenWidth, screenHeight));
        setX(pos.x);
        setY(pos.y);
    }

    public ScreenAnchor getDefaultAnchor() {
        return anchor.getDefault();
    }

    public int getDefaultX() {
        return x.getDefault();
    }

    public int getDefaultY() {
        return y.getDefault();
    }
}
