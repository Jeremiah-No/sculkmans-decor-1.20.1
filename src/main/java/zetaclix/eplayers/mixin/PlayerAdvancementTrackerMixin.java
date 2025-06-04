package zetaclix.eplayers.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import static zetaclix.eplayers.EliminatedPlayers.playerBanned;

@Mixin(PlayerAdvancementTracker.class)
public class PlayerAdvancementTrackerMixin {
    @Shadow
    private ServerPlayerEntity owner;

    @ModifyExpressionValue(method = "grantCriterion", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/AdvancementDisplay;shouldAnnounceToChat()Z"))
    private boolean eplayers$maybeActuallyDontAnnounce(boolean original) {
        return original && !playerBanned(this.owner.getUuid());
    }
}