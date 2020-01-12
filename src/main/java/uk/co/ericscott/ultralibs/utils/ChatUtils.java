package uk.co.ericscott.ultralibs.utils;

import org.bukkit.ChatColor;

public class ChatUtils
{

    public static String translate(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
