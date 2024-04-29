package fi.makelord95.simplehome.commands;

import fi.makelord95.simplehome.database.DatabaseManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    private final DatabaseManager databaseManager;

    public TabCompleter(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            return null;
        }

        if (command.getName().equalsIgnoreCase("home") || command.getName().equalsIgnoreCase("delhome")) {
            if (strings.length == 1) {
                try {
                    return databaseManager.getHomes(player.getUniqueId().toString());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (command.getName().equalsIgnoreCase("sethome") || command.getName().equalsIgnoreCase("homes")) {
            return new ArrayList<>();
        }
        return null;
    }
}
