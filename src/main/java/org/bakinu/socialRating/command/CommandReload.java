package org.bakinu.socialRating.command;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bakinu.socialRating.database.Database;
import org.bakinu.socialRating.service.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;

public class CommandReload implements CommandExecutor {
    private final Database database;
    private final Config config;
    private final MiniMessage miniMessage;
    private final Path path;

    public CommandReload(Database database, Config config, MiniMessage miniMessage, Path path) {
        this.database = database;
        this.config = config;
        this.miniMessage = miniMessage;
        this.path = path;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        try {
            database.disconnect();
            database.connect();
            config.reload(path);
            commandSender.sendMessage(miniMessage.deserialize(config.getCommand().getSrreload().getMessage()));
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}
