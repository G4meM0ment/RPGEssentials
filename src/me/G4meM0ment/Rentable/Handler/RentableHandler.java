package me.G4meM0ment.Rentable.Handler;

import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.block.Sign;

import com.sk89q.worldedit.bukkit.selections.Selection;

public class RentableHandler {
	
	RPGEssentials plugin;
	
	public RentableHandler(RPGEssentials plugin) {
		this.plugin = plugin;
	}
	
	public void proceedRentable(Sign sign, String header, String price, String time, Selection sel) {
		if(sign == null || header == null || price == null || time == null /*|| sel == null*/) return;
		
		String timeString = null;
		if(time.contains("m")) {
			timeString = time.split("m")[0]+" min";
		} else if(time.contains("h")) {
			timeString = time.split("h")[0]+" hours";
		} else if(time.contains("d")) {
			timeString = time.split("d")[0]+" days";
		} else {
			//TODO add messenger
			return;
		}
		
		sign.setLine(0, "["+header+"]");
		sign.setLine(1, price/*+" "+plugin.getEconomy().currencyNamePlural()*/);
		sign.setLine(2, "for"); //TODO get string from config
		sign.setLine(3, timeString);
		sign.update(true);
	}

}
