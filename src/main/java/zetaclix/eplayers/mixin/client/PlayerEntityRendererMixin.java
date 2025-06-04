package zetaclix.eplayers.mixin.client;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zetaclix.eplayers.EliminatedPlayers;

import static zetaclix.eplayers.EliminatedPlayers.playerBanned;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @Inject(method = "getTexture(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)Lnet/minecraft/util/Identifier;",
    at = @At("HEAD"), cancellable = true)
    private void eplayers$removedTexture(AbstractClientPlayerEntity entity, CallbackInfoReturnable<Identifier> cir) {
        if (playerBanned(entity.getUuid()))
            cir.setReturnValue(EliminatedPlayers.id("textures/entity/removed.png"));
    }

    @Inject(method = "renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"), cancellable = true)
    private void eplayers$noLabel(AbstractClientPlayerEntity entity, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (playerBanned(entity.getUuid())) ci.cancel();
    }
}
