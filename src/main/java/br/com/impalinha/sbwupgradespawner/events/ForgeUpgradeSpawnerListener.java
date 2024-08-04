package br.com.impalinha.sbwupgradespawner.events;

import br.com.impalinha.sbwupgradespawner.Main;
import br.com.impalinha.sbwupgradespawner.interfaces.IUpgrade;
import br.com.impalinha.sbwupgradespawner.upgrades.ArmaduraUpgrade;
import br.com.impalinha.sbwupgradespawner.upgrades.EspadasUpgrade;
import br.com.impalinha.sbwupgradespawner.upgrades.ForjaUpgrade;
import br.com.impalinha.sbwupgradespawner.upgrades.PicaretaUpgrade;
import br.com.impalinha.sbwupgradespawner.utils.Constants;
import br.com.impalinha.sbwupgradespawner.utils.LevelUtils;
import br.com.impalinha.sbwupgradespawner.utils.TipoUpgrade;
import io.github.pronze.sba.SBA;
import io.github.pronze.sba.game.GameStorage;
import io.github.pronze.sba.game.IGameStorage;
import io.github.pronze.sba.utils.ShopUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.RunningTeam;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStore;
import org.screamingsandals.bedwars.api.game.ItemSpawner;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static br.com.impalinha.sbwupgradespawner.utils.Constants.*;
import static br.com.impalinha.sbwupgradespawner.utils.LevelUtils.*;

public class ForgeUpgradeSpawnerListener implements Listener {

    private final BedwarsAPI api;
    private final Main pl;
    public ForgeUpgradeSpawnerListener(BedwarsAPI api, Main pl) {
        this.api = api;
        this.pl = pl;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onForgeUpgradeBoughtSpawner(InventoryClickEvent event) {
        HumanEntity whoClicked = event.getWhoClicked();
        if(whoClicked.getOpenInventory().getTitle().equals(Constants.TITULO_MENU)) {
            event.setCancelled(true);
            List<IUpgrade> upgrades = List.of(new ForjaUpgrade(), new EspadasUpgrade(), new ArmaduraUpgrade(), new PicaretaUpgrade());

            for (IUpgrade upgrade : upgrades) {
                if(event.getSlot() == upgrade.getSlot()) {
                    Player p = (Player) whoClicked;
                    Game gameOfPlayer = api.getGameOfPlayer(p);
                    RunningTeam teamOfPlayer = gameOfPlayer.getTeamOfPlayer(p);
                    IGameStorage iGameStorage = SBA.getInstance().getGameStorage(gameOfPlayer).orElseThrow();
                    int currentLevel = upgrade.getCurrentLevel(p, iGameStorage, teamOfPlayer);
                    int canUP = upgrade.canUpgrade(p, currentLevel, iGameStorage);
                    if(canUP == CONDICAO_PODE_UPAR) {
                        int levelQueVai = currentLevel + 1;
                        upgrade.setLevel(teamOfPlayer, iGameStorage, levelQueVai);
                        for(Player cp : teamOfPlayer.getConnectedPlayers()) {
                            cp.sendMessage(getColor(teamOfPlayer.getColor()) + whoClicked.getName() + " " + ChatColor.YELLOW + "adquiriu a melhoria " + ChatColor.GREEN + upgrade.getNome() + " " + retornarEmRomano(levelQueVai) + ChatColor.YELLOW + ".");
                        }
                        int diamantesNecessarios = upgrade.getDiamantesNecessarios(currentLevel);
                        removeDiamonds(p, diamantesNecessarios, LevelUtils.getDiamante(1));
                    } else if(canUP == CONDICAO_NAO_TEM_DINA) {
                        p.sendMessage(ChatColor.RED + "Você não possui diamantes o suficiente.");
                    } else if(canUP == CONDICAO_LEVEL_MAXIMO_ATINGIDO) {
                        p.sendMessage(ChatColor.RED + "A " + upgrade.getNomeEncantamento() + " do seu time já atingiu o nível máximo.");
                    }
                    break;
                }
            }
        }
    }
}
