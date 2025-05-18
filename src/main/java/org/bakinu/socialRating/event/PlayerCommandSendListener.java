package org.bakinu.socialRating.event;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bakinu.socialRating.database.UserDAO;
import org.bakinu.socialRating.service.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandSendListener implements Listener {
    private final Config config;
    private final UserDAO userDAO;
    private final MiniMessage miniMessage;

    public PlayerCommandSendListener(Config config, UserDAO userDAO, MiniMessage miniMessage) {
        this.config = config;
        this.userDAO = userDAO;
        this.miniMessage = miniMessage;
    }

    @EventHandler
    public void onCommandUsed(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!config.getLowRatingEvents().getMuteCommands().isEnabled()) return;

        if (userDAO.getRating(player) >= config.getLowRatingEvents().getMuteCommands().getRating()) return;

        String message = event.getMessage();
        String command = message.split(" ")[0];

        if (!config.getLowRatingEvents().getMuteCommands().getBlockedCommands().contains(command)) return;
        player.sendMessage(miniMessage.deserialize(config.getLowRatingEvents().getMuteCommands().getMessage()));
        event.setCancelled(true);
    }
}