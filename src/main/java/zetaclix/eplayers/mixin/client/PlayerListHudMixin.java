package zetaclix.eplayers.mixin.client;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

import static zetaclix.eplayers.EliminatedPlayers.playerBanned;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
    @ModifyReturnValue(
            method = "collectPlayerEntries",
            at = @At("RETURN"))
    private List<PlayerListEntry> eplayers$modifyDeathMessage(List<PlayerListEntry> original) {
        var copy = new ArrayList<>(original);
        copy.removeIf((entry) -> playerBanned(entry.getProfile().getId()));
        return ImmutableList.copyOf(copy);
    }
}