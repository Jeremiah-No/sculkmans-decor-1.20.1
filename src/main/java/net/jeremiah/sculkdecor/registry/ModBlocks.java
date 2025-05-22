package net.jeremiah.sculkdecor.registry;

import net.jeremiah.sculkdecor.SculkmansDecor;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.brain.task.SonicBoomTask;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlocks {
    public static final Block SCULK_BONE_BLOCK = registerBlock("sculk_bone_block",
            new Block(Block.Settings.copy(Blocks.BONE_BLOCK)));

    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, SculkmansDecor.id(name), block);
    }

    public static void registerModBlocks() {
        SculkmansDecor.LOGGER.info("Registering ModBlocks for " + SculkmansDecor.MOD_ID);
    }
}
