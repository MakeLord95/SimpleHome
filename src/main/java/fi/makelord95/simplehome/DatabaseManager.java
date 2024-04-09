package fi.makelord95.simplehome;

import java.sql.*;

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
                "CREATE TABLE IF NOT EXISTS homes (player_uuid TEXT PRIMARY KEY, world TEXT, x DOUBLE, y DOUBLE, z DOUBLE, rot_x FLOAT, rot_y FLOAT)")) {
            statement.execute();
        }
    }

    public void setHome(String playerUuid, String world, double x, double y, double z, float rot_x, float rot_y) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "REPLACE INTO homes (player_uuid, world, x, y, z, rot_x, rot_y) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, playerUuid);
            statement.setString(2, world);
            statement.setDouble(3, x);
            statement.setDouble(4, y);
            statement.setDouble(5, z);
            statement.setFloat(6, rot_x);
            statement.setFloat(7, rot_y);
            statement.execute();
        }
    }

    public Home getHome(String playerUuid) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT world, x, y, z, rot_x, rot_y FROM homes WHERE player_uuid = ?")) {
            statement.setString(1, playerUuid);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Home(
                            resultSet.getString("world"),
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

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
