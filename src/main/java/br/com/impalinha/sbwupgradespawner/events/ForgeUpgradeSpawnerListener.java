package br.com.impalinha.sbwupgradespawner.events;

import br.com.impalinha.sbwupgradespawner.Main;
import br.com.impalinha.sbwupgradespawner.utils.Constants;
import br.com.impalinha.sbwupgradespawner.utils.LevelUtils;
import br.com.impalinha.sbwupgradespawner.utils.TipoUpgrade;
import io.github.pronze.sba.SBA;
import io.github.pronze.sba.game.GameStorage;
import io.github.pronze.sba.game.IGameStorage;
import io.github.pronze.sba.utils.ShopUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        if(event.getWhoClicked().getOpenInventory().getTitle().equals(Constants.TITULO_MENU)) {
            String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
            if(displayName != null) {
                if(displayName.contains("Forja")) {
                    event.setCancelled(true);
                    List<ItemSpawner> itemSpawner = Main.gameTimeItemSpawner.get(event.getWhoClicked());
                    double level = itemSpawner.get(0).getLevel();
                    int diamantesNecessarios = LevelUtils.getDiamantesNecessarios((int) level, TipoUpgrade.FORJA);

                    double levelQueVai = level;
                    ItemStack diamante = LevelUtils.getDiamante(1);
                    if(level == 5)  {
                        event.getWhoClicked().sendMessage(ChatColor.RED + "O gerador do seu time já atingiu o nível máximo.");
                    } else {
                        if(hasEnoughDiamonds((Player) event.getWhoClicked(), diamantesNecessarios, diamante)) {
                            Player whoClicked = (Player) event.getWhoClicked();
                            RunningTeam teamOfPlayer = api.getGameOfPlayer(whoClicked).getTeamOfPlayer(whoClicked);
                            List<Player> connectedPlayers = teamOfPlayer.getConnectedPlayers();
                            for(ItemSpawner i : itemSpawner) {
                                i.setLevel(levelQueVai+1);
                                i.setCurrentLevel(levelQueVai+1);
                            }
                            removeDiamonds(whoClicked, diamantesNecessarios, diamante);
                            for(Player p : connectedPlayers) {
                                p.sendMessage(getColor(teamOfPlayer.getColor()) + whoClicked.getName() + " " + ChatColor.YELLOW + "adquiriu a melhoria " + ChatColor.GREEN + "Forja " + retornarEmRomano(levelQueVai) + ChatColor.YELLOW + ".");
                            }
                        } else {
                            event.getWhoClicked().sendMessage(ChatColor.RED + "Você não possui diamantes o suficiente.");
                        }
                    }
                } else if(displayName.contains("Espada")) {
                    Player whoClicked = (Player) event.getWhoClicked();
                    Game gameOfPlayer = api.getGameOfPlayer(whoClicked);
                    RunningTeam teamOfPlayer = gameOfPlayer.getTeamOfPlayer(whoClicked);


                    event.setCancelled(true);
                    Optional<IGameStorage> gameStorage1 = SBA.getInstance().getGameStorage(gameOfPlayer);
                    IGameStorage gameStorage = gameStorage1.get();
                    int level = gameStorage.getSharpnessLevel(teamOfPlayer).orElse(0);
                    int diamantesNecessarios = LevelUtils.getDiamantesNecessarios(level, TipoUpgrade.ESPADAS);
                    int levelQueVai = level + 1;
                    ItemStack diamante = LevelUtils.getDiamante(1);
                    if(level >= 2)  {
                        event.getWhoClicked().sendMessage(ChatColor.RED + "A Afiação do seu time já atingiu o nível máximo.");
                    } else {
                        if(hasEnoughDiamonds((Player) event.getWhoClicked(), diamantesNecessarios, diamante)) {
                            List<Player> connectedPlayers = teamOfPlayer.getConnectedPlayers();
                            gameStorage.setSharpnessLevel(teamOfPlayer, levelQueVai);
                            teamOfPlayer.getConnectedPlayers().forEach(teamPlayer -> {
                                Arrays.stream(teamPlayer.getInventory().getContents())
                                        .filter(Objects::nonNull)
                                        .forEach(item -> {
                                            ShopUtil.applyTeamEnchants(teamPlayer, item);
                                        });
                            });
                            removeDiamonds(whoClicked, diamantesNecessarios, diamante);
                            for(Player p : connectedPlayers) {
                                p.sendMessage(getColor(teamOfPlayer.getColor()) + whoClicked.getName() + " " + ChatColor.YELLOW + "adquiriu a melhoria " + ChatColor.GREEN + "Afiação " + retornarEmRomano(levelQueVai) + ChatColor.YELLOW + ".");
                            }
                        } else {
                            event.getWhoClicked().sendMessage(ChatColor.RED + "Você não possui diamantes o suficiente.");
                        }
                    }
                }
            }
        }
    }
}
