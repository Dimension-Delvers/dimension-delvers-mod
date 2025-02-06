package com.dimensiondelvers.dimensiondelvers.init;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.*;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.HashMap;
import java.util.function.Supplier;

import static com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry.ABILITY_REGISTRY_KEY;


@EventBusSubscriber(modid = DimensionDelvers.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModAbilities {

    //Just turn this into a list of locations for the player data tracking part. Don't need this fancy any more.
    public static HashMap<ResourceLocation, Boolean> COOLDOWN_MAP = new HashMap<>(); //TODO look into a better way of storing and handling cooldowns, since this only works in static reference of the abilities
    public static final AbstractAbility SUMMON_ARROW = new SummonArrow(DimensionDelvers.id("summon_arrow"));
    public static final AbstractAbility HEAL = new Heal(DimensionDelvers.id("heal"));
    public static final AbstractAbility BOOST = new Boost(DimensionDelvers.id("boost"));

    //TODO constants or rarely updated values should be attributes. Such as: Max Mana, CDR, Crit Chance ETC, modifiers can be applied when learning new abilities to scale these factors.
    //TODO maybe move this for order of registering the data to go with them
    @SubscribeEvent
    public static void registerAbilities(RegisterEvent event) {
        DimensionDelvers.LOGGER.info("Registering Abilities!");
        event.register(ABILITY_REGISTRY_KEY, registry -> {
            registry.register(SUMMON_ARROW.GetName(), SUMMON_ARROW);
            registry.register(HEAL.GetName(), HEAL);
            registry.register(BOOST.GetName(), BOOST);
        });

    }

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, DimensionDelvers.MODID);

    //***Using Attributes to manage and scale this***
    //    private static final Supplier<AttachmentType<Integer>> MANA = ATTACHMENT_TYPES.register(
    //            DimensionDelvers.MODID + ".mana", () -> AttachmentType.builder(() -> 100).serialize(Codec.INT).build()
    //    );

    public static final HashMap<ResourceLocation, Supplier<AttachmentType<Integer>>> COOL_DOWN_ATTACHMENTS = new HashMap<>();

    public static void setupCooldowns()
    {
        //TODO This is bad lmao
        for(ResourceLocation loc: ModAbilities.COOLDOWN_MAP.keySet())
        {
            DimensionDelvers.LOGGER.info("ADDING LOC " + loc);
            COOL_DOWN_ATTACHMENTS.put(loc, ATTACHMENT_TYPES.register(
                   GetAbilityCoolDownDataName(loc), () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
            ));
        }

    }


    //You cannot use a : in a name within a registry, so to register other mod abilities we convert it to a weird string - also add -cooldown, so we can debug it
    public static String GetAbilityCoolDownDataName(ResourceLocation location)
    {
        return location.getNamespace() + "-" + location.getPath() + "-cooldown";
    }

}
