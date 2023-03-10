package net.resolutemc.resoluteitems.CommandManager;

import net.resolutemc.resoluteitems.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabComplete implements TabCompleter {

    public TabComplete() {
        Main.plugin.getCommand("ResoluteItems").setTabCompleter(this);
    }

    List<String> arguments = new ArrayList<>();

    /**
     *
     * @param sender Source of the command.  For players tab-completing a
     *     command, this will be the player, not the command.
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args The arguments passed to the command, including final
     *     partial argument to be completed
     * @return reruns the command alias that was sent
     */
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (arguments.isEmpty()) {
            arguments.add("Reload");
            arguments.add("List");
            arguments.add("Give");
        }

        List<String> result = new ArrayList<>();

        if (args.length == 1) {
            for (String s : arguments) {
                if (s.toLowerCase().startsWith(args[0].toLowerCase())) result.add(s);
            }
            return result;
        }
        arguments.clear();
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("Give")) {
                Player target = Bukkit.getPlayer(args[2]);
                if (target == null) return null;
                arguments.add(target.getName());
                for (String s : arguments) {
                    if (s.toLowerCase().startsWith(args[2].toLowerCase())) result.add(s);
                }
                return result;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) return null;
            if (args[1].equalsIgnoreCase(target.getName())) {
                arguments.add("SUNTALISMAN");
                arguments.add("POSEIDONTALISMAN");
                for (String s : arguments) {
                    if (s.toLowerCase().startsWith(args[2].toLowerCase())) result.add(s);
                }
                arguments.clear();
                return result;
            }
            arguments.clear();

        }
        return null;
    }
}
