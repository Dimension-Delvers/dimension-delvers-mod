package com.wanderersoftherift.wotr.gui.configuration;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import org.joml.Vector2i;

/**
 * Provides anchors for UI element positioning
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

    public HorizontalAnchor getHorizontal() {
        return horizontalAnchor;
    }

    public VerticalAnchor getVertical() {
        return verticalAnchor;
    }

    public Vector2i getPos(int x, int y, int width, int height, int screenWidth, int screenHeight) {
        return new Vector2i(horizontalAnchor.getPos(x, width, screenWidth),
                verticalAnchor.getPos(y, height, screenHeight));
    }

    public static ScreenAnchor get(HorizontalAnchor horizontalAnchor, VerticalAnchor verticalAnchor) {
        return lookup.get(horizontalAnchor, verticalAnchor);
    }
}
