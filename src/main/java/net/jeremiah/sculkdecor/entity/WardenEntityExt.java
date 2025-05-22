package net.jeremiah.sculkdecor.entity;

import com.mojang.authlib.GameProfile;
import org.jetbrains.annotations.Nullable;

public interface WardenEntityExt {
    @Nullable GameProfile getSummoner();
    void setSummoner(@Nullable GameProfile plr);
}
