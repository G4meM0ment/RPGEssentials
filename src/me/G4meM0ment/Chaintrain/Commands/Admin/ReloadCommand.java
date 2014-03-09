package me.G4meM0ment.Chaintrain.Commands.Admin;

import me.G4meM0ment.Chaintrain.DataStorage.PlayerData;
import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandInfo(
		name = "reload",
		pattern = "reload",
		usage = "/chaintrain reload",
		desc = "Reloads the files and brings data to cache",
		permission = "chaintrain.admin"
	)
public class ReloadCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		
		PlayerData pD = new PlayerData();
		plugin.getChaintrain().reloadConfig();
		pD.reloadConfig();
		pD.loadDataFromFile();
		Messenger.sendMessage(sender, ChatColor.GRAY+"Files reloaded");
		return true;
	}	
}
