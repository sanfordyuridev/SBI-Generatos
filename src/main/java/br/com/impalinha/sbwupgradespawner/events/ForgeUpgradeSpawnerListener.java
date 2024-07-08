package br.com.impalinha.sbwupgradespawner.events;

import br.com.impalinha.sbwupgradespawner.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.game.ItemSpawner;

public class ForgeUpgradeSpawnerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onForgeUpgradeBoughtSpawner(InventoryClickEvent event) {
        if(event.getWhoClicked().getOpenInventory().getTitle().contains("shop")) {
            String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
            if(displayName != null) {
                if(displayName.contains("Forja")) {
                    event.setCancelled(true);
                    ItemSpawner itemSpawner = Main.playerItemSpawner.get(event.getWhoClicked());
                    int diamantesNecessarios = -1;
                    if (itemSpawner.getLevel() != 5) {
                        double levelQueVai = itemSpawner.getLevel() + 1;
                        if (levelQueVai == 1) {
                            diamantesNecessarios = 2;
                        } else if (levelQueVai == 2) {
                            diamantesNecessarios = 4;
                        } else if (levelQueVai == 3) {
                            diamantesNecessarios = 6;
                        } else if (levelQueVai == 4) {
                            diamantesNecessarios = 8;
                        }

                        if(event.getWhoClicked().getInventory().containsAtLeast(new ItemStack(Material.DIAMOND), diamantesNecessarios)) {
                            event.getWhoClicked().sendMessage(ChatColor.GREEN + "Você evoluiu o gerador do seu time!");
                            event.getWhoClicked().getInventory().remove(new ItemStack(Material.DIAMOND, diamantesNecessarios));
                            itemSpawner.setLevel(levelQueVai);
                        } else {
                            event.getWhoClicked().sendMessage(ChatColor.RED + "Você não possui diamantes o suficiente.");
                        }
                    }
                    event.getWhoClicked().sendMessage("Level atual Spawner: " + itemSpawner.getLevel());
                    event.getWhoClicked().sendMessage("Time: " + itemSpawner.getTeam());
                }
            }
        }
    }
}
