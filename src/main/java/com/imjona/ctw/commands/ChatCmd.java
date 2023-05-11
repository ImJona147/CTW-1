package com.imjona.ctw.commands;

import com.imjona.ctw.CTW;
import com.imjona.ctw.database.PlayerData;
import com.imjona.ctw.enums.ChatType;
import com.imjona.ctw.utils.UtilsPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatCmd implements CommandExecutor {
    private final CTW ctw;

    public ChatCmd(CTW ctw) {
        this.ctw = ctw;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
        Player player = (Player) sender;
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (cmd.matches("chat") || cmd.matches("cc")) {
            if (args.length == 1) {
                if (pd != null) {
                    if (args[0].matches("global")) {
                        if (!pd.hasChatType(ChatType.TEAM)) {
                            UtilsPlugin.sendMessagePlayer(player, "&c¡Ya tienes activado el chat en global!");
                            return true;
                        }
                        UtilsPlugin.sendMessagePlayer(player, "&f¡Has cambiado tú chat a modo: &a&lGLOBAL&f!");
                        pd.setChatType(ChatType.GLOBAL);
                    } else if (args[0].matches("team")) {
                        if (!pd.hasChatType(ChatType.GLOBAL)) {
                            UtilsPlugin.sendMessagePlayer(player, "&c¡Ya tienes activado el chat en team!");
                            return true;
                        }
                        UtilsPlugin.sendMessagePlayer(player, "&f¡Has cambiado tú chat a modo: &a&lTEAM&f!");
                        pd.setChatType(ChatType.TEAM);
                    } else if (args[0].matches("clan")) {
                        UtilsPlugin.sendMessagePlayer(player, "&c¡Está opción aún esta en desarrollo!");
                    } else {
                        UtilsPlugin.sendMessagePlayer(player, "&c¡Uso correcto: &7/chat <global/team/clan> &c!");
                    }
                }
                return true;
            }
            UtilsPlugin.sendMessagePlayer(player, "&c¡Uso correcto: &7/chat <global/team/clan> &c!");
        }
        return false;
    }
}
