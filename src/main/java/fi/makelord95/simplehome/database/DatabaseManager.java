package fi.makelord95.simplehome.database;

import fi.makelord95.simplehome.models.Home;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



public class DatabaseManager {

    private final Connection connection;

    public DatabaseManager(String databasePath) throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
            createTables();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTables() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS homes (home_uuid TEXT PRIMARY KEY, player_uuid TEXT, home_name TEXT, " +
                        "world_name TEXT, x DOUBLE, y DOUBLE, z DOUBLE, rot_x FLOAT, rot_y FLOAT)")) {
            statement.execute();
        }
    }

    public void setHome(String homeName, String playerUuid, String worldName, double x, double y, double z, float rot_x, float rot_y) throws SQLException {
        String homeUuid = new UUID(playerUuid.hashCode(), System.currentTimeMillis()).toString();

        try (PreparedStatement statement = connection.prepareStatement(
                "REPLACE INTO homes (home_uuid, player_uuid, home_name, world_name, x, y, z, rot_x, rot_y) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, homeUuid);
            statement.setString(2, playerUuid);
            statement.setString(3, homeName);
            statement.setString(4, worldName);
            statement.setDouble(5, x);
            statement.setDouble(6, y);
            statement.setDouble(7, z);
            statement.setFloat(8, rot_x);
            statement.setFloat(9, rot_y);
            statement.execute();
        }
    }

    public Home getHome(String playerUuid, String homeName) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT home_name, world_name, x, y, z, rot_x, rot_y FROM homes WHERE player_uuid = ? AND home_name = ?")) {
            statement.setString(1, playerUuid);
            statement.setString(2, homeName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Home(
                            resultSet.getString("home_name"),
                            resultSet.getString("world_name"),
                            resultSet.getDouble("x"),
                            resultSet.getDouble("y"),
                            resultSet.getDouble("z"),
                            resultSet.getFloat("rot_x"),
                            resultSet.getFloat("rot_y"));
                }
            }
        }

        return null;
    }

    public List<String> getHomes(String playerUuid) throws SQLException {
        List<String> homes = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT home_name FROM homes WHERE player_uuid = ?")) {
            statement.setString(1, playerUuid);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    homes.add(resultSet.getString("home_name"));
                }
            }
        }

        return homes;
    }

    public void delHome(String playerUuid, String homeName) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM homes WHERE player_uuid = ? AND home_name = ?")) {
            statement.setString(1, playerUuid);
            statement.setString(2, homeName);
            statement.execute();
        }
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
