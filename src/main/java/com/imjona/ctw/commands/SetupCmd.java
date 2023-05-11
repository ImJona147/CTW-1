package com.imjona.ctw.commands;

import com.imjona.ctw.CTW;
import com.imjona.ctw.utils.UtilsPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashSet;

public class SetupCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (label.matches("ctw") || label.matches("setup")) {
            if (!player.hasPermission("CTW.admin")) {
                UtilsPlugin.sendMessagePlayer(player, "&cNo tienes permisos para ejecutar este comando");
                return true;
            }
            if (args.length == 1) {
                if (args[0].matches("setspecspawn")) {
                    setSpawn("spec", player);
                } else if (args[0].matches("setredspawn")) {
                    setSpawn("red", player);
                } else if (args[0].matches("setbluespawn")) {
                    setSpawn("blue", player);
                } else if (args[0].matches("setredwool")) {
                    setLoc("red", player);
                } else if (args[0].matches("setpinkwool")) {
                    setLoc("pink", player);
                } else if (args[0].matches("setcyanwool")) {
                    setLoc("cyan", player);
                } else if (args[0].matches("setbluewool")) {
                    setLoc("blue", player);
                } else if (args[0].matches("setarena")) {
                    Selection sel = CTW.get().getWorldEditPlugin().getSelection(player);
                    if (sel == null) {
                        UtilsPlugin.sendMessagePlayer(player, "&cPrimero debes seleccionar un área con el hacha");
                        return true;
                    }
                    CTW.get().getMapConfigManager().setArenaArea("ArenaArea", sel);
                    UtilsPlugin.sendMessagePlayer(player, "&aSe ha colocado el área de la arena &e" + CTW.get().getMapManager().getCurrentMap());
                } else if (args[0].matches("setwoolprotected")) {
                    addArea("wool", player);
                } else if (args[0].matches("setotherprotected")) {
                    addArea("other", player);
                } else if (args[0].matches("setsapwnprotected")) {
                    addArea("spawn", player);
                } else if (args[0].matches("setrednoaccess")) {
                    addArea("red", player);
                } else if (args[0].matches("setbluenoaccess")) {
                    addArea("blue", player);
                } else if (args[0].matches("setkit")) {
                    saveKit(player);
                } else if (args[0].matches("save")) {
                    saveArena();
                } else if (args[0].matches("reload")) {
                    CTW.get().getConfigHandler().loadConfig();
                    CTW.get().getLanguage().loadLangFile();
                    CTW.get().getMenusHandler().loadConfig();
                    CTW.get().getMapConfigManager().reloadConfig();
                    UtilsPlugin.sendMessagePlayer(player, "&aSe ha recargado la configuración correctamente");
                } else if (args[0].matches("setteamplayers")) {
                    UtilsPlugin.sendMessagePlayer(player, "&cComando correcto: &e/ctw setteamplayers <número>");
                } else if (args[0].matches("setmaxhight")) {
                    UtilsPlugin.sendMessagePlayer(player, "&cComando correcto: &e/ctw setmaxhight <número>");
                } else if (args[0].matches("setminhight")) {
                    UtilsPlugin.sendMessagePlayer(player, "&cComando correcto: &e/ctw setminhight <número>");
                } else if (args[0].matches("settime")) {
                    UtilsPlugin.sendMessagePlayer(player, "&cComando correcto: &e/ctw settime <DAY/NIGHT>");
                } else {
                    sendInfoPlayer(player);
                }
            } else if (args.length == 2) {
                if (args[0].matches("setteamplayers")) {
                    setNumberToConfig(args[1], player, "MaxPlayersPerTeam");
                } else if (args[0].matches("setmaxhight")) {
                    setNumberToConfig(args[1], player, "ProtectedHight.maxHight");
                } else if (args[0].matches("setminhight")) {
                    setNumberToConfig(args[1], player, "ProtectedHight.minHight");
                } else if (args[0].matches("settime")) {
                    CTW.get().getMapConfigManager().setString(args[1], "Time");
                    UtilsPlugin.sendMessagePlayer(player, "&aHas establecido el tiempo en &e" + args[1]);
                } else {
                    sendInfoPlayer(player);
                }
            } else {
                sendInfoPlayer(player);
            }
        }
        return false;
    }

    private void setNumberToConfig(String raw, Player player, String path) {
        try {
            int numero = Integer.parseInt(raw);
            CTW.get().getMapConfigManager().setInteger(numero, path);
            UtilsPlugin.sendMessagePlayer(player, "&aSe ha colocado el número &e" + numero + "&a en &6" + path);
        } catch(NumberFormatException e) {
            UtilsPlugin.sendMessagePlayer(player, "&cPor favor coloca un número");
        }
    }

    private void saveArena() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(CTW.get(), () -> {
            try {
                FileUtils.deleteDirectory(new File("Maps" + "/" + CTW.get().getMapManager().getCurrentMap() + "/" + "data"));
                FileUtils.deleteDirectory(new File("Maps" + "/" + CTW.get().getMapManager().getCurrentMap() + "/" + "region"));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    FileUtils.copyDirectory(new File("Map-" + CTW.get().getMapManager().getCurrentMap() + "/" + "data"), new File("Maps" + "/" + CTW.get().getMapManager().getCurrentMap() + "/" + "data"));
                    FileUtils.copyDirectory(new File("Map-" + CTW.get().getMapManager().getCurrentMap() + "/" + "region"), new File("Maps" + "/" + CTW.get().getMapManager().getCurrentMap() + "/" + "region"));
                    FileUtils.copyFile(new File("Map-" + CTW.get().getMapManager().getCurrentMap() + "/" + "level.dat"), new File("Maps" + "/" + CTW.get().getMapManager().getCurrentMap() + "/" + "level.dat"));
                } catch (Exception e) {
                    UtilsPlugin.sendMessageConsole("&cHa ocurrido un error al guardar el mapa: ");
                    e.printStackTrace();
                }
            }
        }, 20L);
        CTW.get().getMapConfigManager().reloadConfig();
    }

    private void setSpawn(String type, Player player) {
        switch (type) {
            case "spec":
                CTW.get().getMapConfigManager().setSpawnPoint(player.getLocation(), "SpectatorSpawn");
                UtilsPlugin.sendMessagePlayer(player, "&aSe ha colocado el spawn del espectador correctamente");
                break;
            case "red":
                CTW.get().getMapConfigManager().setSpawnPoint(player.getLocation(), "RedTeam.Spawn");
                UtilsPlugin.sendMessagePlayer(player, "&aSe ha colocado el spawn del equipo &c&lROJO &acorrectamente");
                break;
            case "blue":
                CTW.get().getMapConfigManager().setSpawnPoint(player.getLocation(), "BlueTeam.Spawn");
                UtilsPlugin.sendMessagePlayer(player, "&aSe ha colocado el spawn del equipo &9&lAZUL &acorrectamente");
                break;
        }
    }

    private void setLoc(String type, Player player) {
        Block b = player.getTargetBlock((HashSet<Byte>) null, 6);
        switch (type) {
            case "red":
                CTW.get().getMapConfigManager().setBlockLocation(b.getLocation(), "RedTeam.RedWool");
                UtilsPlugin.sendMessagePlayer(player, "&aSe ha colocado la lana &c&lROJA &adel equipo &c&lROJO");
                break;
            case "pink":
                CTW.get().getMapConfigManager().setBlockLocation(b.getLocation(), "RedTeam.PinkWool");
                UtilsPlugin.sendMessagePlayer(player, "&aSe ha colocado la lana &d&lROSA &adel equipo &c&lROJO");
                break;
            case "blue":
                CTW.get().getMapConfigManager().setBlockLocation(b.getLocation(), "BlueTeam.BlueWool");
                UtilsPlugin.sendMessagePlayer(player, "&aSe ha colocado la lana &9&lAZUL &adel equipo &9&lAZUL");
                break;
            case "cyan":
                CTW.get().getMapConfigManager().setBlockLocation(b.getLocation(), "BlueTeam.CyanWool");
                UtilsPlugin.sendMessagePlayer(player, "&aSe ha colocado la lana &3&lCIAN &adel equipo &9&lAZUL");
                break;
        }
    }

    private void addArea(String data, Player player) {
        Selection sel = CTW.get().getWorldEditPlugin().getSelection(player);
        if (sel == null) {
            UtilsPlugin.sendMessagePlayer(player, "&cPrimero debes seleccionar un área con el hacha");
            return;
        }
        switch (data) {
            case "spawn":
                CTW.get().getMapConfigManager().addArea("SpawnProtectedAreas", sel);
                UtilsPlugin.sendMessagePlayer(player, "&aSe ha añadido una protección de 'spawn'");
                break;
            case "wool":
                CTW.get().getMapConfigManager().addArea("WoolProtectedAreas", sel);
                UtilsPlugin.sendMessagePlayer(player, "&aSe ha añadido una protección de 'lanas'");
                break;
            case "other":
                CTW.get().getMapConfigManager().addArea("OtherProtectedAreas", sel);
                UtilsPlugin.sendMessagePlayer(player, "&aSe ha añadido una protección de 'otros'");
                break;
            case "blue":
                CTW.get().getMapConfigManager().addArea("BlueNoAccess", sel);
                UtilsPlugin.sendMessagePlayer(player, "&aSe ha añadido una región de &c&lNO ACESSO &adel equipo &9&lAZUL &e#" + CTW.get().getMapConfigManager().getBlueNoAccess().size());
                break;
            case "red":
                CTW.get().getMapConfigManager().addArea("RedNoAccess", sel);
                UtilsPlugin.sendMessagePlayer(player, "&aSe ha añadido una región de &c&lNO ACESSO &adel equipo &c&lROJO &e#" + CTW.get().getMapConfigManager().getBlueNoAccess().size());
                break;
        }
    }

    private void saveKit(Player player) {
        for (int i=0;i<player.getInventory().getSize();i++) {
            ItemStack item = player.getInventory().getItem(i);
            CTW.get().getMapConfigManager().saveStartupKit(item, i);
            UtilsPlugin.sendMessagePlayer(player, "&aSe ha guardado el kit correctamente");
        }
    }

    private void sendInfoPlayer(Player player) {
        UtilsPlugin.sendMessagePlayer(player, "&c&m---------------&8[&e&lCTW&8]&c&m---------------");
        UtilsPlugin.sendMessagePlayer(player, "");
        UtilsPlugin.sendMessagePlayer(player, "&b/ctw setspecspawn &f- &7Coloca el spawn del &e&lESPECTADOR");
        UtilsPlugin.sendMessagePlayer(player, "&b/ctw setredspawn &f- &7Coloca el spawn del equipo &c&lROJO");
        UtilsPlugin.sendMessagePlayer(player, "&b/ctw setbluespawn &f- &7Coloca el spawn del equipo &9&lAZUL");
        UtilsPlugin.sendMessagePlayer(player, "&b/ctw setredwool &f- &7Establece el lugar donde se pondrá la lana &c&lROJA");
        UtilsPlugin.sendMessagePlayer(player, "&b/ctw setpinkwool &f- &7Establece el lugar donde se pondrá la lana &d&lROSA");
        UtilsPlugin.sendMessagePlayer(player, "&b/ctw setbluewool &f- &7Establece el lugar donde se pondrá la lana &9&lAZUL");
        UtilsPlugin.sendMessagePlayer(player, "&b/ctw setcyanwool &f- &7Establece el lugar donde se pondrá la lana &3&lCIAN");
        UtilsPlugin.sendMessagePlayer(player, "&b/ctw setspawnprotected &f- &7Agrega una zona protegida solo para el spawn &e(Hay pvp)");
        UtilsPlugin.sendMessagePlayer(player, "&b/ctw setwoolprotected &f- &7Agrega una zona protegida solo para las lanas &e(Hay pvp)");
        UtilsPlugin.sendMessagePlayer(player, "&b/ctw setotherprotected &f- &7Agrega una zona protegida para otras cosas &e(No hay pvp)");
        UtilsPlugin.sendMessagePlayer(player, "&b/ctw setmaxhight &f- &7Establece el máximo de construcción");
        UtilsPlugin.sendMessagePlayer(player, "&b/ctw setminhight &f- &7Establece el minimo de construcción y la coord donde morirán todos");
        UtilsPlugin.sendMessagePlayer(player, "&b/ctw settime &f- &7Coloca el clima del mapa &e(day/night)");
        UtilsPlugin.sendMessagePlayer(player, "&b/ctw setarena &f- &7Establece el arena del juego");
        UtilsPlugin.sendMessagePlayer(player, "&b/ctw setbluenoaccess &f- &7Establece el lugar donde no podran acceder el equipo &9&lAZUL");
        UtilsPlugin.sendMessagePlayer(player, "&b/ctw setrednoaccess &f- &7Establece el lugar donde no podran acceder el equipo &c&lROJO");
        UtilsPlugin.sendMessagePlayer(player, "&b/ctw setkit &f- &7Establece el kit del mapa");
        UtilsPlugin.sendMessagePlayer(player, "&b/ctw setteamplayers &f- &7Establece el máximo de jugadores por equipo");
        UtilsPlugin.sendMessagePlayer(player, "&b/ctw save &f- &7Guarda el mapa");
        UtilsPlugin.sendMessagePlayer(player, "&b/ctw reload &f- &7Recarga los archivos de configuración");
        UtilsPlugin.sendMessagePlayer(player, "");
    }
}
