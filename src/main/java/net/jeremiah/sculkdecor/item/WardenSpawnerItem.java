package net.jeremiah.sculkdecor.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LargeEntitySpawnHelper;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class WardenSpawnerItem extends Item {
    public WardenSpawnerItem() {
        super(new Settings());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var handStack = user.getStackInHand(hand);
        if (!(handStack.getItem() instanceof WardenSpawnerItem)) {
            return TypedActionResult.pass(handStack);
        }
        if (!world.isClient()) {
            LargeEntitySpawnHelper.trySpawnAt(EntityType.WARDEN, SpawnReason.TRIGGERED, (ServerWorld) world,
                    user.getBlockPos(), 20, 5, 6,
                    LargeEntitySpawnHelper.Requirements.WARDEN);
        }
        return TypedActionResult.success(handStack, true);
    }
}
