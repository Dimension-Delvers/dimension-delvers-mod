package com.dimensiondelvers.dimensiondelvers.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class JumpAbility extends AbstractAbility{

    private final int cooldown;
    private final int manaCost;
    private final int jumpHeight;

    public JumpAbility(int cooldown, int manaCost, int jumpHeight) {

        this.cooldown = cooldown;
        this.manaCost = manaCost;
        this.jumpHeight = jumpHeight;
    }
    @Override
    public MapCodec<? extends AbstractAbility> getCodec() {
        return JUMP_CODEC;
    }

    public static final MapCodec<JumpAbility> JUMP_CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Codec.INT.fieldOf("cooldown").forGetter(JumpAbility::getCooldown),
                        Codec.INT.fieldOf("manaCost").forGetter(JumpAbility::getManaCost),
                        Codec.INT.fieldOf("height").forGetter(JumpAbility::getJumpHeight)
                 ).apply(instance, JumpAbility::new)
               );
    @Override
    public void activateAbility() {

    }

    public int getCooldown() {
        return cooldown;
    }

    public int getManaCost() {
        return manaCost;
    }

    public int getJumpHeight() {
        return jumpHeight;
    }
}
