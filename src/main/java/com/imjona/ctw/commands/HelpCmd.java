package com.imjona.ctw.commands;

import com.imjona.ctw.utils.UtilsPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (label.matches("ayuda") || label.matches("help")) {
            if (args.length == 0) {
                UtilsPlugin.sendMessagePlayer(player, "&c&m---------------&8[&e&lCTW&8]&c&m---------------");
                UtilsPlugin.sendMessagePlayer(player, "");
                UtilsPlugin.sendMessagePlayer(player, "&b/join - /jugar &f- &7Ingresa a una partida");
                UtilsPlugin.sendMessagePlayer(player, "&b/stats - /estadística &f- &7Ve tus estadísticas");
                UtilsPlugin.sendMessagePlayer(player, "&b/g <msg> - /global <msg> &f- &7Habla de forma global");
                UtilsPlugin.sendMessagePlayer(player, "&b/leave - /salir &f- &7Salir de una partida");
                UtilsPlugin.sendMessagePlayer(player, "&b/chat <global/team> &f- &7Activa los diferentes chat");
                UtilsPlugin.sendMessagePlayer(player, "&b/editkit - /kitedit &f- &7Edita el kit a tu gusto");
                UtilsPlugin.sendMessagePlayer(player, "&b/editkit save - /kitedit save &f- &7Guarda los cambios de tu kit");
            }
            return false;
        }
        return false;
    }
}
