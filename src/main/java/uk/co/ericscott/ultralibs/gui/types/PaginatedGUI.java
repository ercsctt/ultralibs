package uk.co.ericscott.ultralibs.gui.types;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.ericscott.ultralibs.gui.buttons.GUIButton;
import uk.co.ericscott.ultralibs.gui.buttons.InventoryListener;
import uk.co.ericscott.ultralibs.gui.buttons.UnclickableButton;
import uk.co.ericscott.ultralibs.utils.ItemBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PaginatedGUI implements InventoryHolder {

    /* BEGIN: CONFIGURATION */
    private static final String CHAT_PREFIX = "&c&lGUI  &c";
    private static final String NO_PREVIOUS_PAGES = "There are no previous pages.";
    private static final String NO_ADDITIONAL_PAGES = "There are no additional pages.";
    /* END: CONFIGURATION */

    private static InventoryListener inventoryListenerGUI;
    private Map<Integer, GUIButton> items;
    private Map<Integer, GUIButton> toolbarItems;
    private int currentPage;
    private String name;

    /**
     * Creates a PaginatedGUI. This is a Spigot 'Inventory Menu' that
     * will automatically add pages to accommodate additional items.
     *
     * <br>
     * <p>
     * Color Codes are supported (and should be prepended with an
     * ampersand [&amp;]; e.g. &amp;c for red.)
     *
     * @param name The desired name of the PaginatedGUI.
     */
    public PaginatedGUI(String name) {
        items = new HashMap<>();
        toolbarItems = new HashMap<>();
        currentPage = 0;
        this.name = ChatColor.translateAlternateColorCodes('&', name);
    }

    /**
     * Simply an alias to register the Inventory listeners for a certain plugin.
     * Intended to improve code readability.
     *
     * @param plugin The Spigot plugin instance that you wish to register the listeners for.
     */
    public static void prepare(JavaPlugin plugin) {
        if (inventoryListenerGUI == null) {
            inventoryListenerGUI = new InventoryListener();
            plugin.getServer().getPluginManager().registerEvents(inventoryListenerGUI, plugin);
        }
    }

    /**
     * Gets the display name of the PaginatedGUI.
     *
     * <br>
     *
     * <b>Note:</b> If your inventory's display name contains
     * color codes, this will have substituted the
     * ampersands (&amp;)s with the rendering engine's
     * symbol (&sect;).
     *
     * @return The inventory's display name.
     */
    public String getDisplayName() {
        return name;
    }

    /**
     * Sets the display name of the PaginatedGUI.
     *
     * <br>
     * <p>
     * Color Codes are supported (and should be prepended with an
     * ampersand [&amp;]; e.g. &amp;c for red.)
     *
     * @param name The desired name of the PaginatedGUI.
     */
    public void setDisplayName(String name) {
        this.name = ChatColor.translateAlternateColorCodes('&', name);
    }

    /**
     * Adds the provided {@link GUIButton} to the PaginatedGUI.
     *
     * <br>
     *
     * <b>Note:</b> This will place the button after the highest slot.
     * So if you have buttons in slot 0, 1 and 5, this will place the
     * added button in slot 6.
     *
     * @param button The button you wish to add.
     */
    public void addButton(GUIButton button) {
        // Get the current maximum slot in the 'items' list.
        int slot = 0;
        for (int nextSlot : items.keySet()) {
            if (nextSlot > slot) {
                slot = nextSlot;
            }
        }

        // Add one to get the next maximum slot.
        if (!items.isEmpty()) {
            slot++;
        }

        // Put the button in that slot.
        items.put(slot, button);
    }

    /**
     * Adds the provided {@link GUIButton} but places it in the desired slot in the PaginatedGUI.
     *
     * @param slot   The desired slot for the button.
     * @param button The button you wish to add.
     */
    public void setButton(int slot, GUIButton button) {
        items.put(slot, button);
    }

    /**
     * Removes the {@link GUIButton} from the provided slot.
     *
     * @param slot The slot containing the button you wish to remove.
     */
    public void removeButton(int slot) {
        items.remove(slot);
    }

    /**
     * Gets the {@link GUIButton} in the provided slot.
     *
     * @param slot The slot containing the GUIButton that you wish to get.
     * @return The GUIButton in the provided slot.
     */
    public GUIButton getButton(int slot) {
        if (slot < 45) {
            return items.get(slot);
        } else {
            return toolbarItems.get(slot - 45);
        }
    }

    /**
     * Adds the provided {@link GUIButton} but places it in the desired slot in the PaginatedGUI's toolbar.
     *
     * @param slot   The desired slot for the button.
     * @param button The button you wish to add.
     * @throws IllegalArgumentException This will occur if the slot is less than 0 or higher than 8 (as this is outside the toolbar slot range.)
     */
    public void setToolbarItem(int slot, GUIButton button) {
        if (slot < 0 || slot > 8) {
            throw new IllegalArgumentException("The desired slot is outside the bounds of the toolbar slot range. [0-8]");
        }

        toolbarItems.put(slot, button);
    }

    /**
     * Removes the {@link GUIButton} from the provided slot.
     *
     * @param slot The slot containing the button you wish to remove.
     * @throws IllegalArgumentException This will occur if the slot is less than 0 or higher than 8 (as this is outside the toolbar slot range.)
     */
    public void removeToolbarItem(int slot) {
        if (slot < 0 || slot > 8) {
            throw new IllegalArgumentException("The desired slot is outside the bounds of the toolbar slot range. [0-8]");
        }

        toolbarItems.remove(slot);
    }

    /**
     * Increments the current page.
     * You will need to refresh the inventory for those who have it open with {@link #refreshInventory(HumanEntity)}
     *
     * @return Whether or not the page could be changed (false when the max page is currently open as it cannot go further.)
     */
    public boolean nextPage() {
        if (currentPage < getFinalPage()) {
            currentPage++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Decrements the current page.
     * You will need to refresh the inventory for those who have it open with {@link #refreshInventory(HumanEntity)}
     *
     * @return Whether or not the page could be changed (false when the first page is currently active as it cannot go further.)
     */

    public boolean previousPage() {
        if (currentPage > 0) {
            currentPage--;
            return true;
        } else {
            return false;
        }
    }

    /**
     * An alias for {@link #getFinalPage()}.
     *
     * @return The highest page number that can be viewed.
     * @deprecated Use {@link #getFinalPage()} instead.
     */
    public int getMaxPage() {
        return getFinalPage();
    }

    /**
     * Gets the number of the final page of the PaginatedGUI.
     *
     * @return The highest page number that can be viewed.
     */
    public int getFinalPage() {
        // Get the highest slot number.
        int slot = 0;
        for (int nextSlot : items.keySet()) {
            if (nextSlot > slot) {
                slot = nextSlot;
            }
        }

        // Add one to make the math easier.
        double highestSlot = slot + 1;

        // Divide by 45 and round up to get the page number.
        // Then subtract one to convert it to an index.
        return (int) Math.ceil(highestSlot / (double) 45) - 1;
    }

    /**
     * Simply an alias that executes {@link HumanEntity#closeInventory()} and then
     * {@link HumanEntity#openInventory(Inventory)}.
     *
     * @param holder The HumanEntity that you wish to refresh the inventory for.
     */
    public void refreshInventory(HumanEntity holder) {
        Inventory topInventory = holder.getOpenInventory().getTopInventory();
        topInventory.setContents(getInventory().getContents());
        ((Player)holder).updateInventory();
    }

    /**
     * Returns the Spigot {@link Inventory} that represents the PaginatedGUI.
     * This can then by shown to a player using {@link HumanEntity#openInventory(Inventory)}.
     *
     * <br>
     * <p>
     * This also allows getting the PaginatedGUI instance with {@link InventoryHolder#getInventory()}.
     * Used internally ({@link InventoryListener}) to get the GUIButton and therefore listener from the raw slot.
     *
     * @return The Spigot Inventory that represents the PaginatedGUI.
     */
    @Override
    public Inventory getInventory() {
        // Create an inventory (and set an appropriate size.)
        int rowsRequired = (int) Math.ceil(items.size() / 9.0); // calculate rows required
        int inventorySize = (rowsRequired <= 5 ? (rowsRequired * 9) : 45); // calculate how many slots are required, based on the rows

        Inventory inventory = Bukkit.createInventory(this, (getFinalPage() > 0) ? 54 : inventorySize, name);

        /* BEGIN PAGINATION */
        GUIButton backButton = new GUIButton(new ItemBuilder(new ItemStack(Material.ARROW, 1))
                .setName(ChatColor.translateAlternateColorCodes('&', "&cPrevious Page"))
                .toItemStack());

        GUIButton pageIndicator = new UnclickableButton(new ItemBuilder(new ItemStack(Material.PAPER, (currentPage + 1)))
                .setName(
                        ChatColor.translateAlternateColorCodes('&', "&7Page &e" + (currentPage + 1) + " &7of &e" + (getFinalPage() + 1))
                )
                .toItemStack());

        GUIButton nextButton = new GUIButton(new ItemBuilder(new ItemStack(Material.ARROW, 1))
                .setName(ChatColor.translateAlternateColorCodes('&', "&aNext Page"))
                .toItemStack());

        GUIButton filler = new UnclickableButton(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) DyeColor.GRAY.getData()))
                .setName(ChatColor.RED + " ")
                .toItemStack());

        backButton.setListener(event -> {
            event.setCancelled(true);
            PaginatedGUI menu = (PaginatedGUI) event.getClickedInventory().getHolder();

            if (!menu.previousPage()) {
                ((Player) event.getWhoClicked()).sendMessage(ChatColor.translateAlternateColorCodes('&',
                        CHAT_PREFIX + NO_PREVIOUS_PAGES));
                return;
            }

            refreshInventory(event.getWhoClicked());
        });

        nextButton.setListener(event -> {
            event.setCancelled(true);
            PaginatedGUI menu = (PaginatedGUI) event.getClickedInventory().getHolder();

            if (!menu.nextPage()) {
                ((Player) event.getWhoClicked()).sendMessage(ChatColor.translateAlternateColorCodes('&',
                        CHAT_PREFIX + NO_ADDITIONAL_PAGES));
                return;
            }

            refreshInventory(event.getWhoClicked());
        });
        /* END PAGINATION */

        // Where appropriate, include pagination.

        if (getFinalPage() > 0){

            toolbarItems.clear();

            toolbarItems.put(4, pageIndicator);

            if (currentPage > 0)
                toolbarItems.put(0, backButton);

            if (currentPage < getFinalPage())
                toolbarItems.put(8, nextButton);

            for(int i = 0; i <= 8; i++){
                if(!toolbarItems.containsKey(i)){
                    toolbarItems.put(i, filler);
                }
            }
        }

        // Add the main inventory items
        int counter = 0;
        for (int key = (currentPage * 45); key <= Collections.max(items.keySet()); key++) {
            if (counter >= 45)
                break;

            if (items.containsKey(key)) {
                inventory.setItem(counter, items.get(key).getItem());
            }

            counter++;
        }

        // Finally, add the toolbar items.
        for (int toolbarItem : toolbarItems.keySet()) {
            int rawSlot = toolbarItem + 45;
            inventory.setItem(rawSlot, toolbarItems.get(toolbarItem).getItem());
        }

        return inventory;
    }

}
