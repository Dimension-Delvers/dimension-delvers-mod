package com.dimensiondelvers.dimensiondelvers.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SetItemDamageFunction.class)
public class MixinSetItemDamageFunction {

    // Prevents the log from being spammed whenever this runs
    // isDamageableItem currently always returns false with the no-durability implementation
    // so this method running is unneccessary currently
    @Inject(method = "run", at = @At("HEAD"), cancellable = true)
    private void preventConsoleSpam(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
        cir.setReturnValue(stack);
    }
}
