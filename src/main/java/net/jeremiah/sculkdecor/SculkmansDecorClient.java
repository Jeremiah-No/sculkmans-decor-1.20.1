package net.jeremiah.sculkdecor;

import net.fabricmc.api.ClientModInitializer;
import net.jeremiah.sculkdecor.registry.ModItems;
import net.minecraft.client.item.ModelPredicateProviderRegistry;

public class SculkmansDecorClient implements ClientModInitializer {
    public static boolean renderingGui = false;

    @Override
    public void onInitializeClient() {
        ModelPredicateProviderRegistry.register(ModItems.ECHO_GLAIVE, SculkmansDecor.id("gui"),
                (stack, world, entity, seed) -> renderingGui ? 0f : 1f);
    }
}
