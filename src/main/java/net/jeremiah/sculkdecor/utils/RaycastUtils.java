package net.jeremiah.sculkdecor.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import org.lwjgl.system.MathUtil;

import java.util.List;

public class RaycastUtils {
    public static <T extends Entity> RaycastResult<T> spherecast(List<T> entities, Vec3d origin, Vec3d end, double maxDist) {
        T closest = null;
        double closestDist = Double.POSITIVE_INFINITY;
        for (T entity : entities) {
            var dist = linePointDist(origin, end, entity.getPos());
            if (dist <= maxDist && dist <= closestDist) {
                closest = entity;
                closestDist = dist;
            }
        }
        return new RaycastResult<>(closest != null, closest);
    }

    private static double linePointDist(Vec3d a, Vec3d b, Vec3d p) {
        return b.subtract(a).crossProduct(a.subtract(p)).length() / b.subtract(a).length();
    }

    public record RaycastResult<T extends Entity>(boolean hit, @Nullable("hit=false") T entity) {}
}
