package com.imjona.ctw.events;

import com.imjona.ctw.CTW;
import com.imjona.ctw.database.PlayerData;
import com.imjona.ctw.enums.ChatType;
import com.imjona.ctw.enums.Teams;
import com.imjona.ctw.utils.UtilsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class PlayerChat implements Listener {
    private final CTW ctw;

    public PlayerChat(CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (!this.ctw.getConfigHandler().getBoolean("Settings.Modules.DisableChatModule") &&
                !event.isCancelled()) {
            event.setCancelled(true);
            Teams team = ctw.getTeamManager().getTeam(player);
            if (pd != null) {
                if (pd.hasChatType(ChatType.GLOBAL)) {
                    String mFormat = this.ctw.getConfigHandler().getString("ChatFormat.GlobalChatCmd");
                    mFormat = mFormat.replaceAll("<GlobalPrefix>", this.ctw.getLanguage().getMessage("ChatMessages.GlobalPrefix"));
                    mFormat = mFormat.replaceAll("<PlayerName>", UtilsPlugin.getTeamColor(player));
                    mFormat = mFormat.replaceAll("<Message>", event.getMessage());
                    mFormat = mFormat.replaceAll("<Coins>", pd.getCoins().toString());
                    mFormat = mFormat.replaceAll("<Score>", pd.getScore().toString());
                    UtilsPlugin.sendMessageAllPlayers(mFormat);
                } else if (pd.hasChatType(ChatType.TEAM)) {
                    if (team == Teams.SPECTATOR) {
                        List<Player> spec = this.ctw.getTeamManager().getSpectators();
                        for (Player sp : spec) {
                            String msg = this.ctw.getConfigHandler().getString("ChatFormat.Spectator").replaceAll("<TeamPrefix>", this.ctw.getLanguage().getMessage("ChatMessages.SpectatorPrefix"));
                            msg = msg.replaceAll("<PlayerName>", player.getName());
                            msg = msg.replaceAll("<Message>", event.getMessage());
                            msg = msg.replaceAll("<Coins>", pd.getCoins().toString());
                            msg = msg.replaceAll("<Score>", pd.getScore().toString());
                            UtilsPlugin.sendMessagePlayer(sp, msg);
                        }
                    } else if (team == Teams.BLUE) {
                        List<Player> blue = this.ctw.getTeamManager().getTeamBlue();
                        for (Player bp : blue) {
                            String msg = this.ctw.getConfigHandler().getString("ChatFormat.BlueTeam").replaceAll("<TeamPrefix>", this.ctw.getLanguage().getMessage("ChatMessages.TeamPrefix"));
                            msg = msg.replaceAll("<PlayerName>", player.getName());
                            msg = msg.replaceAll("<Message>", event.getMessage());
                            msg = msg.replaceAll("<Coins>", pd.getCoins().toString());
                            msg = msg.replaceAll("<Score>", pd.getScore().toString());
                            UtilsPlugin.sendMessagePlayer(bp, msg);
                        }
                    } else if (team == Teams.RED) {
                        List<Player> red = this.ctw.getTeamManager().getTeamRed();
                        for (Player rp : red) {
                            String msg = this.ctw.getConfigHandler().getString("ChatFormat.RedTeam").replaceAll("<TeamPrefix>", this.ctw.getLanguage().getMessage("ChatMessages.TeamPrefix"));
                            msg = msg.replaceAll("<PlayerName>", player.getName());
                            msg = msg.replaceAll("<Message>", event.getMessage());
                            msg = msg.replaceAll("<Coins>", pd.getCoins().toString());
                            msg = msg.replaceAll("<Score>", pd.getScore().toString());
                            UtilsPlugin.sendMessagePlayer(rp, msg);
                        }
                    }
                }
            }
        }
    }
}
