package me.G4meM0ment.RPGEssentials;

import org.bukkit.entity.Player;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

public class PermHandler {
	
	private RPGEssentials plugin;
	
	public PermHandler(RPGEssentials plugin) {
		this.plugin = plugin;
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
