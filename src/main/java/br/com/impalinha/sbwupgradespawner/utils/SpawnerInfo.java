package br.com.impalinha.sbwupgradespawner.utils;

import org.screamingsandals.bedwars.api.game.ItemSpawner;

public class SpawnerInfo {
    private final ItemSpawner spawner;
    private final double level;
    private Boolean podeUpar;

    public SpawnerInfo(ItemSpawner spawner, double level) {
        this.spawner = spawner;
        this.level = level;
    }

    public ItemSpawner getSpawner() {
        return spawner;
    }

    public double getLevel() {
        return level;
    }

    public Boolean getPodeUpar() {
        return podeUpar;
    }

    public void setPodeUpar(Boolean podeUpar) {
        this.podeUpar = podeUpar;
    }
}