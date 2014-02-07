package me.G4meM0ment.DeathAndRebirth.Handler;

import me.G4meM0ment.DeathAndRebirth.Types.Shrine;

import org.bukkit.entity.Player;

public class PermHandler {
	
	/**
	 * Checks for any matching permission
	 * @param p
	 * @return
	 */
	public boolean hasNoDropPerm(Player p)
	{
		if(p == null) return false;
		if(p.hasPermission("dar.nodrop")) return true;
		return false;
	}
	
	/**
	 * Checks for any matching permission
	 * @param p
	 * @return
	 */
	public boolean hasAdminPerm(Player p)
	{
		if(p == null) return false;
		if(p.hasPermission("dar.admin")) return true;
		return false;
	}

	/**
	 * Checks for any matching permission
	 * @param p
	 * @return
	 */
	public boolean hasReloadPerm(Player p)
	{
		if(p == null) return false;
		if(p.hasPermission("dar.reload")) return true;
		if(p.hasPermission("dar.admin")) return true;
		return false;
	}
	
	/**
	 * Checks for any matching permission
	 * @param p
	 * @return
	 */
	public boolean hasShrinePerm(Player p, Shrine s)
	{
		if(p == null) return false;
		if(p.hasPermission("dar.shrine."+s.getName())) return true;
		if(p.hasPermission("dar.shrine.*")) return true;
		return false;
	}
	
	/**
	 * Checks for any matching permission
	 * @param p
	 * @return
	 */
	public boolean hasIgnorePerm(Player p)
	{
		if(p == null) return false;
		if(p.hasPermission("dar.ignore")) return true;
		return false;
	}
}
