package net.jeremiah.sculkdecor.registry;

import net.jeremiah.sculkdecor.SculkmansDecor;
import net.jeremiah.sculkdecor.block.SonicBoomGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
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

    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, SculkmansDecor.id(name), block);
    }

    public static void register() {
    }
}
