package com.imjona.ctw.commands;

import com.imjona.ctw.CTW;
import com.imjona.ctw.database.PlayerData;
import com.imjona.ctw.utils.UtilsPlugin;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class StatsCmd implements CommandExecutor {
    private final CTW ctw;

    public StatsCmd(CTW ctw) {
        this.ctw = ctw;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (label.matches("stats") || label.matches("estadisticas") || label.matches("estadistica")) {
            if (args.length == 0) {
                sendStats(player);
            }
        }
        return false;
    }

    private void sendStats(Player player) {
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (pd != null) {
            int score = pd.getScore();
            int meleeKills = pd.getMeleeKills();
            int bowKills = pd.getBowKills();
            int totalKills = meleeKills + bowKills;
            int distance = pd.getDistanceKill();
            int wools = pd.getWoolsPlaced();
            int wins = pd.getWins();
            int loses = pd.getLoses();
            List<String> list = new ArrayList<>(ctw.getLanguage().getMessageList("ChatStats"));
            List<String> rawMsg = new ArrayList<>();
            for (String msg : list) {
                String s1 = msg.replaceAll("%score%", String.valueOf(score));
                String a1 = s1.replaceAll("%meleeKills%", String.valueOf(meleeKills));
                String a2 = a1.replaceAll("%bowKills%", String.valueOf(bowKills));
                String a3 = a2.replaceAll("%totalKills%", String.valueOf(totalKills));
                String a4 = a3.replaceAll("%distance%", String.valueOf(distance));
                String a5 = a4.replaceAll("%wools%", String.valueOf(wools));
                String a6 = a5.replaceAll("%wins%", String.valueOf(wins));
                String a7 = a6.replaceAll("%loses%", String.valueOf(loses));
                String a8 = a7.replaceAll("&", "§");
                rawMsg.add(a8);
            }
            String woolA = ctw.getAchievements().getCurrentAchievementWool(player);
            if (woolA != null) {
                String s2 = "   &f" + woolA;
                rawMsg.add(s2.replaceAll("&", "§"));
            }
            String shooterA = ctw.getAchievements().getCurrentAchievementShooter(player);
            if (shooterA != null) {
                String s3 = "   &f" + shooterA;
                rawMsg.add(s3.replaceAll("&", "§"));
            }
            String meleeA = ctw.getAchievements().getCurrentAchievementMelee(player);
            if (meleeA != null) {
                String s4 = "   &f" + meleeA;
                rawMsg.add(s4.replaceAll("&", "§"));
            }
            String overpoweredA = ctw.getAchievements().getCurrentAchievementOver(player);
            if (overpoweredA != null) {
                String s5 = "   &f" + overpoweredA;
                rawMsg.add(s5.replaceAll("&", "§"));
            }
            String sniperA = ctw.getAchievements().getCurrentAchievementDistance(player);
            if (sniperA != null) {
                String s6 = "   &f" + sniperA;
                rawMsg.add(s6.replaceAll("&", "§"));
            }
            rawMsg.add(" ");
            String[] data = new String[rawMsg.size()];
            data = rawMsg.toArray(data);
            player.sendMessage(data);
            UtilsPlugin.sendSoundPlayer(player, Sound.SUCCESSFUL_HIT);
        }
    }
}
