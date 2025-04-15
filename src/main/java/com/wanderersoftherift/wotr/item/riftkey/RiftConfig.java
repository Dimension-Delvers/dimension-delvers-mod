package com.wanderersoftherift.wotr.item.riftkey;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.init.ModRiftThemes;
import com.wanderersoftherift.wotr.world.level.levelgen.theme.RiftTheme;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record RiftConfig(int tier, Holder<RiftTheme> theme, Optional<Integer> seed) {

    public static final Codec<RiftConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("tier").forGetter(RiftConfig::tier),
            RiftTheme.CODEC.fieldOf("theme").forGetter(RiftConfig::theme),
            Codec.INT.optionalFieldOf("seed").forGetter(RiftConfig::seed)
    ).apply(instance, RiftConfig::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RiftConfig> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, RiftConfig::tier,
            ByteBufCodecs.holderRegistry(ModRiftThemes.RIFT_THEME_KEY), RiftConfig::theme,
            ByteBufCodecs.INT.apply(ByteBufCodecs::optional), RiftConfig::seed,
            RiftConfig::new
    );

    public RiftConfig(int tier, Holder<RiftTheme> theme) {
        this(tier, theme, Optional.empty());
    }

    public List<Component> getTooltips() {
        ResourceLocation themeId = ResourceLocation.parse(theme.getRegisteredName());
        Component themeName = Component.translatable("rift_theme." + themeId.getNamespace() + "." + themeId.getPath());
        return List.of(Component.translatable("tooltip." + WanderersOfTheRift.MODID + ".rift_key_tier", tier).withColor(ChatFormatting.GRAY.getColor()),
                Component.translatable("tooltip." + WanderersOfTheRift.MODID + ".rift_key_theme", themeName).withColor(ChatFormatting.GRAY.getColor()));
    }

    public static class Builder {
        private int tier = 1;
        private Holder<RiftTheme> theme;
        private Optional<Integer> seed = Optional.empty();

        public Builder(@Nullable RiftConfig existing) {
            this.tier = existing.tier();
            this.theme = existing.theme();
            this.seed = existing.seed();
        }

        public Builder(Holder<RiftTheme> theme) {
            this.theme = theme;
        }

        public Builder setTier(int tier) {
            this.tier = tier;
            return this;
        }

        public Builder setTheme(Holder<RiftTheme> theme) {
            this.theme = theme;
            return this;
        }

        public Builder setSeed(int seed) {
            this.seed = Optional.of(seed);
            return this;
        }

        public RiftConfig build() {
            return new RiftConfig(tier, theme, seed);
        }

    }
}
