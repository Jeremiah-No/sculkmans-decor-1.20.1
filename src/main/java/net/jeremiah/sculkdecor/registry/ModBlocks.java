package net.jeremiah.sculkdecor.registry;

import net.jeremiah.sculkdecor.SculkmansDecor;
import net.jeremiah.sculkdecor.block.SonicBoomGenerator;
import net.jeremiah.sculkdecor.block.XPBank;
import net.minecraft.block.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class ModBlocks {
    private static final Block.Settings BONE_BLOCK_SETTINGS = Block.Settings.copy(Blocks.BONE_BLOCK);
    private static final Block.Settings BROWN_MUSHROOM_SETTINGS = Block.Settings.copy(Blocks.BROWN_MUSHROOM);

    public static final Block SCULK_BONE_BLOCK = registerBlock("sculk_bone_block",
            new PillarBlock(BONE_BLOCK_SETTINGS));

    public static final Block SCULK_BONE_BLOCK_BRICKS = registerBlock("sculk_bone_block_bricks",
            new Block(BONE_BLOCK_SETTINGS));
    public static final Block SMOOTH_SCULK_BONE_BLOCK = registerBlock("smooth_sculk_bone_block",
            new Block(BONE_BLOCK_SETTINGS));
    public static final Block SCULK_BONE_BLOCK_CROSSROADS = registerBlock("sculk_bone_block_crossroads",
            new Block(BONE_BLOCK_SETTINGS));
    public static final Block CARVED_SCULK_BONE_BLOCK_CROSSROADS = registerBlock("carved_sculk_bone_block_crossroads",
            new Block(BONE_BLOCK_SETTINGS));
    public static final Block CARVED_SCULK_BONE_BLOCK = registerBlock("carved_sculk_bone_block",
            new PillarBlock(BONE_BLOCK_SETTINGS));
    public static final Block CHISELED_SCULK_BONE_BRICKS = registerBlock("chiseled_sculk_bone_bricks",
            new Block(BONE_BLOCK_SETTINGS));
    public static final Block SONIC_BOOM_GENERATOR = registerBlock("sonic_boom_generator",
            new SonicBoomGenerator());
    public static final Block XP_BANK = registerBlock("xp_bank",
            new XPBank());

    public static final Block SCULK_BONE_BLOCK_STAIRS = registerBlock("sculk_bone_block_stairs",
            new StairsBlock(SCULK_BONE_BLOCK.getDefaultState(), BONE_BLOCK_SETTINGS));
    public static final Block SCULK_BONE_BLOCK_SLAB = registerBlock("sculk_bone_block_slab",
            new SlabBlock(BONE_BLOCK_SETTINGS));
    public static final Block SCULK_BONE_BLOCK_WALL = registerBlock("sculk_bone_block_wall",
            new WallBlock(BONE_BLOCK_SETTINGS));

    public static final Block SCULKSHROOM = registerBlock("sculkshroom", new MushroomPlantBlock(
            BROWN_MUSHROOM_SETTINGS
                    .mapColor(MapColor.LAPIS_BLUE),
            null
    ) {
        @Override
        public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
            BlockPos blockPos = pos.down();
            BlockState blockState = world.getBlockState(blockPos);
            return blockState.isOf(Blocks.SCULK) ||
                    (world.getBaseLightLevel(pos, 0) < 13 && this.canPlantOnTop(blockState, world, blockPos));
        }
    });

    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, SculkmansDecor.id(name), block);
    }

    public static void register() {
    }
}