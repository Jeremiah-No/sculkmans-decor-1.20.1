package net.jeremiah.sculkdecor.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public final class CameraStunHandler {

    private static int stunTicks = 0;
    private static float lockedYaw;
    private static float lockedPitch;

    public static void stun(int ticks) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        stunTicks = ticks;
        lockedYaw = client.player.getYaw();
        lockedPitch = client.player.getPitch();
    }
    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (stunTicks <= 0 || client.player == null) return;

            client.player.setYaw(lockedYaw);
            client.player.setPitch(lockedPitch);
            client.player.prevYaw = lockedYaw;
            client.player.prevPitch = lockedPitch;

            stunTicks--;
        });
    }
}
