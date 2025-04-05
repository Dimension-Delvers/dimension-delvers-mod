package com.wanderersoftherift.wotr.common;


import com.wanderersoftherift.wotr.Registries.AbilityRegistry;
import com.wanderersoftherift.wotr.Registries.UpgradeRegistry;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.abilities.AbstractAbility;
import com.wanderersoftherift.wotr.init.ModAttributes;
import com.wanderersoftherift.wotr.networking.ModPayloads;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;


@EventBusSubscriber(modid = WanderersOfTheRift.MODID, bus = EventBusSubscriber.Bus.MOD)
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
        event.register(AbilityRegistry.EFFECT_TARGETING_REGISTRY);
    }

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        WanderersOfTheRift.LOGGER.info("Registering Datapacks");
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
        if(!event.has(EntityType.PLAYER, ModAttributes.MAX_MANA)) {
            event.add(EntityType.PLAYER, ModAttributes.MAX_MANA);
        }

        if(!event.has(EntityType.PLAYER, ModAttributes.HEAL_POWER)) {
            event.add(EntityType.PLAYER, ModAttributes.HEAL_POWER);
        }

        if(!event.has(EntityType.PLAYER, ModAttributes.COOLDOWN)) {
            event.add(EntityType.PLAYER, ModAttributes.COOLDOWN);
        }

        if (!event.has(EntityType.PLAYER, ModAttributes.ABILITY_AOE)) {
            event.add(EntityType.PLAYER, ModAttributes.ABILITY_AOE);
        }

        if (!event.has(EntityType.PLAYER, ModAttributes.PROJECTILE_SPREAD)) {
            event.add(EntityType.PLAYER, ModAttributes.PROJECTILE_SPREAD);
        }

        if (!event.has(EntityType.PLAYER, ModAttributes.PROJECTILE_COUNT)) {
            event.add(EntityType.PLAYER, ModAttributes.PROJECTILE_COUNT);
        }
    }


}
