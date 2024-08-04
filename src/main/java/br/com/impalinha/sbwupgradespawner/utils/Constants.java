package br.com.impalinha.sbwupgradespawner.utils;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final String TITULO_MENU = ChatColor.DARK_GRAY + "Melhorias do Time";

    public static final String START_LEVEL_EMERALD = "start-level-emerald";
    public static final String MIN_AMOUNT_EMERALD = "min-amount-emerald-generated";
    public static final String MAX_AMOUNT_EMERALD = "max-amount-emerald-generated";

    public static final String TEAM_UPGRADES = "TeamUpgrades";

    public static int MAX_LEVEL_FORJA = 5;
    public static int MAX_LEVEL_ARMADURA = 5;
    public static int MAX_LEVEL_ESPADAS = 2;
    public static int MAX_LEVEL_PICARETA = 2;


    public static int SLOT_FORJA = 13;
    public static int SLOT_ESPADA = 10;
    public static int SLOT_ARMADURA = 11;
    public static int SLOT_PICARETA = 12;

    public static String ESPADA_NOME = "Espada";
    public static String FORJA_NOME = "Forja";
    public static String ARMADURA_NOME = "Armadura";

    public static int CONDICAO_LEVEL_MAXIMO_ATINGIDO = 1;
    public static int CONDICAO_NAO_TEM_DINA = 2;
    public static int CONDICAO_PODE_UPAR = -1;

    public static String PERMISSION_ADMIN_SBI = "sbi.admin";
    public static String RELOAD_CMD = "reload";

    public static int SIZE_MENU = 27;

    public static String NOME_DIAMANTE_PADRAO = ChatColor.BLUE + "Diamond";

    public static int[] FORJA_DIAMANTE = {2, 4, 6, 8};
    public static int[] ESPADA_DIAMANTE = {4, 16};
    public static int[] ARMADURA_DIAMANTE = {2, 4, 8, 16};
    public static int[] PICARETA_DIAMANTE = {2, 4};

    public static Map<TipoUpgrade, int[]> REQUISITOS = new HashMap<>();
}