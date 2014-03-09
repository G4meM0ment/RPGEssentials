package me.G4meM0ment.Chaintrain.Commands.Admin;

import me.G4meM0ment.Chaintrain.DataStorage.PlayerData;
import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandInfo(
		name = "save",
		pattern = "save",
		usage = "/chaintrain save",
		desc = "Saves the cached data to file",
		permission = "chaintrain.admin"
	)
public class SaveCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		
		PlayerData pD = new PlayerData();
		plugin.getChaintrain().saveConfig();
		pD.saveDataToFile();
		Messenger.sendMessage(sender, ChatColor.GRAY+"Files saved");
		return true;
	}	
}

