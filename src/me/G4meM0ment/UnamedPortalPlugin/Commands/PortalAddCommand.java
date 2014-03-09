package me.G4meM0ment.UnamedPortalPlugin.Commands;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;
import me.G4meM0ment.UnamedPortalPlugin.Handler.PortalHandler;
import me.G4meM0ment.UnamedPortalPlugin.Portal.Portal;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

@CommandInfo(
		name = "add",
		pattern = "add",
		usage = "/upp add <name> (<destination>)",
		desc = "Adds a new portal, for the given worldedit selection",
		permission = "upp.admin"
	)
public class PortalAddCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		if(!(sender instanceof Player)) {
			Messenger.sendMessage(sender, "You need to be a player");
			return true;
		}
		if(args.length <= 0) return false;
		
		Player player = (Player) sender;
		PortalHandler ph = new PortalHandler(plugin.getUnnamedPortalPlugin());
		String portal = args[0];
		Portal dest = null;
		Location l = null;
		if(args.length > 1) {
			dest = ph.getPortal(args[1]);
			l = new Location(dest.getMax().getWorld(), dest.getMax().getX()-((dest.getMax().getX()-dest.getMin().getX())/2),
															  dest.getMax().getY()-((dest.getMax().getY()-dest.getMin().getY())/2),
															  dest.getMax().getZ()-((dest.getMax().getZ()-dest.getMin().getZ())/2));
		}
		
		Selection sel = plugin.getWorldEdit().getSelection(player);
		if(sel == null)
			ph.createPortal(new Portal(portal, player.getLocation(), player.getLocation(), l));
		else
			ph.createPortal(new Portal(portal, sel.getMaximumPoint(), sel.getMinimumPoint(), l));
		Messenger.sendMessage(player, "Portal created");
		return true;
	}	
}
