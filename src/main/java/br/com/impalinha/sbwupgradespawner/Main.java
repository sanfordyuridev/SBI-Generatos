package br.com.impalinha.sbwupgradespawner;

import br.com.impalinha.sbwupgradespawner.commands.ReloadConfig;
import br.com.impalinha.sbwupgradespawner.events.*;
import br.com.impalinha.sbwupgradespawner.utils.TipoUpgrade;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.ItemSpawner;

import java.util.HashMap;
import java.util.List;

import static br.com.impalinha.sbwupgradespawner.utils.Constants.*;

public final class Main extends JavaPlugin {

    public static Main plugin;
    public static BedwarsAPI bw;
    public static HashMap<Player, List<ItemSpawner>> gameTimeItemSpawner;

    @Override
    public void onEnable() {
        init();
    }

    @Override
    public void onDisable() {
        getLogger().info(" ");
        getLogger().info(" Plugin SBI-Generators Desligado");
        getLogger().info(" Created By Impalinha");
        getLogger().info(" ");
    }

    public void registrarEventos() {
        Bukkit.getPluginManager().registerEvents(new ForgeGenerateResourceListener(), this);
        Bukkit.getPluginManager().registerEvents(new InteractUpgradeNpcListener(bw), this);
        Bukkit.getPluginManager().registerEvents(new ForgeUpgradeSpawnerListener(bw, this), this);
    }

    public void registrarDiamantesNecessarios() {
        REQUISITOS.put(TipoUpgrade.FORJA, FORJA_DIAMANTE);
        REQUISITOS.put(TipoUpgrade.ESPADAS, ESPADA_DIAMANTE);
        REQUISITOS.put(TipoUpgrade.ARMADURA, ARMADURA_DIAMANTE);
        REQUISITOS.put(TipoUpgrade.PICARETA, PICARETA_DIAMANTE);
    }

    public void registrarComandos() {
        getCommand("sbi").setExecutor(new ReloadConfig());
    }

    public void init() {
        if(!Bukkit.getPluginManager().getPlugin("BedWars").isEnabled()) {
            Bukkit.getPluginManager().disablePlugin(this);
            getLogger().info("");
            getLogger().info(" Plugin SBI-Generators Depende do Plugin BedWars");
            getLogger().info(" Desabilitando o plugin");
            getLogger().info("");
        } else {
	        plugin = this;
            bw = BedwarsAPI.getInstance();
            gameTimeItemSpawner = new HashMap<>();
	        saveDefaultConfig();
	        registrarEventos();
	        registrarComandos();
            registrarDiamantesNecessarios();
            getLogger().info(" ");
            getLogger().info(" Plugin SBI-Generators Ligado");
            getLogger().info(" Created By Impalinha");
            getLogger().info(" ");
        }
    }
}
