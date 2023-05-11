package com.imjona.ctw.managers.game;

import com.imjona.ctw.CTW;
import com.imjona.ctw.utils.UtilsPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class MapConfigManager {
    private final CTW ctw;
    private byte maxPlayers;
    private String time;
    private Location spectatorSpawn;
    private Location redSpawn;
    private Location blueSpawn;
    private Location redWool;
    private Location pinkWool;
    private Location blueWool;
    private Location cyanWool;
    private int maxHight;
    private int minHight;
    private final List<CuboidSelection> woolProtectedAreas = new ArrayList<>();
    private final List<CuboidSelection> spawnProtectedAreas = new ArrayList<>();
    private final List<CuboidSelection> othersProtectedAreas = new ArrayList<>();
    private final List<CuboidSelection> redNoAccess = new ArrayList<>();
    private final List<CuboidSelection> blueNoAccess = new ArrayList<>();
    private CuboidSelection gameArea;
    private ItemStack[] startupKit;
    public MapConfigManager(CTW ctw) {
        this.ctw = ctw;
        createMapConfigFolder();
    }

    private void createMapConfigFolder() {
        File mapConfigFolder = new File(this.ctw.getDataFolder(), "MapConfigs");
        if (!mapConfigFolder.exists())
            mapConfigFolder.mkdir();
    }

    public void reloadConfig() {
        this.othersProtectedAreas.clear();
        this.redNoAccess.clear();
        this.blueNoAccess.clear();
        this.spawnProtectedAreas.clear();
        this.woolProtectedAreas.clear();
        this.startupKit = null;
        loadConfig(this.ctw.getMapManager().getCurrentMap());
    }

    public void loadConfig(final String mapName) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(this.ctw, () -> {
            File mapConfigFolder = new File(MapConfigManager.this.ctw.getDataFolder(), "MapConfigs");
            if (!mapConfigFolder.exists())
                mapConfigFolder.mkdir();
            File mapConfig = new File(MapConfigManager.this.ctw.getDataFolder() + "/MapConfigs", mapName + ".yml");
            if (!mapConfig.exists()) {
                CTW.get().saveResource("MapConfig.yml", false);
                File rawConfig = new File(MapConfigManager.this.ctw.getDataFolder(), "MapConfig.yml");
                try {
                    FileUtils.moveFileToDirectory(rawConfig, mapConfigFolder, false);
                    File fileToRename = new File(MapConfigManager.this.ctw.getDataFolder() + "/MapConfigs", "MapConfig.yml");
                    fileToRename.renameTo(mapConfig);
                } catch (Exception e) {
                    UtilsPlugin.sendMessageConsole("&cal crear la configuracidel mapa! &7" + e.getMessage());
                    e.printStackTrace();
                }
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(mapConfig);
            MapConfigManager.this.maxPlayers = (byte)config.getInt("MaxPlayersPerTeam");
            MapConfigManager.this.time = config.getString("Time");
            MapConfigManager.this.spectatorSpawn = UtilsPlugin.getLoc(config.getString("SpectatorSpawn"));
            MapConfigManager.this.redSpawn = UtilsPlugin.getLoc(config.getString("RedTeam.Spawn"));
            MapConfigManager.this.blueSpawn = UtilsPlugin.getLoc(config.getString("BlueTeam.Spawn"));
            MapConfigManager.this.redWool = UtilsPlugin.getLocation(config.getString("RedTeam.RedWool"));
            MapConfigManager.this.pinkWool = UtilsPlugin.getLocation(config.getString("RedTeam.PinkWool"));
            MapConfigManager.this.blueWool = UtilsPlugin.getLocation(config.getString("BlueTeam.BlueWool"));
            MapConfigManager.this.cyanWool = UtilsPlugin.getLocation(config.getString("BlueTeam.CyanWool"));
            MapConfigManager.this.maxHight = config.getInt("ProtectedHight.maxHight");
            MapConfigManager.this.minHight = config.getInt("ProtectedHight.minHight");
            List<String> spawnprotected = config.getStringList("SpawnProtectedAreas");
            if (!spawnprotected.isEmpty())
                for (String list : spawnprotected) {
                    String[] data = list.split(";");
                    Location loc1 = new Location(CTW.get().getMapManager().getCurrentMapWorld(), Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]));
                    Location loc2 = new Location(CTW.get().getMapManager().getCurrentMapWorld(), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]));
                    CuboidSelection cs = new CuboidSelection(CTW.get().getMapManager().getCurrentMapWorld(), loc1, loc2);
                    MapConfigManager.this.spawnProtectedAreas.add(cs);
                }
            List<String> othersprotected = config.getStringList("OthersProtectedAreas");
            if (!othersprotected.isEmpty())
                for (String list : othersprotected) {
                    String[] data = list.split(";");
                    Location loc1 = new Location(CTW.get().getMapManager().getCurrentMapWorld(), Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]));
                    Location loc2 = new Location(CTW.get().getMapManager().getCurrentMapWorld(), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]));
                    CuboidSelection cs = new CuboidSelection(CTW.get().getMapManager().getCurrentMapWorld(), loc1, loc2);
                    MapConfigManager.this.othersProtectedAreas.add(cs);
                }
            List<String> woolprotected = config.getStringList("WoolProtectedAreas");
            if (!woolprotected.isEmpty())
                for (String list : woolprotected) {
                    String[] data = list.split(";");
                    Location loc1 = new Location(CTW.get().getMapManager().getCurrentMapWorld(), Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]));
                    Location loc2 = new Location(CTW.get().getMapManager().getCurrentMapWorld(), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]));
                    CuboidSelection cs = new CuboidSelection(CTW.get().getMapManager().getCurrentMapWorld(), loc1, loc2);
                    MapConfigManager.this.woolProtectedAreas.add(cs);
                }
            List<String> redNoAcces = config.getStringList("RedNoAccess");
            if (!redNoAcces.isEmpty())
                for (String list : redNoAcces) {
                    String[] data = list.split(";");
                    Location loc1 = new Location(CTW.get().getMapManager().getCurrentMapWorld(), Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]));
                    Location loc2 = new Location(CTW.get().getMapManager().getCurrentMapWorld(), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]));
                    CuboidSelection cs = new CuboidSelection(CTW.get().getMapManager().getCurrentMapWorld(), loc1, loc2);
                    MapConfigManager.this.redNoAccess.add(cs);
                }
            List<String> BlueNoAccess = config.getStringList("BlueNoAccess");
            if (!BlueNoAccess.isEmpty())
                for (String list : BlueNoAccess) {
                    String[] data = list.split(";");
                    Location loc1 = new Location(CTW.get().getMapManager().getCurrentMapWorld(), Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]));
                    Location loc2 = new Location(CTW.get().getMapManager().getCurrentMapWorld(), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]));
                    CuboidSelection cs = new CuboidSelection(CTW.get().getMapManager().getCurrentMapWorld(), loc1, loc2);
                    MapConfigManager.this.blueNoAccess.add(cs);
                }
            String[] arenaA = config.getString("ArenaArea").split(";");
            Location arenaLoc1 = new Location(CTW.get().getMapManager().getCurrentMapWorld(), Integer.parseInt(arenaA[0]), Integer.parseInt(arenaA[1]), Integer.parseInt(arenaA[2]));
            Location arenaLoc2 = new Location(CTW.get().getMapManager().getCurrentMapWorld(), Integer.parseInt(arenaA[3]), Integer.parseInt(arenaA[4]), Integer.parseInt(arenaA[5]));
            MapConfigManager.this.gameArea = new CuboidSelection(CTW.get().getMapManager().getCurrentMapWorld(), arenaLoc1, arenaLoc2);
            ArrayList<ItemStack> items = new ArrayList<>();
            for (int i = 0; i < 36; i++) {
                ItemStack item = config.getItemStack("StartupKit." + i);
                items.add(item);
            }
            MapConfigManager.this.startupKit = items.toArray(new ItemStack[0]);
            items.clear();
        });
    }

    public List<String> getArea(String path, Location loc) {
        File mapConfig = new File(this.ctw.getDataFolder() + "/MapConfigs", this.ctw.getMapManager().getCurrentMap() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(mapConfig);
        Set<String> areaList = new HashSet<>(config.getStringList(path));
        List<String> areas = new ArrayList<>();
        if (!areaList.isEmpty())
            for (String list : areaList) {
                String[] data = list.split(";");
                Location l1 = new Location(CTW.get().getMapManager().getCurrentMapWorld(), Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]));
                Location l2 = new Location(CTW.get().getMapManager().getCurrentMapWorld(), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]));
                CuboidSelection cs = new CuboidSelection(this.ctw.getMapManager().getCurrentMapWorld(), l1, l2);
                if (cs.contains(loc))
                    areas.add(list);
            }
        return areas;
    }

    public void removeArea(String path, String data) {
        File mapConfig = new File(this.ctw.getDataFolder() + "/MapConfigs", this.ctw.getMapManager().getCurrentMap() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(mapConfig);
        Set<String> areaList = new HashSet<>(config.getStringList(path));
        areaList.remove(data);
        config.set(path, Arrays.asList(areaList.toArray()));
        try {
            config.save(mapConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addArea(String path, Selection sel) {
        File mapConfig = new File(this.ctw.getDataFolder() + "/MapConfigs", this.ctw.getMapManager().getCurrentMap() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(mapConfig);
        String data = UtilsPlugin.getSelection(sel);
        Set<String> areaList = new HashSet<>(config.getStringList(path));
        areaList.add(data);
        config.set(path, Arrays.asList(areaList.toArray()));
        try {
            config.save(mapConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setArenaArea(String path, Selection sel) {
        File mapConfig = new File(this.ctw.getDataFolder() + "/MapConfigs", this.ctw.getMapManager().getCurrentMap() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(mapConfig);
        String data = UtilsPlugin.getSelection(sel);
        config.set(path, data);
        try {
            config.save(mapConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setString(String data, String path) {
        File mapConfig = new File(this.ctw.getDataFolder() + "/MapConfigs", this.ctw.getMapManager().getCurrentMap() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(mapConfig);
        config.set(path, data);
        try {
            config.save(mapConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setInteger(int number, String path) {
        File mapConfig = new File(this.ctw.getDataFolder() + "/MapConfigs", this.ctw.getMapManager().getCurrentMap() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(mapConfig);
        config.set(path, Integer.valueOf(number));
        try {
            config.save(mapConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBlockLocation(Location loc, String path) {
        File mapConfig = new File(this.ctw.getDataFolder() + "/MapConfigs", this.ctw.getMapManager().getCurrentMap() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(mapConfig);
        config.set(path, UtilsPlugin.setLoc(loc));
        try {
            config.save(mapConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSpawnPoint(Location loc, String path) {
        File mapConfig = new File(this.ctw.getDataFolder() + "/MapConfigs", this.ctw.getMapManager().getCurrentMap() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(mapConfig);
        config.set(path, UtilsPlugin.setLocNoWorld(loc));
        try {
            config.save(mapConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveStartupKit(ItemStack i, int slot) {
        File mapConfig = new File(this.ctw.getDataFolder() + "/MapConfigs", this.ctw.getMapManager().getCurrentMap() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(mapConfig);
        config.set("StartupKit." + slot, i);
        try {
            config.save(mapConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CTW getCtw() {
        return this.ctw;
    }

    public byte getMaxPlayers() {
        return this.maxPlayers;
    }

    public String getTime() {
        return this.time;
    }

    public Location getSpectatorSpawn() {
        return this.spectatorSpawn;
    }

    public Location getRedSpawn() {
        return this.redSpawn;
    }

    public Location getBlueSpawn() {
        return this.blueSpawn;
    }

    public Location getRedWool() {
        return this.redWool;
    }

    public Location getPinkWool() {
        return this.pinkWool;
    }

    public Location getBlueWool() {
        return this.blueWool;
    }

    public Location getCyanWool() {
        return this.cyanWool;
    }

    public Location getWool(String wool) {
        if (wool.matches("Red"))
            return this.redWool;
        if (wool.matches("Pink"))
            return this.pinkWool;
        if (wool.matches("Cyan"))
            return this.cyanWool;
        if (wool.matches("Blue"))
            return this.blueWool;
        return null;
    }

    public int getMaxHight() {
        return this.maxHight;
    }

    public int getMinHight() {
        return this.minHight;
    }

    public List<CuboidSelection> getSpawnProtectedAreas() {
        return this.spawnProtectedAreas;
    }

    public List<CuboidSelection> getOthersProtectedAreas() {
        return this.othersProtectedAreas;
    }

    public List<CuboidSelection> getWoolProtectedAreas() {
        return this.woolProtectedAreas;
    }

    public List<CuboidSelection> getRedNoAccess() {
        return this.redNoAccess;
    }

    public List<CuboidSelection> getBlueNoAccess() {
        return this.blueNoAccess;
    }

    public CuboidSelection getGameArea() {
        return this.gameArea;
    }

    public ItemStack[] getStartupKit() {
        return this.startupKit;
    }
}
