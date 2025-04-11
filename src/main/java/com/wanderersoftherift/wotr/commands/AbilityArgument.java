package com.wanderersoftherift.wotr.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.wanderersoftherift.wotr.Registries.AbilityRegistry;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.abilities.AbstractAbility;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Argument for specifying an ability
 */
public class AbilityArgument implements ArgumentType<ResourceLocation> {
    private static final Collection<String> EXAMPLES = Stream.of(WanderersOfTheRift.id("fireball"), WanderersOfTheRift.id("mega_boost"))
            .map(ResourceLocation::toString)
            .collect(Collectors.toList());
    private static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(
            argument -> Component.translatableEscape("argument.wotr.ability.invalid", argument)
    );

    public ResourceLocation parse(StringReader reader) throws CommandSyntaxException {
        return ResourceLocation.read(reader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return context.getSource() instanceof SharedSuggestionProvider
                ? SharedSuggestionProvider.suggestResource(((SharedSuggestionProvider)context.getSource()).registryAccess().lookupOrThrow(AbilityRegistry.DATA_PACK_ABILITY_REG_KEY).keySet(), builder)
                : Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static AbilityArgument ability() {
        return new AbilityArgument();
    }

    public static AbstractAbility getAbility(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        ResourceLocation resourcelocation = context.getArgument(name, ResourceLocation.class);
        Optional<AbstractAbility> result = context.getSource().getLevel().registryAccess().lookup(AbilityRegistry.DATA_PACK_ABILITY_REG_KEY).flatMap(
                registry -> Optional.ofNullable(registry.getValue(resourcelocation))
        );
        if (result.isPresent()) {
            return result.get();
        } else {
            throw ERROR_INVALID_VALUE.create(resourcelocation);
        }
    }
}