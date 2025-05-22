package net.jeremiah.sculkdecor.item;

import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.jeremiah.sculkdecor.SculkmansDecor;
import net.jeremiah.sculkdecor.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
public static final ItemGroup SCULK_ITEMS_GROUP = Registry.register(Registries.ITEM_GROUP, new Identifier(SculkmansDecor.MOD_ID, "sculk_items"),
        FabricItemGroup.builder().displayName(Text.translatable("itemgroup.sculk_items"))
                .icon(() -> new ItemStack(Items.SCULK_CATALYST)).entries((displayContext, entries) -> {
                    entries.add((ModItems.SCULK_BONE));

                    entries.add((ModBlocks.SCULK_BONE_BLOCK));

                    entries.add((Items.SCULK));
                    entries.add((Items.WARDEN_SPAWN_EGG));
                    entries.add((Items.SCULK_VEIN));
                    entries.add((Items.SCULK_CATALYST));
                    entries.add((Items.SCULK_SENSOR));
                    entries.add((Items.SCULK_SHRIEKER));
                    entries.add((Items.CALIBRATED_SCULK_SENSOR));
                }).build());

    public static void registerItemGroups(){
        SculkmansDecor.LOGGER.info("Registering Item Groups for " + SculkmansDecor.MOD_ID);
    }
}
