package org.bakinu.socialRating.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.*;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class Database {
    private final Path projectPath;
    private final InputStream SQLFile;

    private HikariDataSource dataSource;

    public Database(Path projectPath, InputStream SQLFile) {
        this.projectPath = projectPath;
        this.SQLFile = SQLFile;
    }

    public void connect() throws SQLException, IOException {
        HikariConfig hikariConfig = createHikaryConfig();

        try {
            dataSource = new HikariDataSource(hikariConfig);
        } catch (HikariPool.PoolInitializationException e) {
            throw new RuntimeException(e);
        }

        try (Connection ignored = getConnection()){
            executeFile(SQLFile);
        }
    }

    @NotNull
    public Connection getConnection() throws SQLException {
        if (dataSource == null) throw new SQLException("Not initialized");

        return dataSource.getConnection();
    }

    public void disconnect() {
        if (dataSource != null) {
            dataSource.getHikariPoolMXBean().softEvictConnections();
            dataSource.close();
        }
    }

    private HikariConfig createHikaryConfig() {
        HikariConfig hikariConfig = new HikariConfig();

        String connectionURL = "jdbc:sqlite:";
            connectionURL = connectionURL +
                projectPath.toString() +
                File.separator +
                "database.db";

        hikariConfig.setMaximumPoolSize(5);
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setConnectionTimeout(30000);
        hikariConfig.addDataSourceProperty("busy_timeout", 30000);
        hikariConfig.addDataSourceProperty("journal_mode", "WAL");
        hikariConfig.addDataSourceProperty("synchronous", "NORMAL");
        hikariConfig.addDataSourceProperty("journal_size_limit", "6144000");


        hikariConfig.setJdbcUrl(connectionURL);
        hikariConfig.setPoolName("RatingDatabase");

        return hikariConfig;
    }

    private void executeFile(InputStream inputStream) throws SQLException, IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            StringBuilder builder = new StringBuilder();

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("--")) continue;

                builder.append(line);

                if (line.endsWith(";")) {
                    statement.execute(builder.toString());
                    builder.setLength(0);
                }
            }
        }
    }
}