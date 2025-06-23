package com.wanderersoftherift.wotr.client.tooltip;

import com.mojang.blaze3d.platform.InputConstants;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.init.WotrDataComponentType;
import com.wanderersoftherift.wotr.init.WotrItems;
import com.wanderersoftherift.wotr.init.client.WotrKeyMappings;
import com.wanderersoftherift.wotr.item.runegem.RunegemData;
import com.wanderersoftherift.wotr.item.runegem.RunegemShape;
import com.wanderersoftherift.wotr.item.runegem.RunegemTier;
import com.wanderersoftherift.wotr.modifier.Modifier;
import com.wanderersoftherift.wotr.modifier.ModifierTier;
import com.wanderersoftherift.wotr.modifier.TieredModifier;
import com.wanderersoftherift.wotr.modifier.effect.AbstractModifierEffect;
import com.wanderersoftherift.wotr.modifier.effect.AttributeModifierEffect;
import com.wanderersoftherift.wotr.util.TextureUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ScrollWheelHandler;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.ItemSlotMouseAction;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector2i;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Paginated Runegem tooltip rendering
 * may be beneficial to seperate the pagination into an abstract class
 */

public class RunegemTooltipRenderer implements ClientTooltipComponent {
    private final RunegemComponent cmp;
    private final ResourceLocation tierLocation;
    private final ResourceLocation shapeLocation;
    private final Dimension tierDimensions;
    private final Dimension shapeDimensions;

    private static int currentIndex;
    private static int maxIndex;



    public RunegemTooltipRenderer(RunegemComponent cmp) {
        this.cmp = cmp;
        this.tierLocation = getTierResourceLocation(this.cmp.data.tier());
        this.shapeLocation = getShapeResourceLocation(this.cmp.data.shape());
        this.tierDimensions = new Dimension(TextureUtils.getTextureWidth(this.tierLocation), TextureUtils.getTextureHeight(this.tierLocation));
        this.shapeDimensions = new Dimension(TextureUtils.getTextureWidth(this.shapeLocation), TextureUtils.getTextureHeight(this.shapeLocation));

        if (maxIndex == -1) maxIndex = getModifierGroups(cmp.runegem).size();
    }

    @Override
    public int getHeight(@NotNull Font font) {
        int height = 30;
        RunegemData.ModifierGroup group = this.cmp.data.modifierLists().get(currentIndex);


        for (TieredModifier ignored : group.modifiers()) {
            height += 10;
        }

        if (this.cmp.data.modifierLists().size() > 1) {
            height += 10;
        }

        if (!isKeyDown()) {
            height += 10;
        }

        return height;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        int width = 0;

        width = Math.max(width, font.width(Component.translatable(WanderersOfTheRift.translationId("tooltip", "runegem.modifiers"))));
        width = Math.max(width, font.width(Component.translatable("tooltip." + WanderersOfTheRift.MODID + ".show_extra_info", WotrKeyMappings.SHOW_TOOLTIP_INFO.getKey().getDisplayName().getString())));
        RunegemData.ModifierGroup group = this.cmp.data.modifierLists().get(currentIndex);
        List<TieredModifier> mods = new ArrayList<>(group.modifiers());
        mods.sort(Comparator.comparing(mod -> mod.getName().getString())); // Sort alphabetically

        ResourceLocation socketable = getSocketable(getCurrentModifierGroup(this.cmp.runegem));
        int socketableWidth = TextureUtils.getTextureWidth(socketable);

        width = Math.max(width, 10 + tierDimensions.width + shapeDimensions.width + socketableWidth);

        for (TieredModifier tieredModifier : mods) {

            Holder<Modifier> mod = tieredModifier.modifier();
            int tier = tieredModifier.tier();

            if (mod.getKey() == null) {
                continue;
            }

            MutableComponent cmp = Component.literal("> ").withStyle(ChatFormatting.DARK_GRAY);
            cmp.append(Component.literal("[T" + tier + "] ").withColor((mod.value().getColor())));
            cmp.append(Component
                    .translatable(WanderersOfTheRift.translationId("modifier", mod.getKey().location())).withStyle(Style.EMPTY.withColor(mod.value().getColor())));


            if (isKeyDown()) {
                String tierInfo = "";
                ModifierTier modifier = getModifierTiers(tieredModifier).get(tier - 1);
                for (AbstractModifierEffect effect : getModifierEffects(modifier)) {
                    tierInfo = getTierInfoString(effect, tier);
                }

                cmp.append(tierInfo);
            }


            width = Math.max(width, font.width(cmp));
        }

        return width;
    }

    @Override
    public void renderText(@NotNull Font font, int x, int y, @NotNull Matrix4f matrix, MultiBufferSource.@NotNull BufferSource bufferSource) {
        y += 10;
        if (!isKeyDown()) {
            font.drawInBatch(
                    Component.translatable("tooltip." + WanderersOfTheRift.MODID + ".show_extra_info",
                            WotrKeyMappings.SHOW_TOOLTIP_INFO.getKey().getDisplayName().getString()).withStyle(ChatFormatting.DARK_GRAY),
                    x, y, 0xFFFFFF, true, matrix, bufferSource,
                    Font.DisplayMode.NORMAL, 0, 15_728_880);
            y+= 10;
        }


        font.drawInBatch(Component.translatable(WanderersOfTheRift.translationId("tooltip", "runegem.modifiers")), x, y, 0xFFFFFF, true, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15_728_880);

        y += 10;

        RunegemData.ModifierGroup group = this.cmp.data.modifierLists().get(currentIndex);
        List<TieredModifier> mods = new ArrayList<>(group.modifiers());
        mods.sort(Comparator.comparing(mod -> mod.getName().getString())); // Sort alphabetically


        for (TieredModifier tieredModifier : mods) {
            Holder<Modifier> mod = tieredModifier.modifier();
            int tier = tieredModifier.tier();

            if (mod.getKey() == null) {
                continue;
            }


            MutableComponent cmp = Component.literal("> ").withStyle(ChatFormatting.DARK_GRAY);
            cmp.append(Component.literal("[T" + tier + "] ").withColor((mod.value().getColor())));
            cmp.append(Component
                    .translatable(WanderersOfTheRift.translationId("modifier", mod.getKey().location())).withStyle(Style.EMPTY.withColor(mod.value().getColor())));

            if (isKeyDown()) {
                String tierInfo = "";
                ModifierTier modifier = getModifierTiers(tieredModifier).get(tier - 1);
                for (AbstractModifierEffect effect : getModifierEffects(modifier)) {
                    tierInfo = getTierInfoString(effect, tier);
                }

                cmp.append(Component.literal(tierInfo).withStyle(ChatFormatting.DARK_GRAY));
            }

            font.drawInBatch(
                    cmp,
                    x, y, 0xFFFFFF, true, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15_728_880
            );
            y += 10;
        }


        ClientTooltipComponent.super.renderText(font, x, y, matrix, bufferSource);
    }



    @Override
    public void renderImage(@NotNull Font font, int x, int y, int width, int height, GuiGraphics guiGraphics) {
        ResourceLocation socketable = getSocketable(getCurrentModifierGroup(this.cmp.runegem));
        int socketableHeight = TextureUtils.getTextureHeight(socketable);
        int socketableWidth = TextureUtils.getTextureWidth(socketable);


        guiGraphics.blit(RenderType.GUI_TEXTURED, tierLocation, x, y, 0, 0, tierDimensions.width, tierDimensions.height, tierDimensions.width, tierDimensions.height);
        guiGraphics.blit(RenderType.GUI_TEXTURED, shapeLocation, x + 5 + tierDimensions.width, y, 0, 0, shapeDimensions.width, shapeDimensions.height, shapeDimensions.width, shapeDimensions.height);
        guiGraphics.blit(RenderType.GUI_TEXTURED, socketable, x + 10 + tierDimensions.width + shapeDimensions.width, y, 0, 0, socketableWidth, socketableHeight, socketableWidth, socketableHeight);



        RunegemData.ModifierGroup group = this.cmp.data.modifierLists().get(currentIndex);




        y += (10 * group.modifiers().size()) + 30;

        if (!isKeyDown()) y+= 10;


        if (maxIndex <= 1) return;
        int dotSize = 4;
        int gap = 3;
        int shadowOffset = 1;
        int totalWidth = maxIndex * dotSize + (maxIndex - 1) * gap;
        int startX = x + (width - totalWidth) / 2;
        int dotY = y;

        for (int i = 0; i < maxIndex; i++) {
            int dotX = startX + i * (dotSize + gap);

            int selectedSize = dotSize + 2;
            int halfDiff = (selectedSize - dotSize) / 2;

            // Drop shadow
            int shadowColor = 0xFF222222;
            guiGraphics.fill(dotX + shadowOffset, dotY + shadowOffset, dotX + dotSize + shadowOffset, dotY + dotSize + shadowOffset, shadowColor);

            if (i == currentIndex) {
                guiGraphics.fill(dotX - halfDiff + shadowOffset, dotY - halfDiff + shadowOffset,
                        dotX + dotSize + halfDiff + shadowOffset, dotY + dotSize + halfDiff + shadowOffset,
                        0xFF222222);

                guiGraphics.fill(dotX - 1, dotY - 1, dotX + dotSize + 1, dotY + dotSize + 1, 0xFFAAAAAA);

            } else {
                // Inactive: normal dark gray
                guiGraphics.fill(dotX, dotY, dotX + dotSize, dotY + dotSize, 0xFF555555);
            }
        }



        ClientTooltipComponent.super.renderImage(font, x, y, width, height, guiGraphics);
    }

    private static ResourceLocation getTierResourceLocation(RunegemTier tier) {
        return WanderersOfTheRift.id("textures/tooltip/runegem/tier/" + tier.getName() + ".png");
    }

    private static ResourceLocation getShapeResourceLocation(RunegemShape shape) {
        return WanderersOfTheRift.id("textures/tooltip/runegem/shape/text/" + shape.getName() + ".png");
    }

    private static ResourceLocation getSocketable(RunegemData.ModifierGroup modifierGroup) {
        if (modifierGroup.supportedItems().unwrapKey().isPresent()) {
            return WanderersOfTheRift.id("textures/tooltip/runegem/socketable/" + modifierGroup.supportedItems().unwrapKey().get().location().getPath() + ".png");
        }

        return null;
    }

    private static List<RunegemData.ModifierGroup> getModifierGroups(ItemStack stack) {
        if (stack != null && stack.has(WotrDataComponentType.RUNEGEM_DATA)) {
            RunegemData gemData = stack.get(WotrDataComponentType.RUNEGEM_DATA);
            if (gemData != null) {
                return gemData.modifierLists();
            }
        }

        return null;
    }

    private static RunegemData.ModifierGroup getCurrentModifierGroup(ItemStack stack) {
        return getModifierGroups(stack).get(currentIndex);
    }

    private static String getTierInfoString(AbstractModifierEffect effect, int tier) {
        if (effect instanceof AttributeModifierEffect attr) {
            return " (T" + tier + " : " + formatRoll(attr.getMinimumRoll()) + " - " + formatRoll(attr.getMaximumRoll())
                    + ")";
        }
        return " (T " + tier + ")";
    }

    private static String formatRoll(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }

    private static List<ModifierTier> getModifierTiers(TieredModifier mod) {
        return mod.modifier().value().getModifierTierList();
    }

    private static List<AbstractModifierEffect> getModifierEffects(ModifierTier tier) {
        return tier.getModifierEffects();
    }

    private static boolean isKeyDown() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(),
                WotrKeyMappings.SHOW_TOOLTIP_INFO.getKey().getValue());
    }

    public static class RunegemMouseActions implements ItemSlotMouseAction {
        private final ScrollWheelHandler scrollWheelHandler;

        public RunegemMouseActions() {
            this.scrollWheelHandler = new ScrollWheelHandler();
        }

        @Override
        public boolean matches(Slot slot) {
            return slot.getItem().is(WotrItems.RUNEGEM) && slot.getItem().get(WotrDataComponentType.RUNEGEM_DATA) != null;
        }

        @Override
        public boolean onMouseScrolled(double xOffset, double yOffset, int ignored, @NotNull ItemStack itemStack) {
            List<RunegemData.ModifierGroup> groups = getModifierGroups(itemStack);

            if (groups == null || groups.isEmpty()) {
                return false;
            }

            maxIndex = groups.size();

            Vector2i scroll = this.scrollWheelHandler.onMouseScroll(xOffset, yOffset);
            int direction = scroll.y == 0 ? -scroll.x : scroll.y;

            if(direction == 0) {
                return false;
            }

            int nextIndex = ScrollWheelHandler.getNextScrollWheelSelection(direction, currentIndex, maxIndex);
            if (nextIndex != currentIndex) {
                currentIndex = nextIndex;
                return true;
            }

            return false;
        }

        @Override
        public void onStopHovering(@NotNull Slot slot) {
            RunegemTooltipRenderer.currentIndex = 0;
            RunegemTooltipRenderer.maxIndex = -1;
        }

        @Override
        public void onSlotClicked(@NotNull Slot slot, @NotNull ClickType clickType) {
        }
    }


    public record RunegemComponent(ItemStack runegem, RunegemData data) implements TooltipComponent {
    }
}
