package br.com.impalinha.sbwupgradespawner.commands;

import br.com.impalinha.sbwupgradespawner.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static br.com.impalinha.sbwupgradespawner.utils.Constants.PERMISSION_ADMIN_SBI;
import static br.com.impalinha.sbwupgradespawner.utils.Constants.RELOAD_CMD;

public class ReloadConfig implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player p = (Player) commandSender;
        if(p.hasPermission(PERMISSION_ADMIN_SBI)) {
            if(args[0].equalsIgnoreCase(RELOAD_CMD)) {
                Main.plugin.saveConfig();
                Main.plugin.reloadConfig();
                p.sendMessage(ChatColor.GOLD + "[Sbi-Gennerators] " + ChatColor.WHITE
                        + "Config.yml recarregada com sucesso.");
            }
        }
        return false;
    }

}
