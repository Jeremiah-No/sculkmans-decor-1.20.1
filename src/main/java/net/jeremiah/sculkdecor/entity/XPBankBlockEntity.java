package net.jeremiah.sculkdecor.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.jeremiah.sculkdecor.registry.ModBlockEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class XPBankBlockEntity extends BlockEntity {
    public static final int MAX_STORED_XP = 50;

    private int storedXP = 0;
    @Environment(EnvType.CLIENT)
    public float renderer_animation = 0;

    public XPBankBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.XP_BANK, pos, state);
    }

    @Override
    public void markDirty() {
        assert world != null;
        world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        super.markDirty();
    }

    public int getStoredXP() {
        return storedXP;
    }

    public void setStoredXP(int v) {
        storedXP = MathHelper.clamp(v, 0, MAX_STORED_XP);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("StoredXP", storedXP);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        storedXP = nbt.getInt("StoredXP");
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
