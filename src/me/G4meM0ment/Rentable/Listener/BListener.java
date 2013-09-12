package me.G4meM0ment.Rentable.Listener;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.sk89q.worldedit.bukkit.selections.Selection;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.Rentable.Handler.RentableHandler;

public class BListener implements Listener{
	
	private RPGEssentials plugin;
	private RentableHandler rh;
	
	
	public BListener(RPGEssentials plugin) {
		this.plugin = plugin;
		rh = new RentableHandler(plugin);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onSignChange(SignChangeEvent event) {
		if(event.getBlock() == null) return;
		
		System.out.println("Debug: 1");
		
		Player p = event.getPlayer();
		Selection sel = plugin.getWorldEdit().getSelection(p);
		if(p == null /*|| sel == null*/) return;
		//TODO add messenger
		
		String header = null, price = null, time = null;
		if(event.getLine(0).equalsIgnoreCase("[Rent]")) {
			for(String s : event.getLines()) {
				if(s.equalsIgnoreCase("[Rent]")) continue;
				if(!s.contains("p:") && !s.contains("t:")) header = s;
				if(s.contains("p:")) {
					price = s.split(":")[1];
				}
				if(s.contains("t:")) {
					time = s.split(":")[1];
				}
			}
			rh.proceedRentable((Sign) event.getBlock().getState(), header, price, time, sel);
		}
	}

}
