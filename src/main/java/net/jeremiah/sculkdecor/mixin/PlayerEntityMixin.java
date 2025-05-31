package net.jeremiah.sculkdecor.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import net.jeremiah.sculkdecor.entity.WardenEntityExt;
import net.jeremiah.sculkdecor.utils.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.task.DigTask;
import net.minecraft.entity.ai.brain.task.DismountVehicleTask;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity {
    private PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract GameProfile getGameProfile();

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void sculkdecor$interactWarden(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (this.isSneaking() && entity instanceof WardenEntity warden) {
            final var wext = (WardenEntityExt) warden;
            if (!PlayerUtils.playerMatch((PlayerEntity) (Entity) this, wext.sculkdecor$getSummoner())) {
                return;
            }

            final var wbrain = warden.getBrain();
            if (wbrain.hasActivity(Activity.DIG) || wbrain.hasActivity(Activity.EMERGE)) {
                return;
            }

            wbrain.forgetAll();
            wbrain.setTaskList(
                    Activity.DIG,
                    ImmutableList.of(Pair.of(0, new DismountVehicleTask()), Pair.of(1, new DigTask<>(100))),
                    ImmutableSet.of()
            );
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D", ordinal = 0))
    private void sculkdecor$agroOwnedWardens(Entity target, CallbackInfo ci) {
        var world = this.getWorld();
        var entities = world.getEntitiesByClass(WardenEntity.class, new Box(
                this.getPos().subtract(new Vec3d(-50, -10, -50)),
                this.getPos().add(new Vec3d(50, 10, 50))
        ), e -> true);
        for (WardenEntity e : entities) {
            var ext = (WardenEntityExt) e;
            var owner = ext.sculkdecor$getSummoner();
            if (!this.getGameProfile().equals(owner)) continue;
            e.increaseAngerAt(target, 100, false);
            e.updateAttackTarget((LivingEntity) target);
        }
    }
}
