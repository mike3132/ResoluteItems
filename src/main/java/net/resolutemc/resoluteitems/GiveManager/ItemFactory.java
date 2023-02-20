package net.resolutemc.resoluteitems.GiveManager;

import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class ItemFactory {

    public static ItemStack getItem(String string) {
        switch (string.toUpperCase(Locale.ROOT)) {
            case "EXAMPLE":
                return null;
        }
        return null;
    }
}
