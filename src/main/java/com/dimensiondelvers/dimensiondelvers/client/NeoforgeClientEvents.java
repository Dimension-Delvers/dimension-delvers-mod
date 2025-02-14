package com.dimensiondelvers.dimensiondelvers.client;


import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.AbstractAbility;
import com.dimensiondelvers.dimensiondelvers.abilities.types.CooldownAbility;
import com.dimensiondelvers.dimensiondelvers.networking.data.OpenUpgradeMenu;
import com.dimensiondelvers.dimensiondelvers.networking.data.UseAbility;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.dimensiondelvers.dimensiondelvers.client.ModClientEvents.*;
import static com.dimensiondelvers.dimensiondelvers.init.ModAbilities.*;
import static net.minecraft.client.renderer.RenderType.GUI_TEXTURED_OVERLAY;

@EventBusSubscriber(modid = DimensionDelvers.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class NeoforgeClientEvents {

    //NOTE: Placeholder to activating abilities, we would want a better way handling the control scheme in the future
    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event)
    {
        //TODO static register abilities and use their names here
        //TODO also better handling of this for better control scheme based on weapons etc.
        //Also look into not allowing hold down in the future
        while (ARROW_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new UseAbility(SUMMON_ARROW_ABILITY.get().getName().toString()));
        }

        while (HEAL_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new UseAbility(HEAL_ABILITY.get().getName().toString()));
        }

        while (BOOST_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new UseAbility(BOOST_ABILITY.get().getName().toString()));
        }

        while (ARMOR_STAND_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new UseAbility(ARMOR_STAND_ABILITY.get().getName().toString()));
        }

        while (OPEN_UPGRADE_MENU_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new OpenUpgradeMenu("")); //TODO maybe make this open any type of menu??? idk yet
        }

        while (PRETTY_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new UseAbility(BE_PRETTY.get().getName().toString()));
        }

        while (SMOL_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new UseAbility(BE_SMOL.get().getName().toString()));
        }
    }

    //TODO remove this and do a better render stuff later
    @SubscribeEvent
    public static void hudRender(RenderGuiEvent.Post event)
    {
//        event.getGuiGraphics().drawString(Minecraft.getInstance().font, "TEST", 0, 0, 10);

        int x = 0;
        int size = 18;
        for(AbstractAbility ability: COOLDOWN_ABILITIES)
        {
            DeferredHolder<Attribute, RangedAttribute> cd = ((CooldownAbility)ability).GetCooldownLength();
            if(cd != null)
            {
                double totalCD = Minecraft.getInstance().player.getAttributeValue(cd);
                double percent = Minecraft.getInstance().player.getData(COOL_DOWN_ATTACHMENTS.get(ability.getName())) / (totalCD * 20); //TODO look into syncing attributes with modifier to client
                event.getGuiGraphics().blit(GUI_TEXTURED_OVERLAY, ability.getIcon(), x, 0, 0.0f, 0.0f, size, size - (int)Math.floor(size * percent), size, size);
            }
            else
            {
                event.getGuiGraphics().blit(GUI_TEXTURED_OVERLAY, ability.getIcon(), x, 0, 0, 0, size, size, size, size);
            }
            x += size + 2;
        }

        boolean isToggled = Minecraft.getInstance().player.getData(TOGGLE_ATTACHMENTS.get(ARMOR_STAND_ABILITY.get().getName()));
        if(isToggled) event.getGuiGraphics().blit(GUI_TEXTURED_OVERLAY, ResourceLocation.withDefaultNamespace("textures/item/armor_stand.png"), 0,size,0,0,16, 16, 16, 16);
    }
}
