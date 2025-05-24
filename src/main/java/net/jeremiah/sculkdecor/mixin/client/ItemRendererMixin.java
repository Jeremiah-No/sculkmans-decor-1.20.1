package net.jeremiah.sculkdecor.mixin.client;

import net.jeremiah.sculkdecor.utils.CustomItemModels;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @ModifyVariable(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At("HEAD"), argsOnly = true)
    private BakedModel useCustomModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded,
                                      MatrixStack matrices, VertexConsumerProvider vertexConsumer, int light, int overlay) {
        final var itemModels = CustomItemModels.getItemModels();
        final var modelID = itemModels.get(stack.getItem());
        if (modelID == null || renderMode == ModelTransformationMode.GUI) return value;
        return ((ItemRendererAccessor) this)
                .sculkdecor$getModels()
                .getModelManager()
                .getModel(modelID);
    }
}
