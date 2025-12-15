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

@Mixin(WardenEntity.class)
public abstract class WardenEntityMixin extends Entity implements WardenEntityExt {

    @Unique
    private @Nullable GameProfile summoner;

    protected WardenEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "isValidTarget", at = @At("HEAD"), cancellable = true)
    private void sculkdecor$preventTargetingSummoner(@Nullable Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (summoner == null) return;
        if (entity instanceof PlayerEntity player &&
                PlayerUtils.playerMatch(player, summoner)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/WardenEntity;increaseAngerAt(Lnet/minecraft/entity/Entity;IZ)V"
            ),
            cancellable = true
    )
    private void sculkdecor$ignoreSummonerDamage(
            DamageSource source,
            float amount,
            CallbackInfoReturnable<Boolean> cir,
            @Local boolean bl,
            @Local Entity entity
    ) {
        if (entity instanceof PlayerEntity player &&
                PlayerUtils.playerMatch(player, summoner)) {
            cir.setReturnValue(bl);
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void sculkdecor$writeSummoner(NbtCompound nbt, CallbackInfo ci) {
        if (summoner != null) {
            NbtCompound gp = new NbtCompound();
            NbtHelper.writeGameProfile(gp, summoner);
            nbt.put("Summoner", gp);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void sculkdecor$readSummoner(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("Summoner")) {
            summoner = NbtHelper.toGameProfile(nbt.getCompound("Summoner"));
        } else {
            summoner = null;
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
