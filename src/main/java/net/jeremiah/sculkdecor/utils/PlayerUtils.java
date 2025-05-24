package net.jeremiah.sculkdecor.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@SuppressWarnings("unused")
public class PlayerUtils {
    public static UUID getPlayerUUID(@NotNull PlayerEntity plr) {
        return plr.getGameProfile().getId();
    }

    public static boolean playerMatch(PlayerEntity plr, UUID uuid) {
        return getPlayerUUID(plr).equals(uuid);
    }

    public static boolean playerMatch(UUID uuid, PlayerEntity plr) {
        return playerMatch(plr, uuid);
    }

    public static boolean playerMatch(PlayerEntity plr, GameProfile gp) {
        return playerMatch(plr, gp != null ? gp.getId() : null);
    }

    public static boolean playerMatch(GameProfile gp, PlayerEntity plr) {
        return playerMatch(plr, gp);
    }

    public static boolean playerMatch(PlayerEntity plr, PlayerEntity plr2) {
        return playerMatch(plr, plr2 != null ? plr2.getGameProfile() : null);
    }
}
