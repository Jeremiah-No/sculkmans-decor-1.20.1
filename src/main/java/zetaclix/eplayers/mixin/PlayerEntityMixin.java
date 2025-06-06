package zetaclix.eplayers.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static zetaclix.eplayers.EliminatedPlayers.playerBanned;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity {
    private PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "getEntityName", at = @At("HEAD"), cancellable = true)
    private void eplayers$changeUsername(CallbackInfoReturnable<String> cir) {
        if (playerBanned(getUuid())) {
            cir.setReturnValue("§kRemovedPlayer");
        }
    }

    @Inject(method = "getName", at = @At("HEAD"), cancellable = true)
    private void eplayers$changeUsername2(CallbackInfoReturnable<Text> cir) {
        if (playerBanned(getUuid())) {
            cir.setReturnValue(Text.literal("RemovedPlayer").fillStyle(Style.EMPTY.withObfuscated(true)));
        }
    }

    @Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true)
    private void eplayers$changeUsername3(CallbackInfoReturnable<Text> cir) {
        if (playerBanned(getUuid())) {
            cir.setReturnValue(Text.literal("RemovedPlayer").fillStyle(Style.EMPTY.withObfuscated(true)));
        }
    }
}
