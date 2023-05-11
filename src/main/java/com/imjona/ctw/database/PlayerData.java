package com.imjona.ctw.database;

import com.imjona.ctw.CTW;
import com.imjona.ctw.enums.ChatType;
import com.imjona.ctw.enums.PlayerOptions;
import com.imjona.ctw.managers.game.LastDamage;
import com.imjona.ctw.utils.UtilsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlayerData {
    private final String name;
    private final String uuid;
    private final int wins;
    private final int loses;
    private int score;
    private int distanceRecord;
    private int woolsPlaced;
    private int bowKills;
    private int meleeKills;
    private PlayerOptions options;
    private ChatType chatType;
    private final LastDamage lastDamage;

    public PlayerData(String name,
                      String uuid,
                      int wins,
                      int loses,
                      int score,
                      int distanceRecord,
                      int woolsPlaced,
                      int bowKills,
                      int meleeKills) {
        this.name = name;
        this.uuid = uuid;
        this.wins = wins;
        this.loses = loses;
        this.score = score;
        this.distanceRecord = distanceRecord;
        this.woolsPlaced = woolsPlaced;
        this.bowKills = bowKills;
        this.meleeKills = meleeKills;
        this.options = PlayerOptions.NO_EDIT;
        this.chatType = ChatType.TEAM;
        this.lastDamage = new LastDamage();
    }

    /*
        Tipo de Chat
     */

    public void setChatType(ChatType chat) {
        this.chatType = chat;
    }

    public boolean hasChatType(ChatType type) {
        switch (type) {
            case TEAM:
                return this.chatType == ChatType.TEAM;
            case GLOBAL:
                return this.chatType == ChatType.GLOBAL;
            case CLAN:
                return this.chatType == ChatType.CLAN;
        }
        return false;
    }

    /*
        Opciones
     */

    public void setEditKitType(PlayerOptions type) {
        this.options = type;
    }

    public boolean hasEditType(PlayerOptions po) {
        switch (po) {
            case EDIT:
                return this.options == PlayerOptions.EDIT;
            case NO_EDIT:
                return this.options == PlayerOptions.NO_EDIT;
        }
        return false;
    }

    /*
     * DATOS SOBRE PUNTOS DE JUGADOR
     *
     */
    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int sc) {
        this.score += sc;
    }

    public void takeScore(int sc) {
        this.score = score - sc;
    }

    public void removeScore() {
        this.score--;
    }

    public Integer getScore() {
        return this.score;
    }

    /*
     * DATOS SOBRE ASESINATO CON ARCO A MAYOR DISTANCIA
     *
     */

    public void setDistanceKill(int distance) {
        int count = getDistanceKill();
        if (distance > count)
            this.distanceRecord = distance;
    }

    public int getDistanceKill() {
        return this.distanceRecord;
    }

    /*
     *
     * DATOS DE LANAS COLOCADA
     *
     */

    public void addWoolPlaced() {
        this.woolsPlaced++;
    }

    public Integer getWoolsPlaced() {
        return this.woolsPlaced;
    }

    /*
     * DATOS DE ASESINATOS
     *
     */

    public void addBowKill() {
        this.bowKills++;

    }

    public void addMeleeKill() {
        this.meleeKills++;
    }

    public int getTotalKills() {
        return meleeKills + bowKills;
    }

    public int getBowKills() {
        return this.bowKills;
    }

    public int getMeleeKills() {
        return this.meleeKills;
    }

    /*
     * Getter's
     */

    public String getName() {
        return this.name;
    }

    public String getUuid() {
        return this.uuid;
    }

    public int getWins() {
        return this.wins;
    }

    public int getLoses() {
        return this.loses;
    }

    public LastDamage getLastDamage() {
        return lastDamage;
    }

    /*
     * OTRAS COSAS
     */


    public Boolean hasAchievement(Player player, String achievement) {
        if (achievement.matches("SNIPER1") || achievement.matches("SNIPER2") || achievement.matches("SNIPER3") || achievement.matches("SNIPER4") || achievement.matches("SNIPER5"))
            return CTW.get().getAchievements().hasAchievementDistance(player, Achievements.Sniper.valueOf(achievement));
        if (achievement.matches("MELEE1") || achievement.matches("MELEE2") || achievement.matches("MELEE3") || achievement.matches("MELEE4") || achievement.matches("MELEE5"))
            return CTW.get().getAchievements().hasAchievementMelee(player, Achievements.Melee.valueOf(achievement));
        if (achievement.matches("OVERPOWERED1") || achievement.matches("OVERPOWERED2") || achievement.matches("OVERPOWERED3") || achievement.matches("OVERPOWERED4") || achievement.matches("OVERPOWERED5"))
            return CTW.get().getAchievements().hasAchievementOver(player, Achievements.OverPowered.valueOf(achievement));
        if (achievement.matches("SHOOTER1") || achievement.matches("SHOOTER2") || achievement.matches("SHOOTER3") || achievement.matches("SHOOTER4") || achievement.matches("SHOOTER5"))
            return CTW.get().getAchievements().hasAchievementShooter(player, Achievements.Shooter.valueOf(achievement));
        if (achievement.matches("WOOLMASTER1") || achievement.matches("WOOLMASTER2") || achievement.matches("WOOLMASTER3") || achievement.matches("WOOLMASTER4") || achievement.matches("WOOLMASTER5"))
            return CTW.get().getAchievements().hasAchievementWool(player, Achievements.Wool.valueOf(achievement));
        return true;
    }

    /*
     * ECONOMIA
     */

    public void addCoins(Player player, Double amount) {
        if (player.hasPermission("CTW.3xCoinMultiplier")) {
            CTW.get().economy.depositPlayer(player, amount * 3.0D);
        } else if (player.hasPermission("CTW.2xCoinMultiplier")) {
            CTW.get().economy.depositPlayer(player, amount * 2.0D);
        } else {
            CTW.get().economy.depositPlayer(player, amount);
        }
    }

    public void takeCoins(Player player, Double amount) {
        double balance = CTW.get().economy.getBalance(player);
        if (balance >= amount) {
            CTW.get().economy.withdrawPlayer(player, amount);
        } else {
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 3.0F);
            String msg = CTW.get().getLanguage().getMessage("ChatMessages.NotEnoughCoins").replaceAll("%coinsNeeded%", String.valueOf(amount));
            UtilsPlugin.sendMessagePlayer(player, msg.replaceAll("%balance%", Double.toString(balance)));
        }
    }

    public Double getCoins() {
        double balance;
        Player player = Bukkit.getPlayer(this.name);
        balance = CTW.get().economy.getBalance(player);
        return balance;
    }
}
