package fi.makelord95.simplehome.commands;

import fi.makelord95.simplehome.SimpleHome;
import fi.makelord95.simplehome.database.DatabaseManager;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class SetHomeCommand implements CommandExecutor {

    private final DatabaseManager databaseManager;
    private final SimpleHome plugin;

    public SetHomeCommand(DatabaseManager databaseManager, SimpleHome plugin) {
        this.databaseManager = databaseManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("Only players can use this command.");
            return true;
        }

        if (strings.length != 1) {
            commandSender.sendMessage("Usage: /sethome <home name>");
            return true;
        }

        String homeName = strings[0];

        if (player.getWorld().getEnvironment() != World.Environment.NORMAL) {
            commandSender.sendMessage("You can only set home in the overworld.");
            return true;
        }

        try {
            if (databaseManager.getHomes(player.getUniqueId().toString()).size() >= this.plugin.getConfig().getInt("max-homes")) {
                commandSender.sendMessage("You can only have 5 homes.");
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            if (databaseManager.getHome(player.getUniqueId().toString(), homeName) != null) {
                commandSender.sendMessage("You already have a home with that name.");
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String playerUuid = player.getUniqueId().toString();
        String worldName = player.getWorld().getName();
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();
        float rot_x = player.getLocation().getYaw();
        float rot_y = player.getLocation().getPitch();

        rot_x = Math.round(rot_x / 90) * 90;
        rot_y = Math.round(rot_y / 90) * 90;

        try {
            databaseManager.setHome(homeName, playerUuid, worldName, x, y, z, rot_x, rot_y);

            commandSender.sendMessage("Home set!");

        } catch (Exception e) {
            commandSender.sendMessage("Failed to set home" + e.getMessage());
        }

        return true;
    }
}
