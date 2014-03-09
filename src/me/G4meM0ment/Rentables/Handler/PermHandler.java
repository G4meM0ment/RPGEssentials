package me.G4meM0ment.Rentables.Handler;

import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.entity.Player;

public class PermHandler {
	private static RPGEssentials plugin;
	
	public PermHandler(RPGEssentials plugin) {
		PermHandler.plugin = plugin;
	}

	public static boolean hasRentablesPerm(Player p) {
		if(plugin.getConfig().getBoolean("UsePermissions")) {
			if((p.hasPermission("rentables.*") || p.hasPermission("rentables.admin") || p.hasPermission("rentables.create") || p.hasPermission("rpge.admin") || p.hasPermission("rpge.*")))
				return true;
			else
				return false;				
		}
		else
			return true;
	}
	
	public static boolean hasRentablesAdminPerm(Player p) {
		if(plugin.getConfig().getBoolean("UsePermissions")) {
			if((p.hasPermission("rentables.admin") || p.hasPermission("rpge.admin") || p.hasPermission("rentables.*") || p.hasPermission("rpge.*")))
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
