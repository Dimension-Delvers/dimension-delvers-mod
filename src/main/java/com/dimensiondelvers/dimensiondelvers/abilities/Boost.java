package com.dimensiondelvers.dimensiondelvers.abilities;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import com.dimensiondelvers.dimensiondelvers.networking.data.CooldownActivated;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;

public class Boost extends AbstractCooldownAbility {
    public Boost(ResourceLocation abilityName) {
        super(abilityName); //TODO look into using attributes for storing this data
        setIcon(ResourceLocation.withDefaultNamespace("textures/mob_effect/wind_charged.png"));
    }

    @Override
    public void OnActivate(Player p) {
        if(this.CanPlayerUse(p) && !this.IsOnCooldown(p)) {
            this.SetCooldown(p, GetCooldownLength()); //TODO maybe make helper to calculate time based on ticks for find a different method (maybe include in the attribute???)

            DimensionDelvers.LOGGER.info("BOOSTING!");

            p.setDeltaMovement(p.getLookAngle().scale((int)p.getAttributeValue(AbilityAttributes.BOOST_STRENGTH)));
            ((ServerPlayer)p).connection.send(new ClientboundSetEntityMotionPacket(((ServerPlayer)p))); //This is the secret sauce to making the movement work

            return;
        }

        //this is an example of handing a case where the player cannot use an ability
        if(!this.CanPlayerUse(p))
        {
            p.sendSystemMessage(Component.literal("You Cannot use Boost!"));
        }
    }

    @Override
    public void onDeactivate(Player p) {

    }

    //TODO look into how to better handle this situation, we shouldn't need to identify each cooldown per ability this way.
    @Override
    public DeferredHolder<Attribute, RangedAttribute> GetCooldownLength() {
        return AbilityAttributes.BOOST_COOLDOWN;
    }


}
