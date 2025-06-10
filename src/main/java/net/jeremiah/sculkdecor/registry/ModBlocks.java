package net.jeremiah.sculkdecor.registry;

import net.jeremiah.sculkdecor.SculkmansDecor;
import net.jeremiah.sculkdecor.block.SonicBoomGenerator;
import net.minecraft.block.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class ModBlocks {
    private static final Block.Settings BONE_BLOCK_SETTINGS = Block.Settings.copy(Blocks.BONE_BLOCK);
    private static final Block.Settings BROWN_MUSHROOM_BLOCK_SETTINGS = Block.Settings.copy(Blocks.BROWN_MUSHROOM);


    public static final RegistryKey<ConfiguredFeature<?, ?>> HUGE_BROWN_MUSHROOM_KEY = RegistryKey.of(
            RegistryKeys.CONFIGURED_FEATURE,
            new Identifier("minecraft", "huge_brown_mushroom")
    );

    public static final Block SCULK_BONE_BLOCK = registerBlock("sculk_bone_block",
            new Block(BONE_BLOCK_SETTINGS));

    public static final Block SCULK_BONE_BLOCK_BRICKS = registerBlock("sculk_bone_block_bricks",
            new Block(BONE_BLOCK_SETTINGS));
    public static final Block SMOOTH_SCULK_BONE_BLOCK = registerBlock("smooth_sculk_bone_block",
            new Block(BONE_BLOCK_SETTINGS));
    public static final Block CHISELED_SCULK_BONE_BRICKS = registerBlock("chiseled_sculk_bone_bricks",
            new Block(BONE_BLOCK_SETTINGS));
    public static final Block SONIC_BOOM_GENERATOR = registerBlock("sonic_boom_generator",
            new SonicBoomGenerator());

    public static final Block SCULK_BONE_BLOCK_STAIRS = registerBlock("sculk_bone_block_stairs",
            new StairsBlock(SCULK_BONE_BLOCK.getDefaultState(), BONE_BLOCK_SETTINGS));
    public static final Block SCULK_BONE_BLOCK_SLAB = registerBlock("sculk_bone_block_slab",
            new SlabBlock(BONE_BLOCK_SETTINGS));
    public static final Block SCULK_BONE_BLOCK_WALL = registerBlock("sculk_bone_block_wall",
            new WallBlock(BONE_BLOCK_SETTINGS));

    public static final Block SCULKSHROOM = Registry.register(
            Registries.BLOCK,
            new Identifier(SculkmansDecor.MOD_ID, "sculkshroom"),
            new MushroomPlantBlock(
                    Block.Settings.copy(Blocks.BROWN_MUSHROOM),
                    HUGE_BROWN_MUSHROOM_KEY
            )
    );

    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, SculkmansDecor.id(name), block);
    }

    public static void register() {
    }
}