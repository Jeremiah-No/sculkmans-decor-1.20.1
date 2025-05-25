package net.jeremiah.sculkdecor.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class SonicBoomUtils {
    public static void create(ServerWorld world, LivingEntity attacker, LivingEntity target, Vec3d dist) {
        final Vec3d origin = attacker.getEyePos();
        final Vec3d dir = dist.normalize();

        for (int i = 1; i < MathHelper.floor(dist.length()) + 7; i++) {
            final Vec3d pos = origin.add(dir.multiply(i));
            world.spawnParticles(ParticleTypes.SONIC_BOOM, pos.x, pos.y, pos.z, 1, 0.0, 0.0, 0.0, 0.0);
        }

        world.playSound(origin.getX(), origin.getY(), origin.getZ(), SoundEvents.ENTITY_WARDEN_SONIC_BOOM,
                SoundCategory.PLAYERS, 3.0f, 1.0f, true);
        if (target != null) noParticles(world, attacker, target, dir);
    }

    public static void noParticles(ServerWorld world, LivingEntity attacker, LivingEntity target, Vec3d dir) {
        target.damage(attacker.getDamageSources().magic(), 10.0f);
        final var knockres = 1 - target.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
        final double d = 0.5 * knockres;
        final double e = 2.5 * knockres;
        target.addVelocity(dir.getX() * e, dir.getY() * d, dir.getZ() * e);
    }
}
