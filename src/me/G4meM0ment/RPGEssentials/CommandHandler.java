package me.G4meM0ment.RPGEssentials;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler {
	
	private RPGEssentials plugin;
	private PermHandler ph;
	
	private String mainCmd = "ge";
	
	public CommandHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		ph = new PermHandler(plugin);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
			
		if(command.getName().equalsIgnoreCase(mainCmd) && args.length > 0 && args[0].equals("reload") && sender instanceof Player && ph.checkReloadPerms(player)) {
			plugin.reloadRPGEssentials();
			player.sendMessage("RPGEssentials all plugins reloaded!");
		    return true;
		}
		if(command.getName().equalsIgnoreCase("rpgitem") && args.length > 0 && args[0].equals("reload") && ph.checkReloadPerms(player) && !(sender instanceof Player)) {
			plugin.reloadRPGEssentials();
		}
		return false;
	}
}
