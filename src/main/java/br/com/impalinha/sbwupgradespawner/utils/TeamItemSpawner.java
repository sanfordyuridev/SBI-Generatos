package br.com.impalinha.sbwupgradespawner.utils;

import org.screamingsandals.bedwars.api.game.ItemSpawner;

public class TeamItemSpawner {

    private ItemSpawner ironItemSpawner;
    private ItemSpawner goldItemSpawner;

    public TeamItemSpawner(ItemSpawner ironItemSpawner, ItemSpawner goldItemSpawner) {
        this.ironItemSpawner = ironItemSpawner;
        this.goldItemSpawner = goldItemSpawner;
    }

    public ItemSpawner getIronItemSpawner() {
        return ironItemSpawner;
    }

    public ItemSpawner getGoldItemSpawner() {
        return goldItemSpawner;
    }
}
