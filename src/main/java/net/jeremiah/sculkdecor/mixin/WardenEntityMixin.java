package net.jeremiah.sculkdecor.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import net.jeremiah.sculkdecor.entity.WardenEntityExt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(WardenEntity.class)
public class WardenEntityMixin implements WardenEntityExt {
    @Unique
    private @Nullable GameProfile summoner;

    @Unique
    private @Nullable UUID getUUID() {
        return summoner != null ? summoner.getId() : null;
    }

    @Inject(method = "isValidTarget", at = @At("HEAD"), cancellable = true)
    private void sculkdecor$notSummoner(@Nullable Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (summoner == null) return;
        if (entity instanceof PlayerEntity plr && plr.getGameProfile().getId().equals(getUUID())) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "damage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/WardenEntity;increaseAngerAt(Lnet/minecraft/entity/Entity;IZ)V"),
            cancellable = true)
    private void sculkdecor$notAttackSummoner(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir,
                                              @Local boolean bl,
                                              @Local Entity entity) {
        if (entity instanceof PlayerEntity plr &&
                plr.getGameProfile().getId().equals(summoner != null ? getUUID() : null)) {
            cir.setReturnValue(bl);
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void sculkdecor$writeNbt(NbtCompound nbt, CallbackInfo ci) {
        if (summoner != null) {
            var gp = new NbtCompound();
            NbtHelper.writeGameProfile(gp, summoner);
            nbt.put("Summoner", gp);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void sculkdecor$readNbt(NbtCompound nbt, CallbackInfo ci) {
        try {
            var gp = nbt.getCompound("Summoner");
            summoner = NbtHelper.toGameProfile(gp);
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                summoner = null;
                return;
            }
            throw e;
        }
    }

    @SuppressWarnings("mixin")

    @Override
    public @Nullable GameProfile sculkdecor$getSummoner() {
        return summoner;
    }

    @Override
    public void sculkdecor$setSummoner(@Nullable GameProfile plr) {
        summoner = plr;
    }
}