package com.dimensiondelvers.dimensiondelvers.abilities;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import static com.dimensiondelvers.dimensiondelvers.init.ModAbilities.COOL_DOWN_ATTACHMENTS;

public class Boost extends AbstractAbility {
    public Boost(ResourceLocation abilityName) {
        super(abilityName, true); //TODO look into using attributes for storing this data
    }

    @Override
    public void OnActivate(Player p) {
        if(COOL_DOWN_ATTACHMENTS.containsKey(this.GetName()))
        {
            int boostCoolDown = p.getData(COOL_DOWN_ATTACHMENTS.get(this.GetName()));
            if(boostCoolDown == 0)
            {
                p.setData(COOL_DOWN_ATTACHMENTS.get(this.GetName()), (int)p.getAttributeValue(AbilityAttributes.BOOST_COOLDOWN) * 20); //TODO maybe make helper to calculate time based on ticks for find a different method (maybe include in the attribute???)
                DimensionDelvers.LOGGER.info("BOOSTING!");
                p.setDeltaMovement(p.getLookAngle().scale((int)p.getAttributeValue(AbilityAttributes.BOOST_STRENGTH)));

                ((ServerPlayer)p).connection.send(new ClientboundSetEntityMotionPacket(((ServerPlayer)p))); //This is the secret sauce to making the movement work
            }
        }


    }

    @Override
    public void OnDeactivate(Player p) {

    }
}
