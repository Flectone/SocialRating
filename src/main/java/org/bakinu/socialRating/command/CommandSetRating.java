package org.bakinu.socialRating.command;

import org.apache.commons.lang3.StringUtils;
import org.bakinu.socialRating.database.UserDAO;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

public class CommandSetRating implements CommandExecutor {
    private final UserDAO userDAO;
    public CommandSetRating(UserDAO userDAO) {
        this.userDAO = userDAO;
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
            e.printStackTrace();
            return false;
        }

        int finalRating = userDAO.getRating(player) + integer;
        userDAO.update(player, finalRating);

        return true;
    }
}
