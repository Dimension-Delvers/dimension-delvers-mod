package com.dimensiondelvers.dimensiondelvers.init;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.*;
import com.dimensiondelvers.dimensiondelvers.abilities.types.CooldownAbility;
import com.dimensiondelvers.dimensiondelvers.abilities.types.DurationAbility;
import com.dimensiondelvers.dimensiondelvers.abilities.types.ToggleAbility;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry.*;


@EventBusSubscriber(modid = DimensionDelvers.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModAbilities {

    //This is a map of location to a specific attachment type for cooldowns
    //TODO look into optimizing this to not need the list or map, might be worth keeping the Location in the list, and then lookup from the Attachments registry.
    public static final HashMap<ResourceLocation, AttachmentType<Integer>> COOL_DOWN_ATTACHMENTS = new HashMap<>();
    public static List<AbstractAbility> COOLDOWN_ABILITIES = new ArrayList<>();
    public static final HashMap<ResourceLocation, AttachmentType<Boolean>> TOGGLE_ATTACHMENTS = new HashMap<>();
    public static List<AbstractAbility> TOGGLE_ABILITIES = new ArrayList<>();
    public static final HashMap<ResourceLocation, AttachmentType<Integer>> DURATION_ATTACHMENTS = new HashMap<>();
    public static List<AbstractAbility> DURATION_ABILITIES = new ArrayList<>();
    public static final HashMap<ResourceLocation, AttachmentType<Boolean>> ABILITY_UNLOCKED_ATTACHMENTS = new HashMap<>();
    public static final DeferredHolder<AbstractAbility, AbstractAbility> SUMMON_ARROW_ABILITY = ABILITY_REGISTRY_DEF.register(
            "ability/summon_arrow",
            SummonArrow::new
    );
    public static final DeferredHolder<AbstractAbility, AbstractAbility> HEAL_ABILITY = ABILITY_REGISTRY_DEF.register(
            "ability/heal",
            Heal::new
    );
    public static final DeferredHolder<AbstractAbility, AbstractAbility> BOOST_ABILITY = ABILITY_REGISTRY_DEF.register(
            "ability/boost",
            Boost::new
    );

    public static final DeferredHolder<AbstractAbility, AbstractAbility> ARMOR_STAND_ABILITY = ABILITY_REGISTRY_DEF.register(
            "ability/summon_armor_stand",
            SummonArmorStand::new
    );

    public static final DeferredHolder<AbstractAbility, AbstractAbility> BE_PRETTY = ABILITY_REGISTRY_DEF.register(
            "ability/be_pretty",
            Particles::new
    );

    public static final DeferredHolder<AbstractAbility, AbstractAbility> BE_SMOL = ABILITY_REGISTRY_DEF.register(
            "ability/be_smol",
            Smol::new
    );

    //TODO constants or rarely updated values should be attributes. Such as: Max Mana, CDR, Crit Chance ETC, modifiers can be applied when learning new abilities to scale these factors.
    @SubscribeEvent
    public static void registerAttachments(RegisterEvent event) {

        event.register(NeoForgeRegistries.ATTACHMENT_TYPES.key(), registry -> {

            DimensionDelvers.LOGGER.info("Registering Ability Stuff");
            registerCooldowns(registry);
            registerToggles(registry);
            registerDurationAbilities(registry);
            registerAbilityUnlocks(registry);

        });


    }

    private static void registerCooldowns(RegisterEvent.RegisterHelper<AttachmentType<?>> registry) {
        COOLDOWN_ABILITIES = ABILITY_REGISTRY_DEF.getRegistry().get().stream().filter(AbstractAbility::HasCooldown).collect(Collectors.toList());
        for(AbstractAbility abstractAbility: COOLDOWN_ABILITIES)
        {
            DimensionDelvers.LOGGER.info("Adding Cool down for: " + abstractAbility.getName());
            AttachmentType<Integer> attachmentType = AttachmentType.builder(() -> 0).serialize(Codec.INT).build();

            //This is done in case we have multiple things we need to track on the player per ability, such as "if unlocked, is active, cooldown, etc."
            ResourceLocation abilityCoolDownLoc = ResourceLocation.fromNamespaceAndPath(abstractAbility.getName().getNamespace(), "cooldowns/" + abstractAbility.getName().getPath());
            registry.register(abilityCoolDownLoc, attachmentType);
            COOL_DOWN_ATTACHMENTS.put(abstractAbility.getName(), attachmentType);
        }
    }

    private static void registerToggles(RegisterEvent.RegisterHelper<AttachmentType<?>> registry) {
        TOGGLE_ABILITIES = ABILITY_REGISTRY_DEF.getRegistry().get().stream().filter(AbstractAbility::IsToggle).collect(Collectors.toList());
        for(AbstractAbility abstractAbility: TOGGLE_ABILITIES)
        {
            DimensionDelvers.LOGGER.info("Adding Toggle for: " + abstractAbility.getName());
            AttachmentType<Boolean> attachmentType = AttachmentType.builder(() -> false).serialize(Codec.BOOL).build();

            ResourceLocation abilityToggleLoc = ResourceLocation.fromNamespaceAndPath(abstractAbility.getName().getNamespace(), "toggles/" + abstractAbility.getName().getPath());
            registry.register(abilityToggleLoc, attachmentType);
            TOGGLE_ATTACHMENTS.put(abstractAbility.getName(), attachmentType);
        }
    }

    private static void registerDurationAbilities(RegisterEvent.RegisterHelper<AttachmentType<?>> registry) {
        DURATION_ABILITIES = ABILITY_REGISTRY_DEF.getRegistry().get().stream().filter(AbstractAbility::HasDuration).collect(Collectors.toList());
        for(AbstractAbility abstractAbility: DURATION_ABILITIES)
        {
            DimensionDelvers.LOGGER.info("Adding Duration for: " + abstractAbility.getName());
            AttachmentType<Integer> attachmentType = AttachmentType.builder(() -> 0).serialize(Codec.INT).build();

            ResourceLocation abilityToggleLoc = ResourceLocation.fromNamespaceAndPath(abstractAbility.getName().getNamespace(), "durations/" + abstractAbility.getName().getPath());
            registry.register(abilityToggleLoc, attachmentType);
            DURATION_ATTACHMENTS.put(abstractAbility.getName(), attachmentType);
        }
    }

    private static void registerAbilityUnlocks(RegisterEvent.RegisterHelper<AttachmentType<?>> registry)
    {
        for(AbstractAbility abstractAbility: ABILITY_REGISTRY.stream().toList())
        {
            DimensionDelvers.LOGGER.info("Adding Unlock for: " + abstractAbility.getName());
            AttachmentType<Boolean> attachmentType = AttachmentType.builder(() -> false).serialize(Codec.BOOL).build();

            ResourceLocation abilityUnlockLoc = ResourceLocation.fromNamespaceAndPath(abstractAbility.getName().getNamespace(), "unlocks/" + abstractAbility.getName().getPath());
            registry.register(abilityUnlockLoc, attachmentType);
            ABILITY_UNLOCKED_ATTACHMENTS.put(abstractAbility.getName(), attachmentType);
        }
    }
}
