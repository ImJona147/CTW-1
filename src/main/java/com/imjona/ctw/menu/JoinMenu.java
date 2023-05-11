package com.imjona.ctw.menu;

import com.imjona.ctw.CTW;
import com.imjona.ctw.MenusHandler;
import com.imjona.ctw.utils.UtilsBuilder;
import com.imjona.ctw.utils.UtilsPlugin;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class JoinMenu implements Listener {
    public static void joinMenu(Player player) {
        MenusHandler menus = CTW.get().getMenusHandler();
        String title = UtilsPlugin.corregirColor(menus.getString("JoinMenu.TitleMenu"));
        int slots = menus.getInt("JoinMenu.Slots");

        GuiManager inv = new GuiManager(title, slots);

        for (String key : menus.getConfigurationSection("JoinMenu.Items").getKeys(false)) {
            key = "JoinMenu.Items." + key;
            int slot = menus.getInt(key + ".slot");
            inv.setItems(slot, UtilsBuilder.makeItem(menus, key, player));
        }
        inv.open(player);
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 3.0F, 3.0F);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        MenusHandler menus = CTW.get().getMenusHandler();
        String title = UtilsPlugin.corregirColor(menus.getString("JoinMenu.TitleMenu"));
        Player player = (Player) event.getWhoClicked();
        if (player.getOpenInventory().getTopInventory().getTitle().equals(title)) {
            if (event.getCurrentItem() == null || event.getCurrentItem().getType().name().contains("AIR")) {
                event.setCancelled(true);
                return;
            }
            if (event.getSlotType() == null) {
                event.setCancelled(true);
            } else {
                int slot = event.getSlot();
                event.setCancelled(true);
                if (event.getClickedInventory().equals(player.getOpenInventory().getTopInventory())) {
                    for (String key : menus.getConfigurationSection("JoinMenu.Items").getKeys(false)) {
                        key = "JoinMenu.Items." + key;
                        if (slot == 11) {
                            CTW.get().getTeamManager().addRedTeam(player);
                            player.closeInventory();
                            return;
                        } else if (slot == 13) {
                            CTW.get().getTeamManager().autoAddTeam(player);
                            player.closeInventory();
                            return;
                        } else if (slot == 15) {
                            CTW.get().getTeamManager().addBlueTeam(player);
                            player.closeInventory();
                            return;
                        }
                    }
                }
            }
        }
    }
}
