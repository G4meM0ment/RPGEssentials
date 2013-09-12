package me.G4meM0ment.UnamedPortalPlugin.Handler;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.UnamedPortalPlugin.Portal.Portal;

import org.bukkit.entity.Player;

public class PermHandler {
	
	private RPGEssentials plugin;
	
	public PermHandler(RPGEssentials plugin) {
		this.plugin = plugin;
	}

	public boolean hasPortalPerm(Player p, Portal portal) {
		if(plugin.getConfig().getBoolean("UsePermissions")) {
			if((p.hasPermission("upp.portal.*") || p.hasPermission("upp.admin") || p.hasPermission("upp.portal."+portal.getID()) || p.hasPermission("rpge.admin")))
				return true;
			else
				return false;				
		}
		else
			return true;
	}
	
	public boolean hasPortalAdminPerm(Player p) {
		if(plugin.getConfig().getBoolean("UsePermissions")) {
			if((p.hasPermission("upp.admin") || p.hasPermission("rpge.admin") || p.hasPermission("upp.*") || p.hasPermission("rpge.*")))
				return true;
			else
				return false;				
		}
		else
			if(p.isOp())
				return true;
			else
				return false;
	}
}
