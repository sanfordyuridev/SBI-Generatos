package br.com.impalinha.sbwupgradespawner.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.screamingsandals.bedwars.api.events.BedwarsUpgradeImprovedEvent;

public class ForgeUpgradeListener implements Listener {

    @EventHandler
    public void onUpgradeBought(BedwarsUpgradeImprovedEvent event) {
        Bukkit.broadcastMessage("LEVEL UP = " + event.getUpgrade().getLevel());
        Bukkit.broadcastMessage("UPOU PARA O LEVEL = " + event.getNewLevel());
        Bukkit.broadcastMessage("NOME UPGRADE = " + event.getUpgrade().getName());
        Bukkit.broadcastMessage("NOME STORAGE = " + event.getStorage().getUpgradeName());
        Bukkit.broadcastMessage("LEVEL ANTIGO = " + event.getOldLevel());
    }
}