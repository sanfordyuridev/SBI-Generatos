package br.com.impalinha.sbwupgradespawner;

import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.bedwars.api.events.BedwarsUpgradeBoughtEvent;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onUpdateSpawnerTeam(BedwarsUpgradeBoughtEvent e) {
    }
}
