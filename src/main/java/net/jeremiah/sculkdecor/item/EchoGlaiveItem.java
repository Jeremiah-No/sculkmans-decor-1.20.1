package net.jeremiah.sculkdecor.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.jeremiah.sculkdecor.SculkmansDecor;
import net.jeremiah.sculkdecor.entity.WardenEntityExt;
import net.jeremiah.sculkdecor.registry.ModToolMaterial;
import net.jeremiah.sculkdecor.utils.RaycastUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.UUID;

public final class EchoGlaiveItem extends SwordItem {
    public static final Identifier SONIC_BOOM_PACKET_ID = SculkmansDecor.id("sonic_boom");
    private static final UUID ATTACK_REACH_MODIFIER_ID = UUID.fromString("76a8dee3-3e7e-4e11-ba46-a19b0c724567");
    private static final UUID REACH_MODIFIER_ID = UUID.fromString("a31c8afc-a716-425d-89cd-0d373380e6e7");

    private static final int WARDEN_SPAWN_LEVEL_COST = 10;
    private static final int WARDEN_HP = 100;
    private static final int WARDEN_DMG = 8;
    private static final double WARDEN_SUMMON_COOLDOWN = 120;
    private static final double SONIC_BOOM_RANGE = 20;
    private static final double SONIC_BOOM_RADIUS = 2;
    private static final double SONIC_BOOM_COOLDOWN = 20;
    private static final int SONIC_BOOM_CASTS = 10;

    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public EchoGlaiveItem() {
        super(ModToolMaterial.ECHO_SHARD, 1, 1.2f, new Settings()
                .fireproof()
                .rarity(Rarity.EPIC));

        var builder = ImmutableMultimap.<EntityAttribute, EntityAttributeModifier>builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID,
                "Weapon modifier", 9 - 1, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID,
                "Weapon modifier", 1.4 - 4, EntityAttributeModifier.Operation.ADDITION));
        builder.put(ReachEntityAttributes.ATTACK_RANGE, new EntityAttributeModifier(ATTACK_REACH_MODIFIER_ID,
                "Weapon modifier", 0.75, EntityAttributeModifier.Operation.ADDITION));
        builder.put(ReachEntityAttributes.REACH, new EntityAttributeModifier(REACH_MODIFIER_ID,
                "Weapon modifier", 1.5, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var handStack = user.getStackInHand(hand);
        if (user.isSneaking()) {
            if (!world.isClient()) {
                var warden_opt = LargeEntitySpawnHelper.trySpawnAt(EntityType.WARDEN, SpawnReason.TRIGGERED, (ServerWorld) world,
                        user.getBlockPos(), 20, 5, 6,
                        LargeEntitySpawnHelper.Requirements.WARDEN);
                if (warden_opt.isEmpty()) {
                    return TypedActionResult.fail(handStack);
                }
                var warden = warden_opt.get();

                // Give Warden custom effects or attributes if needed
                ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> attribs = new ImmutableMultimap.Builder<>();
                attribs.put(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier("generic.max_health",
                        WARDEN_HP - 500, EntityAttributeModifier.Operation.ADDITION));
                attribs.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier("generic.attack_damage",
                        WARDEN_DMG - 30, EntityAttributeModifier.Operation.ADDITION));
                warden.getAttributes().addTemporaryModifiers(attribs.build());
                warden.setHealth(WARDEN_HP);
                ((WardenEntityExt) warden).sculkdecor$setSummoner(user.getGameProfile());

                if (user.experienceLevel < WARDEN_SPAWN_LEVEL_COST) {
                    var toDamage = WARDEN_SPAWN_LEVEL_COST - user.experienceLevel;
                    user.addExperienceLevels(-user.experienceLevel);
                    user.damage(user.getDamageSources().magic(), toDamage);
                } else {
                    user.addExperienceLevels(-WARDEN_SPAWN_LEVEL_COST);
                }

                if (!user.getAbilities().creativeMode) {
                    user.getItemCooldownManager().set(this, (int) (20 * WARDEN_SUMMON_COOLDOWN));
                }
                return TypedActionResult.success(handStack, true);
            }
            return TypedActionResult.success(handStack);
        } else if (world.isClient()) {
            final var client = MinecraftClient.getInstance();
            final var camera = client.getCameraEntity();
            if (camera == null) return TypedActionResult.pass(handStack);

            final var origin = camera.getEyePos();
            final var dir = camera.getRotationVecClient();
            final var dist = dir.multiply(SONIC_BOOM_RANGE);
            final var end = origin.add(dist);
            final var box = new Box(origin.subtract(SONIC_BOOM_RANGE, SONIC_BOOM_RANGE, SONIC_BOOM_RANGE),
                    origin.add(SONIC_BOOM_RANGE, SONIC_BOOM_RANGE, SONIC_BOOM_RANGE));

            final var allTargets = new ArrayList<LivingEntity>();

            for (int i = 0; i < SONIC_BOOM_CASTS; i++) {
                final var entities = world.getEntitiesByClass(LivingEntity.class, box, e -> !e.isSpectator()
                        && !e.isInvulnerableTo(user.getDamageSources().magic())
                        && e != user
                        && !allTargets.contains(e));
                final var result = RaycastUtils.spherecast(entities, origin, end, SONIC_BOOM_RADIUS);

                final var target = result.result();
                if (result.hit()) allTargets.add(target);
            }

            final var bytes = PacketByteBufs.create();
            bytes.writeDouble(dist.getX());
            bytes.writeDouble(dist.getY());
            bytes.writeDouble(dist.getZ());
            bytes.writeInt(allTargets.size());
            for (LivingEntity target : allTargets) {
                bytes.writeInt(target.getId());
            }
            ClientPlayNetworking.send(SONIC_BOOM_PACKET_ID, bytes);
            user.playSound(SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.PLAYERS, 4.0f, 1.0f);
            if (!user.getAbilities().creativeMode) {
                user.getItemCooldownManager().set(this, (int) (20 * SONIC_BOOM_COOLDOWN));
            }
            return TypedActionResult.success(handStack, true);
        }
        return TypedActionResult.pass(handStack);
    }

}
