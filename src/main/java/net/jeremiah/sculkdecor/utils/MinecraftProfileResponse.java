package net.jeremiah.sculkdecor.utils;

import com.mojang.authlib.yggdrasil.response.Response;

import java.util.UUID;

public class MinecraftProfileResponse extends Response {
    private UUID id;
    private String name;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
