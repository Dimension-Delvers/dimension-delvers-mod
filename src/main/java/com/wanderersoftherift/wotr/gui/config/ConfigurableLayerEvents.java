package com.wanderersoftherift.wotr.gui.config;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.init.client.ModConfigurableLayers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

/**
 * Events related to configurable layers
 */
@EventBusSubscriber(modid = WanderersOfTheRift.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class ConfigurableLayerEvents {

    @SubscribeEvent
    public static void postInitPauseScreen(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof PauseScreen) {
            event.addListener(
                    Button.builder(Component.translatable(WanderersOfTheRift.translationId("screen", "configure_hud")),
                            button -> {
                                Minecraft minecraft = Minecraft.getInstance();
                                minecraft.setScreen(new HUDConfigScreen(
                                        Component.translatable(
                                                WanderersOfTheRift.translationId("screen", "configure_hud"))));
                                minecraft.mouseHandler.releaseMouse();
                            }).pos(0, 0).size(80, 20).build());
        }
    }

    @SubscribeEvent
    public static void preRenderLayers(RenderGuiLayerEvent.Pre event) {
        // TODO: If the layer event gets cancelled the pose stack will be ruined, so probably need to add a different
        // hook for this
        ConfigurableLayer layer = ModConfigurableLayers.CONFIGURABLE_LAYER_REGISTRY.getValue(event.getName());
        if (layer != null) {
            layer.preRender(event.getGuiGraphics());
        }
    }

    @SubscribeEvent
    public static void postRenderLayers(RenderGuiLayerEvent.Post event) {
        ConfigurableLayer layer = ModConfigurableLayers.CONFIGURABLE_LAYER_REGISTRY.getValue(event.getName());
        if (layer != null) {
            layer.postRender(event.getGuiGraphics());
        }
    }
}
