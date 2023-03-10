package net.resolutemc.resoluteitems.Talismans.ItemManager;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import net.resolutemc.resoluteitems.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class SunTalismanItem {

    public static ItemStack getSunTalisman() {
        HeadDatabaseAPI hdbAPI = new HeadDatabaseAPI();

        ItemStack item = hdbAPI.getItemHead("573");
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();

        for (String realLore : Main.plugin.getConfig().getStringList("Sun-Talisman-Lore")) {
            lore.add(Main.chatColor("" + realLore));
        }
        meta.setDisplayName(Main.chatColor("" + Main.plugin.getConfig().getString("Sun-Talisman-Name")));
        meta.setLore(lore);
        NamespacedKey key = new NamespacedKey(Main.plugin, "Sun-Talisman-Key");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "Sun-Talisman");
        item.setItemMeta(meta);
        return item;
    }

}
