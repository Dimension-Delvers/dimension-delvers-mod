package com.wanderersoftherift.wotr.gui.hud;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.abilities.AbstractAbility;
import com.wanderersoftherift.wotr.abilities.Serializable.PlayerCooldownData;
import com.wanderersoftherift.wotr.client.ModClientEvents;
import com.wanderersoftherift.wotr.init.ModAttachments;
import com.wanderersoftherift.wotr.item.skillgem.AbilitySlots;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static com.wanderersoftherift.wotr.init.ModAttachments.COOL_DOWNS;

public final class AbilityBar {

    private static final ResourceLocation BACKGROUND = WanderersOfTheRift.id("textures/gui/hud/ability_bar/background.png");
    private static final ResourceLocation COOLDOWN_OVERLAY = WanderersOfTheRift.id("textures/gui/hud/ability_bar/cooldown_overlay.png");

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
        PlayerCooldownData cooldowns = player.getData(COOL_DOWNS);

        renderBackground(graphics, abilitySlots);
        renderAbilities(graphics, player, abilitySlots, cooldowns);
    }

    private static void renderAbilities(GuiGraphics graphics, LocalPlayer player, AbilitySlots abilitySlots, PlayerCooldownData cooldowns) {
        Font font = Minecraft.getInstance().font;
        int yOffset = BAR_OFFSET_Y + SKILL_START_OFFSET_Y;
        for (int slot = 0; slot < abilitySlots.getSlots(); slot++) {
            AbstractAbility ability = abilitySlots.getAbilityInSlot(slot);
            if (ability != null) {
                graphics.blit(RenderType::guiTextured, ability.getIcon(), BAR_OFFSET_X + SKILL_OFFSET_X, yOffset + slot * 18, 0, 0, 16, 16, 16, 16);
            }

            if (cooldowns.isOnCooldown(slot)) {
                int overlayHeight = Math.clamp((int) (16 * cooldowns.getCooldown(slot) / ability.getBaseCooldown()), 0, 16);
                graphics.blit(RenderType::guiTextured, COOLDOWN_OVERLAY, BAR_OFFSET_X + SKILL_OFFSET_X, yOffset + slot * 18  + 16 - overlayHeight, 0, 0, 16, overlayHeight, 16, 16);
            }

            Component keyText = ModClientEvents.ABILITY_SLOT_KEYS.get(slot).getTranslatedKeyMessage();
            int keyTextWidth = font.width(keyText);
            if (keyTextWidth <= 15) {
                graphics.drawString(font, keyText, BAR_OFFSET_X + SKILL_OFFSET_X + 15 - keyTextWidth, yOffset + (slot + 1) * 18 - font.lineHeight - 2, ChatFormatting.WHITE.getColor());
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
