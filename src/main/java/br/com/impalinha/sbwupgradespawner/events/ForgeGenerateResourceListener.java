package br.com.impalinha.sbwupgradespawner.events;

import br.com.impalinha.sbwupgradespawner.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.events.BedwarsResourceSpawnEvent;

import java.util.Random;

import static br.com.impalinha.sbwupgradespawner.utils.Constants.*;

public class ForgeGenerateResourceListener implements Listener {

    @EventHandler
    public void onForgeItemSpawn(BedwarsResourceSpawnEvent event) {
        double level = event.getSpawner().getLevel();
        Main plugin = Main.plugin;
        int levelMinimoParaComecarAVimEsmeralda = plugin.getConfig().getInt("start-level-emerald");

        if (level >= levelMinimoParaComecarAVimEsmeralda) {
            if (Material.GOLD_INGOT == event.getResource().getType()) {
                Location location = event.getSpawner().getLocation();
                int delayAteSpawnarEsmeraldaAposOOuro = plugin.getConfig().getInt(START_LEVEL_EMERALD);
                int minQuantidadeDeEsmeraldaGerada = plugin.getConfig().getInt(MIN_AMOUNT_EMERALD);
                int maxQuantidadeDeEsmeraldaGerada = plugin.getConfig().getInt(MAX_AMOUNT_EMERALD);
                Random random = new Random();
                int quantidadeDeEsmeraldaGerada = random.nextInt((maxQuantidadeDeEsmeraldaGerada - minQuantidadeDeEsmeraldaGerada) + 1)
                        + minQuantidadeDeEsmeraldaGerada;

                if(level == 5) {
                    quantidadeDeEsmeraldaGerada = quantidadeDeEsmeraldaGerada * 2;
                }

                int finalQuantidadeDeEsmeraldaGerada = quantidadeDeEsmeraldaGerada;

                Bukkit.getScheduler().runTaskLater(plugin, () ->
                                location.getWorld().dropItem(location,
                                        new ItemStack(Material.EMERALD, finalQuantidadeDeEsmeraldaGerada)),
                        (delayAteSpawnarEsmeraldaAposOOuro * 20L));
            }
        }
    }

}
