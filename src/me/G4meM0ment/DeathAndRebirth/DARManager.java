package me.G4meM0ment.DeathAndRebirth;

import me.G4meM0ment.DeathAndRebirth.Handler.GhostHandler;
import me.G4meM0ment.DeathAndRebirth.Handler.ShrineHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

public class DARManager {
	
	@SuppressWarnings("unused")
	private RPGEssentials plugin;
	private static ShrineHandler sH;
	private static GhostHandler gH;
	
	public DARManager(RPGEssentials plugin) {
		this.plugin = plugin;
		
		sH = new ShrineHandler();
		gH = new GhostHandler();
	}
	
	public static ShrineHandler getShrineHander() {
		return sH;
	}
	
	public static GhostHandler getGhostHandler() {
		return gH;
	}

}
