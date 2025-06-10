package net.jeremiah.sculkdecor.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.jeremiah.sculkdecor.entity.SonicBoomGeneratorBlockEntity;
import net.jeremiah.sculkdecor.gui.SonicBoomScreen;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public final class SonicBoomGenerator extends BlockWithEntity {
    private final static VoxelShape SHAPE = VoxelShapes.cuboid(0, 0, 0, 1, 0.5, 1);

    public SonicBoomGenerator() {
        super(Block.Settings.copy(Blocks.BONE_BLOCK).strength(100, 1200));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SonicBoomGeneratorBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient() && placer instanceof PlayerEntity plr) {
            final var be = ((SonicBoomGeneratorBlockEntity)world.getBlockEntity(pos));
            assert be != null;
            be.setOwner(plr.getGameProfile());
            world.markDirty(pos);
            world.updateListeners(pos, state, state, 0);
        }
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) -> ((SonicBoomGeneratorBlockEntity) blockEntity)
                .tick(world, pos, state1);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        final var blk = ((SonicBoomGeneratorBlockEntity) world.getBlockEntity(pos));
        assert blk != null;
        if (!blk.canInteract(player.getGameProfile())) {
            if (world.isClient()) {
                player.sendMessage(Text.translatable("block.sculkdecor.sonic_boom_generator.not_owned"));
            }
            return ActionResult.SUCCESS;
        }
        if (world.isClient()) {
            setClientScreen(pos);
        }

        return ActionResult.SUCCESS;
    }

    @Environment(EnvType.CLIENT)
    private void setClientScreen(BlockPos pos) {
        final var client = MinecraftClient.getInstance();
        client.setScreen(new SonicBoomScreen(pos));
    }
}
