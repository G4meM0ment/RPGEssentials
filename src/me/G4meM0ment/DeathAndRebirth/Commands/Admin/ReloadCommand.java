package me.G4meM0ment.DeathAndRebirth.Commands.Admin;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandInfo(
		name = "reload",
		pattern = "reload",
		usage = "/dar reload config|data",
		desc = "Reloads the files and brings data to cache",
		permission = "dar.admin.reload"
	)
public class ReloadCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		// Grab the argument, if any.
	    String arg1 = (args.length > 0 ? args[0] : "");
	        
	    if(arg1.isEmpty()) {
	    	plugin.getDeathAndRebirth().reloadConfigs();
	    	plugin.getDeathAndRebirth().reloadDataFiles();
	        Messenger.sendMessage(sender, ChatColor.GRAY+"All files reloaded");
	        return true;
	    }

	    if(arg1.equalsIgnoreCase("config")) {
	    	plugin.getDeathAndRebirth().reloadConfigs();
	    	Messenger.sendMessage(sender, ChatColor.GRAY+"Configs reloaded");
	    } else if(arg1.equalsIgnoreCase("data")) {
	    	plugin.getDeathAndRebirth().reloadDataFiles();
	        Messenger.sendMessage(sender, ChatColor.GRAY+"Data files reloaded");
	    } else
	    	return false;
	    return true;
	}	
}
