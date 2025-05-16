package org.bakinu.socialRating.event;

import org.bakinu.socialRating.database.UserDAO;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final UserDAO userDAO;

    public PlayerJoinListener(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @EventHandler
    public void onPlayerJoined(PlayerJoinEvent playerJoinEvent) {
        userDAO.add(playerJoinEvent.getPlayer());
    }
}
