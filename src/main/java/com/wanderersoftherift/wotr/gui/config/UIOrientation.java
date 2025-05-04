package com.wanderersoftherift.wotr.gui.config;

import org.joml.Vector2i;
import org.joml.Vector2ic;

/**
 * Orientation to render a UI element with
 */
public enum UIOrientation {
    HORIZONTAL(new Vector2i(1, 0)),
    VERTICAL(new Vector2i(0, 1));

    private final Vector2i axis;

    UIOrientation(Vector2i axis) {
        this.axis = axis;
    }

    public UIOrientation next() {
        return UIOrientation.values()[(ordinal() + 1) % UIOrientation.values().length];
    }

    public Vector2ic axis() {
        return axis;
    }
}
