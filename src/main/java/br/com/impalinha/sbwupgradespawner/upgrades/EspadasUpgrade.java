package br.com.impalinha.sbwupgradespawner.upgrades;

import br.com.impalinha.sbwupgradespawner.interfaces.IUpgrade;
import br.com.impalinha.sbwupgradespawner.utils.LevelUtils;
import br.com.impalinha.sbwupgradespawner.utils.TipoUpgrade;
import io.github.pronze.sba.game.IGameStorage;
import io.github.pronze.sba.utils.ShopUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.RunningTeam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static br.com.impalinha.sbwupgradespawner.utils.Constants.*;
import static br.com.impalinha.sbwupgradespawner.utils.LevelUtils.hasEnoughDiamonds;

public class EspadasUpgrade implements IUpgrade {

    @Override
    public ItemStack createItem(Player player, RunningTeam team, IGameStorage gameStorage) {
        ItemStack itemStackEspadas = new ItemStack(Material.IRON_SWORD);
        ItemMeta itemMetaEspadas = itemStackEspadas.getItemMeta();
        List<String> loreEspadas = new ArrayList<>();

        int currentLevelEspada = getCurrentLevel(player, gameStorage, team);

        itemMetaEspadas.setDisplayName(ChatColor.RED + "Afiação nas Espadas " + LevelUtils.retornarEmRomano(currentLevelEspada - 1));
        loreEspadas.add(ChatColor.GRAY + "O seu time ganha Afiação ");
        loreEspadas.add(ChatColor.GRAY + "permanente em todas ");
        loreEspadas.add(ChatColor.GRAY + "as espadas. ");
        loreEspadas.add("");

        loreEspadas.add((currentLevelEspada >= 1 ? ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 1: Afiação I, " + ChatColor.AQUA + "4 Diamantes ");
        loreEspadas.add((currentLevelEspada >= 2 ? ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 2: Afiação II, " + ChatColor.AQUA + "16 Diamantes ");
        loreEspadas.add("");

        int canUP = canUpgrade(player, currentLevelEspada, gameStorage);

        if (canUP == CONDICAO_PODE_UPAR) {
            loreEspadas.add(ChatColor.GREEN + "Clique para subir o Level.");
        } else if(canUP == CONDICAO_NAO_TEM_DINA) {
            loreEspadas.add(ChatColor.RED + "Você não tem Diamantes o suficiente.");
        } else if(canUP == CONDICAO_LEVEL_MAXIMO_ATINGIDO) {
            loreEspadas.add(ChatColor.GREEN + "Level máximo atingido");
        }

        itemMetaEspadas.setLore(loreEspadas);
        itemMetaEspadas.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStackEspadas.setItemMeta(itemMetaEspadas);
        return itemStackEspadas;
    }

    @Override
    public int getCurrentLevel(Player player, IGameStorage gameStorage, RunningTeam team) {
        return gameStorage.getSharpnessLevel(team).orElse(0);
    }

    @Override
    public int getMaxLevel() {
        return MAX_LEVEL_ESPADAS;
    }

    @Override
    public int getDiamantesNecessarios(int currentLevel) {
        return LevelUtils.getDiamantesNecessarios(currentLevel, TipoUpgrade.ESPADAS);
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
        return SLOT_ESPADA;
    }

    @Override
    public String getNome() {
        return ESPADA_NOME;
    }

    @Override
    public String getNomeEncantamento() {
        return "Afiação";
    }

    @Override
    public void setLevel(RunningTeam team, IGameStorage gameStorage, int novoLevel) {
        gameStorage.setSharpnessLevel(team, novoLevel);
        team.getConnectedPlayers().forEach(teamPlayer -> {
            Arrays.stream(teamPlayer.getInventory().getContents())
                    .filter(Objects::nonNull)
                    .forEach(item -> {
                        ShopUtil.applyTeamEnchants(teamPlayer, item);
                    });
        });
    }
}
