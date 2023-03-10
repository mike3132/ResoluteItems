package net.resolutemc.resoluteitems;

import net.resolutemc.resoluteitems.CommandManager.ItemCommand;
import net.resolutemc.resoluteitems.CommandManager.TabComplete;
import net.resolutemc.resoluteitems.ConfigManager.ConfigLoader;
import net.resolutemc.resoluteitems.Talismans.EventManager.PoseidonTalismanEvents;
import net.resolutemc.resoluteitems.Talismans.EventManager.SunTalismanEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main plugin;

    public static String chatColor(String chatColor) {
        return ChatColor.translateAlternateColorCodes('&', chatColor);
    }

    @Override
    public void onEnable() {
        plugin = this;
        // Plugin startup logic
        getServer().getConsoleSender().sendMessage(chatColor("&5Resolute &4Items &7> &2ENABLED"));

        // Event loader
        Bukkit.getPluginManager().registerEvents(new SunTalismanEvents(), this);
        Bukkit.getPluginManager().registerEvents(new PoseidonTalismanEvents(), this);


        // Command loader
        registerItemCommand();
        registerTabComplete();



        // Config loader
        saveDefaultConfig();
        getConfig();
        ConfigLoader.MESSAGES.create();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getConsoleSender().sendMessage(chatColor("&5Resolute &4Items &7> &4DISABLED"));
    }

    public void registerItemCommand() {
        new ItemCommand();
    }
    public void registerTabComplete() {
        new TabComplete();
    }


}
