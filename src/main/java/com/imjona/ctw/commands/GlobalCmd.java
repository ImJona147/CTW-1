package com.imjona.ctw.commands;

import com.imjona.ctw.CTW;
import com.imjona.ctw.database.PlayerData;
import com.imjona.ctw.utils.UtilsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GlobalCmd implements CommandExecutor {
    private final CTW ctw;

    public GlobalCmd(CTW ctw) {
        this.ctw = ctw;
    }
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
        Player player = (Player) sender;
        if (cmd.matches("g") || cmd.matches("global")) {
            if (args.length >= 1) {
                sendGlobalMsg(args, player);
                return true;
            }
            UtilsPlugin.sendSoundPlayer(player, Sound.NOTE_PLING);
            UtilsPlugin.sendMessagePlayer(player, ctw.getLanguage().getMessage("ChatMessages.GlobalChatUsage"));
        }
        return false;
    }

    private void sendGlobalMsg(final String[] args, Player player) {
        PlayerData pd = ctw.getPlayerData(player.getName());
        Bukkit.getServer().getScheduler().runTaskAsynchronously(this.ctw, () -> {
            StringBuilder msg = new StringBuilder();
            byte b;
            int i;
            String[] arrayOfString;
            for (i = (arrayOfString = args).length,b = 0;b < i;) {
                String s = arrayOfString[b];
                msg.append(s).append(" ");
                b++;
            }
            if (pd != null) {
                String mFormat = ctw.getConfigHandler().getString("ChatFormat.GlobalChatCmd");
                mFormat = mFormat.replaceAll("<GlobalPrefix>", ctw.getLanguage().getMessage("ChatMessages.GlobalPrefix"));
                mFormat = mFormat.replaceAll("<PlayerName>", UtilsPlugin.getTeamColor(player));
                mFormat = mFormat.replaceAll("<Message>", msg.toString());
                mFormat = mFormat.replaceAll("<Coins>", pd.getCoins().toString());
                mFormat = mFormat.replaceAll("<Score>", pd.getScore().toString());
                UtilsPlugin.sendMessageAllPlayers(mFormat);
            }
        });
    }
}
