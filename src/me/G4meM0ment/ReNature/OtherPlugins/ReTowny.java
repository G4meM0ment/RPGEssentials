package me.G4meM0ment.ReNature.OtherPlugins;

import org.bukkit.Location;

import me.G4meM0ment.RPGEssentials.RPGEssentials;

import com.palmergames.bukkit.towny.Towny;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class ReTowny {
	
	@SuppressWarnings("unused")
	private RPGEssentials plugin;
	private Towny towny;
	private WorldGuardPlugin wg;
	
	public ReTowny(RPGEssentials plugin) {
		this.plugin = plugin;
		towny = plugin.getTowny();
		wg = plugin.getWorldGuard();
	}
	
	public Towny getTowny() {
		return towny;
	}
	
	public boolean isTown(Location l) {
		
		//TODO remove Workaround
		if(wg.getRegionManager(l.getWorld()).getRegion("wa").contains(l.getBlockX(), l.getBlockY(), l.getBlockZ()))
			return true;
		else
			return false;
		
		/*if(towny == null) return false;
		towny.getTownyUniverse();
		if(TownyUniverse.isWilderness(l.getBlock()))
			return false;			

		else {
			towny.getTownyUniverse();
			if(TownyUniverse.getTownBlock(l) != null)
				return true;
			else
				return false;
		} */
	}
}
