package zetaclix.eplayers.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;

import static net.minecraft.command.argument.EntityArgumentType.PLAYER_NOT_FOUND_EXCEPTION;
import static zetaclix.eplayers.EliminatedPlayers.playerBanned;

@Mixin(EntityArgumentType.class)
public class EntityArgumentTypeMixin {
    @ModifyReturnValue(method = "getPlayers", at = @At("RETURN"))
    private static Collection<ServerPlayerEntity> eplayers$dontGetAllPlayersArgType(Collection<ServerPlayerEntity> original)
            throws CommandSyntaxException {
        original.removeIf((serverPlayerEntity -> playerBanned(serverPlayerEntity.getUuid())));
        if (original.isEmpty()) {
            throw PLAYER_NOT_FOUND_EXCEPTION.create();
        }
        return original;
    }
}