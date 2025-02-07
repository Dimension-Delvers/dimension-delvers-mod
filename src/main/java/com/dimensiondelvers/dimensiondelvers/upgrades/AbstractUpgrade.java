package com.dimensiondelvers.dimensiondelvers.upgrades;

import com.dimensiondelvers.dimensiondelvers.init.ModUpgrades;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import static com.dimensiondelvers.dimensiondelvers.init.ModAbilities.ABILITY_UNLOCKED_ATTACHMENTS;
import static com.dimensiondelvers.dimensiondelvers.init.ModAbilities.TOGGLE_ATTACHMENTS;

public abstract class AbstractUpgrade {
    private ResourceLocation upgradeName;
    public AbstractUpgrade(ResourceLocation upgradeName)
    {
        this.upgradeName = upgradeName;
    }

    public ResourceLocation GetName() {
        return upgradeName;
    }
    public boolean IsUnlocked(Player p)
    {
        return ModUpgrades.UPGRADE_UNLOCKED_ATTACHMENTS.containsKey(this.GetName()) && p.getData(ModUpgrades.UPGRADE_UNLOCKED_ATTACHMENTS.get(this.GetName()));
    }

    public void Unlock(Player p) {
        p.setData(ModUpgrades.UPGRADE_UNLOCKED_ATTACHMENTS.get(this.GetName()), true);
    }
    public void Remove(Player p) {
        p.setData(ModUpgrades.UPGRADE_UNLOCKED_ATTACHMENTS.get(this.GetName()), false);
    }

    public String GetTranslationString()
    {
        return "upgrade." + GetName().getNamespace() + "." + GetName().getPath();
    }
}
