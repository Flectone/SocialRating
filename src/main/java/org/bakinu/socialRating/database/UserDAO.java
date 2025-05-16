package org.bakinu.socialRating.database;

import com.destroystokyo.paper.utils.PaperPluginLogger;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private final Database database;
    private final PaperPluginLogger paperPluginLogger;

    public UserDAO(Database database, PaperPluginLogger paperPluginLogger) {
        this.database = database;
        this.paperPluginLogger = paperPluginLogger;
    }

    public void add(Player player) {
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT OR IGNORE INTO `users` (`uuid`) VALUES (?)");
            preparedStatement.setString(1, player.getUniqueId().toString());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
