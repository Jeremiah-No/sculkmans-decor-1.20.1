package net.jeremiah.sculkdecor;

import net.fabricmc.api.ClientModInitializer;
import net.jeremiah.sculkdecor.registry.ModItems;
import net.jeremiah.sculkdecor.utils.CustomItemModels;

public class SculkmansDecorClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CustomItemModels.registerItemModel(ModItems.ECHO_GLAIVE, SculkmansDecor.id("echo_glaive_handheld"), "inventory");}
}
