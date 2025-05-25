package net.jeremiah.sculkdecor.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class SonicBoomUtils {
    public static void create(ServerWorld world, LivingEntity attacker, LivingEntity target) {
        final Vec3d origin = attacker.getEyePos();
        final Vec3d dist = target.getEyePos().subtract(origin);
        final Vec3d dir = dist.normalize();

        for (int i = 1; i < MathHelper.floor(dist.length()) + 7; i++) {
            final Vec3d pos = origin.add(dir.multiply(i));
            world.spawnParticles(ParticleTypes.SONIC_BOOM, pos.x, pos.y, pos.z, 1, 0.0, 0.0, 0.0, 0.0);
        }

        attacker.playSound(SoundEvents.ENTITY_WARDEN_SONIC_BOOM, 3.0f, 1.0f);
        target.damage(world.getDamageSources().sonicBoom(attacker), 10.0f);
        final var knockres = 1 - target.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
        final double d = 0.5 * knockres;
        final double e = 2.5 * knockres;
        target.addVelocity(dir.getX() * e, dir.getY() * d, dir.getZ() * e);
    }
}
