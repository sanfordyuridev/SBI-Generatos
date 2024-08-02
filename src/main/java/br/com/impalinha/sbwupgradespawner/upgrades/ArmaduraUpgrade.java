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

public class ArmaduraUpgrade implements IUpgrade {
    @Override
    public ItemStack createItem(Player player, RunningTeam team, IGameStorage gameStorage) {
        ItemStack itemStackArmadura = new ItemStack(Material.IRON_CHESTPLATE);
        ItemMeta itemMetaArmadura = itemStackArmadura.getItemMeta();
        List<String> loreArmadura = new ArrayList<>();

        int currentLevelArmadura = getCurrentLevel(player, gameStorage, team);

        itemMetaArmadura.setDisplayName(ChatColor.RED + "Reforço na Armadura " + LevelUtils.retornarEmRomano(currentLevelArmadura - 1));
        loreArmadura.add(ChatColor.GRAY + "O seu time ganha Proteção");
        loreArmadura.add(ChatColor.GRAY + "em todas as peças da armadura.");
        loreArmadura.add("");

        loreArmadura.add((currentLevelArmadura >= 1 ? ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 1: Proteção I, " + ChatColor.AQUA + "2 Diamantes ");
        loreArmadura.add((currentLevelArmadura >= 2 ? ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 2: Proteção II, " + ChatColor.AQUA + "4 Diamantes ");
        loreArmadura.add((currentLevelArmadura >= 3 ? ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 2: Proteção II, " + ChatColor.AQUA + "8 Diamantes ");
        loreArmadura.add((currentLevelArmadura >= 4 ? ChatColor.BOLD + "" + ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + ChatColor.GRAY + "Nível 2: Proteção II, " + ChatColor.AQUA + "16 Diamantes ");
        loreArmadura.add("");

        int canUP = canUpgrade(player, currentLevelArmadura, gameStorage);

        if (canUP == CONDICAO_PODE_UPAR) {
            loreArmadura.add(ChatColor.GREEN + "Clique para subir o Level.");
        } else if(canUP == CONDICAO_NAO_TEM_DINA) {
            loreArmadura.add(ChatColor.RED + "Você não tem Diamantes o suficiente.");
        } else if(canUP == CONDICAO_LEVEL_MAXIMO_ATINGIDO) {
            loreArmadura.add(ChatColor.GREEN + "Level máximo atingido");
        }


        itemMetaArmadura.setLore(loreArmadura);
        itemMetaArmadura.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStackArmadura.setItemMeta(itemMetaArmadura);
        return itemStackArmadura;
    }

    @Override
    public int getCurrentLevel(Player p, IGameStorage gameStorage, RunningTeam team) {
        return gameStorage.getProtectionLevel(team).orElse(0);
    }

    @Override
    public int getMaxLevel() {
        return MAX_LEVEL_ARMADURA;
    }

    @Override
    public int getDiamantesNecessarios(int currentLevel) {
        return LevelUtils.getDiamantesNecessarios(currentLevel, TipoUpgrade.ARMADURA);
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
        return SLOT_ARMADURA;
    }

    @Override
    public String getNome() {
        return ARMADURA_NOME;
    }

    @Override
    public String getNomeEncantamento() {
        return "Proteção";
    }

    @Override
    public void setLevel(RunningTeam team, IGameStorage gameStorage, int novoLevel) {
        gameStorage.setProtectionLevel(team, novoLevel);
        team.getConnectedPlayers().forEach(teamPlayer -> {
            Arrays.stream(teamPlayer.getInventory().getContents())
                    .filter(Objects::nonNull)
                    .forEach(item -> {
                        ShopUtil.applyTeamEnchants(teamPlayer, item);
                    });
        });
    }
}
