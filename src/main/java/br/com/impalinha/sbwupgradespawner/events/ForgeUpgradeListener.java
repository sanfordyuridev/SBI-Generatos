package br.com.impalinha.sbwupgradespawner.events;

import br.com.impalinha.sbwupgradespawner.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.RunningTeam;
import org.screamingsandals.bedwars.api.events.BedwarsOpenShopEvent;
import org.screamingsandals.bedwars.api.events.BedwarsUpgradeBoughtEvent;
import org.screamingsandals.bedwars.api.events.BedwarsUpgradeImprovedEvent;
import org.screamingsandals.bedwars.api.game.ItemSpawner;

import java.util.ArrayList;
import java.util.List;

public class ForgeUpgradeListener implements Listener {

    /*
    *
    * Detectar o UPGRADE e cobrar de acordo
    * Configurar para menu de upgrade de forja ficar exibindo o nivel e se foi upado ou nao
    * 
    * */

    @EventHandler
    public void onUpgradeBought(BedwarsOpenShopEvent event) {
        if(event.getStore().getShopCustomName().equalsIgnoreCase("TeamUpgrades")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    InventoryView openInventory = event.getPlayer().getOpenInventory();
                    Inventory invUpgrade = openInventory.getTopInventory();

                    ItemStack itemStack = new ItemStack(Material.FURNACE);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName(ChatColor.RED + "Forja");
                    List<String> lore = new ArrayList<>();
                    RunningTeam teamOfPlayer = event.getGame().getTeamOfPlayer(event.getPlayer());
                    List<ItemSpawner> itemSpawners = event.getGame().getItemSpawners();
                    lore.add(" ");
                    Bukkit.broadcastMessage("ItemSpawners: " + itemSpawners.size());
                    for (ItemSpawner is : itemSpawners) {
                        if (is.getTeam().getName().equals(teamOfPlayer.getName())) {
                            if (is.getLevel() >= 1) {
                                lore.add("LEVEL 1 = OK");
                            } else {
                                lore.add("LEVEL 1 = NAO");
                            }

                            if (is.getLevel() >= 2) {
                                lore.add("LEVEL 2 = OK");
                            } else {
                                lore.add("LEVEL 2 = NAO");
                            }

                            if (is.getLevel() >= 3) {
                                lore.add("LEVEL 3 = OK");
                            } else {
                                lore.add("LEVEL 3 = NAO");
                            }

                            if (is.getLevel() >= 4) {
                                lore.add("LEVEL 4 = OK");
                            } else {
                                lore.add("LEVEL 4 = NAO");
                            }

                            break;
                        }
                    }
                    lore.add(" ");
                    itemMeta.setLore(lore);
                    itemStack.setItemMeta(itemMeta);
                    invUpgrade.setItem(4, itemStack);
                    event.getPlayer().openInventory(invUpgrade);
                }
            }.runTaskLater(Main.plugin, 5);
        }
    }
}