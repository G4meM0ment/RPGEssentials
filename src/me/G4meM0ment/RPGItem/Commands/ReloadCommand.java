package me.G4meM0ment.RPGItem.Commands;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandInfo(
		name = "reload",
		pattern = "reload",
		usage = "/ri reload (config|data)",
		desc = "Reloads the files and brings data to cache",
		permission = "rpgitem.admin.reload"
	)
public class ReloadCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {   
	    plugin.getRPGItem().reloadConfigs();
	    Messenger.sendMessage(sender, ChatColor.GRAY+"All files reloaded");
	    return true;
	}	
}
