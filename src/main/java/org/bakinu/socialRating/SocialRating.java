package org.bakinu.socialRating;

import com.destroystokyo.paper.utils.PaperPluginLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bakinu.socialRating.command.CommandGetRating;
import org.bakinu.socialRating.command.CommandReload;
import org.bakinu.socialRating.command.CommandSetRating;
import org.bakinu.socialRating.database.Database;
import org.bakinu.socialRating.database.UserDAO;
import org.bakinu.socialRating.event.PlayerChattedListener;
import org.bakinu.socialRating.event.PlayerCommandSendListener;
import org.bakinu.socialRating.event.PlayerJoinListener;
import org.bakinu.socialRating.service.Config;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;

public final class SocialRating extends JavaPlugin {
    private Database database;

    @Override
    public void onEnable() {
        Path projectPath = this.getDataFolder().toPath().resolve("config.yml");
        
        Config config = new Config();
        config.reload(projectPath);

        database = new Database(this.getDataFolder().toPath(), this.getResource("sqlite.sql"));

        try {
            database.connect();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        UserDAO userDAO = new UserDAO(database, (PaperPluginLogger) this.getLogger());
        MiniMessage miniMessage = MiniMessage.miniMessage();

        PlayerJoinListener playerJoinListener = new PlayerJoinListener(userDAO);
        getServer().getPluginManager().registerEvents(playerJoinListener, this);

        PlayerChattedListener playerChattedListener = new PlayerChattedListener(userDAO, config, miniMessage);
        getServer().getPluginManager().registerEvents(playerChattedListener, this);

        PlayerCommandSendListener playerCommandSendListener = new PlayerCommandSendListener(config, userDAO, miniMessage);
        getServer().getPluginManager().registerEvents(playerCommandSendListener, this);

        CommandGetRating commandGetRating = new CommandGetRating(config, userDAO, miniMessage);
        this.getCommand("getrating").setExecutor(commandGetRating);

        CommandSetRating commandSetRating = new CommandSetRating(userDAO, config, miniMessage);
        this.getCommand("setrating").setExecutor(commandSetRating);

        CommandReload commandReload = new CommandReload(database, config, miniMessage, projectPath);
        this.getCommand("srreload").setExecutor(commandReload);
    }

    @Override
    public void onDisable() {
        database.disconnect();
    }
}