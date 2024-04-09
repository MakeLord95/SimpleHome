package fi.makelord95.simplehome;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetHomeCommand implements CommandExecutor {

    private final DatabaseManager databaseManager;

    public SetHomeCommand(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;

        if (player.getWorld().getEnvironment() != World.Environment.NORMAL) {
            commandSender.sendMessage("You can only set home in the overworld.");
            return true;
        }

        try {
            databaseManager.setHome(
                    player.getUniqueId().toString(),
                    player.getWorld().getName(),
                    player.getLocation().getBlockX(),
                    player.getLocation().getBlockY(),
                    player.getLocation().getBlockZ(),
                    player.getLocation().getYaw(),
                    player.getLocation().getPitch());

            commandSender.sendMessage("Home set!");

        } catch (Exception e) {
            commandSender.sendMessage("Failed to set home" + e.getMessage());
        }

        return true;
    }
}
