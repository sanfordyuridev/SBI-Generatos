package br.com.impalinha.sbwupgradespawner.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class LevelUtils {

    public static int getDiamantesNecessarios(int levelAtual) {
        int diamantesNecessarios = -1;
        if (levelAtual != 5) {
            double levelQueVai = levelAtual + 1;
            if (levelQueVai == 1) {
                diamantesNecessarios = 2;
            } else if (levelQueVai == 2) {
                diamantesNecessarios = 4;
            } else if (levelQueVai == 3) {
                diamantesNecessarios = 6;
            } else if (levelQueVai == 4) {
                diamantesNecessarios = 8;
            }
        }
        return diamantesNecessarios;
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

    public static ItemStack getDiamante() {
        ItemStack itemStack = new ItemStack(Material.DIAMOND);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE + "Diamond");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
