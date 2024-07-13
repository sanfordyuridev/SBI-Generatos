package br.com.impalinha.sbwupgradespawner.events;

import br.com.impalinha.sbwupgradespawner.Main;
import br.com.impalinha.sbwupgradespawner.utils.LevelUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.game.ItemSpawner;

import java.util.List;

public class ForgeUpgradeSpawnerListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onForgeUpgradeBoughtSpawner(InventoryClickEvent event) {
        if(event.getWhoClicked().getOpenInventory().getTitle().contains("shop")) {
            String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
            if(displayName != null) {
                if(displayName.contains("Forja")) {
                    event.setCancelled(true);
                    List<ItemSpawner> itemSpawner = Main.gameTimeItemSpawner.get(event.getWhoClicked());
                    double level = itemSpawner.get(0).getLevel();
                    int diamantesNecessarios = LevelUtils.getDiamantesNecessarios((int) level);

                    double levelQueVailevelQueVai = level + 1;
                    ItemStack diamante = LevelUtils.getDiamante(diamantesNecessarios);
                    if(level == 5)  {
                        event.getWhoClicked().sendMessage(ChatColor.RED + "O gerador do seu time já atingiu o nível máximo.");
                    } else {
                        if(event.getWhoClicked().getInventory().containsAtLeast(diamante, diamantesNecessarios)) {
                            event.getWhoClicked().sendMessage(ChatColor.GREEN + "Você evoluiu o level do gerador do seu time!");
                            for(ItemSpawner i : itemSpawner) {
                                i.setLevel(levelQueVailevelQueVai);
                                i.setCurrentLevel(levelQueVailevelQueVai);
                            }
                            event.getWhoClicked().getInventory().remove(diamante);
                        } else {
                            event.getWhoClicked().sendMessage(ChatColor.RED + "Você não possui diamantes o suficiente.");
                        }
                    }
                }
            }
        }
    }
}
