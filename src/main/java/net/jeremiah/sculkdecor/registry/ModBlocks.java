package net.jeremiah.sculkdecor.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.jeremiah.sculkdecor.SculkmansDecor;
import net.jeremiah.sculkdecor.block.SonicBoomGenerator;
import net.minecraft.block.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlocks {
    public static final Block SCULK_BONE_BLOCK = registerBlock("sculk_bone_block",
            new Block(Block.Settings.copy(Blocks.BONE_BLOCK)));

    public static final Block SCULK_BONE_BLOCK_BRICKS = registerBlock("sculk_bone_block_bricks",
            new Block(Block.Settings.copy(Blocks.BONE_BLOCK)));
    public static final Block CHISELED_SCULK_BONE_BRICKS = registerBlock("chiseled_sculk_bone_bricks",
            new Block(Block.Settings.copy(Blocks.BONE_BLOCK)));
    public static final Block SONIC_BOOM_GENERATOR = registerBlock("sonic_boom_generator",
            new SonicBoomGenerator());

    public static final Block SCULK_BONE_BLOCK_STAIRS = registerBlock("sculk_bone_block_stairs",
            new StairsBlock(ModBlocks.SCULK_BONE_BLOCK.getDefaultState(), FabricBlockSettings.copyOf(Blocks.BONE_BLOCK)));
    public static final Block SCULK_BONE_BLOCK_SLAB = registerBlock("sculk_bone_block_slab",
            new SlabBlock(FabricBlockSettings.copyOf(Blocks.BONE_BLOCK)));
    public static final Block SCULK_BONE_BLOCK_WALL = registerBlock("sculk_bone_block_wall",
            new WallBlock(FabricBlockSettings.copyOf(Blocks.BONE_BLOCK)));

    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, SculkmansDecor.id(name), block);
    }

    public static void register() {
    }
}
