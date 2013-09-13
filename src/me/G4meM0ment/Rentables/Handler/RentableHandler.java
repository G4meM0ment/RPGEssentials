package me.G4meM0ment.Rentables.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.G4meM0ment.RPGEssentials.WorldEdit.WorldEditHandler;
import me.G4meM0ment.Rentables.DataStorage.RentableData;
import me.G4meM0ment.Rentables.Rentable.Rentable;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import com.sk89q.worldedit.bukkit.selections.Selection;

public class RentableHandler {
	
	private WorldEditHandler weHandler;
	private RentableData rentData;
	
	private static HashMap<String, Rentable> rentables = new HashMap<String, Rentable>();
	
	public RentableHandler() {
		weHandler = new WorldEditHandler();
		rentData = new RentableData();
	}
	
	public boolean isRentable(Block b) {
		if(b == null) return false;
		
		for(Rentable r : getRentables().values()) {
			for(Block block : r.getBlocks()) {
				if(block.getLocation().distance(b.getLocation()) == 0) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean isRentableSign(Block b) {
		if(b == null) return false;
		
		for(Rentable r : getRentables().values()) {
			if(r.getSign().getLocation().distance(b.getLocation()) == 0) {
					return true;
			}
		}
		return false;
	}
	
	public Rentable getRentableBySign(Block b) {
		for(Rentable r : getRentables().values()) {
			if(r.getSign().getLocation().distance(b.getLocation()) == 0)
					return r;
		}
		return null;
	}
	public Rentable getRentableByBlock(Block b) {
		for(Rentable r : getRentables().values()) {
			for(Block block : r.getBlocks()) {
				if(block.getLocation().distance(b.getLocation()) == 0)
					return r;
			}
		}
		return null;
	}
	public Rentable getRentableById(String id) {
		for(Rentable r : getRentables().values()) {
			if(r.getID().equalsIgnoreCase(id))
				return r;
		}
		return null;
	}
	
	public HashMap<String, Rentable> getRentables() {
		return rentables;
	}
	
	public List<String> proceedRentable(Sign sign, String header, String price, String time, Selection sel) {
		if(sign == null || header == null || price == null || time == null || sel == null) return null;
		
		String timeString = null;
		List<String> list = new ArrayList<String>();
		
		if(time.contains("m")) {
			timeString = time.split("m")[0]+" min";
		} else if(time.contains("h")) {
			timeString = time.split("h")[0]+" hours";
		} else if(time.contains("d")) {
			timeString = time.split("d")[0]+" days";
		} else {
			//TODO add messenger
			return null;
		}
		
		list.add("["+header+"]");
		list.add( price/*+" "+plugin.getEconomy().currencyNamePlural()*/);
		list.add("for"); //TODO get string from config
		list.add(timeString);
		
		setupRentable(sign.getBlock(), header, price, time, sel);
		
		return list;
	}
	
	private void setupRentable(Block sign, String header, String priceString, String timeString, Selection sel) {
		if(sign == null || header == null || priceString == null || timeString == null || sel == null) return;
		
		List<Block> blocks = weHandler.getSelectedBlocks(sel);
		int price = Integer.parseInt(priceString);
		int time = 0;
		int id = getFreeId();
		if(timeString.contains("m")) {
			time = Integer.parseInt(timeString.split("m")[0]);
		} else if(timeString.contains("h")) {
			time = Integer.parseInt(timeString.split("h")[0])*60;
		} else if(timeString.contains("d")) {
			time = Integer.parseInt(timeString.split("d")[0])*1440;
		}
			
		rentData.getConfig().set(id+".header", header);
		rentData.getConfig().set(id+".sign.world", sign.getWorld().getName());
		rentData.getConfig().set(id+".sign.x", sign.getLocation().getBlockX());
		rentData.getConfig().set(id+".sign.y", sign.getLocation().getBlockY());
		rentData.getConfig().set(id+".sign.z", sign.getLocation().getBlockZ());
		rentData.getConfig().set(id+".price", price);
		rentData.getConfig().set(id+".time", time);
		rentData.saveConfig();
		int counter = 1;
		for(Block b : blocks) {
			rentData.getConfig().set(id+".loaction."+counter+".world", b.getWorld().getName());
			rentData.getConfig().set(id+".location."+counter+".x", b.getLocation().getBlockX());
			rentData.getConfig().set(id+".location."+counter+".y", b.getLocation().getBlockY());
			rentData.getConfig().set(id+".location."+counter+".z", b.getLocation().getBlockZ());
			rentData.saveConfig();
			counter++;
		}
	}
	
	public int getFreeId() {
		int counter = 1;
		String id = rentData.getConfig().getString(counter+".header");
		while(id != null) {
			counter++;
			id = rentData.getConfig().getString(counter+".header");
		}
		return counter;
	}

}
