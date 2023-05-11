package com.imjona.ctw.utils;

import com.imjona.ctw.CTW;
import com.imjona.ctw.database.PlayerData;
import com.imjona.ctw.enums.Teams;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class UtilsPlugin {
    private static final String prefix = "§8[§cCTW§8] §7";

    public static String corregirColor(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void sendMessageConsole(String msg) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        console.sendMessage(corregirColor(prefix + msg));
    }

    public static void sendMessagePlayer(Player player, String msg) {
        player.sendMessage(corregirColor(msg));
    }

    public static void sendMessageAllPlayers(String msg) {
        for (Player all : Bukkit.getServer().getOnlinePlayers())
            all.sendMessage(corregirColor(msg));
    }

    public static void sendSoundAllPlayers(Sound sound) {
        for (Player all : Bukkit.getServer().getOnlinePlayers())
            all.playSound(all.getLocation(), sound, 1.0F, 1.0F);
    }

    public static void sendSoundPlayer(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 3.0F,3.0F);
    }

    public static String setLocToString(Location l) {
        World world = l.getWorld();
        if (world == null)
            return null;
        return world.getName() + ";" +
                l.getBlock() + ";" +
                l.getBlockY() + ";" +
                l.getBlockZ() + ";" +
                l.getYaw() + ";" +
                l.getPitch();
    }

    public static String setLoc(Location loc) {
        return loc.getBlock() + ";" +
                loc.getBlockY() + ";" +
                loc.getBlockZ();
    }

    public static String setLocNoWorld(Location l) {
        return l.getX() + ";" +
                l.getY() + ";" +
                l.getZ() + ";" +
                l.getYaw() + ";" +
                l.getPitch();
    }

    public static Location getLoc(String s) {
        try {
            double x = Double.parseDouble(s.split(";")[0]);
            double y = Double.parseDouble(s.split(";")[1]);
            double z = Double.parseDouble(s.split(";")[2]);
            float pitch = Float.parseFloat(s.split(";")[3]);
            float yaw = Float.parseFloat(s.split(";")[4]);
            return new Location(CTW.get().getMapManager().getCurrentMapWorld(), x, y, z, pitch, yaw);
        } catch (Exception e) {
            sendMessageConsole("&cHa ocurrido un error al obtener Location: ");
            return null;
        }
    }

    public static Location getLocation(String s) {
        try {
            double x = Double.parseDouble(s.split(";")[0]);
            double y = Double.parseDouble(s.split(";")[1]);
            double z = Double.parseDouble(s.split(";")[2]);
            return new Location(CTW.get().getMapManager().getCurrentMapWorld(), x, y, z);
        } catch (Exception ex) {
            sendMessageConsole("&cHa ocurrido un error al obtener Location: ");
            return null;
        }
    }

    public static String getSelection(Selection sel) {
        //PUNTO MÍNIMO DE SELECCIÓN EN WORLD EDIT
        return sel.getMinimumPoint().getBlockX() + ";" +
                sel.getMinimumPoint().getBlockY() + ";" +
                sel.getMinimumPoint().getBlockZ() + ";" +
                //PUNTO MÁXIMO DE SELECCIÓN EN WORLD EDIT
                sel.getMaximumPoint().getBlockX() + ";" +
                sel.getMaximumPoint().getBlockY() + ";" +
                sel.getMaximumPoint().getBlockZ();
    }

    public static String getTeamColor(Player p) {
        String name = p.getName();
        Teams team = CTW.get().getTeamManager().getTeam(p);
        if (team == Teams.RED) {
            name = ChatColor.RED + name;
        } else if (team == Teams.BLUE) {
            name = ChatColor.BLUE + name;
        }
        return name;
    }

    public static String getWeaponLogo(Player player) {
        String icon = "";
        if (player.getItemInHand().getType() != null)
            try {
                if (player.getItemInHand().getType() == Material.IRON_SWORD) {
                    icon = CTW.get().getLanguage().getMessage("Icons.IronSword");
                } else if (player.getItemInHand().getType() == Material.DIAMOND_SWORD) {
                    icon = CTW.get().getLanguage().getMessage("Icons.DiamondSword");
                } else if (player.getItemInHand().getType() == Material.BOW) {
                    icon = CTW.get().getLanguage().getMessage("Icons.Bow");
                } else if (player.getItemInHand().getType() == Material.GOLD_SWORD) {
                    icon = CTW.get().getLanguage().getMessage("Icons.GoldSword");
                } else if (player.getItemInHand().getType() == Material.STONE_SWORD) {
                    icon = CTW.get().getLanguage().getMessage("Icons.StoneSword");
                } else if (player.getItemInHand().getType() == Material.WOOD_SWORD) {
                    icon = CTW.get().getLanguage().getMessage("Icons.WoodSword");
                } else {
                    icon = CTW.get().getLanguage().getMessage("Icons.Other");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        return icon;
    }

    public static void sendScoreMessage(Player player, String score, Integer coins) {
        PlayerData pd = CTW.get().getPlayerData(player.getName());
        if (pd != null) {
            String msg = CTW.get().getLanguage().getMessage("ChatMessages.ScoreMessage").replaceAll("%points%", score)
                    .replaceAll("%score%", pd.getScore().toString());
            if (coins != null)
                if (player.hasPermission("CTW.3xCoinMultiplier")) {
                    int coin = coins * 3;
                    String cMsg = CTW.get().getLanguage().getMessage("ChatMessages.Coins3xMultiplier").replaceAll("%coins%", String.valueOf(coin))
                            .replaceAll("%balance%", pd.getCoins().toString());
                    msg = msg + cMsg;
                } else if (player.hasPermission("CTW.2xCoinMultiplier")) {
                    int coin = coins * 2;
                    String cMsg = CTW.get().getLanguage().getMessage("ChatMessages.Coins2xMultiplier").replaceAll("%coins%", String.valueOf(coin))
                            .replaceAll("%balance%", pd.getCoins().toString());
                    msg = msg + cMsg;
                } else {
                    String cMsg = CTW.get().getLanguage().getMessage("ChatMessages.CoinsNoMultiplier").replaceAll("%coins%", coins.toString())
                            .replaceAll("%balance%", pd.getCoins().toString());
                    msg = msg + cMsg;
                }
            sendMessagePlayer(player, msg);
        }
    }
}
