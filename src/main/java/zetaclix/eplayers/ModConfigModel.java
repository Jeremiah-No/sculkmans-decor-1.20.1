package zetaclix.eplayers;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Hook;
import io.wispforest.owo.config.annotation.Sync;

@Config(name = "eliminated-players", wrapperName = "EPlayersConfig")
public class ModConfigModel {
    @Hook
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public String[] eliminated_players = new String[0];
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public boolean override_skin = true;
}
