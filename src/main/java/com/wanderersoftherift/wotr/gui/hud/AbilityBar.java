package com.wanderersoftherift.wotr.gui.hud;

import com.mojang.blaze3d.platform.InputConstants;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.abilities.AbstractAbility;
import com.wanderersoftherift.wotr.abilities.Serializable.PlayerCooldownData;
import com.wanderersoftherift.wotr.client.ModClientEvents;
import com.wanderersoftherift.wotr.init.ModAttachments;
import com.wanderersoftherift.wotr.item.skillgem.AbilitySlots;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
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
    private static final ResourceLocation SELECTED_OVERLAY = WanderersOfTheRift.id("textures/gui/hud/ability_bar/select.png");

    private static final int BACKGROUND_WIDTH = 24;
    private static final int BACKGROUND_HEIGHT = 60;

    private static final int BAR_OFFSET_X = -4;
    private static final int BAR_OFFSET_Y = -4;
    private static final int SKILL_OFFSET_X = 4;
    private static final int SKILL_START_OFFSET_Y = 4;
    private static final int SKILL_OFFSET_Y = 2;
    private static final float KEYBIND_SCALE = 0.5f;

    public static void render(GuiGraphics graphics, LocalPlayer player, ClientLevel level, DeltaTracker partialTick) {
        AbilitySlots abilitySlots = player.getData(ModAttachments.ABILITY_SLOTS);
        if (abilitySlots.getSlots() == 0) {
            return;
        }
        PlayerCooldownData cooldowns = player.getData(COOL_DOWNS);

        renderBackground(graphics, abilitySlots);
        renderAbilities(graphics, abilitySlots, cooldowns);
        renderAbilityKeyBinds(graphics);
    }

    private static void renderAbilities(GuiGraphics graphics, AbilitySlots abilitySlots, PlayerCooldownData cooldowns) {
        int yOffset = BAR_OFFSET_Y + SKILL_START_OFFSET_Y;
        for (int slot = 0; slot < abilitySlots.getSlots(); slot++) {
            AbstractAbility ability = abilitySlots.getAbilityInSlot(slot);
            if (ability != null) {
                graphics.blit(RenderType::guiTextured, ability.getIcon(), BAR_OFFSET_X + SKILL_OFFSET_X, yOffset + slot * 18, 0, 0, 16, 16, 16, 16);
            }

            if (cooldowns.isOnCooldown(slot)) {
                int overlayHeight = Math.clamp((16L * cooldowns.getCooldownRemaining(slot) / cooldowns.getLastCooldownValue(slot)), 0, 16);
                graphics.blit(RenderType::guiTextured, COOLDOWN_OVERLAY, BAR_OFFSET_X + SKILL_OFFSET_X, yOffset + slot * 18 + 16 - overlayHeight, 0, 0, 16, overlayHeight, 16, 16);
            }
        }
        int selected = abilitySlots.getSelectedSlot();
        graphics.blit(RenderType::guiTextured, SELECTED_OVERLAY, BAR_OFFSET_X + SKILL_OFFSET_X - 6, yOffset + selected * 18 - 3, 0, 0, 28, 22, 28, 22);
    }

    private static void renderAbilityKeyBinds(GuiGraphics graphics) {
        Font font = Minecraft.getInstance().font;
        int yOffset = BAR_OFFSET_Y + SKILL_START_OFFSET_Y;
        graphics.pose().pushPose();
        graphics.pose().scale(KEYBIND_SCALE, KEYBIND_SCALE, KEYBIND_SCALE);
        float inverseScale = 1.0f / KEYBIND_SCALE;
        for (int slot = 0; slot < ModClientEvents.ABILITY_SLOT_KEYS.size(); slot++) {
            Component keyText = getShortKeyDescription(ModClientEvents.ABILITY_SLOT_KEYS.get(slot));
            int keyTextWidth = font.width(keyText);
            if (keyTextWidth > 31) {
                keyText = Component.literal("...");
                keyTextWidth = font.width(keyText);
            }
            graphics.drawString(font, keyText, (int) (inverseScale * (BAR_OFFSET_X + SKILL_OFFSET_X + 16)) - keyTextWidth - 1, (int) (inverseScale * (yOffset + (slot + 1) * 18 - 2)) - font.lineHeight, ChatFormatting.WHITE.getColor());
        }
        graphics.pose().popPose();
    }

    private static Component getShortKeyDescription(KeyMapping keyMapping) {
        return switch (keyMapping.getKeyModifier()) {
            case ALT -> Component.translatable(WanderersOfTheRift.translationId("keybinds", "mod_alt")).append(getUnmodifiedKeyDescription(keyMapping));
            case SHIFT -> Component.translatable(WanderersOfTheRift.translationId("keybinds", "mod_shift")).append(getUnmodifiedKeyDescription(keyMapping));
            case CONTROL -> Component.translatable(WanderersOfTheRift.translationId("keybinds", "mod_ctrl")).append(getUnmodifiedKeyDescription(keyMapping));
            case NONE -> getUnmodifiedKeyDescription(keyMapping);
        };
    }

    private static Component getUnmodifiedKeyDescription(KeyMapping keyMapping) {
        if (keyMapping.getKey().getType() == InputConstants.Type.MOUSE) {
            return Component.literal("M" + keyMapping.getKey().getValue());
        }
        if (keyMapping.getKey().getType() == InputConstants.Type.KEYSYM) {
            return switch (keyMapping.getKey().getValue()) {
                case InputConstants.KEY_LALT -> Component.translatable(WanderersOfTheRift.translationId("keybinds", "l_alt"));
                case InputConstants.KEY_RALT -> Component.translatable(WanderersOfTheRift.translationId("keybinds", "r_alt"));
                case InputConstants.KEY_LCONTROL -> Component.translatable(WanderersOfTheRift.translationId("keybinds", "l_ctrl"));
                case InputConstants.KEY_RCONTROL -> Component.translatable(WanderersOfTheRift.translationId("keybinds", "r_ctrl"));
                default -> keyMapping.getKey().getDisplayName();
            };
        }
        return keyMapping.getKey().getDisplayName();
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
