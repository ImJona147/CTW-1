package com.imjona.ctw.events;

import com.imjona.ctw.CTW;
import com.imjona.ctw.database.PlayerData;
import com.imjona.ctw.enums.ChatType;
import com.imjona.ctw.enums.PlayerOptions;
import com.imjona.ctw.utils.UtilsPlugin;
import com.nametagedit.plugin.NametagEdit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoin implements Listener {
    private final CTW ctw;

    public PlayerJoin(CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void playerJoin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(this.ctw, () -> {
            ctw.registerPlayer(player.getUniqueId().toString() + ".yml");
            if (ctw.getPlayerData(player.getName()) == null) {
                UtilsPlugin.sendMessageConsole("&aCreando nuevo archivo: &e" + player.getUniqueId().toString() + "&a nombre: &6" + player.getName());
                ctw.addPlayerData(new PlayerData(player.getName(), player.getUniqueId().toString(), 0, 0, 0, 0, 0, 0, 0));
            }

            PlayerData pd = ctw.getPlayerData(player.getName());
            ctw.getTeamManager().addSpectator(player);
            ctw.getAchievements().loadInitialAchievementsWool(player);
            ctw.getAchievements().loadInitialAchievementsShooter(player);
            ctw.getAchievements().loadInitialAchievementsMelee(player);
            ctw.getAchievements().loadInitialAchievementsOver(player);
            ctw.getAchievements().loadInitialAchievementsDistance(player);
            //ctw.getKillStreakManager().resetData(player);
            if (pd != null) {
                pd.setChatType(ChatType.TEAM);
                pd.setEditKitType(PlayerOptions.NO_EDIT);
            }
        }, 1L);
    }

    @EventHandler
    public void playerQuit(final PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = ctw.getPlayerData(player.getName());
        Bukkit.getServer().getScheduler().runTaskAsynchronously(this.ctw, () -> {
            ctw.getTeamManager().removeTeamOnDisconnect(player);
            ctw.getWoolManager().removeOnWools(player);
            NametagEdit.getApi().clearNametag(player);
            ctw.getAchievements().removeOnDisconnect(player);
            if (pd != null) {
                pd.setChatType(ChatType.TEAM);
                pd.setEditKitType(PlayerOptions.NO_EDIT);
            }
        });
    }

    @EventHandler
    public void playerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = ctw.getPlayerData(player.getName());
        Bukkit.getServer().getScheduler().runTaskAsynchronously(this.ctw, () -> {
            ctw.getTeamManager().removeTeamOnDisconnect(player);
            NametagEdit.getApi().clearNametag(player);
            ctw.getWoolManager().removeOnWools(player);
            ctw.getAchievements().removeOnDisconnect(player);
            //ctw.getKillStreakManager().removeOnDisconnect(player);
            //ctw.getLastDamageManager().removeData(player);
            if (pd != null) {
                pd.setChatType(ChatType.TEAM);
                pd.setEditKitType(PlayerOptions.NO_EDIT);
            }
        });
    }
}
