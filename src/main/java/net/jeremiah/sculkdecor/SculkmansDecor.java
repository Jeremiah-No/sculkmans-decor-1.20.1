package net.jeremiah.sculkdecor;

import net.fabricmc.api.ModInitializer;

import net.jeremiah.sculkdecor.block.ModBlocks;
import net.jeremiah.sculkdecor.item.ModItemGroups;
import net.jeremiah.sculkdecor.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SculkmansDecor implements ModInitializer {
	public static final String MOD_ID = "sculkdecor";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
	}
}