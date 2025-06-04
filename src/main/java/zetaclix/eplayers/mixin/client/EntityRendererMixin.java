package zetaclix.eplayers.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.joml.FrustumIntersection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static zetaclix.eplayers.EliminatedPlayers.playerBanned;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    @Inject(method = "hasLabel", at = @At("HEAD"), cancellable = true)
    private void eplayers$noNametag(T entity, CallbackInfoReturnable<Boolean> cir) {
        var client = MinecraftClient.getInstance();
        assert client.player != null;
        if (playerBanned(entity.getUuid()) && !client.player.getUuid().equals(entity.getUuid())) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void eplayers$shouldRender(T entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        var client = MinecraftClient.getInstance();
        assert client.player != null;
        if (playerBanned(entity.getUuid()) && !client.player.getUuid().equals(entity.getUuid())) {
            final var intersec = ((FrustumAccessor)frustum).sculkdecor$getIntersection();
            var box = entity.getBoundingBox();
            final var size = new Vec3d(box.maxX - box.minX, box.maxY - box.minY, box.maxZ - box.minZ);
            box = box.stretch(size.multiply(-0.75));
            final float f = (float)(box.minX - x);
            final float g = (float)(box.minY - y);
            final float h = (float)(box.minZ - z);
            final float i = (float)(box.maxX - x);
            final float j = (float)(box.maxY - y);
            final float k = (float)(box.maxZ - z);
            final var result = intersec.intersectAab(f, g, h, i, j, k);
            cir.setReturnValue(result > FrustumIntersection.INSIDE);
        }
    }
}
