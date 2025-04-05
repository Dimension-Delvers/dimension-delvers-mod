package com.wanderersoftherift.wotr.gui.hud;

import com.wanderersoftherift.wotr.abilities.effects.marker.EffectDisplayData;
import com.wanderersoftherift.wotr.abilities.effects.marker.EffectMarker;
import com.wanderersoftherift.wotr.init.ModAttachments;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.NotNull;

/**
 * Displays effect markers attached to the player
 */
public final class EffectBar implements LayeredDraw.Layer {

    private static final int BAR_OFFSET_X = 20;
    private static final int ICON_SIZE = 16;
    public static final float FAST_PULSE_THRESHOLD = 40.0f;
    public static final float SLOW_PULSE_THRESHOLD = 100.0f;

    @Override
    public void render(@NotNull GuiGraphics graphics, @NotNull DeltaTracker deltaTracker) {
        if (Minecraft.getInstance().options.hideGui) {
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        EffectDisplayData data = player.getData(ModAttachments.EFFECT_DISPLAY);

        renderEffects(graphics, data);
    }

    private void renderEffects(GuiGraphics graphics, EffectDisplayData data) {
        int effectCount = 0;
        ObjectIterator<Object2FloatMap.Entry<Holder<EffectMarker>>> iterator = data.iterate();
        while (iterator.hasNext()) {
            Object2FloatMap.Entry<Holder<EffectMarker>> entry = iterator.next();
            EffectMarker marker = entry.getKey().value();
            boolean show = true;
            if (entry.getFloatValue() < FAST_PULSE_THRESHOLD) {
                show = ((int) entry.getFloatValue() % 4) != 0;
            } else if (entry.getFloatValue() < SLOW_PULSE_THRESHOLD) {
                show = ((int) entry.getFloatValue()) % 16 != 0;
            }
            if (show) {
                graphics.blit(RenderType::guiTextured, marker.icon(), BAR_OFFSET_X, effectCount * ICON_SIZE, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
            }
            effectCount++;
        }
    }

}
