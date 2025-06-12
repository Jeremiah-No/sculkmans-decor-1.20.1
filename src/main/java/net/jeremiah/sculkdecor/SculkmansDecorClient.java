package net.jeremiah.sculkdecor;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.jeremiah.sculkdecor.entity.renderer.XPBankBlockRenderer;
import net.jeremiah.sculkdecor.registry.ModBlockEntityTypes;
import net.jeremiah.sculkdecor.registry.ModBlocks;
import net.jeremiah.sculkdecor.registry.ModItems;
import net.jeremiah.sculkdecor.utils.CustomItemModels;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.nbt.NbtElement;

import static net.jeremiah.sculkdecor.item.XPCapacitorItem.NBT_STORED_XP_KEY;

@Environment(EnvType.CLIENT)
public class SculkmansDecorClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register item model
        CustomItemModels.registerItemModel(
                ModItems.ECHO_GLAIVE,
                SculkmansDecor.id("echo_glaive_handheld")
        );

        // Register block render layer
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                ModBlocks.SCULKSHROOM,
                ModBlocks.XP_BANK);

        ModelPredicateProviderRegistry.registerCustomModelData((stack, client,
                                                                entity, seed) -> {
            var nbt = stack.getNbt();
            if (nbt == null) return 0;
            if (!nbt.contains(NBT_STORED_XP_KEY, NbtElement.INT_TYPE)) return 0;
            return nbt.getInt(NBT_STORED_XP_KEY);
        });

        BlockEntityRendererFactories.register(ModBlockEntityTypes.XP_BANK, XPBankBlockRenderer::new);
    }
}