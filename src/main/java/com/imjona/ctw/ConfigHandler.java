package com.imjona.ctw;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.imjona.ctw.utils.UtilsPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler extends FileConfiguration {
    private final CTW ctw;
    private List<String> maps = new ArrayList<>();

    public ConfigHandler(CTW ctw) {
        this.ctw = ctw;
        loadConfig();
    }

    public void loadConfig() {
        File pluginFolder = new File(this.ctw.getDataFolder().toString());
        if (!pluginFolder.exists())
            pluginFolder.mkdir();
        File configFile = new File(this.ctw.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            UtilsPlugin.corregirColor("&7No se encontro &bconfig.yml&7!! Creando uno nuevo...");
            this.ctw.saveDefaultConfig();
        }
        try {
            this.ctw.getConfig().load(configFile);
            UtilsPlugin.sendMessageConsole("&7Config.yml ha sido cargado");
        } catch (Exception e) {
            UtilsPlugin.sendMessageConsole("&7Hubo un error al cargar la config. &cError: &b" + e.getMessage());
        }
        this.maps = getStringList("GameMaps");
    }

    public String getString(String key) {
        if (!this.ctw.getConfig().contains(key)) {
            UtilsPlugin.sendMessageConsole("&7No se pudo encontrar &b" + key + "&7 en la config.yml");
            return "No se encontro " + key;
        }
        return UtilsPlugin.corregirColor(this.ctw.getConfig().getString(key));
    }

    public List<String> getStringList(String key) {
        if (!this.ctw.getConfig().contains(key)) {
            UtilsPlugin.sendMessageConsole("&7No se pudo encontrar &b" + key + "&7 en la config.yml");
            return null;
        }
        return this.ctw.getConfig().getStringList(key);
    }

    public int getInt(String key) {
        if (!this.ctw.getConfig().contains(key)) {
            UtilsPlugin.sendMessageConsole("&7No se pudo encontrar &b" + key + "&7 en la config.yml");
            return 0;
        }
        return this.ctw.getConfig().getInt(key);
    }

    public boolean getBoolean(String key) {
        if (!this.ctw.getConfig().contains(key)) {
            UtilsPlugin.sendMessageConsole("&7No se pudo encontrar &b" + key + "&7 en la config.yml");
            return false;
        }
        return this.ctw.getConfig().getBoolean(key);
    }

    public double getDouble(String key) {
        if (!this.ctw.getConfig().contains(key)) {
            UtilsPlugin.sendMessageConsole("&7No se pudo encontrar &b" + key + "&7 en la config.yml");
            return 0.0D;
        }
        return this.ctw.getConfig().getDouble(key);
    }

    public ConfigurationSection getConfigurationSection(String key) {
        if (!this.ctw.getConfig().contains(key)) {
            UtilsPlugin.sendMessageConsole("&7Could not find &b" + key + " &7in the config");
            return null;
        }
        return this.ctw.getConfig().getConfigurationSection(key);
    }

    public List<String> getMaps() {
        return maps;
    }

    protected String buildHeader() {
        return null;
    }

    public void loadFromString(String arg0) {}

    public String saveToString() {
        return null;
    }
}
