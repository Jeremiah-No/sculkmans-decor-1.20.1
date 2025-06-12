package net.jeremiah.sculkdecor.block;

import net.jeremiah.sculkdecor.entity.XPBankBlockEntity;
import net.jeremiah.sculkdecor.item.XPCapacitorItem;
import net.jeremiah.sculkdecor.registry.ModItems;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public final class XPBank extends GlassBlock implements BlockEntityProvider {
    public XPBank() {
        super(Settings.copy(Blocks.GLASS));
    }

    @Override
    public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
        super.onSyncedBlockEvent(state, world, pos, type, data);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity != null && blockEntity.onSyncedBlockEvent(type, data);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new XPBankBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient()) {
            var be = (XPBankBlockEntity)world.getBlockEntity(pos);
            assert be != null;
            var stored = be.getStoredXP();
            var stack = player.getStackInHand(hand);
            if (stack.isOf(ModItems.XP_CAPACITOR)) {
                var nbt = stack.getOrCreateNbt();
                var maxWithdraw = 10 - nbt.getInt(XPCapacitorItem.NBT_STORED_XP_KEY);
                var withdraw = Math.min(stored, maxWithdraw);
                nbt.putInt(XPCapacitorItem.NBT_STORED_XP_KEY, withdraw);
                be.setStoredXP(stored - withdraw);
            } else if (!player.isSneaking() && stored > 0) {
                be.setStoredXP(stored - 1);
                player.addExperienceLevels(1);
            } else if (player.isSneaking() && player.experienceLevel > 0){
                be.setStoredXP(stored + 1);
                player.addExperienceLevels(-1);
            } else {
                return ActionResult.FAIL;
            }
            be.markDirty();
        }
        return ActionResult.SUCCESS;
    }
}
