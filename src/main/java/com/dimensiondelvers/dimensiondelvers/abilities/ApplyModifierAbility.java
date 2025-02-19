package com.dimensiondelvers.dimensiondelvers.abilities;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;

public class ApplyModifierAbility extends AbstractAbility{
    private AttributeModifier modifier;
    private Holder<Attribute> attribute;

    public ApplyModifierAbility(ResourceLocation abilityName, Holder<Attribute> cooldown, Holder<Attribute> duration, AttributeModifier modifier, Holder<Attribute> attribute) {
        super(abilityName, null);
        this.cooldownAttribute = cooldown;
        this.durationAttribute = duration;
        this.modifier = modifier;
        this.attribute = attribute;
    }


    public static final MapCodec<ApplyModifierAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("ability_name").forGetter(ApplyModifierAbility::getName),
                    RangedAttribute.CODEC.fieldOf("cooldown").forGetter(ApplyModifierAbility::getCooldownLength),
                    RangedAttribute.CODEC.fieldOf("duration").forGetter(ApplyModifierAbility::getDurationLength),
                    AttributeModifier.CODEC.fieldOf("modifier").forGetter(ApplyModifierAbility::getModifier),
                    Attribute.CODEC.fieldOf("attribute").forGetter(ApplyModifierAbility::getAttribute)
            ).apply(instance, ApplyModifierAbility::new)
    );

    @Override
    public MapCodec<? extends AbstractAbility> getCodec() {
        return CODEC;
    }
    public Holder<Attribute> getAttribute() {
        return attribute;
    }

    public AttributeModifier getModifier() {
        return modifier;
    }

    @Override
    public void OnActivate(Player p) {
        if(this.CanPlayerUse(p) && !this.IsOnCooldown(p)) {
            this.setCooldown(p, getCooldownLength());
            this.setDuration(p, getDurationLength()); //TODO maybe make helper to calculate time based on ticks for find a different method (maybe include in the attribute???)

            p.getAttribute(attribute).addOrReplacePermanentModifier(modifier);
        }
    }

    @Override
    public void onDeactivate(Player p) {
        p.getAttribute(attribute).removeModifier(modifier);
    }

    @Override
    public void tick(Player p) {

    }
}
