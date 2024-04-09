package fi.makelord95.simplehome;

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
        Player player = (Player) commandSender;

        try {
            Home home = databaseManager.getHome(player.getUniqueId().toString());

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
