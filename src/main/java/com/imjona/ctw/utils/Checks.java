package com.imjona.ctw.utils;

import com.imjona.ctw.CTW;
import com.imjona.ctw.commands.*;
import com.imjona.ctw.events.*;
import com.imjona.ctw.menu.JoinMenu;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Checks {
    public Checks() {
        checkWorldEdit();
        checkVault();
        registerCommands();
        registerEvents();
    }

    private void checkWorldEdit() {
        if (Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") == null) {
            UtilsPlugin.sendMessageConsole("&7No se ha encontrado el plugin &bWorldEdit");
        } else {
            UtilsPlugin.sendMessageConsole("&7WorldEdit: &aActivado");
            CTW.get().worldEditPlugin = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        }
    }


    private void checkVault() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            UtilsPlugin.sendMessageConsole("&7No se ha encontrado el plugin &bVault");
        } else {
            RegisteredServiceProvider<Economy> ecoprovider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
            if(ecoprovider != null) {
                CTW.get().economy = ecoprovider.getProvider();
                UtilsPlugin.sendMessageConsole("&7El sistema de economia ha sido detectada: &b"+ecoprovider.getProvider().getName());
            } else {
                UtilsPlugin.sendMessageConsole("&7No se ha encontrado un proveedor de economia");
            }
        }
    }

    private void registerCommands() {

        CTW.get().getCommand("join").setExecutor(new JoinCmd(CTW.get()));
        CTW.get().getCommand("chat").setExecutor(new ChatCmd(CTW.get()));
        CTW.get().getCommand("ctw").setExecutor(new SetupCmd());
        CTW.get().getCommand("g").setExecutor(new GlobalCmd(CTW.get()));
        CTW.get().getCommand("stats").setExecutor(new StatsCmd(CTW.get()));
        CTW.get().getCommand("leave").setExecutor(new LeaveCmd(CTW.get()));
        CTW.get().getCommand("editkit").setExecutor(new EditKitCmd(CTW.get()));
        CTW.get().getCommand("ayuda").setExecutor(new HelpCmd());
        //CTW.get().getCommand("generador").setExecutor(new SpawnCmd());

    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getServer().getPluginManager();

        pm.registerEvents(new PlayerJoin(CTW.get()), CTW.get());
        pm.registerEvents(new JoinMenu(), CTW.get());
        pm.registerEvents(new PlayerChat(CTW.get()), CTW.get());
        //pm.registerEvents(new PlayerPickup(CTW.get()), CTW.get());
        pm.registerEvents(new PlayerDeath(CTW.get()), CTW.get());
        pm.registerEvents(new PlayerMove(), CTW.get());
        pm.registerEvents(new PlayerEdit(CTW.get()), CTW.get());
        /*





        //PROTECCIÓN
        pm.registerEvents(new BlockBreak(CTW.get()), CTW.get());
        pm.registerEvents(new BlockExplode(CTW.get()), CTW.get());
        pm.registerEvents(new BlockPlace(CTW.get()), CTW.get());
        pm.registerEvents(new BukkitUsage(CTW.get()), CTW.get());
        pm.registerEvents(new EntityDamage(CTW.get()), CTW.get());
        pm.registerEvents(new TeamDamage(CTW.get()), CTW.get());
        pm.registerEvents(new EntityInteract(CTW.get()), CTW.get());
        pm.registerEvents(new FireSpread(CTW.get()), CTW.get());
        pm.registerEvents(new LiquidFlow(CTW.get()), CTW.get());
        pm.registerEvents(new PistonExtend(CTW.get()), CTW.get());

        pm.registerEvents(new Moving(), CTW.get());

         */
    }
}
