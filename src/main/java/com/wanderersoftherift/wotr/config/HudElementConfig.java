package com.wanderersoftherift.wotr.config;

import com.wanderersoftherift.wotr.gui.configuration.ScreenAnchor;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.joml.Vector2i;

public class HudElementConfig {
    private final ModConfigSpec.EnumValue<ScreenAnchor> anchor;
    private final ModConfigSpec.IntValue x;
    private final ModConfigSpec.IntValue y;

    public HudElementConfig(ModConfigSpec.Builder builder, String elementName, String elementPrefix, ScreenAnchor defaultAnchor, int defaultX, int defaultY) {
        builder.push(" == " + elementName + " Location == ");
        anchor = builder.comment(" Where to position the " + elementName + " relative to")
                .defineEnum(elementPrefix + "Anchor", defaultAnchor);
        x = builder.comment(" Relative horizontal position of the " + elementName)
                .defineInRange(elementPrefix + "X", defaultX, Integer.MIN_VALUE, Integer.MAX_VALUE);
        y = builder.comment(" Relative vertical position of the " + elementName)
                .defineInRange(elementPrefix + "Y", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        builder.pop();
    }

    /**
     * @param width
     * @param height
     * @param screenWidth
     * @param screenHeight
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
}
