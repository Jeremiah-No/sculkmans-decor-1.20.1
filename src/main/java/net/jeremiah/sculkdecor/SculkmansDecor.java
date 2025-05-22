package net.jeremiah.sculkdecor;

import net.fabricmc.api.ModInitializer;
import net.jeremiah.sculkdecor.registry.ModBlocks;
import net.jeremiah.sculkdecor.registry.ModItemGroups;
import net.jeremiah.sculkdecor.registry.ModItems;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SculkmansDecor implements ModInitializer {
    public static final String MOD_ID = "sculkdecor";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModBlocks.registerModBlocks();
        ModItems.registerModItems();
        ModItemGroups.registerItemGroups();
    }
}