package com.dimensiondelvers.dimensiondelvers.common;


import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry;
import com.dimensiondelvers.dimensiondelvers.abilities.AbilityAttributes;
import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import com.dimensiondelvers.dimensiondelvers.networking.ModPayloads;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import static com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry.ABILITY_REGISTRY;
import static com.dimensiondelvers.dimensiondelvers.init.ModAbilities.COOL_DOWN_ATTACHMENTS;

@EventBusSubscriber(modid = DimensionDelvers.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CommonModEvents {

    @SubscribeEvent
    public static void registerNetworkHandlers(final RegisterPayloadHandlersEvent event)
    {
        ModPayloads.register(event.registrar("1"));
    }

    @SubscribeEvent
    static void registerRegistries(NewRegistryEvent event) {
        event.register(ABILITY_REGISTRY);
    }

    @SubscribeEvent
    static void addAttributesToPlayer(EntityAttributeModificationEvent event)
    {
        //TODO look into automating this so it can be less tedious
        if(!event.has(EntityType.PLAYER, AbilityAttributes.MAX_MANA)) {
            event.add(EntityType.PLAYER, AbilityAttributes.MAX_MANA);
        }

        if(!event.has(EntityType.PLAYER, AbilityAttributes.HEAL_EFFECTIVENESS)) {
            event.add(EntityType.PLAYER, AbilityAttributes.HEAL_EFFECTIVENESS);
        }

        if(!event.has(EntityType.PLAYER, AbilityAttributes.HEAL_COOLDOWN)) {
            event.add(EntityType.PLAYER, AbilityAttributes.HEAL_COOLDOWN);
        }

        if(!event.has(EntityType.PLAYER, AbilityAttributes.BOOST_STRENGTH)) {
            event.add(EntityType.PLAYER, AbilityAttributes.BOOST_STRENGTH);
        }

        if(!event.has(EntityType.PLAYER, AbilityAttributes.BOOST_COOLDOWN)) {
            event.add(EntityType.PLAYER, AbilityAttributes.BOOST_COOLDOWN);
        }
        if(!event.has(EntityType.PLAYER, AbilityAttributes.ARROW_COOLDOWN)) {
            event.add(EntityType.PLAYER, AbilityAttributes.ARROW_COOLDOWN);
        }

    }
}
