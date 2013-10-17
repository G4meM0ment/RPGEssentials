package me.G4meM0ment.UnamedPortalPlugin.Handler;

import java.util.ArrayList;
import java.util.List;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.WorldEdit.WorldEditHandler;
import me.G4meM0ment.UnamedPortalPlugin.DataStorage.PortalData;
import me.G4meM0ment.UnamedPortalPlugin.Portal.Portal;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

public class CommandHandler {
	
	@SuppressWarnings("unused")
	private RPGEssentials plugin;
	private PortalHandler ph;
	private WorldEditHandler weHandler;
	private PermHandler permHandler;
	private PortalData portalData;
	
	public CommandHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		ph = new PortalHandler(plugin.getUnnamedPortalPlugin());
		weHandler = new WorldEditHandler();
		permHandler = new PermHandler(plugin);
		portalData = new PortalData();
	}
	
	public boolean exec(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		
		if(args.length > 0 && args[0].equals("help")) {
			if(sender instanceof Player) {
				//TODO add messenger
			} else {
				//TODO add messenger
			}
		}
		
		if(args.length > 0 && args[0].equals("create") && !args[1].isEmpty() && sender instanceof Player) {
			if(!permHandler.hasPortalAdminPerm(player)) {
				player.sendMessage("You don't have permission");
				//TODO add messenger
				return true;
			}
			RPGEssentials plugin = (RPGEssentials) Bukkit.getPluginManager().getPlugin("RPGEssentials");
			String portal = args[1];
			Portal destination = null;
			Location l = null;
			if(args.length > 2) {
				destination = ph.getPortal(args[2]);
				l = destination.getBlocks().get(destination.getBlocks().size()/2).getLocation();
			}
			else {}
				//TODO add messenger
			
			Selection sel = plugin.getWorldEdit().getSelection(player);

			if(sel == null) {
				List<Block> blocks = new ArrayList<Block>();
				blocks.add(player.getLocation().getBlock());
				ph.createPortal(new Portal(portal, blocks, l));
				player.sendMessage("Portal created");
			} else {
				ph.createPortal(new Portal(portal, weHandler.getSelectedBlocks(sel), l));
				player.sendMessage("Portal created");
			}
			return true;
		}
			
		if(args.length > 0 && args[0].equals("setdestination") && !args[1].isEmpty() && sender instanceof Player) {
			if(!permHandler.hasPortalAdminPerm(player)) {
				player.sendMessage("You don't have permission");
				//TODO add messenger
				return true;
			}
			Portal portal = ph.getPortal(args[1]);
			if(portal == null) {
				player.sendMessage("Cannot find portal");
				//TODO add messenger
				return true;
			}
			Location l = null;
			if(args.length > 2) {
				Portal dest = ph.getPortal(args[2]);
				if(dest != null)
					l = dest.getBlocks().get(dest.getBlocks().size()/2).getLocation();
				else
					l = player.getLocation();
			} else
				l = player.getLocation();
			portal.setDestination(l);
			
			if(portal.getDestination() != null) {
				portalData.getConfig().set(portal.getID()+".destination.world", portal.getDestination().getWorld().getName());
				portalData.getConfig().set(portal.getID()+".destination.x", portal.getDestination().getBlockX());
				portalData.getConfig().set(portal.getID()+".destination.y", portal.getDestination().getBlockY());
				portalData.getConfig().set(portal.getID()+".destination.z", portal.getDestination().getBlockZ());
				portalData.saveConfig();
			}
			
			player.sendMessage("Destination changed");
			return true;
		}
		if(args.length > 0 && args[0].equals("delete") && !args[1].isEmpty()) {
			if(sender instanceof Player && !permHandler.hasPortalAdminPerm(player)) {
				//TODO add messenger
				player.sendMessage("You don't have permission");
				return true;
			}
			Portal portal = ph.getPortal(args[1]);
			ph.removePortal(portal);
			player.sendMessage("Portal removed");
			return true;
		}
		
		if(args.length > 0 && args[0].equals("list")) {
			if(sender instanceof Player && !permHandler.hasPortalAdminPerm(player)) {
				//TODO add messenger
				player.sendMessage("You don't have permission");
				return true;
			}
			player.sendMessage("Portals: "+ph.getPortals().keySet());
			return true;
		}
		return false;
	}
}
