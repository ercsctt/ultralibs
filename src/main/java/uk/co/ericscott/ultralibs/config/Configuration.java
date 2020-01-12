package uk.co.ericscott.ultralibs.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Configuration {

    private File file;

    private YamlConfiguration configuration;

    private JavaPlugin plugin;

    public Configuration(JavaPlugin plugin, String name) {

        this.plugin = plugin;

        file = new File(plugin.getDataFolder(), name + ".yml");

        if (!file.getParentFile().exists()) {

            file.getParentFile().mkdir();

        }

        plugin.saveResource(name + ".yml", false);

        configuration = YamlConfiguration.loadConfiguration(file);

    }

    public void reload(){

        configuration = YamlConfiguration.loadConfiguration(file);

    }

    /**
     * Gets a double from the configuration
     *
     * @param path the string path
     * @return the double value
     */
    public double getDouble(String path) {

        if (configuration.contains(path)) {

            return configuration.getDouble(path);

        }

        return 0;
    }

    /**
     * Gets an integer from the configuration
     * @param path the string path
     * @return the integer value
     */
    public int getInt(String path) {

        if (configuration.contains(path)) {

            return configuration.getInt(path);

        }

        return 0;
    }

    /**
     * Gets a boolean from the configuration
     * @param path the string path
     * @return the boolean value
     */
    public boolean getBoolean(String path) {

        if (configuration.contains(path)) {

            return configuration.getBoolean(path);

        }

        return false;
    }

    /**
     * Gets a string from the configuration
     * @param path the string path
     * @return the string value
     */
    public String getString(String path) {

        if (configuration.contains(path)) {

            return ChatColor.translateAlternateColorCodes('&', configuration.getString(path));

        }

        return "ERROR: STRING NOT FOUND";
    }

    /**
     * Gets a string list from the configuration
     * @param path the string path
     * @return the string list value
     */
    public List<String> getStringList(String path) {

        if (configuration.contains(path)) {

            ArrayList<String> strings = new ArrayList<>();

            for (String string : configuration.getStringList(path)) {

                strings.add(ChatColor.translateAlternateColorCodes('&', string));

            }

            return strings;

        }

        return Arrays.asList("ERROR: STRING LIST NOT FOUND!");
    }

    /**
     * Gets a configurationsection from the configuration
     * @param path the string path
     * @return the configuration section
     */
    public ConfigurationSection getConfigurationSection(String path) {
        if (configuration.getConfigurationSection(path) == null) {
            return null;
        } else {
            return configuration.getConfigurationSection(path);
        }
    }

    /**
     * Gets a string and changes the ampersand symbols into chatcolours
     * @param path the string path
     * @return the translated string
     */
    public String translateString(String path) {

        if (configuration.contains(path)) {
            return ChatColor.translateAlternateColorCodes('&', getString(path));
        }

        return "ERROR: STRING NOT FOUND";

    }

    /**
     * Sets data to the configuration
     * @param path the string path
     * @param value the object value
     */
    public void set(String path, Object value) {
        configuration.set(path, value);
    }

}