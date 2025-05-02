package com.wanderersoftherift.wotr.gui.layer;

import com.wanderersoftherift.wotr.config.HudElementConfig;
import net.minecraft.client.gui.LayeredDraw;

public abstract class ConfigurableLayer implements LayeredDraw.Layer {

    private final HudElementConfig config;

    public ConfigurableLayer(HudElementConfig config) {
        this.config = config;
    }

    public HudElementConfig getConfig() {
        return config;
    }

    public abstract int getWidthForConfiguration();

    public abstract int getHeightForConfiguration();
}
