package com.wanderersoftherift.wotr.mixin;

import com.wanderersoftherift.wotr.client.tooltip.RunegemTooltipRenderer;
import net.minecraft.client.gui.ItemSlotMouseAction;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class MixinAbstractContainerScreen extends Screen {
    private MixinAbstractContainerScreen(Component title) {
        super(title);
    }

    @Shadow
    protected abstract void addItemSlotMouseAction(ItemSlotMouseAction itemSlotMouseAction);


    @Inject(method = "init", at = @At("TAIL"))
    private void addSlotMouseActions(CallbackInfo ci) {
        this.addItemSlotMouseAction(new RunegemTooltipRenderer.RunegemMouseActions());
    }
}
