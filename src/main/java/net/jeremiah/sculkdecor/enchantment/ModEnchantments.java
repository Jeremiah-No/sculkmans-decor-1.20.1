package net.jeremiah.sculkdecor.enchantment;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;

public class ModEnchantments {

    public static Enchantment FRIENDLY_FIRE;

    public static void register() {

        FRIENDLY_FIRE = Registry.register(
                Registries.ENCHANTMENT,
                new Identifier("sculkdecor", "friendly_fire"),
                new FriendlyFireEnchantment()
        );
    }
}
