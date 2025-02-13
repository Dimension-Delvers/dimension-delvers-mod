package com.dimensiondelvers.dimensiondelvers.item.runegem;

import com.dimensiondelvers.dimensiondelvers.init.ModDataComponentType;
import com.dimensiondelvers.dimensiondelvers.item.socket.GearSocket;
import com.dimensiondelvers.dimensiondelvers.item.socket.GearSockets;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;

public class Runegem extends Item {
    public Runegem(Properties properties) {
        super(properties);
    }

    //Temporary testing code
    public boolean onStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY || player.level().isClientSide) {
            return false;
        } else {
            RunegemData runegemData = stack.get(ModDataComponentType.RUNEGEM_DATA);
            ItemStack targetStack = slot.getItem();
            if (runegemData == null || stack.getCount() != 1 || targetStack.isEmpty()) {
                return false;
            }
            GearSockets gearSockets = targetStack.get(ModDataComponentType.GEAR_SOCKETS);
            if (gearSockets != null && player.level() instanceof ServerLevel serverLevel) {
                boolean applied = this.applyRunegemToItem(stack, targetStack, runegemData, gearSockets, serverLevel);
                if (!applied) {
                    return false;
                }
                stack.shrink(1);
                return true;
            }
            return false;
        }
    }

    private boolean applyRunegemToItem(ItemStack stack, ItemStack targetStack, RunegemData runegemData, GearSockets gearSockets, ServerLevel serverLevel) {
        List<GearSocket> sockets = gearSockets.sockets();
        int idx = 0;
        for (GearSocket socket : sockets) {
            if (!socket.canBeApplied(runegemData)) {
                idx++;
                continue;
            }
            Holder<Enchantment> enchantment = getRandomModifier(serverLevel, runegemData.tag());
            sockets.set(idx, new GearSocket(socket.runeGemShape(), enchantment, stack));
            targetStack.set(ModDataComponentType.GEAR_SOCKETS, new GearSockets(sockets));
            LogUtils.getLogger().info("Applied Runegem to item");
            return true;
        }
        return false;
    }

    private Holder<Enchantment> getRandomModifier(ServerLevel serverLevel, TagKey<Enchantment> tag) {
        return serverLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                .get(tag)
                .flatMap(holders -> holders.getRandomElement(serverLevel.random))
                .orElse(null);
    }

}
