package com.wanderersoftherift.wotr.gui.config;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import org.joml.Vector2i;

/**
 * Provides anchors for UI element positioning.
 */
public enum ScreenAnchor {
    TOP_LEFT(HorizontalAnchor.LEFT, VerticalAnchor.TOP),
    TOP_CENTER(HorizontalAnchor.CENTER, VerticalAnchor.TOP),
    TOP_RIGHT(HorizontalAnchor.RIGHT, VerticalAnchor.TOP),
    CENTER_LEFT(HorizontalAnchor.LEFT, VerticalAnchor.CENTER),
    CENTER(HorizontalAnchor.CENTER, VerticalAnchor.CENTER),
    CENTER_RIGHT(HorizontalAnchor.RIGHT, VerticalAnchor.CENTER),
    BOTTOM_LEFT(HorizontalAnchor.LEFT, VerticalAnchor.BOTTOM),
    BOTTOM_CENTER(HorizontalAnchor.CENTER, VerticalAnchor.BOTTOM),
    BOTTOM_RIGHT(HorizontalAnchor.RIGHT, VerticalAnchor.BOTTOM);

    private static final Table<HorizontalAnchor, VerticalAnchor, ScreenAnchor> lookup;

    private final HorizontalAnchor horizontalAnchor;
    private final VerticalAnchor verticalAnchor;

    static {
        ImmutableTable.Builder<HorizontalAnchor, VerticalAnchor, ScreenAnchor> builder = ImmutableTable.builder();
        for (ScreenAnchor anchor : ScreenAnchor.values()) {
            builder.put(anchor.horizontalAnchor, anchor.verticalAnchor, anchor);
        }
        lookup = builder.build();
    }

    ScreenAnchor(HorizontalAnchor hAnchor, VerticalAnchor vAnchor) {
        this.horizontalAnchor = hAnchor;
        this.verticalAnchor = vAnchor;
    }

    /**
     * @param horizontalAnchor
     * @param verticalAnchor
     * @return The screen anchor that combines the given horizontal and vertical anchors
     */
    public static ScreenAnchor get(HorizontalAnchor horizontalAnchor, VerticalAnchor verticalAnchor) {
        return lookup.get(horizontalAnchor, verticalAnchor);
    }

    /**
     * @return The horizontal portion of the anchoring
     */
    public HorizontalAnchor getHorizontal() {
        return horizontalAnchor;
    }

    /**
     * @return The vertical portion of the anchoring
     */
    public VerticalAnchor getVertical() {
        return verticalAnchor;
    }

    /**
     * Given the relative coordinates of the element, its dimensions and the screen dimensions, determines the position
     * of the top-left corner of the element
     * 
     * @param relativeX
     * @param relativeY
     * @param width
     * @param height
     * @param screenWidth
     * @param screenHeight
     * @return The top-left position of the element
     */
    public Vector2i getPos(int relativeX, int relativeY, int width, int height, int screenWidth, int screenHeight) {
        return new Vector2i(horizontalAnchor.getPos(relativeX, width, screenWidth),
                verticalAnchor.getPos(relativeY, height, screenHeight));
    }

}
