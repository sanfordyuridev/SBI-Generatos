package br.com.impalinha.sbwupgradespawner;

import br.com.impalinha.sbwupgradespawner.commands.ReloadConfig;
import br.com.impalinha.sbwupgradespawner.events.ForgeGenerateResourceListener;
import br.com.impalinha.sbwupgradespawner.events.ForgeUpgradeListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main plugin;

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
        Bukkit.getPluginManager().registerEvents(new ForgeUpgradeListener(), this);
    }

    public void registrarComandos() {
        getCommand("sbi").setExecutor(new ReloadConfig());
    }

    public void init() {
        plugin = this;
        saveDefaultConfig();
        registrarEventos();
        registrarComandos();
        getLogger().info(" ");
        getLogger().info(" Plugin SBI-Generators Ligado");
        getLogger().info(" Created By Impalinha");
        getLogger().info(" ");
    }

}
