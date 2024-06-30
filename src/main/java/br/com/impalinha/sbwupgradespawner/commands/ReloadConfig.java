package br.com.impalinha.sbwupgradespawner.commands;

import br.com.impalinha.sbwupgradespawner.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadConfig implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player p = (Player) commandSender;
        if(p.hasPermission("sbi.admin")) {
            if(args[0].equalsIgnoreCase("reload")) {
                Main.plugin.saveConfig();
                Main.plugin.reloadConfig();
                p.sendMessage(ChatColor.GOLD + "[Sbi-Gennerators] " + ChatColor.WHITE
                        + "Config.yml recarregada com sucesso.");
            }
        }
        return false;
    }

}
