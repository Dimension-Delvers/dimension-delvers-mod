package com.wanderersoftherift.wotr.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TrialSpawner.class)
public interface TrialSpawnerAccessor {

    @Accessor("normalConfig")
    public void setNormalConfig(Holder<TrialSpawnerConfig> spawnerConfig);

    @Accessor("ominousConfig")
    public void setOminousConfig(Holder<TrialSpawnerConfig> spawnerConfig);
}
