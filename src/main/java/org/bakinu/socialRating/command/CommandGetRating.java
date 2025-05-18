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

public class CommandGetRating implements CommandExecutor {
    private final Config config;
    private final UserDAO userDAO;
    private final MiniMessage miniMessage;

    public CommandGetRating(Config config, UserDAO userDAO, MiniMessage miniMessage) {
        this.config = config;
        this.userDAO = userDAO;
        this.miniMessage = miniMessage;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length < 1) return false;

        Player player = Bukkit.getPlayer(strings[0]);

        if (player == null) return false;

        int rating = userDAO.getRating(player);
        String message = config.getCommand().getGetrating().getMessage()
                .replace("{player_name}", player.getName())
                .replace("{rating}", String.valueOf(rating));

        commandSender.sendMessage(miniMessage.deserialize(message));

        return true;
    }
}
