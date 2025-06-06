package zetaclix.eplayers.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

import static zetaclix.eplayers.EliminatedPlayers.playerBanned;

@Mixin(ServerCommandSource.class)
public class ServerCommandSourceMixin {
    @Shadow
    @Final
    private MinecraftServer server;

    @ModifyReturnValue(method = "getPlayerNames", at = @At("RETURN"))
    private Collection<String> eplayers$dontGetAllPlayersArgType(Collection<String> original) {
        List<String> list = Lists.newArrayList();
        for (ServerPlayerEntity playerListEntry : this.server.getPlayerManager().getPlayerList()) {
            if (playerBanned(playerListEntry.getUuid()))
                list.add(playerListEntry.getEntityName());
        }
        original.removeAll(list);
        return original;
    }
}