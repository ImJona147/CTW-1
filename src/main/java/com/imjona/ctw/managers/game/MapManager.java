package com.imjona.ctw.managers.game;

import com.imjona.ctw.CTW;
import com.imjona.ctw.enums.GameState;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class MapManager {
    private final CTW ctw;
    private byte mapIndex = 0;
    private byte playedMaps = 1;
    private String mapToUnload;
    private String currentMap;
    private World currentMapWorld;

    public MapManager(CTW ctw) {
        this.ctw = ctw;
        this.currentMap = ctw.getConfigHandler().getMaps().get(this.mapIndex);
        createMapsFolder();
        loadFirstMap();
    }

    private void createMapsFolder() {
        File mapsFolder = new File("Maps");
        if (!mapsFolder.exists())
            mapsFolder.mkdir();
    }

    private void loadFirstMap() {
        final String copyMapName = "Map-" + this.currentMap;
        File worldToCopy = new File("Maps/" + this.currentMap);
        this.ctw.getWorldManager().copyWorld(copyMapName, worldToCopy);
        this.ctw.getWorldManager().loadWorld(copyMapName);
        Bukkit.getServer().getScheduler().runTaskLater(this.ctw, () -> {
            MapManager.this.currentMapWorld = Bukkit.getWorld(copyMapName);
            MapManager.this.ctw.getMapConfigManager().loadConfig(MapManager.this.currentMap);
            //MapManager.this.ctw.getWoolManager().removeWools();
            MapManager.this.setMapTime();
            //MapManager.this.ctw.getScoreboardTask().startTimer();
            MapManager.this.ctw.getGameEngine().gameState = GameState.RUNNING;
        },  20L);
    }

    private void setMapTime() {
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(this.ctw, () -> {
            try {
                if (MapManager.this.ctw.getMapConfigManager().getTime().matches("day")) {
                    MapManager.this.currentMapWorld.setTime(6000L);
                    MapManager.this.currentMapWorld.setWeatherDuration(0);
                } else if (MapManager.this.ctw.getMapConfigManager().getTime().matches("night")) {
                    MapManager.this.currentMapWorld.setTime(18000L);
                    MapManager.this.currentMapWorld.setWeatherDuration(0);
                }
            } catch (Exception e) {
                MapManager.this.currentMapWorld.setTime(6000L);
                MapManager.this.currentMapWorld.setWeatherDuration(0);
            }
        },  10L);
    }

    private void nextMapIndex() {
        if (this.mapIndex >= this.ctw.getConfigHandler().getMaps().size() - 1) {
            this.mapIndex = 0;
        } else {
            this.mapIndex = (byte)(this.mapIndex + 1);
        }
        this.playedMaps = (byte)(this.playedMaps + 1);
    }

    public void getNextMap() {
        nextMapIndex();
        this.currentMap = CTW.get().getConfigHandler().getMaps().get(this.mapIndex);
    }

    public void loadNextMap() {
        this.mapToUnload = this.currentMapWorld.getName();
        final String copyMapName = "Map-" + this.currentMap;
        File worldToCopy = new File("Maps" + System.getProperty("file.separator") + this.currentMap);
        this.ctw.getWorldManager().copyWorld(copyMapName, worldToCopy);
        Bukkit.getServer().getScheduler().runTaskLater(this.ctw, () -> MapManager.this.ctw.getWorldManager().loadWorld(copyMapName),  20L);
    }

    public void startNextMap() {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(this.ctw, new Runnable() {
            public void run() {
                String mapName = "Map-" + MapManager.this.currentMap;
                MapManager.this.currentMapWorld = Bukkit.getWorld(mapName);
                MapManager.this.ctw.getMapConfigManager().loadConfig(MapManager.this.currentMap);
                MapManager.this.setMapTime();
                //MapManager.this.ctw.getWoolManager().resetWoolsStats();
                //MapManager.this.ctw.getScoreboardTask().startTimer();
                MapManager.this.ctw.getTeamManager().respawnAllPlayers();
                //MapManager.this.ctw.getWoolManager().removeWools();
                Bukkit.getScheduler().runTaskLater(MapManager.this.ctw, new Runnable() {
                    public void run() {
                        if (MapManager.this.mapToUnload != null) {
                            MapManager.this.ctw.getWorldManager().unloadWorld(MapManager.this.mapToUnload);
                            Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(ctw, new Runnable() {
                                @Override
                                public void run() {
                                    MapManager.this.ctw.getWorldManager().deleteWorld(MapManager.this.mapToUnload);
                                }
                            }, 20L);
                        }
                    }
                },100L);
            }
        });
    }

    public byte getMapIndex() {
        return this.mapIndex;
    }

    public void setMapIndex(byte mapIndex) {
        this.mapIndex = mapIndex;
    }

    public byte getPlayedMaps() {
        return this.playedMaps;
    }

    public void setPlayedMaps(byte playedMaps) {
        this.playedMaps = playedMaps;
    }

    public String getMapToUnload() {
        return this.mapToUnload;
    }

    public void setMapToUnload(String mapToUnload) {
        this.mapToUnload = mapToUnload;
    }

    public String getCurrentMap() {
        return this.currentMap;
    }

    public void setCurrentMap(String currentMap) {
        this.currentMap = currentMap;
    }

    public World getCurrentMapWorld() {
        return this.currentMapWorld;
    }

    public void setCurrentMapWorld(World currentMapWorld) {
        this.currentMapWorld = currentMapWorld;
    }
}
