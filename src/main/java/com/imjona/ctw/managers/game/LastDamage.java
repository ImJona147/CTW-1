package com.imjona.ctw.managers.game;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class LastDamage {
    private final Map<Player, Long> lastDamageTime = new HashMap<>();
    private final Map<Player, Player> lastDamager = new HashMap<>();
    private final Map<Player, String> weaponUsed = new HashMap<>();

    public void setData(Player p, Player damager, String weaponType) {
        this.lastDamageTime.put(p, System.currentTimeMillis());
        this.lastDamager.put(p, damager);
        this.weaponUsed.put(p, weaponType);
    }

    public void removeData(Player p) {
        this.lastDamageTime.remove(p);
        this.lastDamager.remove(p);
        this.weaponUsed.remove(p);
    }

    public Player getKiller(Player p) {
        if (!this.lastDamageTime.containsKey(p))
            return null;
        if (System.currentTimeMillis() - this.lastDamageTime.get(p) > 10000L)
            return null;
        return this.lastDamager.get(p);
    }

    public String getWeaponType(Player p) {
        return this.weaponUsed.get(p);
    }
}
