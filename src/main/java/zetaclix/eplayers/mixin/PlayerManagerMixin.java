package zetaclix.eplayers.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static zetaclix.eplayers.EliminatedPlayers.playerBanned;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
    @Shadow
    @Final
    private List<ServerPlayerEntity> players;

    @Shadow public abstract List<ServerPlayerEntity> getPlayerList();

    @Shadow @Final private MinecraftServer server;

    @Inject(method="checkCanJoin",at=@At("HEAD"), cancellable = true)
    private void eplayers$checkCanJoin(SocketAddress address, GameProfile profile, CallbackInfoReturnable<Text> cir) {
        if (playerBanned(profile.getId())) {
            cir.setReturnValue(null);
        }
    }

    @WrapWithCondition(
            method = "onPlayerConnect",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Z)V"))
    private boolean eplayers$playerLeave(PlayerManager manager, Text text, boolean bl, ClientConnection connect, ServerPlayerEntity player) {
        return !playerBanned(player.getUuid());
    }

    @Inject(method = "broadcast(Lnet/minecraft/text/Text;Ljava/util/function/Function;Z)V", at = @At("HEAD"), cancellable = true)
    private void eplayers$actuallyDontBroadcast(Text message, Function<ServerPlayerEntity, Text> playerMessageFactory, boolean bl, CallbackInfo ci) {
        if (message.getContent() instanceof TranslatableTextContent transCon) {
            String Key = transCon.getKey();
            Optional<Object> texts = Arrays.stream(transCon.getArgs()).filter(obj -> obj instanceof Text text).findFirst();
            if (Key.equals("multiplayer.player.left") && texts.isPresent() && ((Text) texts.get()).getString().contains("RemovedPlayer")) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "broadcast(Lnet/minecraft/network/message/SignedMessage;Ljava/util/function/Predicate;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/message/MessageType$Parameters;)V", at = @At("HEAD"), cancellable = true)
    private void eplayers$noBroadcasty(SignedMessage message, Predicate<ServerPlayerEntity> shouldSendFiltered, ServerPlayerEntity sender, MessageType.Parameters params, CallbackInfo ci) {
        if (message.getContent().getString().startsWith("/")) return;
        if (sender == null) return;
        if (playerBanned(sender.getUuid())) {
            ci.cancel();
        }
    }

    @Environment(EnvType.SERVER)
    @ModifyReturnValue(method = "getPlayerList", at=@At("RETURN"))
    private List<ServerPlayerEntity> eplayers$getPlayerList(List<ServerPlayerEntity> original) {
        original.removeIf(e -> playerBanned(e.getUuid()));
        return original;
    }

    @ModifyReturnValue(method = "getPlayerNames", at = @At("RETURN"))
    private String[] eplayers$dontGetAllPlayersArgType(String[] original) {
        for (int i = 0; i < this.players.size(); ++i) {
            if (!playerBanned(this.players.get(i).getGameProfile().getId())) {
                original[i] = this.players.get(i).getGameProfile().getName();
            }
        }
        return original;
    }
}