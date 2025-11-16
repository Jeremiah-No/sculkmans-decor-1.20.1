package net.jeremiah.sculkdecor.item;

import net.jeremiah.sculkdecor.registry.ModToolMaterial;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

public class WardensWaraxeItem extends AxeItem {
    public WardensWaraxeItem() {
        super(ModToolMaterial.ECHO_SHARD,
                5,        // attack damage modifier
                -3f,    // attack speed
                new Item.Settings().fireproof().rarity(Rarity.EPIC)
        );
    }
}



