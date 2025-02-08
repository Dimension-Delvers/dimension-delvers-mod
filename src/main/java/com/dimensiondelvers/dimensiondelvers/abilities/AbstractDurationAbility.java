package com.dimensiondelvers.dimensiondelvers.abilities;

import com.dimensiondelvers.dimensiondelvers.abilities.types.DurationAbility;
import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.registries.DeferredHolder;

public class AbstractDurationAbility extends AbstractAbility implements DurationAbility {
    public AbstractDurationAbility(ResourceLocation abilityName) {
        super(abilityName);
    }

    @Override
    public void OnActivate(Player p) {

    }

    @Override
    public void onDeactivate(Player p) {

    }

    @Override
    public boolean isActive(Player p) {
        return ModAbilities.DURATION_ATTACHMENTS.containsKey(this.getName()) && p.getData(ModAbilities.DURATION_ATTACHMENTS.get(this.getName())) > 0;
    }

    @Override
    public void setDuration(Player p, DeferredHolder<Attribute, RangedAttribute> attribute) {
        p.setData(ModAbilities.DURATION_ATTACHMENTS.get(this.getName()), (int)p.getAttributeValue(attribute) * 20); //TODO maybe make helper to calculate time based on ticks for find a different method (maybe include in the attribute???)
    }

    @Override
    public void tick(Player p) {

    }
}
