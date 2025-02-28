package com.dimensiondelvers.dimensiondelvers.item.essence;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Item;

import java.util.Map;

public final class EssenceTypeLookup {

    private static Table<Item, EssenceType, Integer> lookup = ImmutableTable.of();

    public static void generateLookup(RegistryAccess registryAccess) {
        final Table<Item, EssenceType, Integer> newLookup = HashBasedTable.create();
        registryAccess.lookup(EssenceType.ESSENCE_TYPE_REGISTRY_KEY).ifPresent(
                essenceRegistry -> essenceRegistry.stream().forEach(
                        essenceType -> essenceType.streamItems().forEach(
                                pair -> pair.getKey().forEach(
                                        item -> newLookup.put(item.value(), essenceType, pair.getValue())))));
        lookup = ImmutableTable.copyOf(newLookup);
    }

    public static void clearLookup() {
        lookup = ImmutableTable.of();
    }

    public static Map<EssenceType, Integer> getEssencesFor(Item item) {
        return lookup.row(item);
    }

    private EssenceTypeLookup() {

    }
}
