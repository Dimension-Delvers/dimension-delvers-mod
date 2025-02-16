package com.dimensiondelvers.dimensiondelvers.upgrades;

import com.dimensiondelvers.dimensiondelvers.init.ModUpgrades;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUpgrade {
    /*
     * TODO look into making these data-packable as well. Should not be too hard if we rely on attributes. Otherwise, idk maybe Patrigan has insight
     */
    private ResourceLocation upgradeName;
    private List<AbstractUpgrade> requiredUpgrades;
    public AbstractUpgrade(ResourceLocation upgradeName)
    {
        this.upgradeName = upgradeName;
        this.requiredUpgrades = new ArrayList<>();
    }

    public void addRequirement(AbstractUpgrade upgrade)
    {
        requiredUpgrades.add(upgrade);
    }

    public ResourceLocation GetName() {
        return upgradeName;
    }
    public boolean isUnlocked(Player p)
    {
        return ModUpgrades.UPGRADE_UNLOCKED_ATTACHMENTS.containsKey(this.GetName()) && p.getData(ModUpgrades.UPGRADE_UNLOCKED_ATTACHMENTS.get(this.GetName()));
    }

    public boolean canUnlock(Player p)
    {
        return requiredUpgrades.stream().filter(abstractUpgrade -> !abstractUpgrade.isUnlocked(p)).toList().isEmpty();
    }

    public void unlock(Player p) {
        if(!canUnlock(p)) {
            MutableComponent message = Component.translatable("ability.dimensiondelvers.cannot_unlock");
            for(AbstractUpgrade upgrade: requiredUpgrades.stream().filter(abstractUpgrade -> !abstractUpgrade.isUnlocked(p)).toList())
            {
                message.append(Component.translatable(upgrade.getTranslationString()));
                message.append(" ");
            }
            ((ServerPlayer)p).sendSystemMessage(message);

            return;
        }


        p.setData(ModUpgrades.UPGRADE_UNLOCKED_ATTACHMENTS.get(this.GetName()), true);
    }
    public void remove(Player p) {
        p.setData(ModUpgrades.UPGRADE_UNLOCKED_ATTACHMENTS.get(this.GetName()), false);
    }

    public String getTranslationString()
    {
        return "upgrade." + GetName().getNamespace() + "." + GetName().getPath();
    }
}
