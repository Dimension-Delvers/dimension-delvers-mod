package com.dimensiondelvers.dimensiondelvers.abilities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SummonArrow extends AbstractAbility {
    public SummonArrow(ResourceLocation location) {
        super(location);
    }


    @Override
    public void OnActivate(Player p) {

        //TODO implement cooldown
        if(!p.level().isClientSide)
        {
            ServerLevel level = (ServerLevel) p.level();
            Arrow arrow = new Arrow(level, p.position().x, p.position().y + 1.5, p.position().z, new ItemStack(Items.ARROW), null);
            arrow.setOwner(p);
            arrow.shoot( p.getLookAngle().x, p.getLookAngle().y, p.getLookAngle().z, (float) (3 + p.getDeltaMovement().length()), 0);
            level.addFreshEntity(arrow);
        }
    }

    @Override
    public void OnDeactivate(Player p) {

    }
}
