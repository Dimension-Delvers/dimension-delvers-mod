package com.dimensiondelvers.dimensiondelvers.abilities;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
            ((ServerPlayer)p).sendSystemMessage(Component.literal("You cannot use summon!"));
            return;
        }

        ArmorStand stand = new ArmorStand(p.level(), p.position().x, p.position().y, p.position().z);
        stand.setCustomName(Component.literal("TEST-" + p.getName().getString()));
        p.level().addFreshEntity(stand);

    }

    @Override
    public void onDeactivate(Player p) {
        if(!this.CanPlayerUse(p))
        {
            ((ServerPlayer)p).sendSystemMessage(Component.literal("You cannot use summon!"));
            return;
        }

        //This is just an example, would be much better to use something like owner and kill based on that or similar, also the range here is limited etc.
        ArmorStand stand = p.level().getEntitiesOfClass(ArmorStand.class, AABB.ofSize(p.position(), 100,100,100)).stream().filter(ent -> ent.getName().getString().equalsIgnoreCase("TEST-"+p.getName().getString())).toList().getFirst();

        if(stand != null) stand.kill((ServerLevel) stand.level());

    }

}
