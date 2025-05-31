package net.jeremiah.sculkdecor.entity;

import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.jeremiah.sculkdecor.SculkmansDecor;
import net.jeremiah.sculkdecor.registry.ModBlockEntityTypes;
import net.jeremiah.sculkdecor.utils.SonicBoomUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public final class SonicBoomGeneratorBlockEntity extends BlockEntity {
    public static final Identifier NETWORK_CHANNEL = SculkmansDecor.id("sonic_boom_generator");

    private static final int RANGE = 15;
    private static final float COOLDOWN = 2;

    private final Set<GameProfile> ignored = new HashSet<>();
    private GameProfile owner = null;
    private int cooldown = 0;

    public SonicBoomGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.SONIC_BOOM_GENERATOR, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("Cooldown", cooldown);
        if (owner != null) {
            NbtCompound nbtOwner = new NbtCompound();
            NbtHelper.writeGameProfile(nbtOwner, owner);
            nbt.put("Owner", nbtOwner);
        }
        NbtList nbtIgnored = new NbtList();
        for (GameProfile gp : ignored) {
            NbtCompound nbtGp = new NbtCompound();
            NbtHelper.writeGameProfile(nbtGp, gp);
            nbtIgnored.add(nbtGp);
        }
        nbt.put("Ignored", nbtIgnored);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        cooldown = nbt.getInt("Cooldown");
        owner = null;
        ignored.clear();

        var nbtOwner = nbt.getCompound("Owner");
        if (nbtOwner != null) {
            owner = NbtHelper.toGameProfile(nbtOwner);
        }
        var nbtIgnored = nbt.getList("Ignored", NbtElement.COMPOUND_TYPE);
        for (NbtElement elem : nbtIgnored) {
            ignored.add(NbtHelper.toGameProfile((NbtCompound) elem));
        }
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient() || owner == null) return;
        if (cooldown != 0) {
            cooldown--;
            return;
        }

        final var plr = world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), RANGE, e -> e.canHit() &&
                !e.isSpectator() &&
                !((PlayerEntity) e).getAbilities().creativeMode &&
                !canInteract(((PlayerEntity) e).getGameProfile()));
        if (plr == null) return;
        final var origin = pos.toCenterPos();
        SonicBoomUtils.create((ServerWorld) world, origin, plr, plr.getPos().subtract(origin));
        cooldown = (int) (20 * COOLDOWN);
    }

    public void addIgnoredPlayer(GameProfile plr) {
        ignored.add(plr);
        markDirty();
        assert world != null;
        world.updateListeners(pos, getCachedState(), getCachedState(), 0);
    }

    public void removeIgnoredPlayer(GameProfile plr) {
        ignored.remove(plr);
        markDirty();
        assert world != null;
        world.updateListeners(pos, getCachedState(), getCachedState(), 0);
    }

    public boolean canInteract(GameProfile gp) {
        if (owner == null || owner.equals(gp)) return true;
        // doesn't work on cracked servers ?
        for (GameProfile ignored : ignored) {
            if (ignored.equals(gp)) return true;
        }
        return false;
    }

    @Environment(EnvType.CLIENT)
    public void addIgnoredPlayerClient(GameProfile plr) {
        final var buf = PacketByteBufs.create();
        buf.writeInt(0);
        buf.writeBlockPos(this.pos);
        buf.writeGameProfile(plr);
        ClientPlayNetworking.send(NETWORK_CHANNEL, buf);
    }

    @Environment(EnvType.CLIENT)
    public void removeIgnoredPlayerClient(GameProfile plr) {
        final var buf = PacketByteBufs.create();
        buf.writeInt(1);
        buf.writeBlockPos(this.pos);
        buf.writeGameProfile(plr);
        ClientPlayNetworking.send(NETWORK_CHANNEL, buf);
    }

    public Set<GameProfile> getIgnoredPlayers() {
        return ImmutableSet.copyOf(ignored);
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public GameProfile getOwner() {
        return owner;
    }

    public void setOwner(GameProfile gp) {
        owner = gp;
        markDirty();
    }
}
