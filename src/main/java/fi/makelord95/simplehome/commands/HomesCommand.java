package fi.makelord95.simplehome.commands;

import fi.makelord95.simplehome.database.DatabaseManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

public class HomesCommand implements CommandExecutor {
    private final DatabaseManager databaseManager;

    public HomesCommand(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("Only players can use this command.");
            return true;
        }

        try {
            List<String> homes = databaseManager.getHomes(player.getUniqueId().toString());

            commandSender.sendMessage("Your homes:");

            for (String homeName : homes) {
                commandSender.sendMessage(" - " + homeName);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
