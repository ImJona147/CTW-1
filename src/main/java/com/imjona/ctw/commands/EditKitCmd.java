package com.imjona.ctw.commands;

import com.imjona.ctw.CTW;
import com.imjona.ctw.PlayerConfig;
import com.imjona.ctw.database.PlayerData;
import com.imjona.ctw.enums.PlayerOptions;
import com.imjona.ctw.utils.UtilsPlugin;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EditKitCmd implements CommandExecutor {
    private final CTW ctw;

    public EditKitCmd(CTW ctw) {
        this.ctw = ctw;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
        Player player = (Player) sender;
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (cmd.matches("editkit") || cmd.matches("kitedit")) {
            if (!player.hasPermission("CTW.KitEdit")) {
                UtilsPlugin.sendMessagePlayer(player, ctw.getLanguage().getMessage("ChatMessages.NoPermissions"));
                return true;
            }
            if (pd != null) {
                if (args.length == 0) {
                    if (!ctw.getTeamManager().isSpectator(player)) {
                        UtilsPlugin.sendMessagePlayer(player, ctw.getLanguage().getMessage("ChatMessages.NoSpecToEditkit"));
                        return true;
                    }
                    if (pd.hasEditType(PlayerOptions.NO_EDIT)) {
                        player.setGameMode(GameMode.ADVENTURE);
                        player.teleport(ctw.getMapConfigManager().getSpectatorSpawn());
                        player.getInventory().clear();
                        player.getInventory().setContents(ctw.getMapConfigManager().getStartupKit());
                        pd.setEditKitType(PlayerOptions.EDIT);
                        UtilsPlugin.sendMessagePlayer(player, ctw.getLanguage().getMessage("ChatMessages.KitEditSaveUsage"));
                    } else {
                        UtilsPlugin.sendMessagePlayer(player, ctw.getLanguage().getMessage("ChatMessages.AlreadyKitEdit"));
                    }
                } else if (args.length == 1) {
                    if (args[0].matches("save") || args[0].matches("guardar")) {
                        if (pd.hasEditType(PlayerOptions.EDIT)) {
                            guardarKit(player);
                            player.getInventory().clear();
                            UtilsPlugin.sendMessagePlayer(player, ctw.getLanguage().getMessage("ChatMessages.KitEditSave"));
                            pd.setEditKitType(PlayerOptions.NO_EDIT);
                            player.setGameMode(GameMode.SPECTATOR);
                        } else {
                            UtilsPlugin.sendMessagePlayer(player, ctw.getLanguage().getMessage("ChatMessages.NoKitEditing"));
                        }
                    } else {
                        UtilsPlugin.sendMessagePlayer(player, "&c¡Argumentos invalidos, por favor usa &7/kitedit save!");
                    }
                } else {
                    UtilsPlugin.sendMessagePlayer(player, "&c¡Argumentos invalidos, por favor usa &7/kitedit save!");
                }
            }
        }
        return false;
    }

    private void guardarKit(Player player) {
        for (PlayerConfig playerConfigs : ctw.getConfigPlayers()) {
            FileConfiguration players = playerConfigs.getConfig();
            for (int i=0;i<player.getInventory().getSize();i++) {
                ItemStack item = player.getInventory().getItem(i);
                players.set("Kit." + ctw.getMapManager().getCurrentMap() + ".StartupKit." + i, item);
            }
            ctw.savePlayers();
        }
    }
}
