package br.com.impalinha.sbwupgradespawner.events;

import br.com.impalinha.sbwupgradespawner.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.events.BedwarsResourceSpawnEvent;

public class ForgeGenerateResourceListener implements Listener {

    @EventHandler
    public void onForgeItemSpawn(BedwarsResourceSpawnEvent event) {
        double level = event.getSpawner().getLevel();
        int levelMinimoParaComecarAVimEsmeralda = 2;
        if(level >= levelMinimoParaComecarAVimEsmeralda) {
            if(Material.GOLD_INGOT == event.getResource().getType()) {
                Location location = event.getSpawner().getLocation();
                int delayAteSpawnarEsmeraldaAposOOuro = 5;
                int quantidadeDeEsmeraldaGerada = (int) level;
                Bukkit.getScheduler().runTaskLater(Main.plugin, () ->
                                location.getWorld().dropItem(location,
                                        new ItemStack(Material.EMERALD, quantidadeDeEsmeraldaGerada)),
                        (delayAteSpawnarEsmeraldaAposOOuro * 20L));
            }
        }
    }

}
