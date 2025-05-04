package com.wanderersoftherift.wotr.gui.config;

/**
 * Orientation to render a UI element with
 */
public enum UIOrientation {
    HORIZONTAL,
    VERTICAL;

    public UIOrientation next() {
        return UIOrientation.values()[(ordinal() + 1) % UIOrientation.values().length];
    }
}
