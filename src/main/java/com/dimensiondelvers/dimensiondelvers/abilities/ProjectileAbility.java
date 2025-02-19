package com.dimensiondelvers.dimensiondelvers.abilities;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;


public class ProjectileAbility extends AbstractAbility{
    ResourceLocation projectile;
    public static final MapCodec<ProjectileAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("ability_name").forGetter(ProjectileAbility::getName),
                    RangedAttribute.CODEC.fieldOf("cooldown").forGetter(ProjectileAbility::getCooldownLength),
                    ResourceLocation.CODEC.fieldOf("projectile").forGetter(ProjectileAbility::getProjectile)
            ).apply(instance, ProjectileAbility::new)
    );

    public ProjectileAbility(ResourceLocation abilityName, Holder<Attribute> cooldown, ResourceLocation projectile) {
        super(abilityName, null);
        this.cooldownAttribute = cooldown;
        this.projectile = projectile;
    }

    @Override
    public MapCodec<? extends AbstractAbility> getCodec() {
        return CODEC;
    }

    public ResourceLocation getProjectile() {
        return this.projectile;
    }

    @Override
    public void OnActivate(Player p) {
        if(!this.CanPlayerUse(p))
        {
            ((ServerPlayer)p).sendSystemMessage(Component.literal("No machine gun for you!"));
            return;
        }

        if(!p.level().isClientSide)
        {
            ServerLevel level = (ServerLevel) p.level();
            if(BuiltInRegistries.ENTITY_TYPE.get(this.projectile).isPresent())
            {
                EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(this.projectile).get().value();
                Entity entity = type.create(level, null, p.getOnPos(), EntitySpawnReason.MOB_SUMMONED, false, false);
                if(entity != null)
                {
                    //This is to shift it to the actual player position since for some reason the spawn method only takes block pos which are int bound
                    entity.setPos((int) p.position().x, (int) (p.position().y + 1.5), (int) p.position().z);
                    if(entity instanceof Projectile projectileEntity)
                    {
                        projectileEntity.setOwner(p);
                        projectileEntity.shoot( p.getLookAngle().x, p.getLookAngle().y, p.getLookAngle().z, (float) (3 + p.getDeltaMovement().length()), 0);
                    }
                    level.addFreshEntity(entity);
                }
            }
        }
    }

    @Override
    public void onDeactivate(Player p) {

    }

    @Override
    public void tick(Player p) {

    }
}
