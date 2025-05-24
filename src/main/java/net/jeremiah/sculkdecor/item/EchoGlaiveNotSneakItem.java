package net.jeremiah.sculkdecor.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.jeremiah.sculkdecor.entity.WardenEntityExt;
import net.jeremiah.sculkdecor.registry.ModToolMaterial;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LargeEntitySpawnHelper;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.UUID;

public class EchoGlaiveNotSneakItem extends SwordItem {
    protected static final UUID ATTACK_REACH_MODIFIER_ID = UUID.fromString("76a8dee3-3e7e-4e11-ba46-a19b0c724567");
    protected static final UUID REACH_MODIFIER_ID = UUID.fromString("a31c8afc-a716-425d-89cd-0d373380e6e7");

    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public EchoGlaiveNotSneakItem() {
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
        if (!(handStack.getItem() instanceof EchoGlaiveNotSneakItem)) {
            return TypedActionResult.pass(handStack);
        }
        if (!world.isClient()) {
            var warden_opt = LargeEntitySpawnHelper.trySpawnAt(EntityType.WARDEN, SpawnReason.TRIGGERED, (ServerWorld) world,
                    user.getBlockPos(), 20, 5, 6,
                    LargeEntitySpawnHelper.Requirements.WARDEN);
            if (warden_opt.isEmpty()) {
                return TypedActionResult.fail(handStack);
            }
            var warden = warden_opt.get();
            warden.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, -1, 3, false, false));
            ((WardenEntityExt) warden).sculkdecor$setSummoner(user.getGameProfile());
        }
        return TypedActionResult.success(handStack, true);
    }
}
