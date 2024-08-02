package br.com.impalinha.sbwupgradespawner.upgrades;

import br.com.impalinha.sbwupgradespawner.Main;
import br.com.impalinha.sbwupgradespawner.interfaces.IUpgrade;
import br.com.impalinha.sbwupgradespawner.utils.LevelUtils;
import br.com.impalinha.sbwupgradespawner.utils.TipoUpgrade;
import io.github.pronze.sba.game.IGameStorage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.RunningTeam;
import org.screamingsandals.bedwars.api.game.ItemSpawner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static br.com.impalinha.sbwupgradespawner.utils.Constants.*;
import static br.com.impalinha.sbwupgradespawner.utils.LevelUtils.hasEnoughDiamonds;

public class ForjaUpgrade implements IUpgrade {
    @Override
    public ItemStack createItem(Player player, RunningTeam team, IGameStorage gameStorage) {
        ItemStack itemStackForja = new ItemStack(Material.FURNACE);
        ItemMeta itemMetaForja = itemStackForja.getItemMeta();
        List<String> loreForja = new ArrayList<>();

        int currentLevelForja = getCurrentLevel(player, gameStorage, team);

        itemMetaForja.setDisplayName(ChatColor.RED + "Forja " + LevelUtils.retornarEmRomano(currentLevelForja - 1));
        loreForja.add(ChatColor.GRAY + "Aumente o número de recursos ");
        loreForja.add(ChatColor.GRAY + "que nascem em sua ilha.");
        loreForja.add("");

        loreForja.add((currentLevelForja >= 2 ? ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 1: + 50% de Recursos, " + ChatColor.AQUA + "2 Diamantes ");
        loreForja.add((currentLevelForja >= 3 ? ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 2: + 100% de Recursos, " + ChatColor.AQUA + "4 Diamantes ");
        loreForja.add((currentLevelForja >= 4 ? ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 3: Nasce Esmeraldas, " + ChatColor.AQUA + "6 Diamantes ");
        loreForja.add((currentLevelForja >= 5 ? ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 4: + 200% de Recursos, " + ChatColor.AQUA + "8 Diamantes ");
        loreForja.add("");

        int canUP = canUpgrade(player, currentLevelForja, gameStorage);

        if (canUP == CONDICAO_PODE_UPAR) {
            loreForja.add(ChatColor.GREEN + "Clique para subir o Level.");
        } else if(canUP == CONDICAO_NAO_TEM_DINA) {
            loreForja.add(ChatColor.RED + "Você não tem Diamantes o suficiente.");
        } else if(canUP == CONDICAO_LEVEL_MAXIMO_ATINGIDO) {
            loreForja.add(ChatColor.GREEN + "Level máximo atingido");
        }

        itemMetaForja.setLore(loreForja);
        itemStackForja.setItemMeta(itemMetaForja);
        return itemStackForja;
    }

    @Override
    public int getCurrentLevel(Player p, IGameStorage gameStorage, RunningTeam team) {
        List<ItemSpawner> itemSpawners = BedwarsAPI.getInstance().getGameOfPlayer(p).getItemSpawners();
        List<ItemSpawner> filteredItemSpawners = itemSpawners.stream()
                .filter(itemSpawner ->
                        Objects.nonNull(itemSpawner.getTeam()) &&
                                Objects.nonNull(itemSpawner.getTeam().getName()) &&
                                team.getName().equals(itemSpawner.getTeam().getName())
                )
                .collect(Collectors.toList());

        ItemSpawner is = filteredItemSpawners.get(0);
        return (int) is.getLevel();
    }

    @Override
    public int getMaxLevel() {
        return MAX_LEVEL_FORJA;
    }

    @Override
    public int getDiamantesNecessarios(int currentLevel) {
        return LevelUtils.getDiamantesNecessarios(currentLevel, TipoUpgrade.FORJA);
    }

    @Override
    public int canUpgrade(Player player, int currentLevel, IGameStorage gameStorage) {
        if(!hasEnoughDiamonds(player, getDiamantesNecessarios(currentLevel), LevelUtils.getDiamante(1))) {
            return CONDICAO_NAO_TEM_DINA;
        } else if(getCurrentLevel(player, gameStorage, BedwarsAPI.getInstance().getGameOfPlayer(player).getTeamOfPlayer(player)) >= getMaxLevel()) {
            return CONDICAO_LEVEL_MAXIMO_ATINGIDO;
        } else {
           return CONDICAO_PODE_UPAR;
        }
    }

    @Override
    public int getSlot() {
        return SLOT_FORJA;
    }

    @Override
    public String getNome() {
        return FORJA_NOME;
    }

    @Override
    public String getNomeEncantamento() {
        return "Forja";
    }

    @Override
    public void setLevel(RunningTeam team, IGameStorage gameStorage, int novoLevel) {
        Player player = team.getConnectedPlayers().get(0);
        List<ItemSpawner> itemSpawner = Main.gameTimeItemSpawner.get(player);

        for(ItemSpawner i : itemSpawner) {
            i.setLevel(novoLevel);
            i.setCurrentLevel(novoLevel);
        }
    }
}
