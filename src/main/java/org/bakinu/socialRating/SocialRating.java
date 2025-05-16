package org.bakinu.socialRating;

import com.destroystokyo.paper.utils.PaperPluginLogger;
import org.bakinu.socialRating.command.CommandGetRating;
import org.bakinu.socialRating.command.CommandSetRating;
import org.bakinu.socialRating.database.Database;
import org.bakinu.socialRating.database.UserDAO;
import org.bakinu.socialRating.event.PlayerJoinListener;
import org.bakinu.socialRating.service.Config;
import org.bukkit.plugin.java.JavaPlugin;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;

public final class SocialRating extends JavaPlugin {
    private Database database;

    @Override
    public void onEnable() {
        database = new Database(this.getDataFolder().toPath(), this.getResource("sqlite.sql"));

        try {
            database.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UserDAO userDAO = new UserDAO(database, (PaperPluginLogger) this.getLogger());

        Config config = new Config();
        config.reload(this.getDataFolder().toPath().resolve("config.yml"));

        PlayerJoinListener playerJoinListener = new PlayerJoinListener(userDAO);
        getServer().getPluginManager().registerEvents(playerJoinListener, this);

        CommandGetRating commandGetRating = new CommandGetRating();
        this.getCommand("getrating").setExecutor(commandGetRating);

        CommandSetRating commandSetRating = new CommandSetRating(userDAO);
        this.getCommand("setrating").setExecutor(commandSetRating);
    }

    @Override
    public void onDisable() {
        database.disconnect();
    }
}