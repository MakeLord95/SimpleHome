package fi.makelord95.simplehome.models;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Home {

    private final String world;
    private final double x;
    private final double y;
    private final double z;
    private final float rot_x;
    private final float rot_y;

    public Home(String name, String world, double x, double y, double z, float rotX, float rotY) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rot_x = rotX;
        this.rot_y = rotY;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, rot_x, rot_y);
    }
}
