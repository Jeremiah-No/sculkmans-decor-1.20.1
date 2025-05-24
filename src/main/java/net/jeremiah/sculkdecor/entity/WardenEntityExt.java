package net.jeremiah.sculkdecor.entity;

import com.mojang.authlib.GameProfile;
import org.jetbrains.annotations.Nullable;

public interface WardenEntityExt {
    @Nullable GameProfile sculkdecor$getSummoner();

    void sculkdecor$setSummoner(@Nullable GameProfile plr);

    default boolean sculkdecor$isOwned() {
        return this.sculkdecor$getSummoner() != null;
    }
}
