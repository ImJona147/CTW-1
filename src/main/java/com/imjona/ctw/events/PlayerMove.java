package com.imjona.ctw.events;

import com.imjona.ctw.CTW;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void onVoid(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getLocation().getY() < CTW.get().getMapConfigManager().getMinHight())
            player.setHealth(0.0D);
    }
}
