package com.wanderersoftherift.wotr.client;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.gui.configuration.ConfigurableLayer;
import com.wanderersoftherift.wotr.gui.configuration.ConfigureHUDScreen;
import com.wanderersoftherift.wotr.init.client.ModConfigurableLayers;
import com.wanderersoftherift.wotr.init.client.ModKeybinds;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;

/**
 * Events related to abilities - key activation detection and mana ticking.
 */
@EventBusSubscriber(modid = WanderersOfTheRift.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class ConfigureHUDEvents {

    @SubscribeEvent
    public static void processConfigureHudKey(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();

        Player player = minecraft.player;
        if (player == null) {
            return;
        }
        if (ModKeybinds.CONFIG_HUD.consumeClick()) {
            if (minecraft.screen == null) {
                // TODO: non-literal
                minecraft.pushGuiLayer(new ConfigureHUDScreen(Component.literal("Configure HUD")));
                minecraft.mouseHandler.releaseMouse();
            }
        }
    }

    @SubscribeEvent
    public static void preRenderLayers(RenderGuiLayerEvent.Pre event) {
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
