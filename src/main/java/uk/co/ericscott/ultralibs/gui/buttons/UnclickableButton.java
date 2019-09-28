package uk.co.ericscott.ultralibs.gui.buttons;

import org.bukkit.inventory.ItemStack;

public class UnclickableButton extends GUIButton {
    /**
     * Creates a GUIButton with the {@link ItemStack} as it's 'icon' in the inventory.
     *
     * @param item The desired 'icon' for the GUIButton.
     */
    public UnclickableButton(ItemStack item) {
        super(item);
        setListener(event -> event.setCancelled(true));
    }
}
