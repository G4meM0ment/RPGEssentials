package me.G4meM0ment.RPGItem.Commands;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;
import me.G4meM0ment.RPGItem.Handler.ListHandler;

import org.bukkit.command.CommandSender;

@CommandInfo(
		name = "list",
		pattern = "list",
		usage = "/ri list",
		desc = "List all loaded items",
		permission = "rpgitem.admin.give"
	)
public class ListCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {   
		Messenger.sendMessage(sender, "Available items: "+ListHandler.getCustomItemTypes().keySet());
		return true;
	}	
}
