package net.jeremiah.sculkdecor.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.jeremiah.sculkdecor.SculkmansDecor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup SCULK_ITEMS_GROUP = Registry.register(Registries.ITEM_GROUP, new Identifier(SculkmansDecor.MOD_ID, "sculk_items"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.sculk_items"))
                    .icon(() -> new ItemStack(Items.SCULK_CATALYST))
                    .entries((displayContext, entries) -> {
                        entries.add((ModItems.SCULK_BONE));
                        entries.add((ModItems.SCULK_BONE_BLOCK));
                        entries.add((ModItems.SMOOTH_SCULK_BONE_BLOCK));
                        entries.add((ModItems.SCULK_BONE_BLOCK_STAIRS));
                        entries.add((ModItems.SCULK_BONE_BLOCK_WALL));
                        entries.add((ModItems.SCULK_BONE_BLOCK_SLAB));
                        entries.add((ModItems.SCULK_BONE_BLOCK_BRICKS));
                        entries.add((ModItems.CHISELED_SCULK_BONE_BRICKS));
                        entries.add((ModItems.SONIC_BOOM_GENERATOR));
                        entries.add((ModItems.ECHO_GLAIVE));

                        entries.add((Items.SCULK));
                        entries.add((Items.ECHO_SHARD));
                        entries.add((Items.WARDEN_SPAWN_EGG));
                        entries.add((Items.SCULK_VEIN));
                        entries.add((Items.SCULK_CATALYST));
                        entries.add((Items.SCULK_SENSOR));
                        entries.add((Items.SCULK_SHRIEKER));
                        entries.add((Items.CALIBRATED_SCULK_SENSOR));
                    }).build());

    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((e) -> {
            e.add(ModItems.SCULK_BONE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((e) -> {
            e.add(ModItems.ECHO_GLAIVE);
        });
    }
}
