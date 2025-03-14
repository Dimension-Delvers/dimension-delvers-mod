package com.wanderersoftherift.wotr.client.gui.hud;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.abilities.AbstractAbility;
import com.wanderersoftherift.wotr.init.ModAttachments;
import com.wanderersoftherift.wotr.item.skillgem.AbilitySlots;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public final class AbilityBar {

    private static final ResourceLocation BACKGROUND = WanderersOfTheRift.id("textures/gui/hud/ability_bar/background.png");

    private static final int BACKGROUND_WIDTH = 24;
    private static final int BACKGROUND_HEIGHT = 60;

    private static final int BAR_OFFSET_X = -4;
    private static final int BAR_OFFSET_Y = -4;
    private static final int SKILL_OFFSET_X = 4;
    private static final int SKILL_START_OFFSET_Y = 4;
    private static final int SKILL_OFFSET_Y = 2;

    public static void render(GuiGraphics graphics, LocalPlayer player, ClientLevel level, DeltaTracker partialTick) {
        AbilitySlots abilitySlots = player.getData(ModAttachments.ABILITY_SLOTS);
        if (abilitySlots.getSlots() == 0) {
            return;
        }
        renderBackground(graphics, abilitySlots);
        renderAbilities(graphics, player, abilitySlots);
    }

    private static void renderAbilities(GuiGraphics graphics, LocalPlayer player, AbilitySlots abilitySlots) {
        int yOffset = BAR_OFFSET_Y + SKILL_START_OFFSET_Y;
        for (int i = 0; i < abilitySlots.getSlots(); i++) {
            AbstractAbility ability = abilitySlots.getAbilityInSlot(i);
            if (ability != null) {
                graphics.blit(RenderType::guiTextured, ability.getIcon(), BAR_OFFSET_X + SKILL_OFFSET_X, yOffset + i * 18, 0, 0, 16, 16, 16, 16);
            }
        }
    }

    private static void renderBackground(GuiGraphics graphics, AbilitySlots abilitySlots) {
        int yOffset = BAR_OFFSET_Y;
        for (int i = 0; i < abilitySlots.getSlots(); i++) {
            if (i == 0) {
                graphics.blit(RenderType::guiTextured, BACKGROUND, BAR_OFFSET_X, yOffset, 0, 0, 24, 20, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
                yOffset += 20;
            } else {
                graphics.blit(RenderType::guiTextured, BACKGROUND, BAR_OFFSET_X, yOffset, 0, 20, 24, 18, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
                yOffset += 18;
            }
        }
        graphics.blit(RenderType::guiTextured, BACKGROUND, BAR_OFFSET_X, yOffset, 0, 56, 24, 4, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
    }

    private AbilityBar() {}
}
