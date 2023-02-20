package net.resolutemc.resoluteitems.ConfigManager;

import net.resolutemc.resoluteitems.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public enum ConfigLoader {


    MESSAGES;


    public File getFile() {
        return new File(Main.plugin.getDataFolder(), this.toString().toLowerCase(Locale.ROOT) + ".yml");
    }


    public FileConfiguration get() {
        return YamlConfiguration.loadConfiguration(getFile());
    }


    public void save(FileConfiguration configuration) {
        try {
            configuration.save(getFile());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void create() {
        Main.plugin.saveResource(this.toString().toLowerCase(Locale.ROOT) + ".yml", false);
    }
}
