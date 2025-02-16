package com.dimensiondelvers.dimensiondelvers.init;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.*;
import com.dimensiondelvers.dimensiondelvers.abilities.Serializable.PlayerCooldownData;
import com.dimensiondelvers.dimensiondelvers.abilities.Serializable.PlayerDurationData;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@EventBusSubscriber(modid = DimensionDelvers.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModAbilities {

    //This is a map of location to a specific attachment type for cooldowns
    //TODO look into optimizing this to not need the list or map, might be worth keeping the Location in the list, and then lookup from the Attachments registry.
    public static final HashMap<ResourceLocation, AttachmentType<Integer>> COOL_DOWN_ATTACHMENTS = new HashMap<>();
    public static final HashMap<ResourceLocation, AttachmentType<Boolean>> TOGGLE_ATTACHMENTS = new HashMap<>();
    public static List<AbstractAbility> TOGGLE_ABILITIES = new ArrayList<>();
    public static final HashMap<ResourceLocation, AttachmentType<Integer>> DURATION_ATTACHMENTS = new HashMap<>();


    //TODO constants or rarely updated values should be attributes. Such as: Max Mana, CDR, Crit Chance ETC, modifiers can be applied when learning new abilities to scale these factors.
    @SubscribeEvent
    public static void registerAttachments(RegisterEvent event) {

        event.register(NeoForgeRegistries.ATTACHMENT_TYPES.key(), registry -> {

            DimensionDelvers.LOGGER.info("Registering Ability Stuff");
//            registerCooldowns(registry);
//            registerToggles(registry);
//            registerDurationAbilities(registry);
//            registerAbilityUnlocks(registry);

        });


    }
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, DimensionDelvers.MODID);
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerCooldownData>> COOL_DOWNS = ATTACHMENT_TYPES.register(
            "cooldowns", () -> AttachmentType.serializable(PlayerCooldownData::new).build()
    );

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerDurationData>> DURATIONS = ATTACHMENT_TYPES.register(
            "durations", () -> AttachmentType.serializable(PlayerDurationData::new).build()
    );


    private static void registerToggles(RegisterEvent.RegisterHelper<AttachmentType<?>> registry) {
//        TOGGLE_ABILITIES = ABILITY_REGISTRY_DEF.getRegistry().get().stream().filter(AbstractAbility::IsToggle).collect(Collectors.toList());
        for(AbstractAbility abstractAbility: TOGGLE_ABILITIES)
        {
            DimensionDelvers.LOGGER.info("Adding Toggle for: " + abstractAbility.getName());
            AttachmentType<Boolean> attachmentType = AttachmentType.builder(() -> false).serialize(Codec.BOOL).build();

            ResourceLocation abilityToggleLoc = ResourceLocation.fromNamespaceAndPath(abstractAbility.getName().getNamespace(), "toggles/" + abstractAbility.getName().getPath());
            registry.register(abilityToggleLoc, attachmentType);
            TOGGLE_ATTACHMENTS.put(abstractAbility.getName(), attachmentType);
        }
    }

    private static void registerAbilityUnlocks(RegisterEvent.RegisterHelper<AttachmentType<?>> registry)
    {
//        for(AbstractAbility abstractAbility: ABILITY_REGISTRY.stream().toList())
//        {
//            DimensionDelvers.LOGGER.info("Adding Unlock for: " + abstractAbility.getName());
//            AttachmentType<Boolean> attachmentType = AttachmentType.builder(() -> false).serialize(Codec.BOOL).build();
//
//            ResourceLocation abilityUnlockLoc = ResourceLocation.fromNamespaceAndPath(abstractAbility.getName().getNamespace(), "unlocks/" + abstractAbility.getName().getPath());
//            registry.register(abilityUnlockLoc, attachmentType);
//            ABILITY_UNLOCKED_ATTACHMENTS.put(abstractAbility.getName(), attachmentType);
//        }
    }
}
