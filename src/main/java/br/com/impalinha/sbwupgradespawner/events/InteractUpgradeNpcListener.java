package br.com.impalinha.sbwupgradespawner.events;

import br.com.impalinha.sbwupgradespawner.Main;
import br.com.impalinha.sbwupgradespawner.interfaces.IUpgrade;
import br.com.impalinha.sbwupgradespawner.upgrades.ArmaduraUpgrade;
import br.com.impalinha.sbwupgradespawner.upgrades.EspadasUpgrade;
import br.com.impalinha.sbwupgradespawner.upgrades.ForjaUpgrade;
import br.com.impalinha.sbwupgradespawner.upgrades.PicaretaUpgrade;
import br.com.impalinha.sbwupgradespawner.utils.Constants;
import br.com.impalinha.sbwupgradespawner.utils.LevelUtils;
import io.github.pronze.sba.SBA;
import io.github.pronze.sba.game.IGameStorage;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.RunningTeam;
import org.screamingsandals.bedwars.api.game.GameStore;
import org.screamingsandals.bedwars.api.game.ItemSpawner;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.impalinha.sbwupgradespawner.utils.Constants.TEAM_UPGRADES;

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

        if (gameStore != null && gameStore.getShopCustomName().equalsIgnoreCase(TEAM_UPGRADES)) {
            Inventory inv = LevelUtils.getInvUpgrade();

            new BukkitRunnable() {
                @Override
                public void run() {
                    event.setCancelled(true);
                    player.openInventory(inv);
                }
            }.runTaskLater(Main.plugin, 1L);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline() || !(player.getOpenInventory().getTitle().equals(Constants.TITULO_MENU))) {
                        this.cancel();
                        return;
                    }

                    Main.gameTimeItemSpawner.put(player, updateInventory(player, player.getOpenInventory().getTopInventory()));
                }
            }.runTaskTimer(Main.plugin, 1L, 1L);
        }

    }

    private List<ItemSpawner> updateInventory(Player player, Inventory invUpgrade) {
        Optional<IGameStorage> opGameStorage = SBA.getInstance().getGameStorage(api.getGameOfPlayer(player));
        IGameStorage gameStorage = opGameStorage.orElseThrow();
        RunningTeam teamOfPlayer = api.getGameOfPlayer(player).getTeamOfPlayer(player);

        List<IUpgrade> upgrades = List.of(new ForjaUpgrade(), new EspadasUpgrade(), new ArmaduraUpgrade(), new PicaretaUpgrade());

        for (IUpgrade upgrade : upgrades) {
            ItemStack upgradeItem = upgrade.createItem(player, teamOfPlayer, gameStorage);
            invUpgrade.setItem(upgrade.getSlot(), upgradeItem);
        }

        List<ItemSpawner> itemSpawners = BedwarsAPI.getInstance().getGameOfPlayer(player).getItemSpawners();
        List<ItemSpawner> filteredItemSpawners = itemSpawners.stream()
                .filter(itemSpawner ->
                        Objects.nonNull(itemSpawner.getTeam()) &&
                                Objects.nonNull(itemSpawner.getTeam().getName()) &&
                                teamOfPlayer.getName().equals(itemSpawner.getTeam().getName())
                )
                .collect(Collectors.toList());

        return filteredItemSpawners;
    }
}
