package uk.co.ericscott.ultralibs.enchantment;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Mat
 * <p>
 * Glow enchantment, just makes an item glow.
 * <p>
 * Doesn't do anything else really but can be used globally.
 * <p>
 * TAKES UP LORE LINE STILL, CAN'T CHANGE THAT.
 * <p>
 * ENCHANTMENT ID REGISTER IS ON 70, DO NOT OVERWRITE ANOTHER CUSTOM ENCHANTMENT USING THAT ID.
 */
public class GlowEnchantment extends Enchantment
{

    public GlowEnchantment(int id) {
        super(id);
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true; //Let it be added to everything.
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

}
