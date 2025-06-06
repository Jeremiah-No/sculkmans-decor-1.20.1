package net.jeremiah.sculkdecor.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class RaycastUtils {
    public static <T extends Entity> RaycastResult<T> spherecast(List<T> entities, Vec3d origin, Vec3d end, double maxDist) {
        T closest = null;
        double closestDist = Double.POSITIVE_INFINITY;
        for (T entity : entities) {
            var line_dist = linePointDist(origin, end, entity.getPos());
            var direct_dist = entity.getPos().subtract(origin).length();
            if (line_dist <= maxDist && direct_dist <= closestDist) {
                closest = entity;
                closestDist = direct_dist;
            }
        }
        return new RaycastResult<>(closest != null, closest);
    }

    private static double linePointDist(Vec3d a, Vec3d b, Vec3d c) {
        final var ab = b.subtract(a);
        final var ac = c.subtract(a);

        final var t = max(0, min(1, ac.dotProduct(ab) / ab.lengthSquared()));
        final var projection = a.add(ab.multiply(t));
        return c.distanceTo(projection);
    }

    public record RaycastResult<T extends Entity>(boolean hit, @Nullable("hit=false") T entity) {
    }
}
