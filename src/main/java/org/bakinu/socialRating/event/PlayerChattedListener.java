package org.bakinu.socialRating.event;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bakinu.socialRating.database.UserDAO;
import org.bakinu.socialRating.service.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChattedListener implements Listener {
    private final UserDAO userDAO;
    private final Config config;
    private final MiniMessage miniMessage;

    public PlayerChattedListener(UserDAO userDAO, Config config, MiniMessage miniMessage) {
        this.userDAO = userDAO;
        this.config = config;
        this.miniMessage = miniMessage;
    }

    @EventHandler
    private void onPlayerChatted(AsyncChatEvent asyncChatEvent) {
        Player player = asyncChatEvent.getPlayer();

        if (!config.getLowRatingEvents().getMuteChat().isEnabled()) return;

        if (userDAO.getRating(player) < config.getLowRatingEvents().getMuteChat().getRating()) {
            player.sendMessage(miniMessage.deserialize(config.getLowRatingEvents().getMuteChat().getMessage()));
            asyncChatEvent.setCancelled(true);
        }
    }
}