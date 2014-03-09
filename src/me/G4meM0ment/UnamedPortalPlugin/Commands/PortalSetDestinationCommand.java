package me.G4meM0ment.UnamedPortalPlugin.Commands;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;
import me.G4meM0ment.UnamedPortalPlugin.DataStorage.PortalData;
import me.G4meM0ment.UnamedPortalPlugin.Handler.PortalHandler;
import me.G4meM0ment.UnamedPortalPlugin.Portal.Portal;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
		name = "setDestination",
		pattern = "setDestination",
		usage = "/upp setDestination <name> (<destination>)",
		desc = "Updates the destination for the given location",
		permission = "upp.admin"
	)
public class PortalSetDestinationCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		
		PortalHandler ph = new PortalHandler(plugin.getUnnamedPortalPlugin());
		Portal portal = ph.getPortal(args[1]);
		PortalData portalData = new PortalData();
		
		if(portal == null) {
			Messenger.sendMessage(sender, "Cannot find portal");
			return true;
		}
		
		Location l = null;
		if(args.length > 1) {
			Portal dest = ph.getPortal(args[1]);
			if(dest != null)
				l = new Location(dest.getMax().getWorld(), dest.getMax().getX()-((dest.getMax().getX()-dest.getMin().getX())/2),
						  dest.getMax().getY()-((dest.getMax().getY()-dest.getMin().getY())/2),
						  dest.getMax().getZ()-((dest.getMax().getZ()-dest.getMin().getZ())/2));
			else {
				Messenger.sendMessage(sender, "Destination portal not found");
				return true;
			}
		} else if(sender instanceof Player)
			l = ((Player)sender).getLocation();
		else {
			Messenger.sendMessage(sender, "You need to be a player or give a portal as destination");
			return true;
		}
		portal.setDestination(l);
		
		if(portal.getDestination() != null) {
			portalData.getConfig().set(portal.getID()+".destination.world", portal.getDestination().getWorld().getName());
			portalData.getConfig().set(portal.getID()+".destination.x", portal.getDestination().getBlockX());
			portalData.getConfig().set(portal.getID()+".destination.y", portal.getDestination().getBlockY());
			portalData.getConfig().set(portal.getID()+".destination.z", portal.getDestination().getBlockZ());
			portalData.saveConfig();
		}
		
		Messenger.sendMessage(sender, "Destination changed");
		return true;
	}	
}
