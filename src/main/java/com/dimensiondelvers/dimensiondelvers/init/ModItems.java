package com.dimensiondelvers.dimensiondelvers.init;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.item.runegem.RuneGemShape;
import com.dimensiondelvers.dimensiondelvers.item.runegem.RuneGemTier;
import com.dimensiondelvers.dimensiondelvers.item.runegem.Runegem;
import com.dimensiondelvers.dimensiondelvers.item.runegem.RunegemData;
import com.dimensiondelvers.dimensiondelvers.item.tools.DDTweaker;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.dimensiondelvers.dimensiondelvers.DimensionDelvers.tagId;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DimensionDelvers.MODID);


    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "example_block",
            ModBlocks.EXAMPLE_BLOCK
    );

    public static final DeferredItem<BlockItem> DEV_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "dev_block",
            ModBlocks.DEV_BLOCK
    );

    public static final DeferredItem<BlockItem> RUNE_ANVIL_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "rune_anvil",
            ModBlocks.RUNE_ANVIL_BLOCK
    );

    public static final DeferredItem<BlockItem> SPRING_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "spring_block",
            ModBlocks.SPRING_BLOCK
    );

    // Trap blocks
    public static final DeferredItem<BlockItem> TRAP_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "trap_block",
            ModBlocks.TRAP_BLOCK
    );

    public static final DeferredItem<BlockItem> PLAYER_TRAP_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "player_trap_block",
            ModBlocks.PLAYER_TRAP_BLOCK
    );

    public static final DeferredItem<BlockItem> MOB_TRAP_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "mob_trap_block",
            ModBlocks.MOB_TRAP_BLOCK
    );


    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem(
            "example_item",
            new Item.Properties().food(new FoodProperties.Builder()
                    .alwaysEdible()
                    .nutrition(1)
                    .saturationModifier(2f)
                    .build()
            )
    );

    //Tools
    public static final DeferredItem<Item> DD_TWEAKER = ITEMS.register(
            "dd_tweaker",
            registryName -> new DDTweaker(new Item.Properties())
    );

    //Runegems
    public static final DeferredItem<Item> RUNEGEM = ITEMS.register("runegem",
            registryName -> new Runegem(new Item.Properties()
                    .component(ModDataComponentType.RUNEGEM_DATA,
                            new RunegemData(RuneGemShape.CIRCLE, tagId(Registries.ENCHANTMENT, "raw_fire_rune"), RuneGemTier.RAW)))
            );

}
