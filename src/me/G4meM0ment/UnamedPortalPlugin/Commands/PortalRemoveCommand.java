package me.G4meM0ment.UnamedPortalPlugin.Commands;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;
import me.G4meM0ment.UnamedPortalPlugin.Handler.PortalHandler;
import me.G4meM0ment.UnamedPortalPlugin.Portal.Portal;

import org.bukkit.command.CommandSender;

@CommandInfo(
		name = "remove",
		pattern = "remove",
		usage = "/upp remove (<name>)",
		desc = "Removes the given portal",
		permission = "upp.admin"
	)
public class PortalRemoveCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		PortalHandler ph = new PortalHandler(plugin.getUnnamedPortalPlugin());
		Portal portal = ph.getPortal(args[0]);
		
		if(portal == null)
			Messenger.sendMessage(sender, "Portal not found");
		else {
			ph.removePortal(portal);
			Messenger.sendMessage(sender, "Portal removed");
		}
		return true;
	}	
}
