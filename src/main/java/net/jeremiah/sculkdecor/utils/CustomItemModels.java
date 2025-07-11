package net.jeremiah.sculkdecor.utils;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class CustomItemModels {
    private static final Map<Item, ModelIdentifier> itemModels = new HashMap<>();

    public static void registerItemModel(@NotNull Item item, @NotNull Identifier id) {
        itemModels.put(item, new ModelIdentifier(id, "inventory"));
    }

    public static ImmutableMap<Item, ModelIdentifier> getItemModels() {
        return ImmutableMap.copyOf(itemModels);
    }
}