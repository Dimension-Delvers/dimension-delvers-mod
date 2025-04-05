package com.wanderersoftherift.wotr.abilities.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class StoredEffectContext {
    public static final Codec<StoredEffectContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("caster").forGetter(x -> x.casterId),
            ItemStack.OPTIONAL_CODEC.fieldOf("abilityItem").forGetter(x -> x.abilityItem)

    ).apply(instance, StoredEffectContext::new));

    private final UUID casterId;
    private final ItemStack abilityItem;

    public StoredEffectContext(EffectContext context) {
        this.casterId = context.caster().getUUID();
        this.abilityItem = context.abilityItem();
    }

    public StoredEffectContext(UUID casterId, ItemStack abilityItem) {
        this.casterId = casterId;
        this.abilityItem = abilityItem;
    }

    public EffectContext toContext(LivingEntity caster) {
        return new EffectContext(caster, abilityItem);
    }

    public UUID getCasterId() {
        return casterId;
    }
}
