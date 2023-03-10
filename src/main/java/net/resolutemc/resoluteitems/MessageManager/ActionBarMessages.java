package net.resolutemc.resoluteitems.MessageManager;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.resolutemc.resoluteitems.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class ActionBarMessages {

    public static void sendPlayerActionBar(Player player, String key) {
        File messagesConfig = new File(Main.plugin.getDataFolder(), "messages.yml");
        YamlConfiguration configMessages = YamlConfiguration.loadConfiguration(messagesConfig);
        String message = configMessages.getString("Messages.Prefix") + configMessages.getString("Messages." + key);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message)));
    }
}
