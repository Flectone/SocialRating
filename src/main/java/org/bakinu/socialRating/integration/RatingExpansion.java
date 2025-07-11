package org.bakinu.socialRating.integration;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bakinu.socialRating.SocialRating;
import org.bakinu.socialRating.database.UserDAO;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RatingExpansion extends PlaceholderExpansion {
    private final SocialRating plugin;
    private final UserDAO userDAO;

    public RatingExpansion(SocialRating plugin, UserDAO userDAO) {
        this.plugin = plugin;
        this.userDAO = userDAO;
    }

    @Override
    @NotNull
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "socialrating"; //
    }

    @Override
    @NotNull
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("player_rating")) {
            return String.valueOf(userDAO.getRating(player.getUniqueId()));
        }

        return null;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        return null;
    }
}
