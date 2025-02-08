package com.dimensiondelvers.dimensiondelvers.abilities;

import com.dimensiondelvers.dimensiondelvers.abilities.types.CooldownAbility;
import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.dimensiondelvers.dimensiondelvers.init.ModAbilities.COOL_DOWN_ATTACHMENTS;

public class AbstractCooldownAbility extends AbstractAbility implements CooldownAbility {

    public AbstractCooldownAbility(ResourceLocation abilityName) {
        super(abilityName);
    }

    @Override
    public void OnActivate(Player p) {

    }

    @Override
    public void onDeactivate(Player p) {

    }

    @Override
    public boolean IsOnCooldown(Player p) {
     //If we registered this ability as one that has a cooldown and the player has a cooldown active for this ability.
        return ModAbilities.COOL_DOWN_ATTACHMENTS.containsKey(this.getName()) && p.getData(ModAbilities.COOL_DOWN_ATTACHMENTS.get(this.getName())) > 0;
    }

    @Override
    public void SetCooldown(Player p, DeferredHolder<Attribute, RangedAttribute> attribute) {
        p.setData(COOL_DOWN_ATTACHMENTS.get(this.getName()), (int)p.getAttributeValue(attribute) * 20); //TODO maybe make helper to calculate time based on ticks for find a different method (maybe include in the attribute???)
    }
}
