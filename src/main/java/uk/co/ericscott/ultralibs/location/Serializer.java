package uk.co.ericscott.ultralibs.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import uk.co.ericscott.ultralibs.math.MathUtil;

import java.util.ArrayList;
import java.util.List;

public class Serializer
{
    /**
     * Serializes a location to the most optimised double (two decimal places)
     *
     * @param loc - Location to be serialized
     * @return serialized location.
     */
    public static String serializeLocation(Location loc) {
        double x = MathUtil.round(loc.getX(), 2);
        double y = MathUtil.round(loc.getY(), 2);
        double z = MathUtil.round(loc.getZ(), 2);
        String world = loc.getWorld().getName();
        float pitch = loc.getPitch();
        float yaw = loc.getYaw();

        String serializedLocation = x + "," + y + "," + z + "," + world + "," + pitch + "," + yaw; //Nicely formatted w/ minimalist

        return serializedLocation;
    }

    /**
     * Quickly rebuilds location from above method.
     * MUST TAKE FORMAT FROM ABOVE
     *
     * @param serializedLocation - String of serialized location, maybe in db or config.
     * @return Rebuilt location.
     */
    public static Location rebuildLocation(String serializedLocation) {
        String[] tokens = serializedLocation.split(",");
        double x = Double.parseDouble(tokens[0]);
        double y = Double.parseDouble(tokens[1]);
        double z = Double.parseDouble(tokens[2]);
        World world = Bukkit.getWorld(tokens[3]);

        float pitch = Float.parseFloat(tokens[4]);

        float yaw = Float.parseFloat(tokens[5]);

        Location rebuiltLocation = new Location(world, x, y, z);

        rebuiltLocation.setPitch(pitch);
        rebuiltLocation.setYaw(yaw);

        return rebuiltLocation;
    }

    /**
     * Converts a list of serialized locations (usually in a config) into Locations again.
     * Found this insanely useful on the random ones.
     *
     * @param serializedLocations - List of serialized locations.
     * @return List of locations that were serialized.
     */
    public static List<Location> convertSerializedLocationList(List<String> serializedLocations) {
        List<Location> newList = new ArrayList<>();
        serializedLocations.forEach(serializedLocation ->
        {
            Location rebuiltLocation = rebuildLocation(serializedLocation);
            newList.add(rebuiltLocation);
        });
        return newList;
    }
}
