package com.wanderersoftherift.wotr.upgrades;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class UnlockAllAbilitiesUpgrade extends AbstractUpgrade{
    public UnlockAllAbilitiesUpgrade(ResourceLocation upgradeName) {
        super(upgradeName);
    }

    @Override
    public void unlock(Player p) {
//        for(AbstractAbility abstractAbility: AbilityRegistry.ABILITY_REGISTRY.stream().toList())
//        {
//            p.setData(ModAbilities.ABILITY_UNLOCKED_ATTACHMENTS.get(abstractAbility.getName()), true);
//        }
        //Use translateable string when you can! This is purely for showing the system, and im too lazy for datagen stuff
        ((ServerPlayer)p).sendSystemMessage(Component.literal("You unlocked all abilities!"));
        super.unlock(p);
    }

    @Override
    public void remove(Player p) {

//        for(AbstractAbility abstractAbility: AbilityRegistry.ABILITY_REGISTRY.stream().toList())
//        {
//            p.setData(ModAbilities.ABILITY_UNLOCKED_ATTACHMENTS.get(abstractAbility.getName()), false);
//        }
        ((ServerPlayer)p).sendSystemMessage(Component.literal("You removed all abilities!"));
        super.remove(p);
    }
}
