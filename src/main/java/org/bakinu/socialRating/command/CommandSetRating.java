package org.bakinu.socialRating.command;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bakinu.socialRating.database.UserDAO;
import org.bakinu.socialRating.service.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandSetRating implements CommandExecutor {
    private final UserDAO userDAO;
    private final Config config;
    private final MiniMessage miniMessage;

    public CommandSetRating(UserDAO userDAO, Config config, MiniMessage miniMessage) {
        this.userDAO = userDAO;
        this.config = config;
        this.miniMessage = miniMessage;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length < 2) return false;

        Player player = Bukkit.getPlayer(strings[0]);
        if (player == null) return false;

        String value = strings[1];

        int integer;

        try {
            integer = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }

        int finalRating = userDAO.getRating(player.getUniqueId()) + integer;
        userDAO.update(player, finalRating);

        String message = config.getCommand().getSetrating().getMessage()
                .replace("{player_name}", player.getName())
                .replace("{value}", value)
                .replace("{rating}", String.valueOf(finalRating));

        commandSender.sendMessage(miniMessage.deserialize(message));

        return true;
    }
}