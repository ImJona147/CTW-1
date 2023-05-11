package com.imjona.ctw.commands;

import com.imjona.ctw.CTW;
import com.imjona.ctw.utils.UtilsPlugin;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCmd implements CommandExecutor {
    private final CTW ctw;

    public LeaveCmd(CTW ctw) {
        this.ctw = ctw;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (label.matches("leave") || label.matches("salir")) {
            if (ctw.getTeamManager().isSpectator(player)) {
                UtilsPlugin.sendMessagePlayer(player, "&c¡Ya estas en modo espectador!");
                return true;
            }
            UtilsPlugin.sendSoundPlayer(player, Sound.CREEPER_HISS);
            UtilsPlugin.sendMessagePlayer(player, ctw.getLanguage().getMessage("ChatMessages.LeaveMsg"));
            ctw.getTeamManager().addSpectator(player);
        }
        return false;
    }
}
