package com.dimensiondelvers.dimensiondelvers.init;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.Serializable.PlayerCooldownData;
import com.dimensiondelvers.dimensiondelvers.abilities.Serializable.PlayerDurationData;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.*;

//@EventBusSubscriber(modid = DimensionDelvers.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModAbilities {

    //TODO constants or rarely updated values should be attributes. Such as: Max Mana, CDR, Crit Chance ETC, modifiers can be applied when learning new abilities to scale these factors.
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, DimensionDelvers.MODID);
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerCooldownData>> COOL_DOWNS = ATTACHMENT_TYPES.register(
            "cooldowns", () -> AttachmentType.serializable(PlayerCooldownData::new).build()
    );

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerDurationData>> DURATIONS = ATTACHMENT_TYPES.register(
            "durations", () -> AttachmentType.serializable(PlayerDurationData::new).build()
    );


    /*
     * Look into registering which abilities have been toggled per player, and
     * whether they are currently unlocked.
     */
    private static void registerToggles(RegisterEvent.RegisterHelper<AttachmentType<?>> registry) {
//        TOGGLE_ABILITIES = ABILITY_REGISTRY_DEF.getRegistry().get().stream().filter(AbstractAbility::IsToggle).collect(Collectors.toList());
//        for(AbstractAbility abstractAbility: TOGGLE_ABILITIES)
//        {
//            DimensionDelvers.LOGGER.info("Adding Toggle for: " + abstractAbility.getName());
//            AttachmentType<Boolean> attachmentType = AttachmentType.builder(() -> false).serialize(Codec.BOOL).build();
//
//            ResourceLocation abilityToggleLoc = ResourceLocation.fromNamespaceAndPath(abstractAbility.getName().getNamespace(), "toggles/" + abstractAbility.getName().getPath());
//            registry.register(abilityToggleLoc, attachmentType);
//            TOGGLE_ATTACHMENTS.put(abstractAbility.getName(), attachmentType);
//        }
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
