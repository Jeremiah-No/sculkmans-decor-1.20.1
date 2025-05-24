package net.jeremiah.sculkdecor.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.jeremiah.sculkdecor.entity.WardenEntityExt;
import net.jeremiah.sculkdecor.utils.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(StatusEffectUtil.class)
public class StatusEffectUtilsMixin {
    @Inject(method = "addEffectToPlayersWithinDistance", at = @At(value = "INVOKE", target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V"))
    private static void sculkdecor$noDarknessForSummoner(ServerWorld world, @Nullable Entity entity, Vec3d origin,
                                                         double range, StatusEffectInstance statusEffectInstance,
                                                         int duration, CallbackInfoReturnable<List<ServerPlayerEntity>> cir,
                                                         @Local List<ServerPlayerEntity> list) {
        if (entity == null) return;
        var summoner = ((WardenEntityExt) entity).sculkdecor$getSummoner();
        if (summoner == null) return;
        list.removeIf((p) -> PlayerUtils.playerMatch(p, summoner));
    }
}
