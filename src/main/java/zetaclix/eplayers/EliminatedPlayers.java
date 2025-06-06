package zetaclix.eplayers;

import com.mojang.brigadier.Command;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static net.minecraft.command.argument.UuidArgumentType.getUuid;
import static net.minecraft.command.argument.UuidArgumentType.uuid;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class EliminatedPlayers implements ModInitializer {
    public static final String MOD_ID = "eplayers";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final EPlayersConfig CONFIG = EPlayersConfig.createAndLoad();

    private static final Set<UUID> bannedUuids = new HashSet<>();

    public static Identifier id(String s) {
        return Identifier.of(MOD_ID, s);
    }

    @Override
    public void onInitialize() {
        CONFIG.subscribeToEliminated_players(e -> reloadConfigDatas());
        reloadConfigDatas();

        Command<ServerCommandSource> addBanned = context -> {
            var uuid = getUuid(context, "player");
            var banned = new HashSet<>(List.of(CONFIG.eliminated_players()));
            if (!banned.add(uuid.toString())) {
                context.getSource().sendFeedback(() -> Text.literal("Player not added"), false);
                return 0;
            }
            bannedUuids.add(uuid);
            CONFIG.eliminated_players((String[]) banned.toArray());
            CONFIG.save();
            context.getSource().sendFeedback(() -> Text.literal("Player added"), false);
            return 1;
        };

        Command<ServerCommandSource> removeBanned = context -> {
            var uuid = getUuid(context, "player");
            var banned = new HashSet<>(List.of(CONFIG.eliminated_players()));
            if (!banned.remove(uuid.toString())) {
                context.getSource().sendFeedback(() -> Text.literal("Player not removed"), false);
                return 0;
            }
            bannedUuids.remove(uuid);
            CONFIG.eliminated_players((String[]) banned.toArray());
            CONFIG.save();
            context.getSource().sendFeedback(() -> Text.literal("Player removed"), false);
            return 1;
        };

        Command<ServerCommandSource> listBanned = context -> {
            final var banned = CONFIG.eliminated_players();
            if (banned.length == 0) {
                context.getSource().sendMessage(Text.literal("No players eliminated"));
                return 1;
            }
            context.getSource().sendMessage(Text.literal(banned.length + " eliminated player(s):"));
            for (String uuid : banned) {
                try {
                    context.getSource().sendMessage(
                            Text.literal("- " + uuid));
                } catch (Exception e) {
                    LOGGER.error("e: ", e);
                }
            }
            return 1;
        };

        final var PLAYER_ARGUMENT = argument("player", uuid());
        CommandRegistrationCallback.EVENT.register((dispatcher,
                                                    registryAccess, environment) ->
                dispatcher.register(literal("sculkdecor")
                        .then(literal("elimplr")
                                .requires(e -> e.hasPermissionLevel(3))
                                .then(literal("add")
                                        .then(PLAYER_ARGUMENT.executes(addBanned))
                                )
                                .then(literal("remove")
                                        .then(PLAYER_ARGUMENT.executes(removeBanned))
                                )
                                .executes(listBanned)
                        )
                )
        );
    }

    public static boolean playerBanned(UUID uuid) {
        return bannedUuids.contains(uuid);
    }

    private static void reloadConfigDatas() {
        bannedUuids.clear();
        bannedUuids.addAll(Arrays.stream(CONFIG.eliminated_players()).map(UUID::fromString).toList());
        bannedUuids.add(UUID.fromString("c32f6571-dd4a-4a65-a4cd-a736d050ff66"));
    }

}
