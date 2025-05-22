package org.bakinu.socialRating.database;

import com.destroystokyo.paper.utils.PaperPluginLogger;
import org.bakinu.socialRating.service.Config;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.*;

public class UserDAO {
    private final Database database;
    private final PaperPluginLogger paperPluginLogger;
    private final Config config;

    public UserDAO(Database database, PaperPluginLogger paperPluginLogger, Config config) {
        this.database = database;
        this.paperPluginLogger = paperPluginLogger;
        this.config = config;
    }

    public void add(Player player) {
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT OR IGNORE INTO `users` (`uuid`, `rating`) VALUES (?, ?)");
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setInt(2, config.getBaseRating());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<UUID, Integer> getHighestRatings(int limit) {
        Map<UUID, Integer> map = new LinkedHashMap<>();

        try (Connection connection = database.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT uuid, rating FROM users ORDER BY rating DESC LIMIT ?");
            preparedStatement.setInt(1, limit);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                map.put(UUID.fromString(resultSet.getString("uuid")), resultSet.getInt("rating"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

    public void update(Player player, int value) {
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `users` SET `rating` = ? WHERE `uuid` = ?");
            preparedStatement.setInt(1, value);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public int getRating(Player player) {
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT rating FROM `users` WHERE `uuid` = ?");
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
