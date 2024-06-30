package br.com.impalinha.sbwupgradespawner.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.screamingsandals.bedwars.api.events.BedwarsUpgradeBoughtEvent;

public class ForgeUpgradeListener implements Listener {

    @EventHandler
    public void onUpgradeBought(BedwarsUpgradeBoughtEvent event) {
        Bukkit.broadcastMessage("Eis o que você evoluiu: " + event.getStorage().getUpgradeName());
    }
}