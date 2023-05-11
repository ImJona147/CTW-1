package com.imjona.ctw.commands;

import com.imjona.ctw.CTW;
import com.imjona.ctw.database.PlayerData;
import com.imjona.ctw.enums.PlayerOptions;
import com.imjona.ctw.menu.JoinMenu;
import com.imjona.ctw.utils.UtilsPlugin;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCmd implements CommandExecutor {
    private final CTW ctw;

    public JoinCmd(CTW ctw) {
        this.ctw = ctw;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
        Player player = (Player) sender;
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (cmd.matches("join") || cmd.matches("jugar")) {
            if (args.length == 0) {
                if (!ctw.getTeamManager().isSpectator(player)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 3.0F);
                    UtilsPlugin.sendMessagePlayer(player, ctw.getLanguage().getMessage("ChatMessages.AlreadyJoinedTeam"));
                    return true;
                }
                if (pd != null && !pd.hasEditType(PlayerOptions.EDIT)) {
                    JoinMenu.joinMenu(player);
                    return true;
                }
                UtilsPlugin.sendMessagePlayer(player, "&c¡No puedes entrar a un equipo mientras estás editando tu kit!");
            } else {
                UtilsPlugin.sendMessagePlayer((Player)sender, ctw.getLanguage().getMessage("ChatMessages.JoinUsage"));
            }
        }
        return false;
    }
}
