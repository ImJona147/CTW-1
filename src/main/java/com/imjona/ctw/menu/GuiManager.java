package com.imjona.ctw.menu;

import java.util.HashMap;
import java.util.Map;

import com.imjona.ctw.CTW;
import com.imjona.ctw.utils.UtilsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class GuiManager {
    public String title;
    public InventoryType inventoryType;
    public int size;
    public Map<Integer, ItemStack> guiItems = new HashMap<>();

    public GuiManager() {}

    public GuiManager(String title, int size) {
        this.title = title;
        this.size = size;
    }

    public GuiManager(String title, InventoryType type) {
        this.title = title;
        this.inventoryType = type;
    }

    public GuiManager setItems(int slot, ItemStack item) {
        this.guiItems.put(slot, item);
        return null;
    }

    public void setInventoryType(InventoryType type) {
        this.inventoryType = type;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public ItemStack getItems(int slot) {
        return this.guiItems.get(slot);
    }

    public Map<Integer, ItemStack> getItems() {
        return this.guiItems;
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, this.size, this.title);
        if (this.inventoryType != null)
            inv = Bukkit.createInventory(null, this.inventoryType, this.title);
        for (Map.Entry<Integer, ItemStack> entry : this.guiItems.entrySet()) {
            try {
                inv.setItem(entry.getKey(), entry.getValue());
            } catch(ArrayIndexOutOfBoundsException e) {
                UtilsPlugin.sendMessageConsole("&7There was an error opening the menu...");
                UtilsPlugin.sendMessageConsole("&7Please send this bug to the creator's discord");
                UtilsPlugin.sendMessageConsole("&c&lERROR: &7");
                e.printStackTrace();
            }
        }
        player.openInventory(inv);
    }

    public void open(final Player player, long delay) {
        (new BukkitRunnable() {
            public void run() {
                GuiManager.this.open(player);
            }
        }).runTaskLater(CTW.get(), delay);
    }
}

