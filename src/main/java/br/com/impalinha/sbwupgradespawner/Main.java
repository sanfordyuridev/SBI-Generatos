package br.com.impalinha.sbwupgradespawner;

import br.com.impalinha.sbwupgradespawner.commands.ReloadConfig;
import br.com.impalinha.sbwupgradespawner.events.ForgeGenerateResourceListener;
import br.com.impalinha.sbwupgradespawner.events.ForgeUpgradeSpawnerListener;
import br.com.impalinha.sbwupgradespawner.events.OpenUpgradeStoreListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.bedwars.api.game.ItemSpawner;

import java.util.HashMap;
import java.util.List;

public final class Main extends JavaPlugin {

    public static Main plugin;
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
        Bukkit.getPluginManager().registerEvents(new OpenUpgradeStoreListener(), this);
        Bukkit.getPluginManager().registerEvents(new ForgeUpgradeSpawnerListener(), this);
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
            gameTimeItemSpawner = new HashMap<>();
	        saveDefaultConfig();
	        registrarEventos();
	        registrarComandos();
            getLogger().info(" ");
            getLogger().info(" Plugin SBI-Generators Ligado");
            getLogger().info(" Created By Impalinha");
            getLogger().info(" ");
        }
    }
}
