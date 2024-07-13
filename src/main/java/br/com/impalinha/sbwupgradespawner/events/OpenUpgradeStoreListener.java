package br.com.impalinha.sbwupgradespawner.events;

import br.com.impalinha.sbwupgradespawner.Main;
import br.com.impalinha.sbwupgradespawner.utils.LevelUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.RunningTeam;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.events.BedwarsOpenShopEvent;
import org.screamingsandals.bedwars.api.game.ItemSpawner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class OpenUpgradeStoreListener implements Listener {

    private HashMap<Team, Integer> contadorPorTime;

    public OpenUpgradeStoreListener() {
        contadorPorTime = new HashMap<>();
    }


    @EventHandler
    public void onUpgradeBought(BedwarsOpenShopEvent event) {
        if (event.getStore().getShopCustomName().equalsIgnoreCase("TeamUpgrades")) {
            new BukkitRunnable() {
                double previousLevel = -1;

                @Override
                public void run() {
                    if (!event.getPlayer().isOnline() || !event.getPlayer().getOpenInventory().getTitle().contains("shop")) {
                        this.cancel();
                        return;
                    }

                    InventoryView openInventory = event.getPlayer().getOpenInventory();
                    Inventory invUpgrade = openInventory.getTopInventory();

                   Main.playerItemSpawner.put(event.getPlayer(), updateInventory(event, invUpgrade, previousLevel));

                }
            }.runTaskTimer(Main.plugin, 0L, 1L);
        }
    }

    private List<ItemSpawner> updateInventory(BedwarsOpenShopEvent event, Inventory invUpgrade, double previousLevel) {
        ItemStack itemStack = new ItemStack(Material.FURNACE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        RunningTeam teamOfPlayer = event.getGame().getTeamOfPlayer(event.getPlayer());
        List<ItemSpawner> itemSpawners = event.getGame().getItemSpawners();
        double currentLevel = previousLevel;

        itemMeta.setDisplayName(ChatColor.RED + "Forja " + LevelUtils.retornarEmRomano(currentLevel - 1));
        lore.add(ChatColor.GRAY + "Aumente o número de ");
        lore.add(ChatColor.GRAY + "recursos que nascem ");
        lore.add(ChatColor.GRAY + "em sua ilha. ");
        lore.add("");

        List<ItemSpawner> filteredItemSpawners = itemSpawners.stream()
                .filter(itemSpawner -> teamOfPlayer.getName().equals(itemSpawner.getTeam().getName()))
                .collect(Collectors.toList());

        ItemSpawner is = filteredItemSpawners.get(0);

        currentLevel = is.getLevel();
        itemMeta.setDisplayName(ChatColor.RED + "Forja " + LevelUtils.retornarEmRomano(currentLevel - 1));

        lore.add((currentLevel >= 2 ? ChatColor.BOLD + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 1: + 50% de Recursos, " + ChatColor.AQUA + "2 Diamantes ");
        lore.add((currentLevel >= 3 ? ChatColor.BOLD + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 2: + 100% de Recursos, " + ChatColor.AQUA + "4 Diamantes ");
        lore.add((currentLevel >= 4 ? ChatColor.BOLD + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 3: Nasce Esmeraldas, " + ChatColor.AQUA + "6 Diamantes ");
        lore.add((currentLevel >= 5 ? ChatColor.BOLD + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 4: + 200% de Recursos, " + ChatColor.AQUA + "8 Diamantes ");
        lore.add("");

        int diamantesNecessarios = LevelUtils.getDiamantesNecessarios((int) currentLevel);

        if(currentLevel != 5) {
            if (event.getPlayer().getInventory().containsAtLeast(new ItemStack(Material.DIAMOND), diamantesNecessarios)) {
                lore.add(ChatColor.GREEN + "Clique para subir o Level.");
            } else {
                lore.add(ChatColor.RED + "Você não tem Diamantes o suficiente.");
            }
        } else {
            lore.add(ChatColor.GREEN + "Level máximo atingido");
        }

        lore.add(" ");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        invUpgrade.setItem(4, itemStack);

        return filteredItemSpawners;
    }
}