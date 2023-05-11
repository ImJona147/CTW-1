package com.imjona.ctw.managers;

import com.imjona.ctw.CTW;
import com.imjona.ctw.LanguageHandler;
import com.imjona.ctw.netherboard.BPlayerBoard;
import com.imjona.ctw.netherboard.Netherboard;
import com.imjona.ctw.utils.UtilsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ScoreboardAdmin {
    private final CTW ctw;
    private int taskID;
    private long startTime = 0L;

    public ScoreboardAdmin(CTW ctw) {
        this.ctw = ctw;
    }

    public int getTaskID() {
        return taskID;
    }

    public void startTimer() {
        this.startTime = System.currentTimeMillis();
    }

    public void stopTimer() {
        this.startTime = 0L;
    }

    public void createScoreboard() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        final LanguageHandler lang = ctw.getLanguage();
        taskID = scheduler.scheduleSyncRepeatingTask(this.ctw, () -> {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                ScoreboardAdmin.this.updateScoreboard(player, lang);
            }
        }, 0L, 10L);
    }

    protected void updateScoreboard(final Player player, LanguageHandler lang) {
        BPlayerBoard board = Netherboard.instance().getBoard(player);
        if (board == null) {
            board = Netherboard.instance().createBoard(player, lang.getMessage("Scoreboards.MainBoard.TitleBoard"));
        }
        List<String> list = lang.getMessageList("Scoreboards.MainBoard.boardBody");
        for (int i=0;i<list.size();i++) {
            String msg = UtilsPlugin.corregirColor(list.get(i).replaceAll("%player_team_red%", ctw.getTeamManager().countRedTeam().toString())
                    .replaceAll("%player_team_blue%", ctw.getTeamManager().countBlueTeam().toString())
                    .replaceAll("%current_map%", ctw.getMapManager().getCurrentMap())
                    .replaceAll("%ctw_player_kill_streak%", "0" /*String.valueOf(ctw.getKillStreakManager().getStreak(player))*/)
                    .replaceAll("%name_team_red%", ctw.getLanguage().getMessage("Words.RedTeam"))
                    .replaceAll("%name_team_blue%", ctw.getLanguage().getMessage("Words.BlueTeam"))
                    .replaceAll("%status_red_wool%", getWoolStatus("red"))
                    .replaceAll("%status_pink_wool%", getWoolStatus("pink"))
                    .replaceAll("%status_cyan_wool%", getWoolStatus("cyan"))
                    .replaceAll("%status_blue_wool%", getWoolStatus("blue"))
                    .replaceAll("%time_game%", getTime()));
            int position = list.size()-i;
            board.set(msg, position);
        }
    }

    private String getWoolStatus(String wool) {
        switch (wool) {
            case "red":
                if (ctw.getWoolManager().isRedPlaced()) {
                    return ctw.getLanguage().getMessage("Scoreboard.WoolPlaced");
                } else if (ctw.getWoolManager().isRedTaken()) {
                    return ctw.getLanguage().getMessage("Scoreboard.WoolPickedUp");
                } else if (!ctw.getWoolManager().isRedTaken()) {
                    return ctw.getLanguage().getMessage("Scoreboard.WoolNotPlaced");
                }
                break;
            case "pink":
                if (ctw.getWoolManager().isPinkPlaced()) {
                    return ctw.getLanguage().getMessage("Scoreboard.WoolPlaced");
                } else if (ctw.getWoolManager().isPinkTaken()) {
                    return ctw.getLanguage().getMessage("Scoreboard.WoolPickedUp");
                } else if (!ctw.getWoolManager().isPinkTaken()) {
                    return ctw.getLanguage().getMessage("Scoreboard.WoolNotPlaced");
                }
                break;
            case "blue":
                if (ctw.getWoolManager().isBluePlaced()) {
                    return ctw.getLanguage().getMessage("Scoreboard.WoolPlaced");
                } else if (ctw.getWoolManager().isBlueTaken()) {
                    return ctw.getLanguage().getMessage("Scoreboard.WoolPickedUp");
                } else if (!ctw.getWoolManager().isBlueTaken()) {
                    return ctw.getLanguage().getMessage("Scoreboard.WoolNotPlaced");
                }
                break;
            case "cyan":
                if (ctw.getWoolManager().isCyanPlaced()) {
                    return ctw.getLanguage().getMessage("Scoreboard.WoolPlaced");
                } else if (ctw.getWoolManager().isCyanTaken()) {
                    return ctw.getLanguage().getMessage("Scoreboard.WoolPickedUp");
                } else if (!ctw.getWoolManager().isCyanTaken()) {
                    return ctw.getLanguage().getMessage("Scoreboard.WoolNotPlaced");
                }
                break;
        }
        return "None";
    }

    public String getTime() {
        if (startTime != 0) {
            long currentTime = System.currentTimeMillis();
            long time = currentTime - startTime;
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(time);
            return (new SimpleDateFormat("mm:ss")).format(cal.getTime());
        }
        return "00:00";
    }
}
