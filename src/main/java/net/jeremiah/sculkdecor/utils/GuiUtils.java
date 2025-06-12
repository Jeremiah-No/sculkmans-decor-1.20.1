package net.jeremiah.sculkdecor.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import java.lang.reflect.InvocationTargetException;

public class GuiUtils {
    @Environment(EnvType.CLIENT)
    public static <T extends Screen> void setClientScreen(Class<T> clazz, Object... args) {
        final var client = MinecraftClient.getInstance();
        final var argClasses = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argClasses[i] = args[i].getClass();
        }
        try {
            client.setScreen(clazz.getDeclaredConstructor(argClasses).newInstance(args));
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void resetClientScreen() {
        final var client = MinecraftClient.getInstance();
        client.execute(() -> client.setScreen(null));
    }
}
