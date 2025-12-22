package net.jeremiah.sculkdecor.enchantment;

import net.jeremiah.sculkdecor.registry.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class FriendlyFireEnchantment extends Enchantment {

    public FriendlyFireEnchantment() {
        super(Rarity.RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return true;
    }

    // Only allow it on the Echo Glaive
    @Override
    public boolean isAcceptableItem(net.minecraft.item.ItemStack stack) {
        return stack.isOf(ModItems.ECHO_GLAIVE);
    }
}
