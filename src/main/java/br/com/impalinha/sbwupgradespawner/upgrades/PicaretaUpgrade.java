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

public class PicaretaUpgrade implements IUpgrade {
    @Override
    public ItemStack createItem(Player player, RunningTeam team, IGameStorage gameStorage) {
        ItemStack itemStackPicareta = new ItemStack(Material.GOLDEN_PICKAXE);
        ItemMeta itemMetaPicareta = itemStackPicareta.getItemMeta();
        List<String> lorePicareta = new ArrayList<>();

        int currentLevelPicareta = getCurrentLevel(player, gameStorage, team);

        itemMetaPicareta.setDisplayName(ChatColor.RED + "Sem tempo Irmão " + LevelUtils.retornarEmRomano(currentLevelPicareta - 1));
        lorePicareta.add(ChatColor.GRAY + "O seu time ganha o efeito ");
        lorePicareta.add(ChatColor.GRAY + "de Pressa permanente. ");
        lorePicareta.add("");

        lorePicareta.add((currentLevelPicareta >= 1 ? ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 1: Pressa I, " + ChatColor.AQUA + "2 Diamantes ");
        lorePicareta.add((currentLevelPicareta >= 2 ? ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 2: Pressa II, " + ChatColor.AQUA + "4 Diamantes ");
        lorePicareta.add("");

        int canUP = canUpgrade(player, currentLevelPicareta, gameStorage);

        if (canUP == CONDICAO_PODE_UPAR) {
            lorePicareta.add(ChatColor.GREEN + "Clique para subir o Level.");
        } else if(canUP == CONDICAO_NAO_TEM_DINA) {
            lorePicareta.add(ChatColor.RED + "Você não tem Diamantes o suficiente.");
        } else if(canUP == CONDICAO_LEVEL_MAXIMO_ATINGIDO) {
            lorePicareta.add(ChatColor.GREEN + "Level máximo atingido");
        }

        itemMetaPicareta.setLore(lorePicareta);
        itemMetaPicareta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStackPicareta.setItemMeta(itemMetaPicareta);
        return itemStackPicareta;
    }

    @Override
    public int getCurrentLevel(Player p, IGameStorage gameStorage, RunningTeam team) {
        return gameStorage.getEfficiencyLevel(team).orElse(0);
    }

    @Override
    public int getMaxLevel() {
        return MAX_LEVEL_PICARETA;
    }

    @Override
    public int getDiamantesNecessarios(int currentLevel) {
        return LevelUtils.getDiamantesNecessarios(currentLevel, TipoUpgrade.PICARETA);
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
        return SLOT_PICARETA;
    }

    @Override
    public String getNome() {
        return "Pressa";
    }

    @Override
    public String getNomeEncantamento() {
        return "Pressa";
    }

    @Override
    public void setLevel(RunningTeam team, IGameStorage gameStorage, int novoLevel) {
        gameStorage.setEfficiencyLevel(team, novoLevel);
        team.getConnectedPlayers().forEach(teamPlayer -> {
            Arrays.stream(teamPlayer.getInventory().getContents())
                    .filter(Objects::nonNull)
                    .forEach(item -> {
                        ShopUtil.applyTeamEnchants(teamPlayer, item);
                    });
        });
    }
}
