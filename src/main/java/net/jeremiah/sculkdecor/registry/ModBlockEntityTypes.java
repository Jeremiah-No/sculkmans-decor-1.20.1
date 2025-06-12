package net.jeremiah.sculkdecor.registry;

import com.mojang.datafixers.types.Type;
import net.jeremiah.sculkdecor.SculkmansDecor;
import net.jeremiah.sculkdecor.entity.SonicBoomGeneratorBlockEntity;
import net.jeremiah.sculkdecor.entity.XPBankBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Util;

public class ModBlockEntityTypes {
    public static final BlockEntityType<SonicBoomGeneratorBlockEntity> SONIC_BOOM_GENERATOR = register(
        "sonic_boom_generator", SonicBoomGeneratorBlockEntity::new, ModBlocks.SONIC_BOOM_GENERATOR
    );
    public static final BlockEntityType<XPBankBlockEntity> XP_BANK = register(
        "xp_bank", XPBankBlockEntity::new, ModBlocks.XP_BANK
    );

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String id,
            BlockEntityType.BlockEntityFactory<T> factory,
            Block... blocks
    ) {
        Type<?> type = Util.getChoiceType(TypeReferences.BLOCK_ENTITY, SculkmansDecor.MOD_ID + ":" + id);
        return Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                SculkmansDecor.id(id),
                BlockEntityType.Builder.create(factory, blocks).build(type)
        );
    }

    public static void register() {
    }


}
