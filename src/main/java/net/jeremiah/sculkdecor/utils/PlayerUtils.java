package net.jeremiah.sculkdecor.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.mojang.authlib.yggdrasil.response.MinecraftProfilePropertiesResponse;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.jeremiah.sculkdecor.SculkmansDecor;
import net.jeremiah.sculkdecor.mixin.YggdrasilAuthenticationServiceAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
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

    @Environment(EnvType.CLIENT)
    public static GameProfile fillGameProfile(GameProfile gp) {
        if (gp.isComplete()) return gp;
        final var auth = ((YggdrasilMinecraftSessionService) MinecraftClient.getInstance().getSessionService()).getAuthenticationService();
        if (gp.getId() == null) {
            gp = new GameProfile(
                    getUserUUID(gp.getName()),
                    gp.getName()
            );
        } else if (gp.getName() == null) {
            try {
                final var url = HttpAuthenticationService.constantURL("https://sessionserver.mojang.com/session/minecraft/profile/" +
                        gp.getId().toString());
                final var resp = ((YggdrasilAuthenticationServiceAccessor) auth).sculkdecor$makeRequest(url, null,
                        MinecraftProfilePropertiesResponse.class, null);
                gp = new GameProfile(
                        resp.getId(),
                        resp.getName()
                );
                gp.getProperties().putAll(resp.getProperties());
            } catch (AuthenticationException e) {
                SculkmansDecor.LOGGER.error("Couldn't retreive user game profile");
                return null;
            }
        }
        gp = MinecraftClient.getInstance().getSessionService().fillProfileProperties(gp, false);
        return gp;
    }

    private static MinecraftProfileResponse getUserProfile(YggdrasilAuthenticationService auth, URL url) {
        try {
            return ((YggdrasilAuthenticationServiceAccessor) auth).sculkdecor$makeRequest(url, null, MinecraftProfileResponse.class, null);
        } catch (AuthenticationException e) {
            return null;
        }
    }

    @Environment(EnvType.CLIENT)
    public static UUID getUserUUID(String username) {
        // "https://api.mojang.com/users/profiles/minecraft/<username>",
        final var auth = ((YggdrasilMinecraftSessionService) MinecraftClient.getInstance().getSessionService()).getAuthenticationService();
        final var resp = getUserProfile(auth, HttpAuthenticationService.constantURL("https://api.mojang.com/users/profiles/minecraft/" + username));
        if (resp == null) return null;
        return resp.getId();
    }
}
