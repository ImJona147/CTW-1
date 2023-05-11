package com.imjona.ctw;

import com.imjona.ctw.utils.UtilsPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class LanguageHandler {
    private final CTW ctw;
    private final Language lang;
    private final File data;
    private FileConfiguration config;
    private enum Language {
        EN
    }

    public LanguageHandler(CTW ctw) {
        this.ctw = ctw;
        this.lang = Language.EN;
        this.data = new File("plugins" + System.getProperty("file.separator") + "CTW" + System.getProperty("file.separator") + "Lang-EN.yml");
        loadLangFile();
    }

    public boolean loadLangFile() {
        if (!this.data.exists()) {
            UtilsPlugin.sendMessageConsole("&7Generando archivo lang: &b" + this.lang);
            this.ctw.saveResource("Lang-EN.yml", false);
        }
        UtilsPlugin.sendMessageConsole("&7Cargando archivo lang: &b" + this.lang);
        try {
            this.config = YamlConfiguration.loadConfiguration(this.data);
            return true;
        } catch (Exception e) {
            UtilsPlugin.sendMessageConsole("&7Error al cargar el archivo lang: &c" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String getMessage(String key) {
        if (!this.config.contains(key)) {
            UtilsPlugin.sendMessageConsole("&7No se encontro en el archivo &e" + key);
            return "Msg no encontrado";
        }
        return UtilsPlugin.corregirColor(this.config.getString(key));
    }

    public List<String> getMessageList(String key) {
        if (!this.config.contains(key)) {
            UtilsPlugin.sendMessageConsole("&7No se encontro en el archivo &e" + key);
            return null;
        }
        return this.config.getStringList(key);
    }
}
