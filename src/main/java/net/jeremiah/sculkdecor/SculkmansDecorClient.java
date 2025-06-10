package net.jeremiah.sculkdecor;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.jeremiah.sculkdecor.registry.ModBlocks;
import net.jeremiah.sculkdecor.registry.ModItems;
import net.jeremiah.sculkdecor.utils.CustomItemModels;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class SculkmansDecorClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register item model
        CustomItemModels.registerItemModel(
                ModItems.ECHO_GLAIVE,
                SculkmansDecor.id("echo_glaive_handheld"),
                "inventory"
        );

        // Register block render layer
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SCULKSHROOM, RenderLayer.getCutout());
    }
}