package org.bakinu.socialRating.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bakinu.socialRating.database.UserDAO;
import org.bakinu.socialRating.service.Config;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CommandTopRating implements CommandExecutor {
    private final MiniMessage miniMessage;
    private final UserDAO userDAO;
    private final Config config;

    public CommandTopRating(MiniMessage miniMessage, UserDAO userDAO, Config config) {
        this.miniMessage = miniMessage;
        this.config = config;
        this.userDAO = userDAO;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        int limit;
        if (strings.length < 1) {
            limit = config.getCommand().getTopRating().getLimit();
        } else {
            try {
                limit = Integer.parseInt(strings[0]);
                if (limit == 0)
                    if (limit > config.getCommand().getTopRating().getLimit() || limit == 0) limit = config.getCommand().getTopRating().getLimit();
            } catch (NumberFormatException e) {
                return false;
            }
        }

        Map<UUID, Integer> map = userDAO.getHighestRatings(limit);

        String messageComponent = "";
        Set<UUID> keys = map.keySet();
        int index = 0;

        for (UUID key : keys) {
            index += 1;
            OfflinePlayer player = Bukkit.getOfflinePlayer(key);

            String message = config.getCommand().getTopRating().getMessage()
                    .replace("{index}", String.valueOf(index))
                    .replace("{player_name}", player.getName())
                    .replace("{rating}", String.valueOf(userDAO.getRating(player.getUniqueId())));
            messageComponent = messageComponent + message + "\n";
        }
        commandSender.sendMessage(miniMessage.deserialize(messageComponent));

        return true;
    }
}
