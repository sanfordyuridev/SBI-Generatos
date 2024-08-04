package br.com.impalinha.sbwupgradespawner.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.bedwars.api.TeamColor;

import java.util.HashMap;
import java.util.Map;

import static br.com.impalinha.sbwupgradespawner.utils.Constants.*;

public abstract class LevelUtils {

    public static int getDiamantesNecessarios(int levelAtual, TipoUpgrade tp) {
        if (!REQUISITOS.containsKey(tp)) {
            return -1;
        }

        int[] niveis = REQUISITOS.get(tp);

        try {
            return niveis[levelAtual];
        } catch (IndexOutOfBoundsException e) {
            return -1;
        }
    }


    public static String retornarEmRomano(double numero) {
        if(numero == 1) {
            return "I";
        } else if(numero == 2) {
            return "II";
        } else if(numero == 3) {
            return "III";
        } else if(numero == 4) {
            return "IV";
        } else {
            return "I";
        }
    }

    public static ItemStack getDiamante(int qtd) {
        ItemStack itemStack = new ItemStack(Material.DIAMOND, qtd);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemMeta != null) {
            itemMeta.setDisplayName(NOME_DIAMANTE_PADRAO);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    public static boolean hasEnoughDiamonds(Player player, int requiredAmount, ItemStack sampleDiamond) {
        Inventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getContents();
        int count = 0;

        for (ItemStack item : contents) {
            if (item != null && item.isSimilar(sampleDiamond)) {
                count += item.getAmount();
                if (count >= requiredAmount) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void removeDiamonds(Player player, int amount, ItemStack sampleDiamond) {
        Inventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getContents();
        int remaining = amount;

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null && item.isSimilar(sampleDiamond)) {
                int itemAmount = item.getAmount();
                if (itemAmount > remaining) {
                    item.setAmount(itemAmount - remaining);
                    break;
                } else {
                    remaining -= itemAmount;
                    contents[i] = null;
                    if (remaining == 0) {
                        break;
                    }
                }
            }
        }

        inventory.setContents(contents);
    }

    public static ChatColor getColor(TeamColor tc) {
        switch (tc) {
            case BLACK:
                return ChatColor.BLACK;
            case BLUE:
                return ChatColor.BLUE;
            case GREEN:
                return ChatColor.GREEN;
            case RED:
                return ChatColor.RED;
            case MAGENTA:
                return ChatColor.LIGHT_PURPLE;
            case ORANGE:
                return ChatColor.GOLD;
            case LIGHT_GRAY:
                return ChatColor.GRAY;
            case GRAY:
                return ChatColor.DARK_GRAY;
            case LIGHT_BLUE:
                return ChatColor.AQUA;
            case LIME:
                return ChatColor.GREEN;
            case CYAN:
                return ChatColor.DARK_AQUA;
            case PINK:
                return ChatColor.LIGHT_PURPLE;
            case YELLOW:
                return ChatColor.YELLOW;
            case WHITE:
                return ChatColor.WHITE;
            case BROWN:
                return ChatColor.DARK_RED;
            default:
                return ChatColor.WHITE;
        }
    }

    public static Inventory getInvUpgrade() {
        return Bukkit.createInventory(null, SIZE_MENU, Constants.TITULO_MENU);
    }

}
