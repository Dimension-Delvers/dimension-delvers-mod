package com.wanderersoftherift.wotr.gui.configuration;

import org.joml.Vector2i;

/**
 * Provides anchors for UI element positioning
 */
public enum ScreenAnchor {
    ABSOLUTE {
        @Override
        public Vector2i getPos(int x, int y, int width, int height, int screenWidth, int screenHeight) {
            return new Vector2i(x - width / 2, y - height / 2);
        }
    },
    TOP_LEFT {
        @Override
        public Vector2i getPos(int x, int y, int width, int height, int screenWidth, int screenHeight) {
            return new Vector2i(x, y);
        }
    },
    TOP_CENTER {
        @Override
        public Vector2i getPos(int x, int y, int width, int height, int screenWidth, int screenHeight) {
            return new Vector2i((screenWidth - width) / 2 + x, y);
        }
    },
    TOP_RIGHT {
        @Override
        public Vector2i getPos(int x, int y, int width, int height, int screenWidth, int screenHeight) {
            return new Vector2i((screenWidth - width) + x, y);
        }
    },
    CENTER_LEFT {
        @Override
        public Vector2i getPos(int x, int y, int width, int height, int screenWidth, int screenHeight) {
            return new Vector2i(x, (screenHeight - height) / 2 + y);
        }
    },
    CENTER {
        @Override
        public Vector2i getPos(int x, int y, int width, int height, int screenWidth, int screenHeight) {
            return new Vector2i((screenWidth - width) / 2 + x, (screenHeight - height) / 2 + y);
        }
    },
    CENTER_RIGHT {
        @Override
        public Vector2i getPos(int x, int y, int width, int height, int screenWidth, int screenHeight) {
            return new Vector2i((screenWidth - width) + x, (screenHeight - height) / 2 + y);
        }
    },
    BOTTOM_LEFT {
        @Override
        public Vector2i getPos(int x, int y, int width, int height, int screenWidth, int screenHeight) {
            return new Vector2i(x, screenHeight - height + y);
        }
    },
    BOTTOM_CENTER {
        @Override
        public Vector2i getPos(int x, int y, int width, int height, int screenWidth, int screenHeight) {
            return new Vector2i((screenWidth - width) / 2 + x, screenHeight - height + y);
        }
    },
    BOTTOM_RIGHT {
        @Override
        public Vector2i getPos(int x, int y, int width, int height, int screenWidth, int screenHeight) {
            return new Vector2i(screenWidth - width + x, screenHeight - height + y);
        }
    };

    public abstract Vector2i getPos(int x, int y, int width, int height, int screenWidth, int screenHeight);
}
