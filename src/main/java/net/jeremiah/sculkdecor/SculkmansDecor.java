package net.jeremiah.sculkdecor;

import com.google.common.collect.ImmutableMultimap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.jeremiah.sculkdecor.client.CameraStunHandler;
import net.jeremiah.sculkdecor.enchantment.ModEnchantments;
import net.jeremiah.sculkdecor.entity.SonicBoomGeneratorBlockEntity;
import net.jeremiah.sculkdecor.item.EchoGlaiveItem;
import net.jeremiah.sculkdecor.registry.*;
import net.jeremiah.sculkdecor.utils.SonicBoomUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class SculkmansDecor implements ModInitializer {
    public static final String MOD_ID = "sculkdecor";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModBlocks.register();
        ModBlockEntityTypes.register();
        ModItems.register();
        ModItemGroups.register();
        ModEnchantments.register();
        CameraStunHandler.register();

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
                            SonicBoomUtils.noParticles(playerEntity, target, dist.normalize());
                        }
                    });
                }
        );
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            Iterator<Map.Entry<PlayerEntity, Integer>> it = EchoGlaiveItem.frozenPlayers.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<PlayerEntity, Integer> entry = it.next();
                PlayerEntity player = entry.getKey();
                int ticksLeft = entry.getValue();

                // Freeze velocity
                player.setVelocity(0, 0, 0);
                player.velocityModified = true;

                // Countdown
                ticksLeft--;
                if (ticksLeft <= 0) it.remove();
                else entry.setValue(ticksLeft);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(SonicBoomGeneratorBlockEntity.NETWORK_CHANNEL,
                (server, player, handler,
                 buf, responseSender) -> {
                    /// 0 for add
                    /// 1 for remove
                    final var kind = buf.readInt();
                    final var pos = buf.readBlockPos();
                    final var gp = buf.readGameProfile();

                    final var world = player.getWorld();

                    server.execute(() -> {
                        final var sonicBoomGen = ((SonicBoomGeneratorBlockEntity) world.getBlockEntity(pos));
                        assert sonicBoomGen != null;

                        // someone trying stuff ? ayo ? cringe ?
                        if (!sonicBoomGen.canInteract(player.getGameProfile())) {
                            player.sendMessage(Text.translatable("block.sculkdecor.sonic_boom_generator.not_owned_insist"));

                            // They're gonna be in hell
                            //          looking at heaven
                            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> modifiers = new ImmutableMultimap.Builder<>();
                            modifiers.put(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier("generic.max_health",
                                    Float.MAX_VALUE, EntityAttributeModifier.Operation.ADDITION));
                            modifiers.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier("generic.attack_damage",
                                    0, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
                            player.getAttributes().addTemporaryModifiers(modifiers.build());
                            player.setHealth(Float.MAX_VALUE);
                            return;
                        }

                        switch (kind) {
                            case 0 -> sonicBoomGen.addIgnoredPlayer(gp);
                            case 1 -> sonicBoomGen.removeIgnoredPlayer(gp);
                        }
                    });
                }
        );
    }
}