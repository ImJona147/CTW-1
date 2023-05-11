package com.imjona.ctw.managers;

import com.imjona.ctw.CTW;
import com.imjona.ctw.PlayerConfig;
import com.imjona.ctw.enums.GameState;
import com.imjona.ctw.enums.Teams;
import com.imjona.ctw.utils.UtilsPlugin;
import com.imjona.ctw.xseries.TitleAPI;
import com.nametagedit.plugin.NametagEdit;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class TeamManager {
    private final CTW ctw;
    private final List<Player> spectator = new ArrayList<>();
    private final List<Player> redTeam = new ArrayList<>();
    private final List<Player> blueTeam = new ArrayList<>();
    public TeamManager(CTW ctw) {
        this.ctw = ctw;
    }

    public void addSpectator(final Player p) {
        this.redTeam.remove(p);
        this.blueTeam.remove(p);
        this.spectator.add(p);

        NametagEdit.getApi().clearNametag(p);
        NametagEdit.getApi().setNametag(p, "&a&lE &a ", "");

        respawnSpectator(p);

        sendJoinMessage(p);
    }

    private void sendJoinMessage(final Player p) {
        int delay = ctw.getConfigHandler().getInt("Settings.JoinMessageDelay") / 1000;
        Bukkit.getScheduler().runTaskLaterAsynchronously(this.ctw, () -> {
            List<String> list = ctw.getLanguage().getMessageList("ChatMessages.JoinMessage");
            for (String msg : list) {
                UtilsPlugin.sendMessagePlayer(p, msg);
                p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 3.0F, 3.0F);
            }
        },delay * 20L);
    }

    public void respawnSpectator(final Player p) {
        resetPlayer(p);

        Location loc = ctw.getMapConfigManager().getSpectatorSpawn();
        Bukkit.getServer().getScheduler().runTask(this.ctw, () -> {
            if (loc != null) {
                p.teleport(loc);
                p.setGameMode(GameMode.ADVENTURE);
            }
        });
    }

    public void addRedTeam(final Player p) {
        this.redTeam.add(p);
        this.blueTeam.remove(p);
        this.spectator.remove(p);

        if (countRedTeam() < ctw.getMapConfigManager().getMaxPlayers() || p.hasPermission("CTW.joinfullteam")) {
            resetPlayer(p);

            NametagEdit.getApi().clearNametag(p);
            NametagEdit.getApi().setNametag(p, "&c&lR &c ", "");

            respawnTeam(p, "Red");
        } else {
            UtilsPlugin.sendSoundPlayer(p, Sound.NOTE_PLING);
            UtilsPlugin.sendMessagePlayer(p, ctw.getLanguage().getMessage("ChatMessages.RedTeamFull"));
        }
    }

    public void addBlueTeam(final Player p) {
        this.redTeam.remove(p);
        this.blueTeam.add(p);
        this.spectator.remove(p);

        if (countBlueTeam() < ctw.getMapConfigManager().getMaxPlayers() || p.hasPermission("CTW.joinfullteam")) {
            resetPlayer(p);

            NametagEdit.getApi().clearNametag(p);
            NametagEdit.getApi().setNametag(p, "&9&lA &9 ", "");

            respawnTeam(p, "Blue");
        } else {
            UtilsPlugin.sendSoundPlayer(p, Sound.NOTE_PLING);
            UtilsPlugin.sendMessagePlayer(p, ctw.getLanguage().getMessage("ChatMessages.BlueTeamFull"));
        }
    }

    public void autoAddTeam(Player player) {
        if (countRedTeam() < ctw.getMapConfigManager().getMaxPlayers() && countRedTeam() <= countBlueTeam()) {
            addRedTeam(player);
        } else if (countBlueTeam() < ctw.getMapConfigManager().getMaxPlayers() && countBlueTeam() <= countRedTeam()) {
            addBlueTeam(player);
        } else if (player.hasPermission("CTW.joinfullteams")) {
            if (countRedTeam() <= countBlueTeam()) {
                addRedTeam(player);
            } else {
                addBlueTeam(player);
            }
        } else {
            UtilsPlugin.sendMessagePlayer(player, ctw.getLanguage().getMessage("ChatMessages.AllTeamsFull"));
            UtilsPlugin.sendSoundPlayer(player, Sound.NOTE_BASS_DRUM);
        }
    }

    public void respawnTeam(final Player p, String team) {
        Bukkit.getServer().getScheduler().runTask(this.ctw, () -> {
            if (ctw.getGameEngine().gameState == GameState.COUNTDOWN) {
                p.setGameMode(GameMode.ADVENTURE);
                p.setAllowFlight(true);
                p.setFlying(true);
                p.setNoDamageTicks(400);
                return;
            }

            p.setGameMode(GameMode.SURVIVAL);

            switch (team) {
                case "Red":
                    p.teleport(ctw.getMapConfigManager().getRedSpawn());
                    setArmor(p, 16711680);
                    break;
                case "Blue":
                    p.teleport(ctw.getMapConfigManager().getBlueSpawn());
                    setArmor(p, 18175);
                    break;
            }
            String title = ctw.getLanguage().getMessage("TitleMessages.Join" + team + "Team.title");
            String subtitle = ctw.getLanguage().getMessage("TitleMessages.Join" + team + "Team.subtitle");
            TitleAPI.sendFullTitle(p, title, subtitle);
            List<String> msg = ctw.getLanguage().getMessageList("ChatMessages.Join" + team + "Team");
            for (String s : msg)
                UtilsPlugin.sendMessagePlayer(p, s);
            p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 3.0F, 3.0F);
            setKitPlayer(p);
        });
    }

    public void respawnOnDeath(Player p, String team) {
        if (ctw.getGameEngine().gameState == GameState.COUNTDOWN) {
            p.setGameMode(GameMode.ADVENTURE);
            p.setAllowFlight(true);
            p.setFlying(true);
            p.setNoDamageTicks(400);
            return;
        }
        p.setGameMode(GameMode.SURVIVAL);
        switch (team) {
            case "red":
                p.teleport(ctw.getMapConfigManager().getRedSpawn());
                setArmor(p, 16711680);
                break;
            case "blue":
                p.teleport(ctw.getMapConfigManager().getBlueSpawn());
                setArmor(p, 18175);
                break;
        }
        setKitPlayer(p);
    }

    public void playerSetWonSpectator(final Player p) {
        p.setGameMode(GameMode.ADVENTURE);
        p.setAllowFlight(true);
        p.setFlying(true);
        p.setNoDamageTicks(400);
        UtilsPlugin.sendMessageConsole("&aSe activo no daño a los jugadores");
    }

    public void respawnAllPlayers() {

    }

    private void setKitPlayer(Player p){
        for (PlayerConfig playerConfig : ctw.getConfigPlayers()) {
            FileConfiguration players = playerConfig.getConfig();
            if (players.contains("Kit." + ctw.getMapManager().getCurrentMap() + ".StartupKit.")) {
                ArrayList<ItemStack> items = new ArrayList<>();
                for (int i=0;i<36;i++) {
                    ItemStack item = players.getItemStack("Kit." + ctw.getMapManager().getCurrentMap() + ".StartupKit." + i);
                    items.add(item);
                    p.getInventory().setContents(items.toArray(new ItemStack[0]));
                }
                continue;
            }
            p.getInventory().setContents(ctw.getMapConfigManager().getStartupKit());
        }
    }

    public void removeTeamOnDisconnect(Player p) {
        this.redTeam.remove(p);
        this.blueTeam.remove(p);
        this.spectator.remove(p);
    }

    public boolean isSpectator(Player player) {
        return this.spectator.contains(player);
    }

    public boolean isRedTeam(Player player) {
        return this.redTeam.contains(player);
    }

    public boolean isBlueTeam(Player player) {
        return this.blueTeam.contains(player);
    }

    public Integer countSpectators() {
        return this.spectator.size();
    }

    public Integer countRedTeam() {
        return this.redTeam.size();
    }

    public Integer countBlueTeam() {
        return this.blueTeam.size();
    }

    public List<Player> getSpectators() {
        return new ArrayList<>(this.spectator);
    }

    public List<Player> getTeamRed() {
        return new ArrayList<>(this.redTeam);
    }

    public List<Player> getTeamBlue() {
        return new ArrayList<>(this.blueTeam);
    }

    public List<Player> getAllTeamPlayers() {
        List<Player> players = new ArrayList<>();
        players.addAll(this.redTeam);
        players.addAll(this.blueTeam);
        return players;
    }

    public Teams getTeam(Player p) {
        Teams team = null;
        if (this.spectator.contains(p)) {
            team = Teams.SPECTATOR;
        } else if (this.redTeam.contains(p)) {
            team = Teams.RED;
        } else if (this.blueTeam.contains(p)) {
            team = Teams.BLUE;
        }
        return team;
    }

    public void setArmor(Player p, int color) {
        //COLOR AZUL INT: 18175
        //COLOR ROJO INT: 16711680
        ItemStack item = new ItemStack(Material.LEATHER_HELMET, 1);
        LeatherArmorMeta meta = (LeatherArmorMeta)item.getItemMeta();
        meta.setColor(Color.fromRGB(color));
        item.setItemMeta(meta);
        p.getInventory().setHelmet(item);
        item = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        meta = (LeatherArmorMeta)item.getItemMeta();
        meta.setColor(Color.fromRGB(color));
        item.setItemMeta(meta);
        p.getInventory().setChestplate(item);
        item = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        meta = (LeatherArmorMeta)item.getItemMeta();
        meta.setColor(Color.fromRGB(color));
        item.setItemMeta(meta);
        p.getInventory().setLeggings(item);
        item = new ItemStack(Material.LEATHER_BOOTS, 1);
        meta = (LeatherArmorMeta)item.getItemMeta();
        meta.setColor(Color.fromRGB(color));
        item.setItemMeta(meta);
        p.getInventory().setBoots(item);
    }

    public void resetPlayer(Player p) {
        p.getInventory().clear();
        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);
        p.setHealth(20.0D);
        p.setFoodLevel(20);
        p.setSaturation(4.0F);
        p.setExp(0.0F);
        p.setLevel(0);
        if (!p.getActivePotionEffects().isEmpty())
            for (PotionEffect po : p.getActivePotionEffects())
                p.removePotionEffect(po.getType());
    }
}
