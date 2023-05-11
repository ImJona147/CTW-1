package com.imjona.ctw.events;

import com.imjona.ctw.CTW;
import com.imjona.ctw.database.PlayerData;
import com.imjona.ctw.enums.PlayerOptions;
import com.imjona.ctw.utils.UtilsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerEdit implements Listener {
    private final CTW ctw;

    public PlayerEdit(CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (pd != null && pd.hasEditType(PlayerOptions.EDIT))
            event.setCancelled(true);
    }

    @EventHandler
    public void onDropItems(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (pd != null && pd.hasEditType(PlayerOptions.EDIT))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (pd != null && pd.hasEditType(PlayerOptions.EDIT))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (pd != null && pd.hasEditType(PlayerOptions.EDIT))
            event.setCancelled(true);
    }

    @EventHandler
    public void onCraftingTable(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (pd != null && pd.hasEditType(PlayerOptions.EDIT) && event.getClickedInventory() != null
        && event.getClickedInventory().getType().equals(InventoryType.CRAFTING) && event.getSlot() == 0
        && event.getSlotType() != null && event.getSlotType().equals(InventoryType.SlotType.RESULT)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickupItems(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (pd != null && pd.hasEditType(PlayerOptions.EDIT))
            event.setCancelled(true);
    }

    @EventHandler
    public void onDamagePlayer(EntityDamageEvent event) {
        Player player = (Player) event.getEntity();
        PlayerData pd = ctw.getPlayerData(player.getName());
        if (pd != null && pd.hasEditType(PlayerOptions.EDIT))
            event.setCancelled(true);
    }
}
