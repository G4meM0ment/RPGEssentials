package me.G4meM0ment.RPGEssentials;

import org.bukkit.entity.Player;

public class PermHandler {
	
	private RPGEssentials plugin;
	
	public PermHandler(RPGEssentials plugin) {
		this.plugin = plugin;
	}

	public boolean hasRPGItemReloadPerms(Player p) {
		if(plugin.getConfig().getBoolean("UsePermissions")) {
			if((p.hasPermission("rpgitem.reload") || p.hasPermission("rpgitem.admin") || p.hasPermission("rpge.admin") || p.hasPermission("rpge.reload")))
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
	
	public boolean hasRPGItemPerms(Player p) {
		if(plugin.getConfig().getBoolean("UsePermissions")) {
			if((p.hasPermission("rpgitem.give") || p.hasPermission("rpgitem.admin") || p.hasPermission("rpge.admin")))
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

	public boolean hasReloadPerms(Player p) {
		if(plugin.getConfig().getBoolean("UsePermissions")) {
			if((p.hasPermission("rpge.admin") || p.hasPermission("rpge.reload")))
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
