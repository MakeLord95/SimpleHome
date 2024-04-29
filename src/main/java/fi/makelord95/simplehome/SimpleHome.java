package fi.makelord95.simplehome;

import fi.makelord95.simplehome.commands.*;
import fi.makelord95.simplehome.database.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.Objects;

public class SimpleHome extends JavaPlugin {

    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        getLogger().info("Beep boop. SimpleHome has now been enabled.");

        File pluginFolder = getDataFolder();
        this.saveDefaultConfig();

        if (!pluginFolder.exists()) {
            if (!pluginFolder.mkdir()) {
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }


        try {
            databaseManager = new DatabaseManager(getDataFolder() + "/homes.db");
        } catch (SQLException e) {
            getLogger().info("Failed to initialize database: " + e.getMessage());
        }

        Objects.requireNonNull(getCommand("home")).setExecutor(new HomeCommand(databaseManager));
        Objects.requireNonNull(getCommand("sethome")).setExecutor(new SetHomeCommand(databaseManager, this));
        Objects.requireNonNull(getCommand("homes")).setExecutor(new HomesCommand(databaseManager));
        Objects.requireNonNull(getCommand("delhome")).setExecutor(new DelHomeCommand(databaseManager));

        Objects.requireNonNull(getCommand("home")).setTabCompleter(new TabCompleter(databaseManager));
        Objects.requireNonNull(getCommand("sethome")).setTabCompleter(new TabCompleter(databaseManager));
        Objects.requireNonNull(getCommand("homes")).setTabCompleter(new TabCompleter(databaseManager));
        Objects.requireNonNull(getCommand("delhome")).setTabCompleter(new TabCompleter(databaseManager));
    }

    @Override
    public void onDisable() {
        getLogger().info("SimpleHome disabled");

        try {
            databaseManager.close();
        } catch (SQLException e) {
            getLogger().info("Failed to close database: " + e.getMessage());
        }
    }
}
