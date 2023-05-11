package com.imjona.ctw.database;

import java.util.HashMap;
import java.util.Map;

import com.imjona.ctw.CTW;
import com.imjona.ctw.utils.UtilsPlugin;
import com.imjona.ctw.xseries.TitleAPI;
import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Achievements {
    private final CTW ctw;
    private final Map<Player, Sniper> sniper = new HashMap<>();
    private final Map<Player, Melee> melee = new HashMap<>();
    private final Map<Player, OverPowered> overpowered = new HashMap<>();
    private final Map<Player, Shooter> shooter = new HashMap<>();
    private final Map<Player, Wool> wool = new HashMap<>();

    /*
     * Enums
     */

    public enum Sniper {
        SNIPER1, SNIPER2, SNIPER3, SNIPER4, SNIPER5
    }

    public enum Melee {
        MELEE1, MELEE2,MELEE3, MELEE4, MELEE5
    }

    public enum OverPowered {
        OVERPOWERED1, OVERPOWERED2, OVERPOWERED3, OVERPOWERED4, OVERPOWERED5
    }

    public enum Shooter {
        SHOOTER1, SHOOTER2, SHOOTER3, SHOOTER4, SHOOTER5
    }

    public enum Wool {
        WOOLMASTER1, WOOLMASTER2, WOOLMASTER3, WOOLMASTER4, WOOLMASTER5
    }

    /*
     * DATOS SOBRE LOGROS DE ARCO
     * DistanceAchievementHandler
     *
     */

    public Achievements(CTW ctw) {
        this.ctw = ctw;
    }

    public void loadInitialAchievementsDistance(Player player) {
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (pd != null) {
            int distance = pd.getDistanceKill();
            if (distance >= ctw.getConfigHandler().getInt("Achievements.Sniper.Distance-For-V")) {
                if (!sniper.containsKey(player))
                    this.sniper.put(player, Sniper.SNIPER5);
            } else if (distance >= ctw.getConfigHandler().getInt("Achievements.Sniper.Distance-For-IV")) {
                if (!sniper.containsKey(player))
                    this.sniper.put(player, Sniper.SNIPER4);
            } else if (distance >= ctw.getConfigHandler().getInt("Achievements.Sniper.Distance-For-III")) {
                if (!sniper.containsKey(player))
                    this.sniper.put(player, Sniper.SNIPER3);
            } else if (distance >= ctw.getConfigHandler().getInt("Achievements.Sniper.Distance-For-II")) {
                if (!sniper.containsKey(player))
                    this.sniper.put(player, Sniper.SNIPER2);
            } else if (distance >= ctw.getConfigHandler().getInt("Achievements.Sniper.Distance-For-I")) {
                if (!sniper.containsKey(player))
                    this.sniper.put(player, Sniper.SNIPER1);
            } else if (!this.sniper.containsKey(player)) {
                this.sniper.put(player, null);
            }
        }
    }

    public void removeOnDisconnect(Player player) {
        this.sniper.remove(player);
        this.melee.remove(player);
        this.overpowered.remove(player);
        this.shooter.remove(player);
        this.wool.remove(player);
    }

    public void checkForAchievementsDistance(Player player) {
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (pd != null) {
            int distance = pd.getDistanceKill();
            Sniper currentA = this.sniper.get(player);
            if (distance >= ctw.getConfigHandler().getInt("Achievements.Sniper.Distance-For-V")) {
                if (currentA != Sniper.SNIPER5) {
                    awardAchievementDistance(player, Sniper.SNIPER5);
                }
            } else if (distance >= ctw.getConfigHandler().getInt("Achievements.Sniper.Distance-For-IV")) {
                if (currentA != Sniper.SNIPER4) {
                    awardAchievementDistance(player, Sniper.SNIPER4);
                }
            } else if (distance >= ctw.getConfigHandler().getInt("Achievements.Sniper.Distance-For-III")) {
                if (currentA != Sniper.SNIPER3) {
                    awardAchievementDistance(player, Sniper.SNIPER3);
                }
            } else if (distance >= ctw.getConfigHandler().getInt("Achievements.Sniper.Distance-For-II")) {
                if (currentA != Sniper.SNIPER2) {
                    awardAchievementDistance(player, Sniper.SNIPER2);
                }
            } else if (distance >= ctw.getConfigHandler().getInt("Achievements.Sniper.Distance-For-I")) {
                if (currentA != Sniper.SNIPER1) {
                    awardAchievementDistance(player, Sniper.SNIPER1);
                }
            }
        }
    }

    private void awardAchievementDistance(final Player player, Sniper achievement) {
        String msg = "";
        if (achievement == Sniper.SNIPER5) {
            msg = ctw.getLanguage().getMessage("Achievements.Sniper-V");
        } else if (achievement == Sniper.SNIPER4) {
            msg = ctw.getLanguage().getMessage("Achievements.Sniper-IV");
        } else if (achievement == Sniper.SNIPER3) {
            msg = ctw.getLanguage().getMessage("Achievements.Sniper-III");
        } else if (achievement == Sniper.SNIPER2) {
            msg = ctw.getLanguage().getMessage("Achievements.Sniper-II");
        } else if (achievement == Sniper.SNIPER1) {
            msg = ctw.getLanguage().getMessage("Achievements.Sniper-I");
        }
        this.sniper.put(player, achievement);
        String chatMsg = ctw.getLanguage().getMessage("ChatMessages.AwardAchievement").replace("%AchievementName%", msg);
        String title = ctw.getLanguage().getMessage("TitleMessages.AwardAchievement.title").replace("%AchievementName%", msg);
        String subtitle = ctw.getLanguage().getMessage("TitleMessages.AwardAchievement.subtitle").replace("%AchievementName%", msg);
        player.awardAchievement(Achievement.SNIPE_SKELETON);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 3.0F);
        UtilsPlugin.sendMessagePlayer(player, chatMsg);
        TitleAPI.sendFullTitle(player, title, subtitle);
        Bukkit.getScheduler().runTaskLaterAsynchronously(ctw, new Runnable() {
            public void run() {
                player.removeAchievement(Achievement.SNIPE_SKELETON);
            }
        }, 20L);
    }

    public String getCurrentAchievementDistance(Player player) {
        Sniper current = this.sniper.get(player);
        if (current != null) {
            if (current == Sniper.SNIPER5)
                return ctw.getLanguage().getMessage("Achievements.Sniper-V");
            if (current == Sniper.SNIPER4)
                return ctw.getLanguage().getMessage("Achievements.Sniper-IV");
            if (current == Sniper.SNIPER3)
                return ctw.getLanguage().getMessage("Achievements.Sniper-III");
            if (current == Sniper.SNIPER2)
                return ctw.getLanguage().getMessage("Achievements.Sniper-II");
            if (current == Sniper.SNIPER1)
                return ctw.getLanguage().getMessage("Achievements.Sniper-I");
        }
        return "None";
    }

    public boolean hasAchievementDistance(Player player, Sniper achievement) {
        if (this.sniper.containsKey(player)) {
            Sniper current = this.sniper.get(player);
            if (achievement == Sniper.SNIPER1) {
                return current == Sniper.SNIPER1 || current == Sniper.SNIPER2 || current == Sniper.SNIPER3 ||
                        current == Sniper.SNIPER4 || current == Sniper.SNIPER5;
            } else if (achievement == Sniper.SNIPER2) {
                return current == Sniper.SNIPER2 || current == Sniper.SNIPER3 ||
                        current == Sniper.SNIPER4 || current == Sniper.SNIPER5;
            } else if (achievement == Sniper.SNIPER3) {
                return current == Sniper.SNIPER3 || current == Sniper.SNIPER4 || current == Sniper.SNIPER5;
            } else if (achievement == Sniper.SNIPER4) {
                return current == Sniper.SNIPER4 || current == Sniper.SNIPER5;
            } else if (achievement == Sniper.SNIPER5) {
                return current == Sniper.SNIPER5;
            }
        }
        return false;
    }

    public String getAchievementNameDistance(Sniper achievement) {
        String data = "";
        if (achievement == Sniper.SNIPER5)
            return ctw.getLanguage().getMessage("Achievements.Sniper-V");
        if (achievement == Sniper.SNIPER4)
            return ctw.getLanguage().getMessage("Achievements.Sniper-IV");
        if (achievement == Sniper.SNIPER3)
            return ctw.getLanguage().getMessage("Achievements.Sniper-III");
        if (achievement == Sniper.SNIPER2)
            return ctw.getLanguage().getMessage("Achievements.Sniper-II");
        if (achievement == Sniper.SNIPER1)
            return ctw.getLanguage().getMessage("Achievements.Sniper-I");
        return data;
    }

    public Integer getRequiredKillsDistance(Sniper achievement) {
        if (achievement == Sniper.SNIPER5)
            return ctw.getConfigHandler().getInt("Achievements.Sniper.Distance-For-V");
        if (achievement == Sniper.SNIPER4)
            return ctw.getConfigHandler().getInt("Achievements.Sniper.Distance-For-IV");
        if (achievement == Sniper.SNIPER3)
            return ctw.getConfigHandler().getInt("Achievements.Sniper.Distance-For-III");
        if (achievement == Sniper.SNIPER2)
            return ctw.getConfigHandler().getInt("Achievements.Sniper.Distance-For-II");
        if (achievement == Sniper.SNIPER1)
            return ctw.getConfigHandler().getInt("Achievements.Sniper.Distance-For-I");
        return 0;
    }

    /*
     * DATOS SOBRE LOGROS ASESINATOS
     * MeleeAchievementHandler
     */

    public void loadInitialAchievementsMelee(Player player) {
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (pd != null) {
            int meleeKills = pd.getMeleeKills();
            if (meleeKills >= ctw.getConfigHandler().getInt("Achievements.Melee-Kills.Kills-For-V")) {
                if (!melee.containsKey(player))
                    this.melee.put(player, Melee.MELEE5);
            } else if (meleeKills >= ctw.getConfigHandler().getInt("Achievements.Melee-Kills.Kills-For-IV")) {
                if (!melee.containsKey(player))
                    this.melee.put(player, Melee.MELEE4);
            } else if (meleeKills >= ctw.getConfigHandler().getInt("Achievements.Melee-Kills.Kills-For-III")) {
                if (!melee.containsKey(player))
                    this.melee.put(player, Melee.MELEE3);
            } else if (meleeKills >= ctw.getConfigHandler().getInt("Achievements.Melee-Kills.Kills-For-II")) {
                if (!melee.containsKey(player))
                    this.melee.put(player, Melee.MELEE2);
            } else if (meleeKills >= ctw.getConfigHandler().getInt("Achievements.Melee-Kills.Kills-For-I")) {
                if (!melee.containsKey(player))
                    this.melee.put(player, Melee.MELEE1);
            } else if (!this.melee.containsKey(player)) {
                this.melee.put(player, null);
            }
        }
    }

    public void checkForAchievementsMelee(Player player) {
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (pd != null) {
            int meleeKills = pd.getMeleeKills();
            Melee currentA = this.melee.get(player);
            if (meleeKills >= ctw.getConfigHandler().getInt("Achievements.Melee-Kills.Kills-For-V")) {
                if (currentA != Melee.MELEE5) {
                    awardAchievementMelee(player, Melee.MELEE5);
                }
            } else if (meleeKills >= ctw.getConfigHandler().getInt("Achievements.Melee-Kills.Kills-For-IV")) {
                if (currentA != Melee.MELEE4) {
                    awardAchievementMelee(player, Melee.MELEE4);
                }
            } else if (meleeKills >= ctw.getConfigHandler().getInt("Achievements.Melee-Kills.Kills-For-III")) {
                if (currentA != Melee.MELEE3) {
                    awardAchievementMelee(player, Melee.MELEE3);
                }
            } else if (meleeKills >= ctw.getConfigHandler().getInt("Achievements.Melee-Kills.Kills-For-II")) {
                if (currentA != Melee.MELEE2) {
                    awardAchievementMelee(player, Melee.MELEE2);
                }
            } else if (meleeKills >= ctw.getConfigHandler().getInt("Achievements.Melee-Kills.Kills-For-I")) {
                if (currentA != Melee.MELEE1) {
                    awardAchievementMelee(player, Melee.MELEE1);
                }
            }
        }
    }

    private void awardAchievementMelee(final Player player, Melee achievement) {
        String msg = "";
        if (achievement == Melee.MELEE5) {
            msg = ctw.getLanguage().getMessage("Achievements.Melee-Kills-V");
        } else if (achievement == Melee.MELEE4) {
            msg = ctw.getLanguage().getMessage("Achievements.Melee-Kills-IV");
        } else if (achievement == Melee.MELEE3) {
            msg = ctw.getLanguage().getMessage("Achievements.Melee-Kills-III");
        } else if (achievement == Melee.MELEE2) {
            msg = ctw.getLanguage().getMessage("Achievements.Melee-Kills-II");
        } else if (achievement == Melee.MELEE1) {
            msg = ctw.getLanguage().getMessage("Achievements.Melee-Kills-I");
        }
        this.melee.put(player, achievement);
        String chatMsg = ctw.getLanguage().getMessage("ChatMessages.AwardAchievement").replace("%AchievementName%", msg);
        String title = ctw.getLanguage().getMessage("TitleMessages.AwardAchievement.title").replace("%AchievementName%", msg);
        String subtitle = ctw.getLanguage().getMessage("TitleMessages.AwardAchievement.subtitle").replace("%AchievementName%", msg);
        player.awardAchievement(Achievement.OVERKILL);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 3.0F);
        UtilsPlugin.sendMessagePlayer(player, chatMsg);
        TitleAPI.sendFullTitle(player, title, subtitle);
        Bukkit.getScheduler().runTaskLaterAsynchronously(ctw, new Runnable() {
            public void run() {
                player.removeAchievement(Achievement.OVERKILL);
            }
        }, 20L);
    }

    public String getCurrentAchievementMelee(Player player) {
        Melee current = this.melee.get(player);
        if (current != null) {
            if (current == Melee.MELEE5)
                return ctw.getLanguage().getMessage("Achievements.Melee-Kills-V");
            if (current == Melee.MELEE4)
                return ctw.getLanguage().getMessage("Achievements.Melee-Kills-IV");
            if (current == Melee.MELEE3)
                return ctw.getLanguage().getMessage("Achievements.Melee-Kills-III");
            if (current == Melee.MELEE2)
                return ctw.getLanguage().getMessage("Achievements.Melee-Kills-II");
            if (current == Melee.MELEE1)
                return ctw.getLanguage().getMessage("Achievements.Melee-Kills-I");
        }
        return "None";
    }

    public boolean hasAchievementMelee(Player player, Melee achievement) {
        if (this.melee.containsKey(player)) {
            Melee current = this.melee.get(player);
            if (achievement == Melee.MELEE1) {
                return current == Melee.MELEE1 || current == Melee.MELEE2 || current == Melee.MELEE3 ||
                        current == Melee.MELEE4 || current == Melee.MELEE5;
            } else if (achievement == Melee.MELEE2) {
                return current == Melee.MELEE2 || current == Melee.MELEE3 ||
                        current == Melee.MELEE4 || current == Melee.MELEE5;
            } else if (achievement == Melee.MELEE3) {
                return current == Melee.MELEE3 || current == Melee.MELEE4 || current == Melee.MELEE5;
            } else if (achievement == Melee.MELEE4) {
                return current == Melee.MELEE4 || current == Melee.MELEE5;
            } else if (achievement == Melee.MELEE5) {
                return current == Melee.MELEE5;
            }
        }
        return false;
    }

    public String getAchievementNameMelee(Melee achievement) {
        if (achievement == Melee.MELEE5)
            return ctw.getLanguage().getMessage("Achievements.Melee-Kills-V");
        if (achievement == Melee.MELEE4)
            return ctw.getLanguage().getMessage("Achievements.Melee-Kills-IV");
        if (achievement == Melee.MELEE3)
            return ctw.getLanguage().getMessage("Achievements.Melee-Kills-III");
        if (achievement == Melee.MELEE2)
            return ctw.getLanguage().getMessage("Achievements.Melee-Kills-II");
        if (achievement == Melee.MELEE1)
            return ctw.getLanguage().getMessage("Achievements.Melee-Kills-I");
        return "None";
    }

    public Integer getRequiredKillsMelee(Melee achievement) {
        if (achievement == Melee.MELEE5)
            return ctw.getConfigHandler().getInt("Achievements.Melee-Kills.Kills-For-V");
        if (achievement == Melee.MELEE4)
            return ctw.getConfigHandler().getInt("Achievements.Melee-Kills.Kills-For-IV");
        if (achievement == Melee.MELEE3)
            return ctw.getConfigHandler().getInt("Achievements.Melee-Kills.Kills-For-III");
        if (achievement == Melee.MELEE2)
            return ctw.getConfigHandler().getInt("Achievements.Melee-Kills.Kills-For-II");
        if (achievement == Melee.MELEE1)
            return ctw.getConfigHandler().getInt("Achievements.Melee-Kills.Kills-For-I");
        return 0;
    }

    /*
     * OverpoweredAchievementHandler
     *
     */

    public void loadInitialAchievementsOver(Player player) {
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (pd != null) {
            int kills = pd.getTotalKills();
            if (kills >= ctw.getConfigHandler().getInt("Achievements.Overpowered.Kills-For-V")) {
                if (!overpowered.containsKey(player))
                    this.overpowered.put(player, OverPowered.OVERPOWERED5);
            } else if (kills >= ctw.getConfigHandler().getInt("Achievements.Overpowered.Kills-For-IV")) {
                if (!overpowered.containsKey(player))
                    this.overpowered.put(player, OverPowered.OVERPOWERED4);
            } else if (kills >= ctw.getConfigHandler().getInt("Achievements.Overpowered.Kills-For-III")) {
                if (!overpowered.containsKey(player))
                    this.overpowered.put(player, OverPowered.OVERPOWERED3);
            } else if (kills >= ctw.getConfigHandler().getInt("Achievements.Overpowered.Kills-For-II")) {
                if (!overpowered.containsKey(player))
                    this.overpowered.put(player, OverPowered.OVERPOWERED2);
            } else if (kills >= ctw.getConfigHandler().getInt("Achievements.Overpowered.Kills-For-I")) {
                if (!overpowered.containsKey(player))
                    this.overpowered.put(player, OverPowered.OVERPOWERED1);
            } else if (!this.overpowered.containsKey(player)) {
                this.overpowered.put(player, null);
            }
        }
    }

    public void checkForAchievementsOver(Player player) {
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (pd != null) {
            int kills = pd.getTotalKills();
            OverPowered currentA = this.overpowered.get(player);
            if (kills >= ctw.getConfigHandler().getInt("Achievements.Overpowered.Kills-For-V")) {
                if (currentA != OverPowered.OVERPOWERED5) {
                    awardAchievementOver(player, OverPowered.OVERPOWERED5);
                }
            } else if (kills >= ctw.getConfigHandler().getInt("Achievements.Overpowered.Kills-For-IV")) {
                if (currentA != OverPowered.OVERPOWERED4) {
                    awardAchievementOver(player, OverPowered.OVERPOWERED4);
                }
            } else if (kills >= ctw.getConfigHandler().getInt("Achievements.Overpowered.Kills-For-III")) {
                if (currentA != OverPowered.OVERPOWERED3) {
                    awardAchievementOver(player, OverPowered.OVERPOWERED3);
                }
            } else if (kills >= ctw.getConfigHandler().getInt("Achievements.Overpowered.Kills-For-II")) {
                if (currentA != OverPowered.OVERPOWERED2) {
                    awardAchievementOver(player, OverPowered.OVERPOWERED2);
                }
            } else if (kills >= ctw.getConfigHandler().getInt("Achievements.Overpowered.Kills-For-I")) {
                if (currentA != OverPowered.OVERPOWERED1) {
                    awardAchievementOver(player, OverPowered.OVERPOWERED1);
                }
            }
        }
    }

    private void awardAchievementOver(final Player player, OverPowered achievement) {
        String msg = "";
        if (achievement == OverPowered.OVERPOWERED5) {
            msg = ctw.getLanguage().getMessage("Achievements.Overpowered-V");
        } else if (achievement == OverPowered.OVERPOWERED4) {
            msg = ctw.getLanguage().getMessage("Achievements.Overpowered-IV");
        } else if (achievement == OverPowered.OVERPOWERED3) {
            msg = ctw.getLanguage().getMessage("Achievements.Overpowered-III");
        } else if (achievement == OverPowered.OVERPOWERED2) {
            msg = ctw.getLanguage().getMessage("Achievements.Overpowered-II");
        } else if (achievement == OverPowered.OVERPOWERED1) {
            msg = ctw.getLanguage().getMessage("Achievements.Overpowered-I");
        }
        this.overpowered.put(player, achievement);
        String chatMsg = ctw.getLanguage().getMessage("ChatMessages.AwardAchievement").replace("%AchievementName%", msg);
        String title = ctw.getLanguage().getMessage("TitleMessages.AwardAchievement.title").replace("%AchievementName%", msg);
        String subtitle = ctw.getLanguage().getMessage("TitleMessages.AwardAchievement.subtitle").replace("%AchievementName%", msg);
        player.awardAchievement(Achievement.OVERPOWERED);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 3.0F);
        UtilsPlugin.sendMessagePlayer(player, chatMsg);
        TitleAPI.sendFullTitle(player, title, subtitle);
        Bukkit.getScheduler().runTaskLaterAsynchronously(ctw, new Runnable() {
            public void run() {
                player.removeAchievement(Achievement.OVERPOWERED);
            }
        }, 20L);
    }

    public String getCurrentAchievementOver(Player player) {
        OverPowered current = this.overpowered.get(player);
        if (current != null) {
            if (current == OverPowered.OVERPOWERED5)
                return ctw.getLanguage().getMessage("Achievements.Overpowered-V");
            if (current == OverPowered.OVERPOWERED4)
                return ctw.getLanguage().getMessage("Achievements.Overpowered-IV");
            if (current == OverPowered.OVERPOWERED3)
                return ctw.getLanguage().getMessage("Achievements.Overpowered-III");
            if (current == OverPowered.OVERPOWERED2)
                return ctw.getLanguage().getMessage("Achievements.Overpowered-II");
            if (current == OverPowered.OVERPOWERED1)
                return ctw.getLanguage().getMessage("Achievements.Overpowered-I");
        }
        return "None";
    }

    public boolean hasAchievementOver(Player player, OverPowered achievement) {
        if (this.overpowered.containsKey(player)) {
            OverPowered current = this.overpowered.get(player);
            if (achievement == OverPowered.OVERPOWERED1) {
                return current == OverPowered.OVERPOWERED1 || current == OverPowered.OVERPOWERED2 || current == OverPowered.OVERPOWERED3 ||
                        current == OverPowered.OVERPOWERED4 || current == OverPowered.OVERPOWERED5;
            } else if (achievement == OverPowered.OVERPOWERED2) {
                return current == OverPowered.OVERPOWERED2 || current == OverPowered.OVERPOWERED3 ||
                        current == OverPowered.OVERPOWERED4 || current == OverPowered.OVERPOWERED5;
            } else if (achievement == OverPowered.OVERPOWERED3) {
                return current == OverPowered.OVERPOWERED3 || current == OverPowered.OVERPOWERED4 || current == OverPowered.OVERPOWERED5;
            } else if (achievement == OverPowered.OVERPOWERED4) {
                return current == OverPowered.OVERPOWERED4 || current == OverPowered.OVERPOWERED5;
            } else if (achievement == OverPowered.OVERPOWERED5) {
                return current == OverPowered.OVERPOWERED5;
            }
        }
        return false;
    }

    public String getAchievementNameOver(OverPowered achievement) {
        if (achievement == OverPowered.OVERPOWERED5)
            return ctw.getLanguage().getMessage("Achievements.Overpowered-V");
        if (achievement == OverPowered.OVERPOWERED4)
            return ctw.getLanguage().getMessage("Achievements.Overpowered-IV");
        if (achievement == OverPowered.OVERPOWERED3)
            return ctw.getLanguage().getMessage("Achievements.Overpowered-III");
        if (achievement == OverPowered.OVERPOWERED2)
            return ctw.getLanguage().getMessage("Achievements.Overpowered-II");
        if (achievement == OverPowered.OVERPOWERED1)
            return ctw.getLanguage().getMessage("Achievements.Overpowered-I");
        return "None";
    }

    public Integer getRequiredKillsOver(OverPowered achievement) {
        if (achievement == OverPowered.OVERPOWERED5)
            return ctw.getConfigHandler().getInt("Achievements.Overpowered.Kills-For-V");
        if (achievement == OverPowered.OVERPOWERED4)
            return ctw.getConfigHandler().getInt("Achievements.Overpowered.Kills-For-IV");
        if (achievement == OverPowered.OVERPOWERED3)
            return ctw.getConfigHandler().getInt("Achievements.Overpowered.Kills-For-III");
        if (achievement == OverPowered.OVERPOWERED2)
            return ctw.getConfigHandler().getInt("Achievements.Overpowered.Kills-For-II");
        if (achievement == OverPowered.OVERPOWERED1)
            return ctw.getConfigHandler().getInt("Achievements.Overpowered.Kills-For-I");
        return 0;
    }

    /*
     * WoolAchievementHandler
     *
     */

    public void loadInitialAchievementsWool(Player player) {
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (pd != null) {
            int wools = pd.getWoolsPlaced();
            if (wools >= ctw.getConfigHandler().getInt("Achievements.WoolMaster.Wools-For-V")) {
                if (!wool.containsKey(player))
                    this.wool.put(player, Wool.WOOLMASTER5);
            } else if (wools >= ctw.getConfigHandler().getInt("Achievements.WoolMaster.Wools-For-IV")) {
                if (!wool.containsKey(player))
                    this.wool.put(player, Wool.WOOLMASTER4);
            } else if (wools >= ctw.getConfigHandler().getInt("Achievements.WoolMaster.Wools-For-III")) {
                if (!wool.containsKey(player))
                    this.wool.put(player, Wool.WOOLMASTER3);
            } else if (wools >= ctw.getConfigHandler().getInt("Achievements.WoolMaster.Wools-For-II")) {
                if (!wool.containsKey(player))
                    this.wool.put(player, Wool.WOOLMASTER2);
            } else if (wools >= ctw.getConfigHandler().getInt("Achievements.WoolMaster.Wools-For-I")) {
                if (!wool.containsKey(player))
                    this.wool.put(player, Wool.WOOLMASTER1);
            } else if (!this.wool.containsKey(player)) {
                this.wool.put(player, null);
            }
        }
    }

    public void checkForAchievementsWool(Player player) {
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (pd != null) {
            int wool = pd.getWoolsPlaced();
            Wool currentA = this.wool.get(player);
            if (wool >= ctw.getConfigHandler().getInt("Achievements.WoolMaster.Wools-For-V")) {
                if (currentA != Wool.WOOLMASTER5) {
                    awardAchievementWool(player, Wool.WOOLMASTER5);
                }
            } else if (wool >= ctw.getConfigHandler().getInt("Achievements.WoolMaster.Wools-For-IV")) {
                if (currentA != Wool.WOOLMASTER4) {
                    awardAchievementWool(player, Wool.WOOLMASTER4);
                }
            } else if (wool >= ctw.getConfigHandler().getInt("Achievements.WoolMaster.Wools-For-III")) {
                if (currentA != Wool.WOOLMASTER3) {
                    awardAchievementWool(player, Wool.WOOLMASTER3);
                }
            } else if (wool >= ctw.getConfigHandler().getInt("Achievements.WoolMaster.Wools-For-II")) {
                if (currentA != Wool.WOOLMASTER2) {
                    awardAchievementWool(player, Wool.WOOLMASTER2);
                }
            } else if (wool >= ctw.getConfigHandler().getInt("Achievements.WoolMaster.Wools-For-I")) {
                if (currentA != Wool.WOOLMASTER1) {
                    awardAchievementWool(player, Wool.WOOLMASTER1);
                }
            }
        }
    }

    private void awardAchievementWool(final Player player, Wool achievement) {
        String msg = "";
        if (achievement == Wool.WOOLMASTER5) {
            msg = ctw.getLanguage().getMessage("Achievements.WoolMaster-V");
        } else if (achievement == Wool.WOOLMASTER4) {
            msg = ctw.getLanguage().getMessage("Achievements.WoolMaster-IV");
        } else if (achievement == Wool.WOOLMASTER3) {
            msg = ctw.getLanguage().getMessage("Achievements.WoolMaster-III");
        } else if (achievement == Wool.WOOLMASTER2) {
            msg = ctw.getLanguage().getMessage("Achievements.WoolMaster-II");
        } else if (achievement == Wool.WOOLMASTER1) {
            msg = ctw.getLanguage().getMessage("Achievements.WoolMaster-I");
        }
        this.wool.put(player, achievement);
        String chatMsg = ctw.getLanguage().getMessage("ChatMessages.AwardAchievement").replace("%AchievementName%", msg);
        String title = ctw.getLanguage().getMessage("TitleMessages.AwardAchievement.title").replace("%AchievementName%", msg);
        String subtitle = ctw.getLanguage().getMessage("TitleMessages.AwardAchievement.subtitle").replace("%AchievementName%", msg);
        player.awardAchievement(Achievement.KILL_WITHER);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 3.0F);
        UtilsPlugin.sendMessagePlayer(player, chatMsg);
        TitleAPI.sendFullTitle(player, title, subtitle);
        Bukkit.getScheduler().runTaskLaterAsynchronously(ctw, new Runnable() {
            public void run() {
                player.removeAchievement(Achievement.KILL_WITHER);
            }
        }, 20L);
    }

    public String getCurrentAchievementWool(Player player) {
        Wool current = this.wool.get(player);
        if (current != null) {
            if (current == Wool.WOOLMASTER5)
                return ctw.getLanguage().getMessage("Achievements.WoolMaster-V");
            if (current == Wool.WOOLMASTER4)
                return ctw.getLanguage().getMessage("Achievements.WoolMaster-IV");
            if (current == Wool.WOOLMASTER3)
                return ctw.getLanguage().getMessage("Achievements.WoolMaster-III");
            if (current == Wool.WOOLMASTER2)
                return ctw.getLanguage().getMessage("Achievements.WoolMaster-II");
            if (current == Wool.WOOLMASTER1)
                return ctw.getLanguage().getMessage("Achievements.WoolMaster-I");
        }
        return "None";
    }

    public boolean hasAchievementWool(Player player, Wool achievement) {
        if (this.wool.containsKey(player)) {
            Wool current = this.wool.get(player);
            if (achievement == Wool.WOOLMASTER1) {
                return current == Wool.WOOLMASTER1 || current == Wool.WOOLMASTER2 || current == Wool.WOOLMASTER3 ||
                        current == Wool.WOOLMASTER4 || current == Wool.WOOLMASTER5;
            } else if (achievement == Wool.WOOLMASTER2) {
                return current == Wool.WOOLMASTER2 || current == Wool.WOOLMASTER3 ||
                        current == Wool.WOOLMASTER4 || current == Wool.WOOLMASTER5;
            } else if (achievement == Wool.WOOLMASTER3) {
                return current == Wool.WOOLMASTER3 || current == Wool.WOOLMASTER4 || current == Wool.WOOLMASTER5;
            } else if (achievement == Wool.WOOLMASTER4) {
                return current == Wool.WOOLMASTER4 || current == Wool.WOOLMASTER5;
            } else if (achievement == Wool.WOOLMASTER5) {
                return current == Wool.WOOLMASTER5;
            }
        }
        return false;
    }

    public String getAchievementNameWool(Wool achievement) {
        if (achievement == Wool.WOOLMASTER5)
            return ctw.getLanguage().getMessage("Achievements.WoolMaster-V");
        if (achievement == Wool.WOOLMASTER4)
            return ctw.getLanguage().getMessage("Achievements.WoolMaster-IV");
        if (achievement == Wool.WOOLMASTER3)
            return ctw.getLanguage().getMessage("Achievements.WoolMaster-III");
        if (achievement == Wool.WOOLMASTER2)
            return ctw.getLanguage().getMessage("Achievements.WoolMaster-II");
        if (achievement == Wool.WOOLMASTER1)
            return ctw.getLanguage().getMessage("Achievements.WoolMaster-I");
        return "None";
    }

    public Integer getRequiredKillsWool(Wool achievement) {
        if (achievement == Wool.WOOLMASTER5)
            return ctw.getConfigHandler().getInt("Achievements.WoolMaster.Wools-For-V");
        if (achievement == Wool.WOOLMASTER4)
            return ctw.getConfigHandler().getInt("Achievements.WoolMaster.Wools-For-IV");
        if (achievement == Wool.WOOLMASTER3)
            return ctw.getConfigHandler().getInt("Achievements.WoolMaster.Wools-For-III");
        if (achievement == Wool.WOOLMASTER2)
            return ctw.getConfigHandler().getInt("Achievements.WoolMaster.Wools-For-II");
        if (achievement == Wool.WOOLMASTER1)
            return ctw.getConfigHandler().getInt("Achievements.WoolMaster.Wools-For-I");
        return 0;
    }

    /*
     *
     * ShooterAchievementHandler
     *
     */

    public void loadInitialAchievementsShooter(Player player) {
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (pd != null) {
            int bowKills = pd.getBowKills();
            if (bowKills >= ctw.getConfigHandler().getInt("Achievements.Bow-Kills.Kills-For-V")) {
                if (!shooter.containsKey(player))
                    this.shooter.put(player, Shooter.SHOOTER5);
            } else if (bowKills >= ctw.getConfigHandler().getInt("Achievements.Bow-Kills.Kills-For-IV")) {
                if (!shooter.containsKey(player))
                    this.shooter.put(player, Shooter.SHOOTER4);
            } else if (bowKills >= ctw.getConfigHandler().getInt("Achievements.Bow-Kills.Kills-For-III")) {
                if (!shooter.containsKey(player))
                    this.shooter.put(player, Shooter.SHOOTER3);
            } else if (bowKills >= ctw.getConfigHandler().getInt("Achievements.Bow-Kills.Kills-For-II")) {
                if (!shooter.containsKey(player))
                    this.shooter.put(player, Shooter.SHOOTER2);
            } else if (bowKills >= ctw.getConfigHandler().getInt("Achievements.Bow-Kills.Kills-For-I")) {
                if (!shooter.containsKey(player))
                    this.shooter.put(player, Shooter.SHOOTER1);
            } else if (!this.shooter.containsKey(player)) {
                this.shooter.put(player, null);
            }
        }
    }

    public void checkForAchievementsShooter(Player player) {
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (pd != null) {
            int bowKills = pd.getBowKills();
            Shooter currentA = this.shooter.get(player);
            if (bowKills >= ctw.getConfigHandler().getInt("Achievements.Bow-Kills.Kills-For-V")) {
                if (currentA != Shooter.SHOOTER5) {
                    awardAchievementShooter(player, Shooter.SHOOTER5);
                }
            } else if (bowKills >= ctw.getConfigHandler().getInt("Achievements.Bow-Kills.Kills-For-IV")) {
                if (currentA != Shooter.SHOOTER4) {
                    awardAchievementShooter(player, Shooter.SHOOTER4);
                }
            } else if (bowKills >= ctw.getConfigHandler().getInt("Achievements.Bow-Kills.Kills-For-III")) {
                if (currentA != Shooter.SHOOTER3) {
                    awardAchievementShooter(player, Shooter.SHOOTER3);
                }
            } else if (bowKills >= ctw.getConfigHandler().getInt("Achievements.Bow-Kills.Kills-For-II")) {
                if (currentA != Shooter.SHOOTER2) {
                    awardAchievementShooter(player, Shooter.SHOOTER2);
                }
            } else if (bowKills >= ctw.getConfigHandler().getInt("Achievements.Bow-Kills.Kills-For-I")) {
                if (currentA != Shooter.SHOOTER1) {
                    awardAchievementShooter(player, Shooter.SHOOTER1);
                }
            }
        }
    }

    private void awardAchievementShooter(final Player player, Shooter achievement) {
        String msg = "";
        if (achievement == Shooter.SHOOTER5) {
            msg = ctw.getLanguage().getMessage("Achievements.Bow-Kills-V");
        } else if (achievement == Shooter.SHOOTER4) {
            msg = ctw.getLanguage().getMessage("Achievements.Bow-Kills-IV");
        } else if (achievement == Shooter.SHOOTER3) {
            msg = ctw.getLanguage().getMessage("Achievements.Bow-Kills-III");
        } else if (achievement == Shooter.SHOOTER2) {
            msg = ctw.getLanguage().getMessage("Achievements.Bow-Kills-II");
        } else if (achievement == Shooter.SHOOTER1) {
            msg = ctw.getLanguage().getMessage("Achievements.Bow-Kills-I");
        }
        this.shooter.put(player, achievement);
        String chatMsg = ctw.getLanguage().getMessage("ChatMessages.AwardAchievement").replace("%AchievementName%", msg);
        String title = ctw.getLanguage().getMessage("TitleMessages.AwardAchievement.title").replace("%AchievementName%", msg);
        String subtitle = ctw.getLanguage().getMessage("TitleMessages.AwardAchievement.subtitle").replace("%AchievementName%", msg);
        player.awardAchievement(Achievement.SNIPE_SKELETON);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 3.0F);
        UtilsPlugin.sendMessagePlayer(player, chatMsg);
        TitleAPI.sendFullTitle(player, title, subtitle);
        Bukkit.getScheduler().runTaskLaterAsynchronously(ctw, new Runnable() {
            public void run() {
                player.removeAchievement(Achievement.SNIPE_SKELETON);
            }
        }, 20L);
    }

    public String getCurrentAchievementShooter(Player player) {
        String data = null;
        Shooter current = this.shooter.get(player);
        if (current != null) {
            if (current == Shooter.SHOOTER5)
                return ctw.getLanguage().getMessage("Achievements.Bow-Kills-V");
            if (current == Shooter.SHOOTER4)
                return ctw.getLanguage().getMessage("Achievements.Bow-Kills-IV");
            if (current == Shooter.SHOOTER3)
                return ctw.getLanguage().getMessage("Achievements.Bow-Kills-III");
            if (current == Shooter.SHOOTER2)
                return ctw.getLanguage().getMessage("Achievements.Bow-Kills-II");
            if (current == Shooter.SHOOTER1)
                return ctw.getLanguage().getMessage("Achievements.Bow-Kills-I");
        }
        return data;
    }

    public boolean hasAchievementShooter(Player player, Shooter achievement) {
        if (this.shooter.containsKey(player)) {
            Shooter current = this.shooter.get(player);
            if (achievement == Shooter.SHOOTER1) {
                return current == Shooter.SHOOTER1 || current == Shooter.SHOOTER2 || current == Shooter.SHOOTER3 ||
                        current == Shooter.SHOOTER4 || current == Shooter.SHOOTER5;
            } else if (achievement == Shooter.SHOOTER2) {
                return current == Shooter.SHOOTER2 || current == Shooter.SHOOTER3 ||
                        current == Shooter.SHOOTER4 || current == Shooter.SHOOTER5;
            } else if (achievement == Shooter.SHOOTER3) {
                return current == Shooter.SHOOTER3 || current == Shooter.SHOOTER4 || current == Shooter.SHOOTER5;
            } else if (achievement == Shooter.SHOOTER4) {
                return current == Shooter.SHOOTER4 || current == Shooter.SHOOTER5;
            } else if (achievement == Shooter.SHOOTER5) {
                return current == Shooter.SHOOTER5;
            }
        }
        return false;
    }

    public String getAchievementNameShooter(Shooter achievement) {
        if (achievement == Shooter.SHOOTER5)
            return ctw.getLanguage().getMessage("Achievements.Bow-Kills-V");
        if (achievement == Shooter.SHOOTER4)
            return ctw.getLanguage().getMessage("Achievements.Bow-Kills-IV");
        if (achievement == Shooter.SHOOTER3)
            return ctw.getLanguage().getMessage("Achievements.Bow-Kills-III");
        if (achievement == Shooter.SHOOTER2)
            return ctw.getLanguage().getMessage("Achievements.Bow-Kills-II");
        if (achievement == Shooter.SHOOTER1)
            return ctw.getLanguage().getMessage("Achievements.Bow-Kills-I");
        return "None";
    }

    public Integer getRequiredKillsShooter(Shooter achievement) {
        if (achievement == Shooter.SHOOTER5)
            return ctw.getConfigHandler().getInt("Achievements.Bow-Kills.Kills-For-V");
        if (achievement == Shooter.SHOOTER4)
            return ctw.getConfigHandler().getInt("Achievements.Bow-Kills.Kills-For-IV");
        if (achievement == Shooter.SHOOTER3)
            return ctw.getConfigHandler().getInt("Achievements.Bow-Kills.Kills-For-III");
        if (achievement == Shooter.SHOOTER2)
            return ctw.getConfigHandler().getInt("Achievements.Bow-Kills.Kills-For-II");
        if (achievement == Shooter.SHOOTER1)
            return ctw.getConfigHandler().getInt("Achievements.Bow-Kills.Kills-For-I");
        return 0;
    }
}
