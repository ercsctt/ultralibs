package uk.co.ericscott.ultralibs.location;

/*
This class stores all relevent location information, nothing else is needed but can return a Location object through "get()"
 */

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LazyLocation {

    private Location location = null;
    private String worldName;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    public LazyLocation(Location loc) {
        setLocation(loc);
    }

    public LazyLocation(final String worldName, final double x, final double y, final double z) {
        this(worldName, x, y, z, 0, 0);
    }

    public LazyLocation(final String worldName, final double x, final double y, final double z, final float yaw, final float pitch) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @java.beans.ConstructorProperties({"location", "worldName", "x", "y", "z", "pitch", "yaw"})
    public LazyLocation(Location location, String worldName, double x, double y, double z, float pitch, float yaw) {
        this.location = location;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    private Location get() {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return null;
        }

        return new Location(world, x, y, z, yaw, pitch);
    }

    public String serialize() {
        return x + "," + y + "," + z + "," + worldName + "," + pitch + "," + yaw;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getWorldName() {
        return this.worldName;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
