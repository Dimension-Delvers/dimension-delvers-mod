package com.wanderersoftherift.wotr.abilities;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.abilities.effects.AttachEffect;
import com.wanderersoftherift.wotr.abilities.effects.EffectContext;
import com.wanderersoftherift.wotr.abilities.effects.StoredEffectContext;
import com.wanderersoftherift.wotr.abilities.effects.marker.EffectMarker;
import com.wanderersoftherift.wotr.init.ModAttachments;
import com.wanderersoftherift.wotr.network.SetEffectMarkerPayload;
import com.wanderersoftherift.wotr.network.UpdateEffectMarkersPayload;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * AttachedEffectData allows effects to be attached to an entity for a durationTicks - after which they are triggered every tick until they expire.
 */
public class AttachedEffectData {
    public static final Codec<AttachedEffectData> CODEC = AttachedEffect.CODEC.listOf().xmap(AttachedEffectData::new, x -> x.effects);

    private final List<AttachedEffect> effects;

    public AttachedEffectData() {
        this(new ArrayList<>());
    }

    private AttachedEffectData(List<AttachedEffect> effects) {
        this.effects = new ArrayList<>(effects);
    }

    /**
     * Ticks all attached effects within a level
     *
     * @param level
     */
    public static void tick(ServerLevel level) {
        level.getEntities(EntityTypeTest.forClass(LivingEntity.class), entity -> entity.hasData(ModAttachments.ATTACHED_EFFECTS)).forEach(entity -> {
            AttachedEffectData data = entity.getData(ModAttachments.ATTACHED_EFFECTS);
            data.tick(entity, level);
            if (data.effects.isEmpty()) {
                entity.removeData(ModAttachments.ATTACHED_EFFECTS);
            }
        });
    }

    public void tick(LivingEntity attachedTo, ServerLevel level) {
        List<AttachedEffect> removedEffects = tickEffects(attachedTo, level);

        if (attachedTo instanceof ServerPlayer player) {
            Map<Holder<EffectMarker>, Integer> updates = new LinkedHashMap<>();
            List<Holder<EffectMarker>> remove = new ArrayList<>();
            for (AttachedEffect effect : removedEffects) {
                effect.attachEffect.getDisplay().ifPresent(display -> {
                    if (!updates.containsKey(display) && !remove.contains(display)) {
                        int remainingDuration = getRemainingDuration(display);
                        if (remainingDuration > 0) {
                            updates.put(display, remainingDuration);
                        } else {
                            remove.add(display);
                        }
                    }
                });
            }
            PacketDistributor.sendToPlayer(player, new UpdateEffectMarkersPayload(updates, remove));
        }
    }

    private int getRemainingDuration(Holder<EffectMarker> display) {
        return effects.stream().filter(x -> display.equals(x.attachEffect.getDisplay().orElse(null))).mapToInt(AttachedEffect::getRemainingDuration).max().orElse(0);
    }

    private @NotNull List<AttachedEffect> tickEffects(LivingEntity attachedTo, ServerLevel level) {
        List<AttachedEffect> removedEffects = new ArrayList<>();
        for (AttachedEffect effect : ImmutableList.copyOf(effects)) {
            if (effect.tick(attachedTo, level)) {
                removedEffects.add(effect);
            }
        }
        effects.removeAll(removedEffects);
        return removedEffects;
    }

    public void attach(LivingEntity attachedTo, EffectContext context, AttachEffect attachEffect) {
        AttachedEffect newEffect = new AttachedEffect(attachEffect, context);
        if (attachedTo instanceof ServerPlayer player) {
            attachEffect.getDisplay().ifPresent(display -> {
                int existingDuration = getRemainingDuration(display);
                int newDuration = newEffect.getRemainingDuration();
                if (existingDuration < newDuration) {
                    PacketDistributor.sendToPlayer(player, new SetEffectMarkerPayload(display, newDuration));
                }
            });
        }
        effects.add(newEffect);
    }

    public Map<Holder<EffectMarker>, Integer> getDisplayData() {
        Map<Holder<EffectMarker>, Integer> result = new LinkedHashMap<>();
        for (AttachedEffect effect : effects) {
            effect.attachEffect.getDisplay().ifPresent(display -> {
                if (!result.containsKey(display) || result.get(display) < effect.getRemainingDuration()) {
                    result.put(display, effect.getRemainingDuration());
                }
            });
        }
        return result;
    }

    private static class AttachedEffect {
        private static final Codec<AttachedEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        AttachEffect.CODEC.fieldOf("attachEffect").forGetter(x -> x.attachEffect),
                        StoredEffectContext.CODEC.fieldOf("context").forGetter(x -> x.context),
                        Codec.INT.fieldOf("triggeredTimes").forGetter(x -> x.triggeredTimes),
                        Codec.INT.fieldOf("ticks").forGetter(x -> x.ticks)
                ).apply(instance, AttachedEffect::new)
        );

        private final AttachEffect attachEffect;
        private final StoredEffectContext context;
        private int triggeredTimes;
        private int ticks;

        private LivingEntity cachedCaster;

        public AttachedEffect(AttachEffect effect, StoredEffectContext context, int triggeredTimes, int ticks) {
            this.attachEffect = effect;
            this.context = context;
            this.triggeredTimes = triggeredTimes;
            this.ticks = ticks;
        }

        public AttachedEffect(AttachEffect effect, EffectContext context) {
            this(effect, new StoredEffectContext(context), 0, 0);
            this.cachedCaster = context.caster();
        }

        /**
         * Ticks this attached effect
         * @param attachedTo The entity it is attached to
         * @param level The level it is within
         * @return Whether to remove this effect
         */
        public boolean tick(Entity attachedTo, ServerLevel level) {
            LivingEntity caster = getCaster(level);
            if (caster == null) {
                return true;
            }
            if (attachEffect.getTriggerPredicate().matches(attachedTo, ticks, caster)) {
                attachEffect.getEffects().forEach(child -> child.apply(attachedTo, Collections.emptyList(), context.toContext(getCaster(level))));
                triggeredTimes++;
            }
            ticks++;
            return !attachEffect.getContinuePredicate().matches(attachedTo, ticks, triggeredTimes, caster);
        }

        public LivingEntity getCaster(ServerLevel level) {
            if (cachedCaster != null) {
                return cachedCaster.isRemoved() ? null : cachedCaster;
            }
            if (level.getEntity(context.getCasterId()) instanceof LivingEntity casterEntity) {
                cachedCaster = casterEntity;
            }
            return cachedCaster;
        }

        public int getRemainingDuration() {
            if (attachEffect.getContinuePredicate().duration() == 0) {
                return Integer.MAX_VALUE;
            }
            return attachEffect.getContinuePredicate().duration() - ticks;
        }

    }
}
