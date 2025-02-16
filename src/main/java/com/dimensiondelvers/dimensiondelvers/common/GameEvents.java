package com.dimensiondelvers.dimensiondelvers.common;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry;
import com.dimensiondelvers.dimensiondelvers.abilities.AbstractAbility;
import com.dimensiondelvers.dimensiondelvers.abilities.Serializable.PlayerCooldownData;
import com.dimensiondelvers.dimensiondelvers.abilities.Serializable.PlayerDurationData;
import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Optional;

import static com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry.DATA_PACK_ABILITY_REG_KEY;
import static com.dimensiondelvers.dimensiondelvers.init.ModAbilities.DURATION_ATTACHMENTS;

@EventBusSubscriber(modid = DimensionDelvers.MODID, bus = EventBusSubscriber.Bus.GAME)
public class GameEvents {

    //TODO This is purely placeholder, find a better way to handle this, because currently each player is looking against every ability.
    //TODO find a way to track each individual players ability (saving this into the player shouldn't be too hard)
    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Post event)
    {
        Player p = event.getEntity();

        PlayerCooldownData cooldowns = p.getData(ModAbilities.COOL_DOWNS);
        cooldowns.reduceCooldowns();
        p.setData(ModAbilities.COOL_DOWNS, cooldowns);

        //TODO replace this with similar situation to above
        PlayerDurationData durations = p.getData(ModAbilities.DURATIONS);
        Optional<Registry<AbstractAbility>> abilities = p.level().registryAccess().lookup(DATA_PACK_ABILITY_REG_KEY);
        if(abilities.isPresent())
        {
            for(AbstractAbility onDuration: durations.getRunningDurations(abilities.get()))
            {
                if(durations.get(onDuration.getName()) == 1) { onDuration.onDeactivate(p);}
                if(onDuration.isActive(p)) onDuration.tick(p);
            }
        }
        durations.reduceDurations();

    }

//    @SubscribeEvent
//    public static void levelLoaded(LevelEvent.Load levelEvent)
//    {
//        if(levelEvent.getLevel().isClientSide()) return;
//
//        DimensionDelvers.LOGGER.info("Datapack exists: " + String.valueOf(levelEvent.getLevel().registryAccess().lookup(DATA_PACK_ABILITY_REG_KEY).isPresent()));
//
//    }

    //TODO look into where to better handle this, we want to register unlocks for abilities
    @SubscribeEvent
    public static void serverLoaded(ServerStartingEvent event)
    {
        DimensionDelvers.LOGGER.info("Server loaded pack exists: " + String.valueOf( event.getServer().registryAccess().lookup(DATA_PACK_ABILITY_REG_KEY).isPresent()));
        if(event.getServer().registryAccess().lookup(DATA_PACK_ABILITY_REG_KEY).isPresent())
        {
            for(AbstractAbility ability: event.getServer().registryAccess().lookup(DATA_PACK_ABILITY_REG_KEY).get())
            {
                DimensionDelvers.LOGGER.info(ability.getName().toString());
            }
        }
    }
}
