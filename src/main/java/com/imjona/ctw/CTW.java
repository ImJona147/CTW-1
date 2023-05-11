package com.imjona.ctw;

import com.imjona.ctw.database.Achievements;
import com.imjona.ctw.database.PlayerData;
import com.imjona.ctw.managers.ScoreboardAdmin;
import com.imjona.ctw.managers.TeamManager;
import com.imjona.ctw.managers.WoolManager;
import com.imjona.ctw.managers.WorldHandler;
import com.imjona.ctw.managers.game.GameEngine;
import com.imjona.ctw.managers.game.MapConfigManager;
import com.imjona.ctw.managers.game.MapManager;
import com.imjona.ctw.utils.Checks;
import com.imjona.ctw.utils.UtilsPlugin;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

public final class CTW extends JavaPlugin {
    private static CTW instance;
    public Economy economy = null;
    private boolean isEnabled = false;
    private boolean isDisabling = false;

    private LanguageHandler languageHandler;
    private ScoreboardAdmin scoreboardAdmin;
    private ConfigHandler configHandler;
    private TeamManager teamManager;
    private MenusHandler menusHandler;

    private MapConfigManager mapConfigManager;
    private MapManager mapManager;
    private WorldHandler worldManager;
    private GameEngine gameEngine;
    //KillStreak
    private WoolManager woolManager;
    //Mysql
    //DataHandler
    private Achievements achievements;
    //LastDamagerManager
    //TakenWool
    //ActionBar
    //Protection
    public WorldEditPlugin worldEditPlugin;
    //Restart
    //ProtectedMove
    private ArrayList<PlayerConfig> configPlayer;
    private ArrayList<PlayerData> playerData;

    @Override
    public void onEnable() {
        instance = this;
        //listas
        this.configPlayer = new ArrayList<>();
        this.playerData = new ArrayList<>();
        new Checks();
        UtilsPlugin.corregirColor("&El plugin &cCTW &7se ha encendido correctamente");
        //actionabr
        this.languageHandler = new LanguageHandler(this);
        //scoreboard
        this.scoreboardAdmin = new ScoreboardAdmin(this);
        this.scoreboardAdmin.createScoreboard();
        this.configHandler = new ConfigHandler(this);
        this.teamManager = new TeamManager(this);
        this.menusHandler = new MenusHandler(this);
        this.worldManager = new WorldHandler();
        this.mapConfigManager = new MapConfigManager(this);
        this.mapManager = new MapManager(this);
        this.gameEngine = new GameEngine(this);
        //killstreak
        this.woolManager = new WoolManager(this);
        this.achievements = new Achievements(this);
        //lastdamage
        //Takenwool
        //protection
        //restart
        //protectedmove
        createPlayersFolder();
        registerPlayers();
        loadPlayers();

        this.isEnabled = true;
    }

    @Override
    public void onDisable() {
        this.isDisabling = true;
        saveDataPlayers();
        worldManager.unloadWorldOnShutDown();
        Bukkit.getServer().getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);
        UtilsPlugin.corregirColor("&7El plugin &cCTW &7se ha apagado correctamente");
    }

    private void createPlayersFolder() {
        File folder;
        try {
            folder = new File(getDataFolder() + File.separator + "players");
            if (!folder.exists())
                folder.mkdirs();
        } catch (SecurityException e) {
            UtilsPlugin.sendMessageConsole("&cHa ocurrido un error al crear la carpeta players");
            e.printStackTrace();
        }
    }

    public void savePlayers() {
        for (PlayerConfig playerConfig : this.configPlayer)
            playerConfig.savePlayerConfig();
    }

    public void registerPlayers() {
        File folder = new File(getDataFolder() + File.separator + "players");
        File[] listOfFile = folder.listFiles();
        if (listOfFile != null) {
            for (File file : listOfFile) {
                if (file.isFile()) {
                    String pathName = file.getName();
                    PlayerConfig config = new PlayerConfig(pathName, this);
                    config.registerPlayerConfig();
                    this.configPlayer.add(config);
                }
            }
        }
    }

    public ArrayList<PlayerConfig> getConfigPlayers() {
        return this.configPlayer;
    }

    public boolean fileAlreadyRegister(String pathName) {
        for (PlayerConfig playerConfig : this.configPlayer) {
            if (playerConfig.getPath().equals(pathName))
                return true;
        }
        return false;
    }

    public PlayerConfig getPlayerConfig(String pathName) {
        for (PlayerConfig playerConfig : this.configPlayer) {
            if (playerConfig.getPath().equals(pathName))
                return playerConfig;
        }
        return null;
    }

    public void registerPlayer(String pathName) {
        if (!fileAlreadyRegister(pathName)) {
            PlayerConfig config = new PlayerConfig(pathName, this);
            config.registerPlayerConfig();
            this.configPlayer.add(config);
        }
    }

    public void removeConfigPlayer(String path) {
        for (int i=0;i<this.configPlayer.size();i++) {
            if (this.configPlayer.get(i).getPath().equals(path))
                this.configPlayer.remove(i);
        }
    }

    public void addPlayerData(PlayerData player) {
        this.playerData.add(player);
    }

    public PlayerData getPlayerData(String name) {
        for (PlayerData p : this.playerData) {
            if (p != null && p.getName() != null && p.getName().equals(name))
                return p;
        }
        return null;
    }

    public ArrayList<PlayerData> getPlayersData() {
        return this.playerData;
    }

    public void loadPlayers() {
        for (PlayerConfig playerConfig : this.configPlayer) {
            FileConfiguration players = playerConfig.getConfig();
            String name = players.getString("PlayerName");
            int wins = 0;
            int loses = 0;
            int score = 0;
            int woolsPlaced = 0;
            int distanceRecord = 0;
            int bowKills = 0;
            int meleeKills = 0;

            if (players.contains("Stats.player_wins"))
                wins = players.getInt("Stats.player_wins");
            if (players.contains("Stats.player_loses"))
                loses = players.getInt("Stats.player_loses");
            if (players.contains("Stats.player_score"))
                score = players.getInt("Stats.player_score");
            if (players.contains("Stats.player_wools_placed"))
                woolsPlaced = players.getInt("Stats.player_wools_placed");
            if (players.contains("Stats.player_distance_record"))
                distanceRecord = players.getInt("Stats.player_distance_record");
            if (players.contains("Stats.player_bowkills"))
                bowKills = players.getInt("Stats.player_bowkills");
            if (players.contains("Stats.player_meleekills"))
                meleeKills = players.getInt("Stats.player_meleekills");

            addPlayerData(new PlayerData(name,
                    playerConfig.getPath().replace(".yml", ""),
                    wins,
                    loses,
                    score,
                    distanceRecord,
                    woolsPlaced,
                    bowKills,
                    meleeKills));
        }
    }

    public void saveDataPlayers() {
        for (PlayerData p : this.playerData) {
            PlayerConfig playerConfig = getPlayerConfig(p.getUuid() + ".yml");
            FileConfiguration players;
            if (playerConfig != null) {
                players = playerConfig.getConfig();
                players.set("PlayerName", p.getName());
                players.set("Stats.player_wins", p.getWins());
                players.set("Stats.player_loses", p.getLoses());
                players.set("Stats.player_score", p.getScore());
                players.set("Stats.player_wools_placed", p.getWoolsPlaced());
                players.set("Stats.player_distance_record", p.getDistanceKill());
                players.set("Stats.player_bowkills", p.getBowKills());
                players.set("Stats.player_meleekills", p.getMeleeKills());
            }
        }
        savePlayers();
    }

    public static CTW get() {
        return instance;
    }

    public TeamManager getTeamManager() {
        return this.teamManager;
    }

    public ConfigHandler getConfigHandler() {
        return this.configHandler;
    }

    public LanguageHandler getLanguage() {
        return languageHandler;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public MapConfigManager getMapConfigManager() {
        return mapConfigManager;
    }

    public WorldHandler getWorldManager() {
        return worldManager;
    }

    public MenusHandler getMenusHandler() {
        return menusHandler;
    }

    public WoolManager getWoolManager() {
        return woolManager;
    }

    public WorldEditPlugin getWorldEditPlugin() {
        return worldEditPlugin;
    }

    public Achievements getAchievements() {
        return achievements;
    }
}
