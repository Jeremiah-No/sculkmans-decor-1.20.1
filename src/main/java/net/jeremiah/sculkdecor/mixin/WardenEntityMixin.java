package net.jeremiah.sculkdecor.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import net.jeremiah.sculkdecor.entity.WardenEntityExt;
import net.jeremiah.sculkdecor.utils.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// TODO: Make all owned wardens attack a target when the summoner attack an entity (exclude other wardens)
@Mixin(WardenEntity.class)
public abstract class WardenEntityMixin extends Entity implements WardenEntityExt {
    @Unique
    private @Nullable GameProfile summoner;

    private WardenEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "isValidTarget", at = @At("HEAD"), cancellable = true)
    private void sculkdecor$summonerNotTarget(@Nullable Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (summoner == null) return;
        if (entity instanceof PlayerEntity plr && PlayerUtils.playerMatch(plr, summoner)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "damage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/WardenEntity;increaseAngerAt(Lnet/minecraft/entity/Entity;IZ)V"),
            cancellable = true)
    private void sculkdecor$summonerDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir,
                                           @Local boolean bl,
                                           @Local Entity entity) {
        if (entity instanceof PlayerEntity plr &&
                PlayerUtils.playerMatch(plr, summoner)) {
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

    @Override
    public @Nullable GameProfile sculkdecor$getSummoner() {
        return summoner;
    }

    @Override
    public void sculkdecor$setSummoner(@Nullable GameProfile plr) {
        summoner = plr;
    }
}