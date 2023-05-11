package com.imjona.ctw.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imjona.ctw.CTW;
import com.imjona.ctw.enums.Wools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class WoolManager {
    private final CTW ctw;
    private final Map<Wools, Boolean> woolsTaken = new HashMap<>();
    private final Map<Wools, Boolean> woolsPlaced = new HashMap<>();
    private final Map<Wools, Player> woolsPlacedByPlayer = new HashMap<>();
    private final List<Player> redTakenList = new ArrayList<>();
    private final List<Player> pinkTakenList = new ArrayList<>();
    private final List<Player> blueTakenList = new ArrayList<>();
    private final List<Player> cyanTakenList = new ArrayList<>();

    public WoolManager(CTW ctw) {
        this.ctw = ctw;
        resetWoolsStats();
    }
    public void resetWoolsStats() {
        this.woolsTaken.put(Wools.RED, false);
        this.woolsTaken.put(Wools.PINK, false);
        this.woolsTaken.put(Wools.CYAN, false);
        this.woolsTaken.put(Wools.BLUE, false);
        this.woolsPlaced.put(Wools.RED, false);
        this.woolsPlaced.put(Wools.PINK, false);
        this.woolsPlaced.put(Wools.CYAN, false);
        this.woolsPlaced.put(Wools.BLUE, false);
        this.redTakenList.clear();
        this.pinkTakenList.clear();
        this.blueTakenList.clear();
        this.cyanTakenList.clear();
        this.woolsPlacedByPlayer.put(Wools.RED, null);
        this.woolsPlacedByPlayer.put(Wools.PINK, null);
        this.woolsPlacedByPlayer.put(Wools.BLUE, null);
        this.woolsPlacedByPlayer.put(Wools.CYAN, null);
    }

    public void removeWools() {
        Bukkit.getServer().getScheduler().runTaskLater(this.ctw, () -> {
            ctw.getMapManager().getCurrentMapWorld().getBlockAt(ctw.getMapConfigManager().getRedWool()).setType(Material.AIR);
            ctw.getMapManager().getCurrentMapWorld().getBlockAt(ctw.getMapConfigManager().getPinkWool()).setType(Material.AIR);
            ctw.getMapManager().getCurrentMapWorld().getBlockAt(ctw.getMapConfigManager().getBlueWool()).setType(Material.AIR);
            ctw.getMapManager().getCurrentMapWorld().getBlockAt(ctw.getMapConfigManager().getCyanWool()).setType(Material.AIR);
        }, 10L);
    }

    public void setRedTaken() {
        this.woolsTaken.put(Wools.RED, true);
    }

    public void setPinkTaken() {
        this.woolsTaken.put(Wools.PINK, true);
    }

    public void setBlueTaken() {
        this.woolsTaken.put(Wools.BLUE, true);
    }

    public void setCyanTaken() {
        this.woolsTaken.put(Wools.CYAN, true);
    }

    public boolean isRedTaken() {
        return this.woolsTaken.get(Wools.RED);
    }

    public boolean isPinkTaken() {
        return this.woolsTaken.get(Wools.PINK);
    }

    public boolean isBlueTaken() {
        return this.woolsTaken.get(Wools.BLUE);
    }

    public boolean isCyanTaken() {
        return this.woolsTaken.get(Wools.CYAN);
    }

    public boolean isWoolTaken(String wool) {
        switch (wool) {
            case "Red":
                return isRedTaken();
            case "Pink":
                return isPinkTaken();
            case "Blue":
                return isBlueTaken();
            case "Cyan":
                return isCyanTaken();
        }
        return false;
    }

    public void setRedPlaced(Player player) {
        this.woolsPlaced.put(Wools.RED, true);
        this.woolsPlacedByPlayer.put(Wools.RED, player);
    }

    public void setPinkPlaced(Player player) {
        this.woolsPlaced.put(Wools.PINK, true);
        this.woolsPlacedByPlayer.put(Wools.PINK, player);
    }

    public void setBluePlaced(Player player) {
        this.woolsPlaced.put(Wools.BLUE, true);
        this.woolsPlacedByPlayer.put(Wools.BLUE, player);
    }

    public void setCyanPlaced(Player player) {
        this.woolsPlaced.put(Wools.CYAN, true);
        this.woolsPlacedByPlayer.put(Wools.CYAN, player);
    }

    public void setWoolPlaced(String wool, Player player) {
        switch (wool) {
            case "Red":
                setRedPlaced(player);
                break;
            case "Pink":
                setPinkPlaced(player);
                break;
            case "Blue":
                setBluePlaced(player);
                break;
            case "Cyan":
                setCyanPlaced(player);
                break;
        }
    }

    public boolean isRedPlaced() {
        return this.woolsPlaced.get(Wools.RED);
    }

    public boolean isPinkPlaced() {
        return this.woolsPlaced.get(Wools.PINK);
    }

    public boolean isBluePlaced() {
        return this.woolsPlaced.get(Wools.BLUE);
    }

    public boolean isCyanPlaced() {
        return this.woolsPlaced.get(Wools.CYAN);
    }

    public boolean isWoolPlaced(String wool) {
        switch (wool) {
            case "Red":
                return isRedPlaced();
            case "Pink":
                return isPinkPlaced();
            case "Blue":
                return isBluePlaced();
            case "Cyan":
                return isCyanPlaced();
        }
        return false;
    }

    public void addRedTakenByPlayer(Player p) {
        this.redTakenList.add(p);
    }

    public void removeRedTakenByPlayer(Player p) {
        this.redTakenList.remove(p);
    }

    public boolean hadRedTakenByPlayer(Player p) {
        return this.redTakenList.contains(p);
    }

    public String getTakenWoolRed() {
        for (Player player : this.redTakenList) {
            if (this.redTakenList.contains(player))
                return player.getName();
        }
        return null;
    }

    public void addPinkTakenByPlayer(Player p) {
        this.pinkTakenList.add(p);
    }

    public void removePinkTakenByPlayer(Player p) {
        this.pinkTakenList.remove(p);
    }

    public boolean hadPinkTakenByPlayer(Player p) {
        return this.pinkTakenList.contains(p);
    }

    public String getTakenWoolPink() {
        for (Player player : this.pinkTakenList) {
            if (this.pinkTakenList.contains(player))
                return player.getName();
        }
        return null;
    }

    public void addBlueTakenByPlayer(Player p) {
        this.blueTakenList.add(p);
    }

    public void removeBlueTakenByPlayer(Player p) {
        this.blueTakenList.remove(p);
    }

    public boolean hadBlueTakenByPlayer(Player p) {
        return this.blueTakenList.contains(p);
    }

    public String getTakenWoolBlue() {
        for (Player player : this.blueTakenList) {
            if (this.blueTakenList.contains(player))
                return player.getName();
        }
        return null;
    }

    public void addCyanTakenByPlayer(Player p) {
        this.cyanTakenList.add(p);
    }

    public void removeCyanTakenByPlayer(Player p) {
        this.cyanTakenList.remove(p);
    }

    public boolean hadCyanTakenByPlayer(Player p) {
        return this.cyanTakenList.contains(p);
    }

    public String getTakenWoolCyan() {
        for (Player player : this.blueTakenList) {
            if (this.blueTakenList.contains(player))
                return player.getName();
        }
        return null;
    }

    public Player getWhoPlacedRedWool() {
        Player player = this.woolsPlacedByPlayer.get(Wools.RED);
        if (player != null && player.isOnline()) {
            return player;
        }
        return null;
    }

    public Player getWhoPlacedPinkWool() {
        Player player = this.woolsPlacedByPlayer.get(Wools.PINK);
        if (player != null && player.isOnline()) {
            return player;
        }
        return null;
    }

    public Player getWhoPlacedCyanWool() {
        Player player = this.woolsPlacedByPlayer.get(Wools.CYAN);
        if (player != null && player.isOnline()) {
            return player;
        }
        return null;
    }

    public Player getWhoPlacedBlueWool() {
        Player player = this.woolsPlacedByPlayer.get(Wools.BLUE);
        if (player != null && player.isOnline()) {
            return player;
        }
        return null;
    }

    public Player getWhoPlacedWool(String wool) {
        switch (wool) {
            case "Red":
                return getWhoPlacedRedWool();
            case "Pink":
                return getWhoPlacedPinkWool();
            case "Blue":
                return getWhoPlacedBlueWool();
            case "Cyan":
                return getWhoPlacedCyanWool();
        }
        return Bukkit.getPlayer("None");
    }

    public void removeOnWools(Player player) {
        this.blueTakenList.remove(player);
        this.cyanTakenList.remove(player);
        this.redTakenList.remove(player);
        this.pinkTakenList.remove(player);
    }
}

