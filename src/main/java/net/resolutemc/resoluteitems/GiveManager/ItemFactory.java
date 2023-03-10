package net.resolutemc.resoluteitems.GiveManager;

import net.resolutemc.resoluteitems.Talismans.ItemManager.PoseidonTalismanItem;
import net.resolutemc.resoluteitems.Talismans.ItemManager.SunTalismanItem;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class ItemFactory {

    public static ItemStack getItem(String string) {
        switch (string.toUpperCase(Locale.ROOT)) {
            case "SUNTALISMAN":
                return SunTalismanItem.getSunTalisman();
            case "POSEIDONTALISMAN":
                return PoseidonTalismanItem.getPoseidonTalisman();
        }
        return null;
    }
}
