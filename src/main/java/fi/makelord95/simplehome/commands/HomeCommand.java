package fi.makelord95.simplehome.commands;

import fi.makelord95.simplehome.database.DatabaseManager;
import fi.makelord95.simplehome.models.Home;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HomeCommand implements CommandExecutor {

    private final DatabaseManager databaseManager;

    public HomeCommand(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("Only players can use this command.");
            return true;
        }

        if (strings.length != 1) {
            commandSender.sendMessage("Usage: /home <home name>");
            return true;
        }

        String homeName = strings[0];

        try {
            Home home = databaseManager.getHome(player.getUniqueId().toString(), homeName);

            if (home == null) {
                commandSender.sendMessage("You don't have a home set.");
                return true;
            }

            player.teleport(home.getLocation());

            commandSender.sendMessage("Welcome home!");

        } catch (Exception e) {
            commandSender.sendMessage("Failed to teleport home" + e.getMessage());
        }
        return true;
    }
}
