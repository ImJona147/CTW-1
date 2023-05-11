package com.imjona.ctw;

import java.io.File;
import java.util.List;

import com.imjona.ctw.utils.UtilsPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nullable;

public class MenusHandler extends FileConfiguration {
    private final File file;
    private FileConfiguration config;
    private final CTW ctw;

    public MenusHandler(CTW ctw) {
        this.ctw = ctw;
        this.file = new File("plugins" + System.getProperty("file.separator") + "CTW" + System.getProperty("file.separator") + "menus.yml");
        loadConfig();
    }

    public boolean loadConfig() {
        if(!file.exists()) {
            UtilsPlugin.corregirColor("&7No se encontro &bmenus.yml&7!! Creando uno nuevo...");
            this.ctw.saveResource("menus.yml", false);
        }
        try {
            this.config = YamlConfiguration.loadConfiguration(file);
            UtilsPlugin.sendMessageConsole("&7menus.yml ha sido cargado");
            return true;
        } catch (Exception e) {
            UtilsPlugin.sendMessageConsole("&7Hubo un error al cargar la config. &cError: &b");
            e.printStackTrace();
            return false;
        }
    }

    public String getString(String key) {
        if(!this.config.contains(key)) {
            UtilsPlugin.sendMessageConsole("&7No se pudo encontrar &b" + key + "&7 en menus.yml");
            return "No se encontro " + key;
        }
        return this.config.getString(key);
    }

    public List<String> getStringList(String key) {
        if(!this.config.contains(key)) {
            UtilsPlugin.sendMessageConsole("&7No se pudo encontrar &b" + key + "&7 en menus.yml");
            return null;
        }
        return this.config.getStringList(key);
    }

    public int getInt(String key) {
        if (!this.config.contains(key)) {
            UtilsPlugin.sendMessageConsole("&7No se pudo encontrar &b" + key + "&7 en menus.yml");
            return 0;
        }
        return this.config.getInt(key);
    }

    public boolean getBoolean(String key) {
        if (!this.config.contains(key)) {
            UtilsPlugin.sendMessageConsole("&7No se pudo encontrar &b" + key + "&7 en menus.yml");
            return false;
        }
        return Boolean.valueOf(this.config.getBoolean(key));
    }

    public double getDouble(String key) {
        if (!this.config.contains(key)) {
            UtilsPlugin.sendMessageConsole("&7No se pudo encontrar &b" + key + "&7 en menus.yml");
            return 0;
        }
        return Double.valueOf(this.config.getDouble(key));
    }

    @Nullable
    public ConfigurationSection getConfigurationSection(String key) {
        if (!this.config.contains(key)) {
            UtilsPlugin.sendMessageConsole("&7Could not find &b" + key + " &7in the config");
            return null;
        }
        return config.getConfigurationSection(key);
    }

    @Override
    protected String buildHeader() {
        return null;
    }

    @Override
    public void loadFromString(String arg0) {

    }

    @Override
    public String saveToString() {
        return null;
    }
}