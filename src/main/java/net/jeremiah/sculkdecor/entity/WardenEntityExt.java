package net.jeremiah.sculkdecor.entity;

import com.mojang.authlib.GameProfile;
import org.jetbrains.annotations.Nullable;

public interface WardenEntityExt {
    @Nullable GameProfile sculkmans_decor$getSummoner();

    void sculkmans_decor$setSummoner(@Nullable GameProfile plr);
}
