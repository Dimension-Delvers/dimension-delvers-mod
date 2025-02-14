package com.dimensiondelvers.dimensiondelvers.abilities;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SummonArrow extends AbstractAbility {
    public SummonArrow(ResourceLocation location) {
        super(location, false, false, false);
    }


    @Override
    public MapCodec<? extends AbstractAbility> getCodec() {
        return null;
    }

    @Override
    public void OnActivate(Player p) {
        if(!this.CanPlayerUse(p))
        {
            ((ServerPlayer)p).sendSystemMessage(Component.literal("Not machine gun for you!"));
            return;
        }

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
    public void onDeactivate(Player p) {

    }

    @Override
    public DeferredHolder<Attribute, RangedAttribute> GetCooldownLength() {
        return null;
    }

    @Override
    public void tick(Player p) {

    }


}
