package net.jeremiah.sculkdecor.mixin.client;

import net.jeremiah.sculkdecor.SculkmansDecorClient;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public class DrawContextMixin {
    @Inject(method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;III)V", at = @At("HEAD"))
    private void sculkdecor$renderingGui(CallbackInfo ci) {
        SculkmansDecorClient.renderingGui = true;
    }

    @Inject(method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;III)V", at = @At("RETURN"))
    private void sculkdecor$renderingHeld(CallbackInfo ci) {
        SculkmansDecorClient.renderingGui = false;
    }
}
