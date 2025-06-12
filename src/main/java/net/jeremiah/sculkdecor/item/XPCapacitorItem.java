package net.jeremiah.sculkdecor.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public final class XPCapacitorItem extends Item {
    public static final String NBT_STORED_XP_KEY = "StoredLevels";
    private static final int MAX_LEVEL_CAPACITY = 10;

    public XPCapacitorItem() {
        super(new Settings()
                .rarity(Rarity.UNCOMMON)
                .maxCount(1));
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return false;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0xa3e637;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        var nbt = stack.getNbt();
        if (nbt == null) return 0;
        var levels = nbt.getInt(NBT_STORED_XP_KEY);
        if (levels < 0) levels = 0;
        else if (levels > MAX_LEVEL_CAPACITY) levels = MAX_LEVEL_CAPACITY;
        return levels * 13 / MAX_LEVEL_CAPACITY;
    }

    @Override
    public ItemStack getDefaultStack() {
        return stackWithCapacity(0);
    }

    public ItemStack stackWithCapacity(int levels) {
        if (levels < 0 || levels > MAX_LEVEL_CAPACITY) levels = MAX_LEVEL_CAPACITY;
        var stack = new ItemStack(this);
        var nbt = new NbtCompound();
        nbt.putInt(NBT_STORED_XP_KEY, levels);
        stack.setNbt(nbt);
        return stack;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var stack = user.getStackInHand(hand);

        final var nbt = stack.getOrCreateNbt();
        assert nbt != null;

        var levels = nbt.getInt(NBT_STORED_XP_KEY);
        if (user.isSneaking() && user.experienceLevel > 0 && levels < MAX_LEVEL_CAPACITY) {
            levels++;
            user.addExperienceLevels(-1);
        } else if (!user.isSneaking() && levels > 0) {
            levels--;
            user.addExperienceLevels(1);
        } else {
            return TypedActionResult.fail(stack);
        }

        if (!world.isClient()) {
            nbt.putInt(NBT_STORED_XP_KEY, levels);
            stack.setNbt(nbt);
        }
        return TypedActionResult.consume(stack);
    }
}
