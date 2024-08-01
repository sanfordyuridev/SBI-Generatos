package br.com.impalinha.sbwupgradespawner.events;

import br.com.impalinha.sbwupgradespawner.Main;
import br.com.impalinha.sbwupgradespawner.utils.Constants;
import br.com.impalinha.sbwupgradespawner.utils.LevelUtils;
import br.com.impalinha.sbwupgradespawner.utils.TipoUpgrade;
import io.github.pronze.sba.SBA;
import io.github.pronze.sba.game.IGameStorage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.RunningTeam;
import org.screamingsandals.bedwars.api.game.GameStore;
import org.screamingsandals.bedwars.api.game.ItemSpawner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.impalinha.sbwupgradespawner.utils.LevelUtils.hasEnoughDiamonds;

public class InteractUpgradeNpcListener implements Listener {

    private final BedwarsAPI api;
    public InteractUpgradeNpcListener(BedwarsAPI api) {
        this.api = api;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteractUpgradeNpcListener(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        List<GameStore> gameStores = api.getGameOfPlayer(player).getGameStores();

        Optional<GameStore> gameStoreOptional = gameStores.stream()
                .filter(gameStore -> (gameStore.getStoreLocation().getWorld()==entity.getLocation().getWorld() &&
                         gameStore.getStoreLocation().getX()==entity.getLocation().getX() &&
                         gameStore.getStoreLocation().getY()==entity.getLocation().getY() &&
                         gameStore.getStoreLocation().getZ()==entity.getLocation().getZ()))
                .findFirst();

        GameStore gameStore = gameStoreOptional.orElseThrow();

        if (gameStore != null && gameStore.getShopCustomName().equalsIgnoreCase("TeamUpgrades")) {
            Inventory inv = LevelUtils.getInvUpgrade();

            new BukkitRunnable() {
                @Override
                public void run() {
                    event.setCancelled(true);
                    player.openInventory(inv);
                }
            }.runTaskLater(Main.plugin, 1L);

            new BukkitRunnable() {
                double previousLevelForja = -1;
                Optional<Integer> previousLevelEspada = Optional.of(-1);

                @Override
                public void run() {
                    if (!player.isOnline() || !(player.getOpenInventory().getTitle().equals(Constants.TITULO_MENU))) {
                        this.cancel();
                        return;
                    }

                    Main.gameTimeItemSpawner.put(player, updateInventory(player, player.getOpenInventory().getTopInventory(), previousLevelForja, previousLevelEspada));
                }
            }.runTaskTimer(Main.plugin, 1L, 1L);
        }

    }

    private List<ItemSpawner> updateInventory(Player player, Inventory invUpgrade, double previousLevelForja, Optional<Integer> previousLevelEspada) {
        ItemStack itemStackForja = new ItemStack(Material.FURNACE);
        ItemMeta itemMetaForja = itemStackForja.getItemMeta();
        List<String> loreForja = new ArrayList<>();

        ItemStack itemStackEspadas = new ItemStack(Material.IRON_SWORD);
        ItemMeta itemMetaEspadas = itemStackEspadas.getItemMeta();
        List<String> loreEspadas = new ArrayList<>();



        Optional<IGameStorage> gameStorage1 = SBA.getInstance().getGameStorage(api.getGameOfPlayer(player));
        IGameStorage gameStorage = gameStorage1.orElseThrow();

        RunningTeam teamOfPlayer = api.getGameOfPlayer(player).getTeamOfPlayer(player);
        List<ItemSpawner> itemSpawners = api.getGameOfPlayer(player).getItemSpawners();
        double currentLevelForja = previousLevelForja;
        int currentLevelEspada = previousLevelEspada.orElse(0);

        itemMetaForja.setDisplayName(ChatColor.RED + "Forja " + LevelUtils.retornarEmRomano(currentLevelForja - 1));
        loreForja.add(ChatColor.GRAY + "Aumente o número de ");
        loreForja.add(ChatColor.GRAY + "recursos que nascem ");
        loreForja.add(ChatColor.GRAY + "em sua ilha. ");
        loreForja.add("");

        itemMetaEspadas.setDisplayName(ChatColor.RED + "Afiação nas Espadas " + LevelUtils.retornarEmRomano(currentLevelEspada - 1));
        loreEspadas.add(ChatColor.GRAY + "O seu time ganha Afiação ");
        loreEspadas.add(ChatColor.GRAY + "permanente em todas ");
        loreEspadas.add(ChatColor.GRAY + "as espadas. ");
        loreEspadas.add("");

        List<ItemSpawner> filteredItemSpawners = itemSpawners.stream()
                .filter(itemSpawner ->
                        Objects.nonNull(itemSpawner.getTeam()) &&
                                Objects.nonNull(itemSpawner.getTeam().getName()) &&
                                teamOfPlayer.getName().equals(itemSpawner.getTeam().getName())
                )
                .collect(Collectors.toList());

        ItemSpawner is = filteredItemSpawners.get(0);

        currentLevelForja = is.getLevel();
        currentLevelEspada = gameStorage.getSharpnessLevel(teamOfPlayer).orElse(0);
        itemMetaForja.setDisplayName(ChatColor.RED + "Forja " + LevelUtils.retornarEmRomano(currentLevelForja - 1));
        itemMetaEspadas.setDisplayName(ChatColor.RED + "Afiação nas Espadas " + LevelUtils.retornarEmRomano(currentLevelEspada - 1));

        loreForja.add((currentLevelForja >= 2 ? ChatColor.BOLD + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 1: + 50% de Recursos, " + ChatColor.AQUA + "2 Diamantes ");
        loreForja.add((currentLevelForja >= 3 ? ChatColor.BOLD + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 2: + 100% de Recursos, " + ChatColor.AQUA + "4 Diamantes ");
        loreForja.add((currentLevelForja >= 4 ? ChatColor.BOLD + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 3: Nasce Esmeraldas, " + ChatColor.AQUA + "6 Diamantes ");
        loreForja.add((currentLevelForja >= 5 ? ChatColor.BOLD + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 4: + 200% de Recursos, " + ChatColor.AQUA + "8 Diamantes ");
        loreForja.add("");

        loreEspadas.add((currentLevelEspada >= 1 ? ChatColor.BOLD + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 1: Afiação I, " + ChatColor.AQUA + "4 Diamantes ");
        loreEspadas.add((currentLevelEspada >= 2 ? ChatColor.BOLD + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 2: Afiação II, " + ChatColor.AQUA + "16 Diamantes ");
        loreEspadas.add("");

        int diamantesNecessariosForja = LevelUtils.getDiamantesNecessarios((int) currentLevelForja, TipoUpgrade.FORJA);
        int diamantesNecessariosEspadas = LevelUtils.getDiamantesNecessarios((int) currentLevelEspada, TipoUpgrade.ESPADAS);

        if(currentLevelForja != 5) {
            if (hasEnoughDiamonds(player, diamantesNecessariosForja, LevelUtils.getDiamante(1))) {
                loreForja.add(ChatColor.GREEN + "Clique para subir o Level.");
            } else {
                loreForja.add(ChatColor.RED + "Você não tem Diamantes o suficiente.");
            }
        } else {
            loreForja.add(ChatColor.GREEN + "Level máximo atingido");
        }

        if(currentLevelEspada != 2) {
            if (hasEnoughDiamonds(player, diamantesNecessariosEspadas, LevelUtils.getDiamante(1))) {
                loreEspadas.add(ChatColor.GREEN + "Clique para subir o Level.");
            } else {
                loreEspadas.add(ChatColor.RED + "Você não tem Diamantes o suficiente.");
            }
        } else {
            loreEspadas.add(ChatColor.GREEN + "Level máximo atingido");
        }

        loreForja.add(" ");
        loreEspadas.add(" ");
        itemMetaForja.setLore(loreForja);
        itemMetaEspadas.setLore(loreEspadas);
        itemMetaEspadas.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStackForja.setItemMeta(itemMetaForja);
        itemStackEspadas.setItemMeta(itemMetaEspadas);
        invUpgrade.setItem(13, itemStackForja);
        invUpgrade.setItem(10, itemStackEspadas);

        return filteredItemSpawners;
    }

}
