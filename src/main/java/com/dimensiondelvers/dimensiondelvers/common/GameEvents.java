package com.dimensiondelvers.dimensiondelvers.common;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.AbstractAbility;
import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import static com.dimensiondelvers.dimensiondelvers.init.ModAbilities.COOL_DOWN_ATTACHMENTS;

@EventBusSubscriber(modid = DimensionDelvers.MODID, bus = EventBusSubscriber.Bus.GAME)
public class GameEvents {

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Post event)
    {
        Player p = event.getEntity();

        //TODO look into adding a method to the ability to handle reducing its own cool down.
        for(AbstractAbility ability: ModAbilities.COOLDOWN_ABILITIES)
        {
            if(p.hasData(COOL_DOWN_ATTACHMENTS.get(ability.GetName())))
            {
                p.setData(COOL_DOWN_ATTACHMENTS.get(ability.GetName()), Math.max(p.getData(COOL_DOWN_ATTACHMENTS.get(ability.GetName())) - 1, 0)); //Decrease to the lowest value 0;
            }

        }

        //TODO send cooldown packet to player to sync their GUI but gotta find where so I am not just sending a whole packet every tick lmao (maybe send over total cooldown time when the ability is activated???)
    }
}
