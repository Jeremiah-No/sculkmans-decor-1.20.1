package net.jeremiah.sculkdecor.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.jeremiah.sculkdecor.SculkmansDecor;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block SCULK_BONE_BLOCK = registerBlock("sculk_bone_block",
            new Block(FabricBlockSettings.copyOf(Blocks.BONE_BLOCK)));

    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block); // ← Correct: Register the item that places the block
        return Registry.register(Registries.BLOCK, new Identifier(SculkmansDecor.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block){
        return Registry.register(Registries.ITEM, new Identifier(SculkmansDecor.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        SculkmansDecor.LOGGER.info("Registering ModBlocks for " + SculkmansDecor.MOD_ID);

    }
}
