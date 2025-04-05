package com.wanderersoftherift.wotr.events;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.init.ModDamageTypes;
import com.wanderersoftherift.wotr.init.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;


@EventBusSubscriber(modid = WanderersOfTheRift.MODID, bus = EventBusSubscriber.Bus.GAME)
public class LivingEntityEvents {
    @SubscribeEvent
    public static void onLivingDamageEventPost(LivingDamageEvent.Post event) {
        if (event.getSource().typeHolder().getKey().equals(ModDamageTypes.FIRE_DAMAGE)) {
            event.getEntity().addEffect(new MobEffectInstance(ModEffects.FIRE_BURN_EFFECT, 20 * 30));
        }
    }
}
