package br.com.impalinha.sbwupgradespawner.events;

import br.com.impalinha.sbwupgradespawner.Main;
import br.com.impalinha.sbwupgradespawner.utils.SpawnerInfo;
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
import org.screamingsandals.bedwars.api.events.BedwarsOpenShopEvent;
import org.screamingsandals.bedwars.api.events.BedwarsUpgradeBoughtEvent;
import org.screamingsandals.bedwars.api.game.ItemSpawner;

import java.util.ArrayList;
import java.util.List;

public class OpenUpgradeStoreListener implements Listener {

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


    private String retornarEmRomano(double numero) {
        if(numero == 1) {
            return "I";
        } else if(numero == 2) {
            return "II";
        } else if(numero == 3) {
            return "III";
        } else if(numero == 4) {
            return "IV";
        } else {
            return "I";
        }
    }

    @EventHandler
    public void onBought(BedwarsUpgradeBoughtEvent e) {
        e.getCustomer().sendMessage("É tu upou");
    }

    private ItemSpawner updateInventory(BedwarsOpenShopEvent event, Inventory invUpgrade, double previousLevel) {
        ItemStack itemStack = new ItemStack(Material.FURNACE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        RunningTeam teamOfPlayer = event.getGame().getTeamOfPlayer(event.getPlayer());
        List<ItemSpawner> itemSpawners = event.getGame().getItemSpawners();
        double currentLevel = previousLevel;
        ItemSpawner relevantSpawner = null;

        itemMeta.setDisplayName(ChatColor.RED + "Forja " + retornarEmRomano(currentLevel - 1));
        lore.add(ChatColor.GRAY + "Aumente o número de ");
        lore.add(ChatColor.GRAY + "recursos que nascem ");
        lore.add(ChatColor.GRAY + "em sua ilha. ");
        lore.add("");

        SpawnerInfo spawnerInfo = null;
        for (ItemSpawner is : itemSpawners) {
            if (is.getTeam().getName().equals(teamOfPlayer.getName())) {
                currentLevel = is.getLevel();
                relevantSpawner = is;

                spawnerInfo = new SpawnerInfo(relevantSpawner, currentLevel);
                lore.add((currentLevel >= 2 ? ChatColor.BOLD + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 1: + 50% de Recursos, " + ChatColor.AQUA + "2 Diamantes ");
                lore.add((currentLevel >= 3 ? ChatColor.BOLD + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 2: + 100% de Recursos, " + ChatColor.AQUA + "4 Diamantes ");
                lore.add((currentLevel >= 4 ? ChatColor.BOLD + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 3: Nasce Esmeraldas, " + ChatColor.AQUA + "6 Diamantes ");
                lore.add((currentLevel >= 5 ? ChatColor.BOLD + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 4: + 200% de Recursos, " + ChatColor.AQUA + "8 Diamantes ");
                lore.add("");

                int diamantesNecessarios = -1;
                if (currentLevel != 5) {
                    double levelQueVai = currentLevel + 1;
                    if (levelQueVai == 1) {
                        diamantesNecessarios = 2;
                    } else if (levelQueVai == 2) {
                        diamantesNecessarios = 4;
                    } else if (levelQueVai == 3) {
                        diamantesNecessarios = 6;
                    } else if (levelQueVai == 4) {
                        diamantesNecessarios = 8;
                    }

                    if (event.getPlayer().getInventory().containsAtLeast(new ItemStack(Material.DIAMOND), diamantesNecessarios)) {
                        lore.add(ChatColor.GREEN + "Clique para subir o Level.");
                        spawnerInfo.setPodeUpar(true);

                    } else {
                        lore.add(ChatColor.RED + "Você não tem Diamantes o suficiente.");
                        spawnerInfo.setPodeUpar(false);
                    }
                }

                break;
            }
        }
        lore.add(" ");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        invUpgrade.setItem(4, itemStack);

        return relevantSpawner;
    }

}