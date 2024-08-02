package br.com.impalinha.sbwupgradespawner.interfaces;

import io.github.pronze.sba.game.IGameStorage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.RunningTeam;

public interface IUpgrade {
    ItemStack createItem(Player player, RunningTeam team, IGameStorage gameStorage);
    int getCurrentLevel(Player p, IGameStorage gameStorage, RunningTeam team);
    int getMaxLevel();
    int getDiamantesNecessarios(int currentLevel);
    boolean canUpgrade(Player player, int currentLevel, IGameStorage gameStorage);
    int getSlot();
}
