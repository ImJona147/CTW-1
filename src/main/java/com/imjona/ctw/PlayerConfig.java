package com.imjona.ctw;

import com.imjona.ctw.utils.UtilsPlugin;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PlayerConfig {
    private FileConfiguration config;
    private File configFile;
    private final String filePath;
    private final CTW ctw;

    public PlayerConfig(String filePath, CTW ctw) {
        this.configFile = null;
        this.config = null;
        this.filePath = filePath;
        this.ctw = ctw;
    }

    public String getPath() {
        return filePath;
    }

    public FileConfiguration getConfig() {
        if (this.config == null)
            reloadPlayerConfig();
        return this.config;
    }

    public void registerPlayerConfig() {
        this.configFile = new File(ctw.getDataFolder() + File.separator + "players", this.filePath);
        if (!this.configFile.exists()) {
            try {
                this.configFile.createNewFile();
            } catch (IOException e) {
                UtilsPlugin.sendMessageConsole("&cHa ocurrido un error al registrar al usuario... (creación de archivo yml)");
                e.printStackTrace();
            }
            this.config = new YamlConfiguration();
            try {
                this.config.load(this.configFile);
            } catch (IOException | InvalidConfigurationException e) {
                UtilsPlugin.sendMessageConsole("&cHa ocurrido un error al cargar al usuario... (creación de archivo yml)");
                e.printStackTrace();
            }
        }
    }

    public void savePlayerConfig() {
        try {
            this.config.save(this.configFile);
        } catch (IOException e) {
            UtilsPlugin.sendMessageConsole("&cHa ocurrido un error al guardar el archivo del usuario");
            e.printStackTrace();
        }
    }

    public void reloadPlayerConfig() {
        if (this.config == null)
            this.configFile = new File(ctw.getDataFolder() + File.separator + "players", this.filePath);
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        if (this.configFile != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(this.configFile);
            this.config.setDefaults(defConfig);
        }
    }
}
