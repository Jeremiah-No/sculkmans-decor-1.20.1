package net.jeremiah.sculkdecor.registry;

import com.mojang.datafixers.types.Type;
import net.jeremiah.sculkdecor.SculkmansDecor;
import net.jeremiah.sculkdecor.entity.SonicBoomGeneratorBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Util;

public class ModBlockEntityTypes {
    private static <T extends BlockEntity> BlockEntityType<T> register(String id, BlockEntityType.Builder<T> builder) {
        Type<?> type = Util.getChoiceType(TypeReferences.BLOCK_ENTITY, SculkmansDecor.MOD_ID + ":" + id);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, SculkmansDecor.id(id), builder.build(type));
    }    public static final BlockEntityType<SonicBoomGeneratorBlockEntity> SONIC_BOOM_GENERATOR = register(
            "sonic_boom_generator", BlockEntityType.Builder.create(SonicBoomGeneratorBlockEntity::new, ModBlocks.SONIC_BOOM_GENERATOR)
    );

    public static void register() {
    }


}
