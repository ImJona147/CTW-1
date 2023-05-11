package com.imjona.ctw.events;

import com.imjona.ctw.CTW;
import com.imjona.ctw.database.PlayerData;
import com.imjona.ctw.enums.Teams;
import com.imjona.ctw.utils.UtilsPlugin;
import com.imjona.ctw.xseries.TitleAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class PlayerDeath implements Listener {
    private final CTW ctw;
    private final List<ItemStack> blackList = new ArrayList<>();

    public PlayerDeath(CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Teams team = ctw.getTeamManager().getTeam(player);
        switch (team) {
            case RED:
                loadChunkCheck(ctw.getMapConfigManager().getRedSpawn());
                event.setRespawnLocation(ctw.getMapConfigManager().getRedSpawn());
                break;
            case BLUE:
                loadChunkCheck(ctw.getMapConfigManager().getBlueSpawn());
                event.setRespawnLocation(ctw.getMapConfigManager().getBlueSpawn());
                break;
            case SPECTATOR:
                event.setRespawnLocation(ctw.getMapConfigManager().getSpectatorSpawn());
        }
    }

    private void respawn(Player player) {
        Teams team = ctw.getTeamManager().getTeam(player);
        switch (team) {
            case RED:
                TitleAPI.sendFullTitle(player, "&a&l¡HAS REAPARECIDO!", "");
                ctw.getTeamManager().respawnOnDeath(player, "red");
                break;
            case BLUE:
                TitleAPI.sendFullTitle(player, "&a&l¡HAS REAPARECIDO!", "");
                ctw.getTeamManager().respawnOnDeath(player, "blue");
                break;
            case SPECTATOR:
                ctw.getTeamManager().respawnSpectator(player);
        }
    }

    private void loadChunkCheck(Location loc) {
        World world = loc.getWorld();
        if (!world.getChunkAt(loc).isLoaded())
            world.loadChunk(world.getChunkAt(loc));
    }

    @EventHandler
    public void onPlayerDeathEvent(final PlayerDeathEvent event) {
        event.setDeathMessage("");
        Bukkit.getServer().getScheduler().runTaskAsynchronously(this.ctw, new Runnable() {
            @Override
            public void run() {
                final Player player = event.getEntity();
                PlayerData pd = ctw.getPlayerData(player.getName());

                if(player.getLastDamageCause() != null) {
                    if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                        Player killer = player.getKiller();
                        PlayerData pdk = ctw.getPlayerData(killer.getName());
                        String msg1 = PlayerDeath.this.ctw.getLanguage().getMessage("ChatMessages.MeleeDeathBroadcast")
                                .replace("%WeaponLogo%", UtilsPlugin.getWeaponLogo(killer))
                                .replaceAll("%KilledPlayer%", UtilsPlugin.getTeamColor(player))
                                .replaceAll("%Killer%", UtilsPlugin.getTeamColor(killer));
                        UtilsPlugin.sendMessageAllPlayers(msg1);
                        if (pd != null && pdk != null) {
                            pd.takeScore(PlayerDeath.this.ctw.getConfigHandler().getInt("Rewards.Score.death"));
                            UtilsPlugin.sendScoreMessage(player, "-" + PlayerDeath.this.ctw.getConfigHandler().getInt("Rewards.Score.death"), null);
                            pdk.addScore(PlayerDeath.this.ctw.getConfigHandler().getInt("Rewards.Score.kill"));
                            pdk.addCoins(killer,
                                    PlayerDeath.this.ctw.getConfigHandler().getDouble("Rewards.Coins.kill"));
                            UtilsPlugin.sendScoreMessage(killer,
                                    "+" + PlayerDeath.this.ctw.getConfigHandler().getInt("Rewards.Score.kill"),
                                    PlayerDeath.this.ctw.getConfigHandler().getInt("Rewards.Coins.kill"));
                            pdk.addMeleeKill();
                            PlayerDeath.this.ctw.getAchievements().checkForAchievementsMelee(killer);
                            PlayerDeath.this.ctw.getAchievements().checkForAchievementsOver(player);
                            //PlayerDeath.this.ctw.getKillStreakManager().addKill(killer);
                        }
                    } else if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                        Player killer = player.getKiller();
                        PlayerData pdk = ctw.getPlayerData(killer.getName());
                        int distance = (int) player.getLocation().distance(killer.getLocation());
                        String msg1 = PlayerDeath.this.ctw.getLanguage().getMessage("ChatMessages.BowDeathBroadcast")
                                .replace("%WeaponLogo%", UtilsPlugin.getWeaponLogo(killer))
                                .replace("%KilledPlayer%", UtilsPlugin.getTeamColor(player))
                                .replace("%Killer%", UtilsPlugin.getTeamColor(killer))
                                .replaceAll("%distance%", String.valueOf(distance));
                        UtilsPlugin.sendMessageAllPlayers(msg1);
                        if (pdk != null && pd != null) {
                            pd.takeScore(PlayerDeath.this.ctw.getConfigHandler().getInt("Rewards.Score.death"));
                            UtilsPlugin.sendScoreMessage(player,
                                    "-" + PlayerDeath.this.ctw.getConfigHandler().getInt("Rewards.Score.death"), null);
                            pdk.addScore(PlayerDeath.this.ctw.getConfigHandler().getInt("Rewards.Score.kill"));
                            pdk.addCoins(killer, PlayerDeath.this.ctw.getConfigHandler().getDouble("Rewards.Coins.kill"));
                            UtilsPlugin.sendScoreMessage(killer,
                                    "+" + PlayerDeath.this.ctw.getConfigHandler().getInt("Rewards.Score.kill"),
                                    PlayerDeath.this.ctw.getConfigHandler().getInt("Rewards.Coins.kill"));
                            pdk.addBowKill();
                            pdk.setDistanceKill(distance);
                            PlayerDeath.this.ctw.getAchievements().checkForAchievementsShooter(killer);
                            PlayerDeath.this.ctw.getAchievements().checkForAchievementsOver(killer);
                            PlayerDeath.this.ctw.getAchievements().checkForAchievementsDistance(killer);
                            //PlayerDeath.this.ctw.getKillStreakManager().addKill(killer);
                        }
                    } else if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID ||
                            player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.LAVA ||
                            player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL) {
                        if (pd != null) {
                            Player killer = pd.getLastDamage().getKiller(player);
                            if (killer != null) {
                                PlayerData pdk = ctw.getPlayerData(killer.getName());
                                if (pdk != null) {
                                    String icon = PlayerDeath.this.ctw.getLanguage().getMessage("Icons.Other");
                                    String msg1 = PlayerDeath.this.ctw.getLanguage().getMessage("ChatMessages.VoidDeathBroadcast")
                                            .replace("%WeaponLogo%", icon)
                                            .replace("%KilledPlayer%", UtilsPlugin.getTeamColor(player))
                                            .replace("%Killer%", UtilsPlugin.getTeamColor(killer));
                                    UtilsPlugin.sendMessageAllPlayers(msg1);
                                    pd.takeScore(PlayerDeath.this.ctw.getConfigHandler().getInt("Rewards.Score.death"));
                                    UtilsPlugin.sendScoreMessage(player,
                                            "-" + PlayerDeath.this.ctw.getConfigHandler().getInt("Rewards.Score.death"), null);
                                    pdk.addScore(PlayerDeath.this.ctw.getConfigHandler().getInt("Rewards.Score.kill"));
                                    pdk.addCoins(killer, PlayerDeath.this.ctw.getConfigHandler().getDouble("Rewards.Coins.kill"));
                                    UtilsPlugin.sendScoreMessage(killer,
                                            "+" + PlayerDeath.this.ctw.getConfigHandler().getInt("Rewards.Score.kill"),
                                            PlayerDeath.this.ctw.getConfigHandler().getInt("Rewards.Coins.kill"));
                                    //PlayerDeath.this.ctw.getKillStreakManager().addKill(killer);
                                    String weaponType = pd.getLastDamage().getWeaponType(player);
                                    if (weaponType.matches("melee")) {
                                        pdk.addMeleeKill();
                                        PlayerDeath.this.ctw.getAchievements().checkForAchievementsMelee(killer);
                                        PlayerDeath.this.ctw.getAchievements().checkForAchievementsOver(killer);
                                    } else if (weaponType.matches("bow")) {
                                        pdk.addBowKill();
                                        PlayerDeath.this.ctw.getAchievements().checkForAchievementsShooter(killer);
                                        PlayerDeath.this.ctw.getAchievements().checkForAchievementsOver(killer);
                                        PlayerDeath.this.ctw.getAchievements().checkForAchievementsDistance(killer);
                                    }
                                }
                            } else if (!PlayerDeath.this.ctw.getTeamManager().isSpectator(player)) {
                                pd.takeScore(PlayerDeath.this.ctw.getConfigHandler().getInt("Rewards.Score.death"));
                                UtilsPlugin.sendScoreMessage(player,
                                        "-" + PlayerDeath.this.ctw.getConfigHandler().getInt("Rewards.Score.death"),
                                        null);
                            }
                        }
                    }
                }
                event.getDrops().clear();
                (new BukkitRunnable() {
                    public void run() {
                        player.spigot().respawn();
                        respawn(player);
                    }
                }).runTaskLater(ctw, 5L);
            }
        });
    }
}
