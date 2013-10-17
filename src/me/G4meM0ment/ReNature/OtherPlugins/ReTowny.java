package me.G4meM0ment.ReNature.OtherPlugins;

import org.bukkit.Location;

import me.G4meM0ment.RPGEssentials.RPGEssentials;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.object.TownyUniverse;

public class ReTowny {
	
	@SuppressWarnings("unused")
	private RPGEssentials plugin;
	private Towny towny;
	
	public ReTowny(RPGEssentials plugin) {
		this.plugin = plugin;
		towny = plugin.getTowny();
	}
	
	public Towny getTowny() {
		return towny;
	}
	
	public boolean isTown(Location l) {
		towny.getTownyUniverse();
		if(TownyUniverse.isWilderness(l.getBlock()))
			return false;
		else {
			towny.getTownyUniverse();
			if(TownyUniverse.getTownBlock(l) != null)
				return true;
			else
				return false;
		}
	}
}
