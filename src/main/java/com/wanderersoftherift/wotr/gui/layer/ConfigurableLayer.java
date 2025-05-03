package com.wanderersoftherift.wotr.gui.layer;

import com.wanderersoftherift.wotr.config.HudElementConfig;
import net.minecraft.network.chat.Component;

public interface ConfigurableLayer {

    Component getName();

    HudElementConfig getConfig();

    int getWidth();

    int getHeight();
}
