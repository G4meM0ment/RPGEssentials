package me.G4meM0ment.Chaintrain.Handler;

import org.bukkit.entity.Player;

public class PermHandler {
	
	public static boolean hasChainPerm(Player player) {
		if(player == null) return false;
		if(player.hasPermission("chaintrain.chain")) return true;
		return false;
	}
	
	public static boolean hasAdminPerm(Player player) {
		if(player == null) return false;
		if(player.hasPermission("chaintrain.admin")) return true;
		return false;
	}

}
