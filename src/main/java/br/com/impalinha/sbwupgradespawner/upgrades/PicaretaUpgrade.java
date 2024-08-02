package br.com.impalinha.sbwupgradespawner.upgrades;

import br.com.impalinha.sbwupgradespawner.interfaces.IUpgrade;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.RunningTeam;

public class PicaretaUpgrade implements IUpgrade {
    @Override
    public ItemStack createItem(Player player, RunningTeam team, IGameStorage gameStorage) {
        return null;
    }

    @Override
    public int getCurrentLevel(Player p, IGameStorage gameStorage, RunningTeam team) {
        return 0;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public int getDiamantesNecessarios(int currentLevel) {
        return 0;
    }

    @Override
    public int canUpgrade(Player player, int currentLevel, IGameStorage gameStorage) {
        return 0;
    }

    @Override
    public int getSlot() {
        return 0;
    }

    @Override
    public String getNome() {
        return "";
    }

    @Override
    public String getNomeEncantamento() {
        return "";
    }

    @Override
    public void setLevel(RunningTeam team, IGameStorage gameStorage, int novoLevel) {

    }
}
