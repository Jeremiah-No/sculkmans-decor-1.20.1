package net.jeremiah.sculkdecor.registry;

import net.jeremiah.sculkdecor.SculkmansDecor;
import net.jeremiah.sculkdecor.item.EchoGlaiveItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModItems {
    public static final Item SCULK_BONE = registerItem("sculk_bone", new Item(new Item.Settings()));
    public static final Item SONIC_BOOM_GENERATOR = registerItem("sonic_boom_generator", new Item(new Item.Settings()));
    public static final Item SCULK_BONE_BLOCK = registerBlockItem("sculk_bone_block", ModBlocks.SCULK_BONE_BLOCK);
    public static final Item SCULK_BONE_BLOCK_BRICKS = registerBlockItem("sculk_bone_block_bricks", ModBlocks.SCULK_BONE_BLOCK_BRICKS);
    public static final Item CHISELED_SCULK_BONE_BRICKS = registerBlockItem("chiseled_sculk_bone_bricks", ModBlocks.CHISELED_SCULK_BONE_BRICKS);
    public static final Item ECHO_GLAIVE = registerItem("echo_glaive", new EchoGlaiveItem());

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, SculkmansDecor.id(name), item);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, SculkmansDecor.id(name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModItems() {
        SculkmansDecor.LOGGER.info("Registering Mod Items for " + SculkmansDecor.MOD_ID);
    }
}
