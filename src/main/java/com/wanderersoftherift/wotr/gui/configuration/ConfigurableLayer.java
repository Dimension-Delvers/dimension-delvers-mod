package com.wanderersoftherift.wotr.gui.configuration;

import com.wanderersoftherift.wotr.config.HudElementConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;

public interface ConfigurableLayer extends LayeredDraw.Layer {

    Component getName();

    HudElementConfig getConfig();

    int getWidth();

    int getHeight();

    default void preRender(GuiGraphics guiGraphics) {
    }

    default void postRender(GuiGraphics guiGraphics) {
    }

}
