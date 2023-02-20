package net.resolutemc.resoluteitems.CommandManager;

import net.resolutemc.resoluteitems.ChatManager.ChatMessages;
import net.resolutemc.resoluteitems.GiveManager.ItemFactory;
import net.resolutemc.resoluteitems.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemCommand implements CommandExecutor {


    public ItemCommand() {
        Main.plugin.getCommand("ResoluteItems").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            ChatMessages.sendConsoleMessage(sender, "Not-enough-args-placeholder");
            return false;
        }
        if (args[0].equalsIgnoreCase("Reload")) {
            if (!sender.hasPermission("ragnarokTools.Reload")) {
                ChatMessages.sendConsoleMessage(sender, "No-permissions-placeholder");
                return false;
            }
            sender.sendMessage(Main.chatColor("&bRagnarok &6Tools &7> &aPlugin config reloaded in &2" + String.valueOf(System.currentTimeMillis() - 1) + " &ams"));
            Main.plugin.reloadConfig();
            return false;
        }

        if (args[0].equalsIgnoreCase("List")) {
            if (!sender.hasPermission("ragnarokTools.List")) {
                ChatMessages.sendConsoleMessage(sender, "No-permissions-placeholder");
                return false;
            }
            //ChatMessages.sendMessageNoPrefix(sender, "Tools-list-placeholder-header");
            //ChatMessages.sendMessageNoPrefix(sender, "Tools-list-A");
            //ChatMessages.sendMessageNoPrefix(sender, "Tools-list-B");
            //ChatMessages.sendMessageNoPrefix(sender, "Tools-list-C");
            //ChatMessages.sendMessageNoPrefix(sender, "Tools-list-placeholder-footer");
            return false;
        }
        if (!sender.hasPermission("ragnarokTools.Give")) {
            ChatMessages.sendConsoleMessage(sender, "No-permissions-placeholder");
            return false;
        }
        if (!args[0].equalsIgnoreCase("Give")) {
            ChatMessages.sendConsoleMessage(sender, "Not-give-placeholder");
            return false;
        }
        if (args.length < 2) {
            ChatMessages.sendConsoleMessage(sender, "Not-player-placeholder");
            return false;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            ChatMessages.sendConsoleMessage(sender, "Player-not-found-placeholder");
            return false;
        }
        if (args.length < 3) {
            ChatMessages.sendConsoleMessage(sender, "Not-tool-placeholder");
            return false;
        }
        ItemStack item = ItemFactory.getItem(args[2]);
        if (item == null) {
            ChatMessages.sendConsoleMessage(sender, "Tool-not-found-placeholder");
            return false;
        }
        int amount = 1;
        if (args.length >= 4) {
            amount = Integer.parseInt(args[3]);
        }
        for (int i = 0; i < amount; i++) {
            if (target.getInventory().firstEmpty() == -1) {
                ChatMessages.sendPlayerMessage(target, "Player-inventory-full-placeholder");
                target.getLocation().getWorld().dropItem(target.getLocation(), item);
                return false;
            }
            ChatMessages.sendPlayerMessage(target, "Player-give-tool-placeholder");
            target.getInventory().addItem(item);
        }



        return false;
    }
}
