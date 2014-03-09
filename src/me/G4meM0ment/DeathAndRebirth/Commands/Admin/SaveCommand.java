package me.G4meM0ment.DeathAndRebirth.Commands.Admin;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandInfo(
		name = "save",
		pattern = "save",
		usage = "/dar save config|data",
		desc = "Save the files and save data from cache to file",
		permission = "dar.admin.save"
	)
public class SaveCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		// Grab the argument, if any.
		String arg1 = (args.length > 1 ? args[0] : "");
		        
		if(arg1.isEmpty()) {
			plugin.getDeathAndRebirth().saveConfigs();
			plugin.getDeathAndRebirth().saveDataFiles();
			Messenger.sendMessage(sender, ChatColor.GRAY+"All files saved");
			return true;
		}

		if(arg1.equalsIgnoreCase("config")) {
			plugin.getDeathAndRebirth().saveConfigs();
			Messenger.sendMessage(sender, ChatColor.GRAY+"Configs saved");
			return true;
		} else if(arg1.equalsIgnoreCase("data")) {
			plugin.getDeathAndRebirth().saveDataFiles();
			Messenger.sendMessage(sender, ChatColor.GRAY+"Data files saved");
			return true;
		} else
			return false; 
	}	
}
