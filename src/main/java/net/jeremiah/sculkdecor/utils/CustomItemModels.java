package net.jeremiah.sculkdecor.utils;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CustomItemModels {
    private static final Map<Item, ModelIdentifier> itemModels = new HashMap<>();

    public static void registerItemModel(@NotNull Item item, @NotNull Identifier id, @NotNull String variant) {
        itemModels.put(item, new ModelIdentifier(id, variant));
    }

    public static ImmutableMap<Item, ModelIdentifier> getItemModels() {
        return ImmutableMap.copyOf(itemModels);
    }
}