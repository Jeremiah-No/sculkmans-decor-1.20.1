package zetaclix.eplayers;

import blue.endless.jankson.Jankson;
import io.wispforest.owo.config.ConfigWrapper;
import io.wispforest.owo.config.Option;
import io.wispforest.owo.util.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EPlayersConfig extends ConfigWrapper<zetaclix.eplayers.ModConfigModel> {

    public final Keys keys = new Keys();

    private final Option<java.lang.String[]> eliminated_players = this.optionForKey(this.keys.eliminated_players);
    private final Option<java.lang.Boolean> override_skin = this.optionForKey(this.keys.override_skin);

    private EPlayersConfig() {
        super(zetaclix.eplayers.ModConfigModel.class);
    }

    private EPlayersConfig(Consumer<Jankson.Builder> janksonBuilder) {
        super(zetaclix.eplayers.ModConfigModel.class, janksonBuilder);
    }

    public static EPlayersConfig createAndLoad() {
        var wrapper = new EPlayersConfig();
        wrapper.load();
        return wrapper;
    }

    public static EPlayersConfig createAndLoad(Consumer<Jankson.Builder> janksonBuilder) {
        var wrapper = new EPlayersConfig(janksonBuilder);
        wrapper.load();
        return wrapper;
    }

    public java.lang.String[] eliminated_players() {
        return eliminated_players.value();
    }

    public void eliminated_players(java.lang.String[] value) {
        eliminated_players.set(value);
    }

    public void subscribeToEliminated_players(Consumer<java.lang.String[]> subscriber) {
        eliminated_players.observe(subscriber);
    }

    public boolean override_skin() {
        return override_skin.value();
    }

    public void override_skin(boolean value) {
        override_skin.set(value);
    }


    public static class Keys {
        public final Option.Key eliminated_players = new Option.Key("eliminated_players");
        public final Option.Key override_skin = new Option.Key("override_skin");
    }
}

