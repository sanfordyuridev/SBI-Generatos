package br.com.impalinha.sbwupgradespawner.utils;

import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.api.game.ItemSpawner;

public class SpawnerPlayer {
    private final Player player;
    private ItemSpawner is;

    public SpawnerPlayer(Player player) {
        this.player = player;
    }

    public void definirGerador(ItemSpawner is) {
        this.is = is;
    }

    public ItemSpawner getIs() {
        return is;
    }

    public void setIs(ItemSpawner is) {
        this.is = is;
    }
}

