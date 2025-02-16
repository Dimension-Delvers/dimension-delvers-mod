package com.dimensiondelvers.dimensiondelvers.init;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.Registries.UpgradeRegistry;
import com.dimensiondelvers.dimensiondelvers.upgrades.AbstractUpgrade;
import com.dimensiondelvers.dimensiondelvers.upgrades.ReduceBoostCoolDownUpgrade;
import com.dimensiondelvers.dimensiondelvers.upgrades.UnlockAbilityUpgrade;
import com.dimensiondelvers.dimensiondelvers.upgrades.UnlockAllAbilitiesUpgrade;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.HashMap;


@EventBusSubscriber(modid = DimensionDelvers.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModUpgrades {


    public static final HashMap<ResourceLocation, AttachmentType<Boolean>> UPGRADE_UNLOCKED_ATTACHMENTS = new HashMap<>();
//    public static final DeferredHolder<AbstractUpgrade, AbstractUpgrade> UNLOCK_ALL_UPGRADE = UpgradeRegistry.UPGRADE_REGISTRY_DEF.register(
//            "unlock_all",
//            UnlockAllAbilitiesUpgrade::new
//    );

//    public static final DeferredHolder<AbstractUpgrade, AbstractUpgrade> UNLOCK_BOOST = UpgradeRegistry.UPGRADE_REGISTRY_DEF.register(
//            "upgrade/unlock_boost",
//            UnlockAbilityUpgrade.UnlockBoostAbilityUpgrade::new
//    );

//    public static final DeferredHolder<AbstractUpgrade, AbstractUpgrade> UNLOCK_ARROW = UpgradeRegistry.UPGRADE_REGISTRY_DEF.register(
//            "upgrade/unlock_arrow",
//            UnlockAbilityUpgrade.UnlockArrowAbilityUpgrade::new
//    );
//    public static final DeferredHolder<AbstractUpgrade, AbstractUpgrade> UNLOCK_HEAL = UpgradeRegistry.UPGRADE_REGISTRY_DEF.register(
//            "upgrade/unlock_heal",
//            UnlockAbilityUpgrade.UnlockHealAbilityUpgrade::new
//    );
//    public static final DeferredHolder<AbstractUpgrade, AbstractUpgrade> UNLOCK_ARMOR_STAND = UpgradeRegistry.UPGRADE_REGISTRY_DEF.register(
//            "upgrade/unlock_armor_stand",
//            UnlockAbilityUpgrade.UnlockArmorStandAbilityUpgrade::new
//    );
//
//    public static final DeferredHolder<AbstractUpgrade, AbstractUpgrade> UNLOCK_PRETTY = UpgradeRegistry.UPGRADE_REGISTRY_DEF.register(
//            "upgrade/unlock_be_pretty",
//            UnlockAbilityUpgrade.UnlockPrettyAbilityUpgrade::new
//    );

//    public static final DeferredHolder<AbstractUpgrade, AbstractUpgrade> UPGRADE_BOOST = UpgradeRegistry.UPGRADE_REGISTRY_DEF.register(
//            "upgrade/upgrade_boost",
//            ReduceBoostCoolDownUpgrade::new
//    );

//    public static final DeferredHolder<AbstractUpgrade, AbstractUpgrade> UNLOCK_SMOL = UpgradeRegistry.UPGRADE_REGISTRY_DEF.register(
//            "upgrade/unlock_be_smol",
//            UnlockAbilityUpgrade.UnlockSmolAbilityUpgrade::new
//    );




    //TODO constants or rarely updated values should be attributes. Such as: Max Mana, CDR, Crit Chance ETC, modifiers can be applied when learning new abilities to scale these factors.
    @SubscribeEvent
    public static void registerUpgradeAttachments(RegisterEvent event) {

        event.register(NeoForgeRegistries.ATTACHMENT_TYPES.key(), registry -> {

            DimensionDelvers.LOGGER.info("Registering Ability Stuff");
            registerUpgradeUnlocks(registry);

        });


    }



    private static void registerUpgradeUnlocks(RegisterEvent.RegisterHelper<AttachmentType<?>> registry)
    {
        for(AbstractUpgrade upgrade: UpgradeRegistry.UPGRADE_REGISTRY.stream().toList())
        {
            DimensionDelvers.LOGGER.info("Adding Unlock for: " + upgrade.GetName());
            AttachmentType<Boolean> attachmentType = AttachmentType.builder(() -> false).serialize(Codec.BOOL).build();

            ResourceLocation abilityUnlockLoc = ResourceLocation.fromNamespaceAndPath(upgrade.GetName().getNamespace(), "unlocks/" + upgrade.GetName().getPath());
            registry.register(abilityUnlockLoc, attachmentType);
            UPGRADE_UNLOCKED_ATTACHMENTS.put(upgrade.GetName(), attachmentType);
        }
    }
}
