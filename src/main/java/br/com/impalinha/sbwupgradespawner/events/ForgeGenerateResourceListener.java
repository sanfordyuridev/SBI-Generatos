package br.com.impalinha.sbwupgradespawner.events;

import br.com.impalinha.sbwupgradespawner.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.events.BedwarsResourceSpawnEvent;

import java.util.Random;

public class ForgeGenerateResourceListener implements Listener {

    @EventHandler
    public void onForgeItemSpawn(BedwarsResourceSpawnEvent event) {
        double level = event.getSpawner().getLevel();
        Main plugin = Main.plugin;
        int levelMinimoParaComecarAVimEsmeralda = plugin.getConfig().getInt("start-level-emerald");

        Team team = event.getSpawner().getTeam();

        if (level >= levelMinimoParaComecarAVimEsmeralda) {
            if (Material.GOLD_INGOT == event.getResource().getType()) {
                Location location = event.getSpawner().getLocation();
                int delayAteSpawnarEsmeraldaAposOOuro = plugin.getConfig().getInt("delay-spawn-emerald-after-gold");
                int minQuantidadeDeEsmeraldaGerada = plugin.getConfig().getInt("min-amount-emerald-generated");
                int maxQuantidadeDeEsmeraldaGerada = plugin.getConfig().getInt("max-amount-emerald-generated");
                Random random = new Random();
                int quantidadeDeEsmeraldaGerada = random.nextInt((maxQuantidadeDeEsmeraldaGerada - minQuantidadeDeEsmeraldaGerada) + 1)
                        + minQuantidadeDeEsmeraldaGerada;
                Bukkit.getScheduler().runTaskLater(plugin, () ->
                                location.getWorld().dropItem(location,
                                        new ItemStack(Material.EMERALD, quantidadeDeEsmeraldaGerada)),
                        (delayAteSpawnarEsmeraldaAposOOuro * 20L));
            }
        }
    }

}
