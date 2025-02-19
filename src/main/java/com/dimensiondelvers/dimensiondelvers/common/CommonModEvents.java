package com.dimensiondelvers.dimensiondelvers.common;


import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry;
import com.dimensiondelvers.dimensiondelvers.Registries.UpgradeRegistry;
import com.dimensiondelvers.dimensiondelvers.abilities.AbilityAttributes;
import com.dimensiondelvers.dimensiondelvers.abilities.AbstractAbility;
import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import com.dimensiondelvers.dimensiondelvers.networking.ModPayloads;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;


@EventBusSubscriber(modid = DimensionDelvers.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CommonModEvents {

    @SubscribeEvent
    public static void registerNetworkHandlers(final RegisterPayloadHandlersEvent event)
    {
        ModPayloads.register(event.registrar("1"));
    }

    @SubscribeEvent
    static void registerRegistries(NewRegistryEvent event) {
        event.register(UpgradeRegistry.UPGRADE_REGISTRY);
        event.register(AbilityRegistry.ABILITY_TYPES_REGISTRY);
        event.register(AbilityRegistry.EFFECTS_REGISTRY);
    }

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        DimensionDelvers.LOGGER.info("Registering Datapacks");
        event.dataPackRegistry(
                // The registry key.
                AbilityRegistry.DATA_PACK_ABILITY_REG_KEY,
                // The codec of the registry contents.
                AbstractAbility.DIRECT_CODEC,
                // The network codec of the registry contents. Often identical to the normal codec.
                // Maybe a reduced variant of the normal codec that omits data that is not needed on the client.
                // May be null. If null, registry entries will not be synced to the client at all.
                // May be omitted, which is functionally identical to passing null (a method overload
                // with two parameters is called that passes null to the normal three parameter method).
                AbstractAbility.DIRECT_CODEC
                // A consumer which configures the constructed registry via the RegistryBuilder.
                // May be omitted, which is functionally identical to passing builder -> {}.
        );
    }

    /* TODO: Look into a better way to handle this once we decide how we want to approach scaling abilities
     * This adds the different attributes to the player for the different abilities
     */
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

        if(!event.has(EntityType.PLAYER, AbilityAttributes.LARGE_BOOST_STRENGTH)) {
            event.add(EntityType.PLAYER, AbilityAttributes.LARGE_BOOST_STRENGTH);
        }

        if(!event.has(EntityType.PLAYER, AbilityAttributes.BOOST_COOLDOWN)) {
            event.add(EntityType.PLAYER, AbilityAttributes.BOOST_COOLDOWN);
        }
        if(!event.has(EntityType.PLAYER, AbilityAttributes.ARROW_COOLDOWN)) {
            event.add(EntityType.PLAYER, AbilityAttributes.ARROW_COOLDOWN);
        }

        if(!event.has(EntityType.PLAYER, AbilityAttributes.PARTICLE_TIME)) {
            event.add(EntityType.PLAYER, AbilityAttributes.PARTICLE_TIME);
        }

        if(!event.has(EntityType.PLAYER, AbilityAttributes.SMOL_TIME)) {
            event.add(EntityType.PLAYER, AbilityAttributes.SMOL_TIME);
        }

        if(!event.has(EntityType.PLAYER, AbilityAttributes.SMOL_COOLDOWN)) {
            event.add(EntityType.PLAYER, AbilityAttributes.SMOL_COOLDOWN);
        }

    }


}
