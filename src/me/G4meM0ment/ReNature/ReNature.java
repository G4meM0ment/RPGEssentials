package me.G4meM0ment.ReNature;

import me.G4meM0ment.RPGEssentials.RPGEssentials.RPGEssentials;
import me.G4meM0ment.ReNature.Handler.ReplaceHandler;
import me.G4meM0ment.ReNature.Listener.BListener;

public class ReNature {

	private ReplaceHandler rh;
	private RPGEssentials rpge;
	private BListener blistener;
	
	public ReNature(RPGEssentials rpge) {
		this.rpge = rpge;
		rh = new ReplaceHandler(rpge);
		blistener = new BListener(rpge);
		rpge.getServer().getPluginManager().registerEvents(blistener, rpge);
	}
	public ReNature() {
		rh = new ReplaceHandler();
	}
	
	public boolean onEnable() {
		//TODO load configs
		rh.start();
		return false;
	}
	
	public boolean onDisable() {
		//TODO save configs return true
		
		rh.workList();
		return false;
	}
	
	
	
}
