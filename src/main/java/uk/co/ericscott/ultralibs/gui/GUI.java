package uk.co.ericscott.ultralibs.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import uk.co.ericscott.ultralibs.gui.buttons.GUIButton;

import java.util.HashMap;
import java.util.Map;

public class GUI implements InventoryHolder {

    private String displayName;

    private int slotSize;

    private Map<Integer, GUIButton> buttons = new HashMap<>();

    public GUI(String name, int slotSize) {
        this.displayName = name;
        this.slotSize = slotSize;
    }

    public int getSlotOf(GUIButton button) {
        return buttons.entrySet().stream().filter(entry -> entry.getValue().equals(button)).findFirst().orElse(null).getKey();
    }

    public GUIButton getButton(int slot) {
        if (buttons.get(slot) == null)
            throw new NullPointerException("There's no such gui button at that slot.");
        else return buttons.get(slot);
    }

    public void addButton(GUIButton button) {
        if (buttons.size() + 1 > slotSize)
            throw new IllegalArgumentException("Your inventory is too big to add all those items! Max size [0-" + slotSize + "]");
        else
            buttons.put(buttons.size() + 1, button);
    }

    public void setButton(int slot, GUIButton button) {
        buttons.put(slot, button);
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, slotSize, ChatColor.translateAlternateColorCodes('&', displayName));
        for (Map.Entry<Integer, GUIButton> button : buttons.entrySet()) {
            inventory.setItem(button.getKey(), button.getValue().getItem());
        }
        return inventory;
    }

    public Map<Integer, GUIButton> getButtons() {
        return this.buttons;
    }

}