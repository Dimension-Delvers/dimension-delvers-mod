package com.wanderersoftherift.wotr.item.riftkey;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class EssencePredicate {

    public static final Codec<EssencePredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("essenceType").forGetter(x -> x.essenceType),
            Codec.INT.optionalFieldOf("min").forGetter(x -> x.min),
            Codec.INT.optionalFieldOf("max").forGetter(x -> x.max),
            Codec.FLOAT.optionalFieldOf("minPercent").forGetter(x -> x.minPercent),
            Codec.FLOAT.optionalFieldOf("maxPercent").forGetter(x-> x.maxPercent)
    ).apply(instance, EssencePredicate::new));

    private final ResourceLocation essenceType;
    private final Optional<Integer> min;
    private final Optional<Integer> max;
    private final Optional<Float> minPercent;
    private final Optional<Float> maxPercent;

    public EssencePredicate(ResourceLocation essenceType, Optional<Integer> min, Optional<Integer> max, Optional<Float> minPercent, Optional<Float> maxPercent) {
        this.essenceType = essenceType;
        this.min = min;
        this.max = max;
        this.minPercent = minPercent;
        this.maxPercent = maxPercent;
    }

    public boolean match(Object2IntMap<ResourceLocation> essenceAmounts) {
        int amount = essenceAmounts.getOrDefault(essenceType, 0);
        if (min.isPresent() && amount < min.get()) {
            return false;
        }
        if (max.isPresent() && amount > max.get()) {
            return false;
        }
        int total = essenceAmounts.values().intStream().sum();
        float percent = 100f * amount / total;
        if (minPercent.isPresent() && percent < minPercent.get()) {
            return false;
        }
        if (maxPercent.isPresent() && percent > maxPercent.get()) {
            return false;
        }
        return true;
    }

    public static class Builder {
        private final ResourceLocation essenceType;
        private Optional<Integer> min = Optional.empty();
        private Optional<Integer> max = Optional.empty();
        private Optional<Float> minPercent = Optional.empty();
        private Optional<Float> maxPercent = Optional.empty();

        public Builder(ResourceLocation essenceType) {
            this.essenceType = essenceType;
        }

        public Builder setMin(int min) {
            this.min = Optional.of(min);
            return this;
        }

        public Builder setMax(int max) {
            this.max = Optional.of(max);
            return this;
        }

        public Builder setMinPercent(float min) {
            this.minPercent = Optional.of(min);
            return this;
        }

        public Builder setMaxPercent(float max) {
            this.maxPercent = Optional.of(max);
            return this;
        }

        public EssencePredicate build() {
            return new EssencePredicate(essenceType, min, max, minPercent, maxPercent);
        }


    }
}
