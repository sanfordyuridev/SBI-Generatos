package br.com.impalinha.sbwupgradespawner.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.bedwars.api.TeamColor;

public abstract class LevelUtils {

    public static int getDiamantesNecessarios(int levelAtual, TipoUpgrade tp) {
        int diamantesNecessarios = -1;
        if(tp.equals(TipoUpgrade.FORJA)) {
            if (levelAtual != 5) {
                double levelQueVai = levelAtual + 1;
                if (levelQueVai == 2) {
                    diamantesNecessarios = 2;
                } else if (levelQueVai == 3) {
                    diamantesNecessarios = 4;
                } else if (levelQueVai == 4) {
                    diamantesNecessarios = 6;
                } else if (levelQueVai == 5) {
                    diamantesNecessarios = 8;
                }
            }
        } else if(tp.equals(TipoUpgrade.ESPADAS)) {
            if (levelAtual != 3) {
                double levelQueVai = levelAtual + 1;
                if (levelQueVai == 2) {
                    diamantesNecessarios = 4;
                } else if (levelQueVai == 3) {
                    diamantesNecessarios = 16;
                }
            }
        } else if(tp.equals(TipoUpgrade.ARMADURA)) {
            if (levelAtual != 5) {
                double levelQueVai = levelAtual + 1;
                if (levelQueVai == 2) {
                    diamantesNecessarios = 2;
                } else if (levelQueVai == 3) {
                    diamantesNecessarios = 4;
                } else if (levelQueVai == 4) {
                    diamantesNecessarios = 8;
                } else if (levelQueVai == 5) {
                    diamantesNecessarios = 16;
                }
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

    public static ItemStack getDiamante(int qtd) {
        ItemStack itemStack = new ItemStack(Material.DIAMOND, qtd);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE + "Diamond");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static boolean hasEnoughDiamonds(Player player, int requiredAmount, ItemStack sampleDiamond) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
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
        int remaining = amount;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.isSimilar(sampleDiamond)) {
                if (item.getAmount() > remaining) {
                    item.setAmount(item.getAmount() - remaining);
                    break;
                } else {
                    remaining -= item.getAmount();
                    player.getInventory().remove(item);
                }
            }
        }
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
        Inventory inv = Bukkit.createInventory(null, 27, Constants.TITULO_MENU);
        return inv;
    }

}
