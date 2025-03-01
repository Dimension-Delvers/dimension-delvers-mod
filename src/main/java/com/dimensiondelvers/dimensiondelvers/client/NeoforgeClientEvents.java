package com.dimensiondelvers.dimensiondelvers.client;


import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry;
import com.dimensiondelvers.dimensiondelvers.abilities.AbstractAbility;
import com.dimensiondelvers.dimensiondelvers.abilities.Serializable.PlayerCooldownData;
import com.dimensiondelvers.dimensiondelvers.networking.data.UseAbility;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;

import static com.dimensiondelvers.dimensiondelvers.client.ModClientEvents.*;
import static com.dimensiondelvers.dimensiondelvers.init.ModAbilities.*;
import static net.minecraft.client.renderer.RenderType.GUI_TEXTURED_OVERLAY;

@EventBusSubscriber(modid = DimensionDelvers.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class NeoforgeClientEvents {

    //NOTE: Placeholder to activating abilities, we would want a better way handling the control scheme in the future
    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event)
    {
        //TODO Better handling of this for better control scheme based on weapons etc.
        //Also look into not allowing hold down in the future
        while (ABILITY_1_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new UseAbility(DimensionDelvers.id("mega_boost").toString()));
        }

        while (ABILITY_2_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new UseAbility(DimensionDelvers.id("test_ability").toString()));
        }

        while (ABILITY_3_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new UseAbility(DimensionDelvers.id("summon_skeletons").toString()));
        }

//        while (ARMOR_STAND_KEY.consumeClick()) {
//            PacketDistributor.sendToServer(new UseAbility(ARMOR_STAND_ABILITY.get().getName().toString()));
//        }

//        while (OPEN_UPGRADE_MENU_KEY.consumeClick()) {
//            PacketDistributor.sendToServer(new OpenUpgradeMenu(""));
//        }

//        while (PRETTY_KEY.consumeClick()) {
//            PacketDistributor.sendToServer(new UseAbility(BE_PRETTY.get().getName().toString()));
//        }

//        while (SMOL_KEY.consumeClick()) {
//            PacketDistributor.sendToServer(new UseAbility(DimensionDelvers.id("be_smol").toString()));
//        }
    }

    //TODO remove this and do a better render stuff later
    @SubscribeEvent
    public static void hudRender(RenderGuiEvent.Post event)
    {
//        event.getGuiGraphics().drawString(Minecraft.getInstance().font, "TEST", 0, 0, 10);

        int x = 0;
        int size = 18;
        Optional<Registry<AbstractAbility>> abilityReg = Minecraft.getInstance().level.registryAccess().lookup(AbilityRegistry.DATA_PACK_ABILITY_REG_KEY);
        if(abilityReg.isPresent()) {
            PlayerCooldownData cooldowns = Minecraft.getInstance().player.getData(COOL_DOWNS);
            for(AbstractAbility ability: cooldowns.getActiveCooldowns(abilityReg.get()))
            {
                Holder<Attribute> cooldownAttribute = ability.getCooldownLength();
                if(cooldownAttribute != null)
                {
                    double totalCD = Minecraft.getInstance().player.getAttributeValue(cooldownAttribute);
                    double percent = cooldowns.getCooldown(ability.getName()) / (totalCD * 20); //TODO look into syncing attributes with modifier to client
                    event.getGuiGraphics().blit(GUI_TEXTURED_OVERLAY, ability.getIcon(), x, 0, 0.0f, 0.0f, size, size - (int)Math.floor(size * percent), size, size);
                }
                else
                {
                    event.getGuiGraphics().blit(GUI_TEXTURED_OVERLAY, ability.getIcon(), x, 0, 0, 0, size, size, size, size);
                }
                x += size + 2;
            }
        }


//        boolean isToggled = Minecraft.getInstance().player.getData(TOGGLE_ATTACHMENTS.get(ARMOR_STAND_ABILITY.get().getName()));
//        if(isToggled) event.getGuiGraphics().blit(GUI_TEXTURED_OVERLAY, ResourceLocation.withDefaultNamespace("textures/item/armor_stand.png"), 0,size,0,0,16, 16, 16, 16);
    }
}
