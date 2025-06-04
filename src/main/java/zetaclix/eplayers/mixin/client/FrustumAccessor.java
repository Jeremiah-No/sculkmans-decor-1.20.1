package zetaclix.eplayers.mixin.client;

import net.minecraft.client.render.Frustum;
import org.joml.FrustumIntersection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Frustum.class)
public interface FrustumAccessor {
    @Accessor("frustumIntersection")
    FrustumIntersection sculkdecor$getIntersection();
}
