package com.dimensiondelvers.dimensiondelvers.upgrades;

import com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry;
import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class UnlockAbilityUpgrade extends AbstractUpgrade{
    public ResourceLocation ability;
    public UnlockAbilityUpgrade(ResourceLocation upgradeName, ResourceLocation abilityName) {
        super(upgradeName);
        this.ability = abilityName;
    }

    @Override
    public void unlock(Player p) {

//        p.setData(ModAbilities.ABILITY_UNLOCKED_ATTACHMENTS.get(AbilityRegistry.ABILITY_REGISTRY.get(ability).get().value().getName()), true);

        //Use translateable string when you can! This is purely for showing the system, and im too lazy for datagen stuff
//        ((ServerPlayer)p).sendSystemMessage(Component.literal("You learned ").append(Component.translatable(AbilityRegistry.ABILITY_REGISTRY.get(ability).get().value().GetTranslationString())));
        super.unlock(p);
    }

    @Override
    public void remove(Player p) {

//        p.setData(ModAbilities.ABILITY_UNLOCKED_ATTACHMENTS.get(AbilityRegistry.ABILITY_REGISTRY.get(ability).get().value().getName()), false);
//        ((ServerPlayer)p).sendSystemMessage(Component.literal("You unlearned ").append(Component.translatable(AbilityRegistry.ABILITY_REGISTRY.get(ability).get().value().GetTranslationString())));
        super.remove(p);
    }

//    public static class UnlockBoostAbilityUpgrade extends UnlockAbilityUpgrade{
//        public UnlockBoostAbilityUpgrade(ResourceLocation upgradeName) {
//            super(upgradeName, ModAbilities.BOOST_ABILITY.get().getName());
//        }
//
//    }

//    public static class UnlockArrowAbilityUpgrade extends UnlockAbilityUpgrade{
//        public UnlockArrowAbilityUpgrade(ResourceLocation upgradeName) {
//            super(upgradeName, ModAbilities.SUMMON_ARROW_ABILITY.get().getName());
//        }
//
//    }

//    public static class UnlockArmorStandAbilityUpgrade extends UnlockAbilityUpgrade{
//        public UnlockArmorStandAbilityUpgrade(ResourceLocation upgradeName) {
//            super(upgradeName, ModAbilities.ARMOR_STAND_ABILITY.get().getName());
//        }
//
//    }

//    public static class UnlockHealAbilityUpgrade extends UnlockAbilityUpgrade{
//        public UnlockHealAbilityUpgrade(ResourceLocation upgradeName) {
//            super(upgradeName, ModAbilities.HEAL_ABILITY.get().getName());
//        }
//
//    }

//    public static class UnlockPrettyAbilityUpgrade extends UnlockAbilityUpgrade{
//        public UnlockPrettyAbilityUpgrade(ResourceLocation upgradeName) {
//            super(upgradeName, ModAbilities.BE_PRETTY.get().getName());
//        }
//
//    }

//    public static class UnlockSmolAbilityUpgrade extends UnlockAbilityUpgrade{
//        public UnlockSmolAbilityUpgrade(ResourceLocation upgradeName) {
//            super(upgradeName, ModAbilities.BE_SMOL.get().getName());
//        }
//
//    }
}



