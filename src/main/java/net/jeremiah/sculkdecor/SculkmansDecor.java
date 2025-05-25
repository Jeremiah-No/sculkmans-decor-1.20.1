package net.jeremiah.sculkdecor;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.jeremiah.sculkdecor.item.EchoGlaiveItem;
import net.jeremiah.sculkdecor.registry.ModBlocks;
import net.jeremiah.sculkdecor.registry.ModItemGroups;
import net.jeremiah.sculkdecor.registry.ModItems;
import net.jeremiah.sculkdecor.utils.SonicBoomUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class SculkmansDecor implements ModInitializer {
    public static final String MOD_ID = "sculkdecor";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModBlocks.registerModBlocks();
        ModItems.registerModItems();
        ModItemGroups.registerItemGroups();

        ServerPlayNetworking.registerGlobalReceiver(EchoGlaiveItem.SONIC_BOOM_PACKET_ID,
                (server, player, handler,
                 buf, responseSender) -> {
                    var dist = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());

                    var world = player.getEntityWorld();
                    var playerEntity = world.getPlayerByUuid(player.getGameProfile().getId());

                    var targets = new ArrayList<LivingEntity>();

                    var targetsCount = buf.readInt();
                    for (int i = 0; i < targetsCount; i++) {
                        targets.add((LivingEntity) world.getEntityById(buf.readInt()));
                    }

                    server.execute(() -> {
                        assert playerEntity != null;
                        SonicBoomUtils.create((ServerWorld) world, playerEntity, null, dist);
                        for (LivingEntity target : targets) {
                            LOGGER.info("Booming: " + target.getId());
                            SonicBoomUtils.noParticles((ServerWorld) world, playerEntity, target, dist.normalize());
                        }
                    });
                });
    }
}