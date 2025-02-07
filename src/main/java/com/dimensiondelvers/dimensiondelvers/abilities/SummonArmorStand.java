package com.dimensiondelvers.dimensiondelvers.abilities;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class SummonArmorStand extends AbstractToggleAbility {
    public SummonArmorStand(ResourceLocation abilityName) {
        super(abilityName);
    }

    @Override
    public void OnActivate(Player p) {
        if(!this.CanPlayerUse(p))
        {
            p.sendSystemMessage(Component.literal("You cannot use summon!"));
            return;
        }

        ArmorStand stand = new ArmorStand(p.level(), p.position().x, p.position().y, p.position().z);
        stand.setCustomName(Component.literal("TEST-" + p.getName().getString()));
        p.level().addFreshEntity(stand);

    }

    @Override
    public void OnDeactivate(Player p) {
        if(!this.CanPlayerUse(p))
        {
            p.sendSystemMessage(Component.literal("You cannot use summon!"));
            return;
        }

        //This is just an example, would be much better to use something like owner and kill based on that or similar, also the range here is limited etc.
        ArmorStand stand = p.level().getNearestEntity(ArmorStand.class, TargetingConditions.forNonCombat().selector(ent -> {
            DimensionDelvers.LOGGER.info(ent.getName().getString());
            return ent.getName().getString().equalsIgnoreCase("TEST-"+p.getName().getString());
        }), null, p.position().x, p.position().y, p.position().z, AABB.ofSize(p.position(), 100,100,100));
        if(stand != null) stand.kill();

    }

}
