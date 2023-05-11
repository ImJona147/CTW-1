package com.imjona.ctw.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.imjona.ctw.utils.UtilsPlugin;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class WorldHandler {
    private final List<World> loadedWorlds = new ArrayList<>();

    public void loadWorld(String worldName) {
        try {
            UtilsPlugin.sendMessageConsole("&7Cargando mundo: &b" + worldName);
            Bukkit.createWorld(WorldCreator.name(worldName));
            World world = Bukkit.getWorld(worldName);
            applyWorldSettings(world);
            removeEntity(world);
            this.loadedWorlds.add(world);
        } catch(Exception e) {
            UtilsPlugin.sendMessageConsole("&7Error al cargar el mundo &b" + worldName + "&cERROR: &7" + e.getMessage());
        }
    }

    public void unloadWorld(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            List<Player> players = new ArrayList<>(world.getPlayers());
            if(!players.isEmpty())
                for(Player player : players) {
                    if(player != null && player.isOnline())
                        player.kickPlayer(UtilsPlugin.corregirColor("&cDeshabilitando mundo..."));
                }
            UtilsPlugin.sendMessageConsole("&7Deshabilitando mundo: &b" + worldName);
            Bukkit.unloadWorld(worldName, false);
            this.loadedWorlds.remove(world);
        }
    }

    public void deleteWorld(String worldName) {
        File worldFolder = new File(worldName);
        if(worldFolder.exists())
            try {
                UtilsPlugin.sendMessageConsole("&7Eliminando mundo: &b" + worldName);
                FileUtils.deleteDirectory(worldFolder);
            } catch(Exception e) {
                UtilsPlugin.sendMessageConsole("&7Error al eliminar el mundo: &b" + worldName + "&c ERROR: &7" + e.getMessage());
            }
    }

    public void copyWorld(String worldName, File worldToCopy) {
        File worldFolder = new File(worldName);
        if(worldFolder.exists())
            deleteWorld(worldName);
        try {
            FileUtils.copyDirectory(worldToCopy, worldFolder);
        } catch(Exception e) {
            UtilsPlugin.sendMessageConsole("&7Error al copiar el mundo: &b" + worldName + "&c ERROR: &7" + e.getMessage());
        }
    }

    public void unloadWorldOnShutDown() {
        List<World> worlds = new ArrayList<>(this.loadedWorlds);
        if(!worlds.isEmpty())
            for(World world : worlds) {
                if(world != null) {
                    unloadWorld(world.getName());
                    deleteWorld(world.getName());
                }
            }
    }

    private void applyWorldSettings(World world) {
        world.setAnimalSpawnLimit(0);
        world.setAutoSave(false);
        world.setDifficulty(Difficulty.EASY);
        world.setKeepSpawnInMemory(true);
        world.setMonsterSpawnLimit(0);
        world.setPVP(true);
        world.setWaterAnimalSpawnLimit(0);
        world.setStorm(false);
        world.setThundering(false);
        world.setWeatherDuration(9999999);
        world.setTime(1000L);
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("showDeathMessages", "true");
        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("mobGriefing", "false");
    }

    private void removeEntity(World world) {
        List<Entity> entities = new ArrayList<>(world.getEntities());
        for (Entity entity : entities) {
            if (entity.getType() != EntityType.ARMOR_STAND && entity.getType() != EntityType.ITEM_FRAME &&
                    entity.getType() != EntityType.LEASH_HITCH && entity.getType() != EntityType.PAINTING)
                entity.remove();
        }
    }
}
