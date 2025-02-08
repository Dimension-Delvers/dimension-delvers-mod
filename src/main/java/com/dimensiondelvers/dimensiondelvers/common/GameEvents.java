package com.dimensiondelvers.dimensiondelvers.common;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.AbstractAbility;
import com.dimensiondelvers.dimensiondelvers.abilities.types.DurationAbility;
import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import static com.dimensiondelvers.dimensiondelvers.init.ModAbilities.COOL_DOWN_ATTACHMENTS;
import static com.dimensiondelvers.dimensiondelvers.init.ModAbilities.DURATION_ATTACHMENTS;

@EventBusSubscriber(modid = DimensionDelvers.MODID, bus = EventBusSubscriber.Bus.GAME)
public class GameEvents {

    //TODO This is purely placeholder, find a better way to handle this, because currently each player is looking against every ability.
    //TODO find a way to track each individual players ability (saving this into the player shouldn't be too hard)
    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Post event)
    {
        Player p = event.getEntity();

        //TODO look into adding a method to the ability to handle reducing its own cool down.
        for(AbstractAbility ability: ModAbilities.COOLDOWN_ABILITIES)
        {
            if(p.hasData(COOL_DOWN_ATTACHMENTS.get(ability.getName())))
            {
                p.setData(COOL_DOWN_ATTACHMENTS.get(ability.getName()), Math.max(p.getData(COOL_DOWN_ATTACHMENTS.get(ability.getName())) - 1, 0)); //Decrease to the lowest value 0;
            }

        }

        for(AbstractAbility ability: ModAbilities.DURATION_ABILITIES)
        {
            if(p.hasData(DURATION_ATTACHMENTS.get(ability.getName())))
            {
                if(p.getData(DURATION_ATTACHMENTS.get(ability.getName())) == 1)
                {
                    ability.onDeactivate(p);
                }
                p.setData(DURATION_ATTACHMENTS.get(ability.getName()), Math.max(p.getData(DURATION_ATTACHMENTS.get(ability.getName())) - 1, 0)); //Decrease to the lowest value 0;
                if(((DurationAbility)ability).isActive(p)) ((DurationAbility)ability).tick(p);
            }

        }

        //TODO send cooldown packet to player to sync their GUI but gotta find where so I am not just sending a whole packet every tick lmao (maybe send over total cooldown time when the ability is activated???)
    }
}
