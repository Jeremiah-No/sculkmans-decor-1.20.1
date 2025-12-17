package net.jeremiah.sculkdecor.registry;

import net.jeremiah.sculkdecor.SculkmansDecor;
import net.jeremiah.sculkdecor.item.*;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModItems {
    public static final Item SCULK_BONE = registerItem("sculk_bone", new Item(new Item.Settings()));
    public static final Item SCULK_BONE_BLOCK = registerItem("sculk_bone_block", ModBlocks.SCULK_BONE_BLOCK);
    public static final Item SCULKSHROOM = registerItem("sculkshroom", ModBlocks.SCULKSHROOM);
    public static final Item SMOOTH_SCULK_BONE_BLOCK = registerItem("smooth_sculk_bone_block", ModBlocks.SMOOTH_SCULK_BONE_BLOCK);
    public static final Item SCULK_BONE_BLOCK_SLAB = registerItem("sculk_bone_block_slab", ModBlocks.SCULK_BONE_BLOCK_SLAB);
    public static final Item SCULK_BONE_BLOCK_STAIRS = registerItem("sculk_bone_block_stairs", ModBlocks.SCULK_BONE_BLOCK_STAIRS);
    public static final Item SCULK_BONE_BLOCK_WALL = registerItem("sculk_bone_block_wall", ModBlocks.SCULK_BONE_BLOCK_WALL);

    public static final Item SONIC_BOOM_GENERATOR =
            registerItem("sonic_boom_generator", ModBlocks.SONIC_BOOM_GENERATOR, new Item.Settings().maxCount(1));

    public static final Item SCULK_BONE_BLOCK_BRICKS = registerItem("sculk_bone_block_bricks", ModBlocks.SCULK_BONE_BLOCK_BRICKS);
    public static final Item CHISELED_SCULK_BONE_BRICKS = registerItem("chiseled_sculk_bone_bricks", ModBlocks.CHISELED_SCULK_BONE_BRICKS);
    public static final Item ECHO_GLAIVE = registerItem("echo_glaive", new EchoGlaiveItem());
    public static final Item ECHO_GLAIVETEST = registerItem("echo_glaivetest", new EchoGlaivetestItem());
    public static final Item SCULK_SCIMITAR = registerItem("sculk_scimitar", new SculkScimitarItem());
    public static final Item WARDENS_WARAXE = registerItem("wardens_waraxe", new WardensWaraxeItem());
    public static final Item XP_CAPACITOR = registerItem("xp_capacitor", new XPCapacitorItem());
    public static final Item XP_BANK = registerItem("xp_bank", ModBlocks.XP_BANK);


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, SculkmansDecor.id(name), item);
    }

    private static Item registerItem(String name, Block block) {
        return Registry.register(Registries.ITEM, SculkmansDecor.id(name),
                new BlockItem(block, new Item.Settings()));
    }

    private static Item registerItem(String name, Block block, Item.Settings settings) {
        return Registry.register(Registries.ITEM, SculkmansDecor.id(name),
                new BlockItem(block, settings));
    }

    public static void register() {
    }
}
