package com.wanderersoftherift.wotr.gui.configuration;

/**
 * Provides anchors for UI element positioning
 */
public enum HorizontalAnchor {
    LEFT {
        @Override
        public int getPos(int x, int width, int screenWidth) {
            return x;
        }
    },
    CENTER {
        @Override
        public int getPos(int x, int width, int screenWidth) {
            return (screenWidth - width) / 2 + x;
        }
    },
    RIGHT {
        @Override
        public int getPos(int x, int width, int screenWidth) {
            return screenWidth - width + x;
        }
    };

    public abstract int getPos(int x, int width, int screenWidth);

    public static HorizontalAnchor getClosest(int pos, int width, int screenWidth) {
        HorizontalAnchor result = LEFT;
        int dist = pos;
        int centerDist = Math.abs(pos + (width - screenWidth) / 2);
        if (centerDist < dist) {
            dist = centerDist;
            result = CENTER;
        }
        int rightDist = screenWidth - pos - width;
        if (rightDist < dist) {
            result = RIGHT;
        }
        return result;
    }

}
