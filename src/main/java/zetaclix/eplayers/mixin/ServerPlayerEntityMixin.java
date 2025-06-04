package zetaclix.eplayers.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static zetaclix.eplayers.EliminatedPlayers.playerBanned;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    public ServerPlayerEntityMixin(World world, BlockPos blockPos, float f, GameProfile gameProfile, @Nullable PlayerPublicKey playerPublicKey) {
        super(world, blockPos, f, gameProfile);
    }

    @Inject(method = "sendMessage(Lnet/minecraft/text/Text;Z)V", at = @At("HEAD"), cancellable = true)
    private void eplayers$dontSendMessage(Text message, boolean bl, CallbackInfo ci) {
        if (playerBanned(this.getUuid())) {
            ci.cancel();
        }
    }


}
