package com.dimensiondelvers.dimensiondelvers.gui.layer.objective;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class StealthObjectiveRenderer extends ObjectiveRenderer {
    private static final ResourceLocation ALARM_SPRITE = DimensionDelvers.id("objective/stealth/alarm");
    private static final ResourceLocation ALERT_SPRITE = DimensionDelvers.id("objective/stealth/alert");
    private static final ResourceLocation BAR_BACKGROUND_SPRITE = DimensionDelvers.id("objective/stealth/bar_background");
    private static final ResourceLocation BAR_PROGRESS_SPRITE = DimensionDelvers.id("objective/stealth/bar_progress");

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        PoseStack poseStack = guiGraphics.pose();
        Minecraft mc = Minecraft.getInstance();
        Window window = mc.getWindow();
        int scaledWidth = window.getGuiScaledWidth();
        int scaledHeight = window.getGuiScaledHeight();
        LocalPlayer player = mc.player;


        int textureWidth = 52;
        int spacing = 5;
        int x = (guiGraphics.guiWidth()/2) - (textureWidth/2);
        int y = 14;
        float progress = 0.84F;
        int progressWidth = (int) (progress * (textureWidth+1));
        guiGraphics.blitSprite(RenderType::guiTextured, ALARM_SPRITE, x-spacing-10 , y-2, 10, 10);
        guiGraphics.blitSprite(RenderType::guiTextured, BAR_BACKGROUND_SPRITE, x, y, textureWidth, 5);
        if (progressWidth > 0) {
            guiGraphics.blitSprite(RenderType::guiTextured, BAR_PROGRESS_SPRITE, textureWidth, 5, 0, 0, x, y, progressWidth, 5);
        }
        if(progress < 0.84F && progress > 0.5F) {
            guiGraphics.blitSprite(RenderType::guiTextured, ALERT_SPRITE, x+textureWidth+spacing , y-4, 14, 14);
        }
        if(progress >= 0.84F && player.tickCount % 16 < 8) {
            guiGraphics.blitSprite(RenderType::guiTextured, ALERT_SPRITE, x+textureWidth+spacing , y-5, 14, 14);
        }

    }
}
