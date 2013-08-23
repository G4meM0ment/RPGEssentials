package me.G4meM0ment.ReNature.OtherPlugins;

import org.bukkit.Location;

import me.G4meM0ment.RPGEssentials.RPGEssentials;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.ps.PS;

public class ReFaction {
	
	private RPGEssentials plugin;
	private MCore mcore;
	private Factions factions;
	
	public ReFaction(RPGEssentials plugin) {
		this.plugin = plugin;
		mcore = plugin.getMCore();
		factions = plugin.getFactions();
	}
	
	public Factions getFactions() {
		return factions;
	}
	public MCore getMCore() {
		return mcore;
	}
	
	public boolean isFaction(Location l) {
		if(factions != null) {
			if(BoardColls.get().getFactionAt(PS.valueOf(l)) != null)
				return true;
		}
		return false;
	}
}
