package com.imjona.ctw.utils;

import com.imjona.ctw.xseries.XMaterial;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UtilsBuilder {

    public static ItemStack getSkullFromValue(String value) {
        ItemStack skull = XMaterial.PLAYER_HEAD.parseItem();

        SkullMeta meta = (SkullMeta) (skull != null ? skull.getItemMeta() : null);
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");
        gameProfile.getProperties().put("textures", new Property("textures", value));
        Field profileField;
        try {
            profileField = meta != null ? meta.getClass().getDeclaredField("profile") : null;
            Objects.requireNonNull(profileField).setAccessible(true);
            profileField.set(meta, gameProfile);
        } catch(IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            UtilsPlugin.sendMessageConsole("&7There was an error loading the texture...");
            UtilsPlugin.sendMessageConsole("&c&lERROR: &7");
            e.printStackTrace();
        }
        Objects.requireNonNull(skull).setItemMeta(meta);
        return skull;
    }

    public static ItemStack getItemFromString(String material) {
        XMaterial mat;
        if (material.startsWith("PLAYER_HEAD-")) {
            material = material.replace("PLAYER_HEAD-", "");
            return getSkullFromValue(material);
        }
        try {
            mat = XMaterial.valueOf(material.split("-")[0]);
        } catch(IllegalArgumentException e) {
            UtilsPlugin.sendMessageConsole("&7There was an error loading the texture...");
            UtilsPlugin.sendMessageConsole("&c&lERROR: &7");
            e.printStackTrace();
            mat = XMaterial.EMERALD_BLOCK;
        }
        return mat.parseItem();
    }

    public static ItemStack makeItem(FileConfiguration config, String key, Player player) {
        String material = config.getString(key + ".material");
        ItemStack item = (material != null) ? getItemFromString(material) : XMaterial.BARRIER.parseItem();
        String name;
        List<String> lore;
        if(item != null) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                name = config.getString(key + ".name");
                lore = config.getStringList(key + ".lore");

                for (int c=0;c<lore.size();c++)
                    lore.set(c, UtilsPlugin.corregirColor(lore.get(c)));

                meta.setDisplayName(UtilsPlugin.corregirColor(name));
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
        }
        return item;
    }
}