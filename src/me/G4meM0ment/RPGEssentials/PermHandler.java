package me.G4meM0ment.RPGEssentials;

import org.bukkit.entity.Player;

public class PermHandler {
	
	private RPGEssentials plugin;
	
	public PermHandler(RPGEssentials plugin) {
		this.plugin = plugin;
	}
	public PermHandler() {
		
	}

	public boolean checkReloadPerms(Player p) {
		if(plugin.getConfig().getBoolean("UsePermissions")) {
			if((p.hasPermission("rpgitem.reload") || p.hasPermission("rpgitem.admin")))
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
