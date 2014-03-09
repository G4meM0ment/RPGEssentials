package me.G4meM0ment.UnamedPortalPlugin.Commands;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;
import me.G4meM0ment.UnamedPortalPlugin.Handler.PortalHandler;

import org.bukkit.command.CommandSender;

@CommandInfo(
		name = "list",
		pattern = "list",
		usage = "/upp list",
		desc = "List all portals",
		permission = "upp.admin"
	)
public class PortalListCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		PortalHandler ph = new PortalHandler(plugin.getUnnamedPortalPlugin());
		Messenger.sendMessage(sender, "Portals: "+ph.getPortals().keySet());
		return true;
	}	
}
