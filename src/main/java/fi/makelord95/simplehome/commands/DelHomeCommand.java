package fi.makelord95.simplehome.commands;

import fi.makelord95.simplehome.database.DatabaseManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class DelHomeCommand implements CommandExecutor {
    private final DatabaseManager databaseManager;

    public DelHomeCommand(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("Only players can use this command.");
            return true;
        }

        if (strings.length != 1) {
            commandSender.sendMessage("Usage: /delhome <home name>");
            return true;
        }

        try {
            databaseManager.delHome(player.getUniqueId().toString(), strings[0]);
            commandSender.sendMessage("Home deleted.");
        } catch (SQLException e) {
            commandSender.sendMessage("Failed to delete home: " + e.getMessage());
        }
        return true;
    }
}
