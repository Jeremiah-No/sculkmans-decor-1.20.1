package net.jeremiah.sculkdecor.item;

import net.jeremiah.sculkdecor.registry.ModToolMaterial;
import net.minecraft.item.SwordItem;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

public class SculkScimitarItem extends SwordItem {
    public SculkScimitarItem() {
        super(ModToolMaterial.ECHO_SHARD,
                3,        // attack damage modifier
                -2.4f,    // attack speed
                new Item.Settings().fireproof().rarity(Rarity.EPIC)
        );
    }
}



