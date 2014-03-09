package me.G4meM0ment.Rentables.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;
import me.G4meM0ment.Rentables.Rentables;
import me.G4meM0ment.Rentables.DataStorage.RentableData;
import me.G4meM0ment.Rentables.Rentable.Rentable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection.Type;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class RentableHandler {
	
	private RentableData rentData;
	
	private static HashMap<String, Rentable> rentables = new HashMap<String, Rentable>();
	private static List<Player> adminModeEnabled = new ArrayList<Player>();
	
	/**
	 * 
	 * @param b
	 * @return
	 */
	public boolean isRentable(Location loc) {
		if(loc == null) return false;
		
		if(getRentableByLocation(loc) != null)
			return true;
		return false;
	}
	/**
	 * 
	 * @param loc
	 * @return
	 */
	public boolean isRentableSign(Location loc) {
		if(loc == null) return false;
		
		for(Rentable r : getRentables().values())
			if(r.getSign().getLocation().distance(loc) == 0)
				return true;
		return false;
	}
	
	/**
	 * 
	 * @param b
	 * @return
	 */
	public Rentable getRentableBySign(Block b) {
		for(Rentable r : getRentables().values()) {
			if(r.getSign().getLocation().getWorld() != b.getLocation().getWorld()) continue;
			if(r.getSign().getLocation().distance(b.getLocation()) == 0)
					return r;
		}
		return null;
	}
	
	/**
	 * 
	 * @param loc
	 * @return
	 */
	public Rentable getRentableByLocation(Location loc) {
		for(Rentable r : getRentables().values()) {
			int locX = loc.getBlockX(),
				locY = loc.getBlockY(),
				locZ = loc.getBlockZ(),
				maxX = r.getMax().getBlockX(),
				maxY = r.getMax().getBlockY(),
				maxZ = r.getMax().getBlockZ(),
				minX = r.getMin().getBlockX(),
				minY = r.getMin().getBlockY(),
				minZ = r.getMin().getBlockZ();
				    										
			if(     locX <= maxX
				&&	locX >= minX
				&&	locY <= maxY
				&&	locY >= minY
				&&	locZ <= maxZ
				&&	locZ >= minZ)
				return r;
		}
		return null;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
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
	public List<Player> getPlayersAdminModeEnabled() {
		return adminModeEnabled;
	}
	
	/**
	 * 
	 * @param sign
	 * @param header
	 * @param price
	 * @param time
	 * @param sel
	 * @param owner
	 * @return
	 */
	public List<String> proceedRentable(Sign sign, String header, String price, String time, Selection sel, Player owner) {
		if(sign == null || header == null || price == null || time == null || sel == null) return null;
		
		RPGEssentials plugin = (RPGEssentials) Bukkit.getPluginManager().getPlugin("RPGEssentials");
		String timeString = null;
		List<String> list = new ArrayList<String>();
		
		String currency = "";
		if(plugin.getEconomy() != null)
			currency = plugin.getEconomy().currencyNamePlural();
		
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
		list.add(price+" "+currency);
		list.add("for"); //TODO get string from config
		list.add(timeString);
		
		setupRentable(sign.getBlock(), header, price, time, timeString, sel, owner);
		return list;
	}
	
	/**
	 * 
	 * @param sign
	 * @param header
	 * @param priceString
	 * @param timeString
	 * @param displayTimeString
	 * @param sel
	 * @param owner
	 */
	private void setupRentable(Block sign, String header, String priceString, String timeString, String displayTimeString, Selection sel, Player owner) {
		if(sign == null || header == null || priceString == null || timeString == null || sel == null) return;
		
		rentData = new RentableData();
		
		Location max = sel.getMaximumPoint();
		Location min = sel.getMinimumPoint();
		double price = Double.parseDouble(priceString);
		int time = 0;
		int id = getFreeId();
		String formattedHeader = "["+header+"]";
		String ownerName = "";
		if(getPlayersAdminModeEnabled().contains(owner) || owner == null)
			ownerName = "";
		else
			ownerName = owner.getName();
		
		if(timeString.contains("m"))
			time = Integer.parseInt(timeString.replace("m", ""));
		else if(timeString.contains("h"))
			time = Integer.parseInt(timeString.replace("h", ""))*60;
		else if(timeString.contains("d"))
			time = Integer.parseInt(timeString.replace("d", ""))*1440;
		
		rentData.getConfig().set(id+".header", formattedHeader);
		rentData.getConfig().set(id+".sign.world", sign.getWorld().getName());
		rentData.getConfig().set(id+".sign.x", sign.getLocation().getBlockX());
		rentData.getConfig().set(id+".sign.y", sign.getLocation().getBlockY());
		rentData.getConfig().set(id+".sign.z", sign.getLocation().getBlockZ());
		rentData.getConfig().set(id+".price", price);
		rentData.getConfig().set(id+".time", time);
		rentData.getConfig().set(id+".displayTime", displayTimeString);
		rentData.getConfig().set(id+".renter", "");
		rentData.getConfig().set(id+".preRenter", "");
		rentData.getConfig().set(id+".owner", ownerName);
		rentData.getConfig().set(id+".remaining", 0);
				
		//max
		rentData.getConfig().set(id+".location.max.world", max.getWorld().getName());
		rentData.getConfig().set(id+".location.max.x", max.getBlockX());
		rentData.getConfig().set(id+".location.max.y", max.getBlockY());
		rentData.getConfig().set(id+".location.max.z", max.getBlockZ());
		
		//min
		rentData.getConfig().set(id+".location.min.world", min.getWorld().getName());
		rentData.getConfig().set(id+".location.min.x", min.getBlockX());
		rentData.getConfig().set(id+".location.min.y", min.getBlockY());
		rentData.getConfig().set(id+".location.min.z", min.getBlockZ());
		
		rentData.saveConfig();
		
		Rentable rentable = new Rentable(sign, max, min, Integer.toString(id), formattedHeader, price, time, ownerName);
		getRentables().put(Integer.toString(id), rentable);
		publishRentable(rentable, "");
		
	}
	
	/**
	 * 
	 * @return
	 */
	public int getFreeId() {
		rentData = new RentableData();
		int counter = 1;
		String id = rentData.getConfig().getString(counter+".header");
		while(id != null) {
			counter++;
			id = rentData.getConfig().getString(counter+".header");
		}
		return counter;
	}
	
	/**
	 * 
	 * @param rent
	 * @param p
	 */
	public void rentRentable(Rentable rent, Player p) {
		if(!rent.getRenter().equalsIgnoreCase(p.getName()) && !rent.getRenter().isEmpty()) {
			Messenger.sendMessage(p, "Already rented");
			return;
		}
		
		Rentables rentables = new Rentables();
		RPGEssentials plugin = (RPGEssentials) Bukkit.getPluginManager().getPlugin("RPGEssentials");
		rentData = new RentableData();
		
		if(rent.getRenter().isEmpty())
			privaticeRentable(rent, p);
		if(plugin.getEconomy() != null) {
			if(plugin.getEconomy().getBalance(p.getName()) >= rent.getPrice()) {
				plugin.getEconomy().withdrawPlayer(p.getName(), rent.getPrice());
				if(!rent.getOwner().isEmpty())
					plugin.getEconomy().depositPlayer(rent.getOwner(), rent.getPrice());
			} else {
				Messenger.sendMessage(p, "Not enough money");
				return;
			}
		}
		
		rent.setRenter(p.getName());
		rent.setRemaining(rent.getRemaining()+rent.getTime());
		p.sendMessage("Rented rentable");
		
		Sign sign = (Sign) rent.getSign().getState();
		sign.setLine(0, rent.getHeader());
		sign.setLine(1, rent.getRenter());
		sign.setLine(2, rentables.getConfig().getString("RentedFormat"));
		sign.setLine(3, rent.getRemaining()+" mins");
		sign.update();
		//TODO add messenger
		
		rentData.getConfig().set(rent.getID()+".renter", p.getName());
		rentData.getConfig().set(rent.getID()+".remaining", rent.getRemaining());
		rentData.saveConfig();
	}
	
	/**
	 * 
	 * @param rent
	 */
	public void unrentRentable(Rentable rent) {
		Rentables rentables = new Rentables();
		rentData = new RentableData();
		RPGEssentials plugin = (RPGEssentials) Bukkit.getPluginManager().getPlugin("RPGEssentials");
		
		String currency = "";
		if(plugin.getEconomy() != null) 
			currency = plugin.getEconomy().currencyNamePlural();
		
		publishRentable(rent, rent.getRenter());
		
		rent.setPreRenter(rent.getRenter());
		rent.setRenter("");
		rent.setRemaining(0);
		if(Bukkit.getPlayerExact(rent.getPreRenter()) != null)
			Bukkit.getPlayerExact(rent.getPreRenter()).sendMessage("Rentable expired");

		Sign sign = (Sign) rent.getSign().getState();
		sign.setLine(0, rent.getHeader());
		sign.setLine(1, Double.toString(rent.getPrice())+" "+currency);
		sign.setLine(2, rentables.getConfig().getString("ForFormat"));
		sign.setLine(3, rentData.getConfig().getString(rent.getID()+".displayTime"));
		sign.update();
		//TODO add messenger
		
		rentData.getConfig().set(rent.getID()+".renter", "");
		rentData.getConfig().set(rent.getID()+".preRenter", rent.getPreRenter());
		rentData.getConfig().set(rent.getID()+".remaining", 0);
		rentData.saveConfig();
	}
	
	/**
	 * 
	 */
	public void startRentableChecker() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable() {
			@Override
			public void run() {
				checkRentables();
			}
		}, 0, 1200);
	}
	/**
	 * 
	 */
	public void checkRentables() {
		rentData = new RentableData();
		for(Rentable r : getRentables().values()) {
			if(r.getRenter().isEmpty()) continue;
			if(r.getRemaining() < 1) {
				unrentRentable(r);
			}
			else {
				r.setRemaining(r.getRemaining()-1);
				rentData.getConfig().set(r.getID()+".remaining", r.getRemaining()-1);
				rentData.saveConfig();
				if(r.getSign().getType() == Material.WALL_SIGN || r.getSign().getType() == Material.SIGN_POST) {
					try{
						Sign sign = (Sign) r.getSign().getState();
						sign.setLine(3, r.getRemaining()+" mins");
						sign.update();
					}catch(ClassCastException e) {
						((RPGEssentials)Bukkit.getPluginManager().getPlugin("RPGEssentials")).getLogger().info("Rentables: Could not cast to sign ("+r.getID()+")");
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param r
	 * @param p
	 */
	private void publishRentable(Rentable r, String p) {
		RPGEssentials plugin = (RPGEssentials) Bukkit.getPluginManager().getPlugin("RPGEssentials");
		LWC lwc = plugin.getLWC();
		
		//iterate threw y
		for(int y = r.getMin().getBlockY(); y <= r.getMax().getBlockY(); y++) {
			//iterate threw z
			for(int z = r.getMin().getBlockZ(); z <= r.getMax().getBlockZ(); z++) {
				//iterate threw x
				for(int x = r.getMin().getBlockX(); x <= r.getMax().getBlockX(); x++) {
					//get the block and handle with lwc
					Block b = new Location(r.getMax().getWorld(), x, y, z).getBlock();
					if(lwc.isProtectable(b)) {
						if(lwc.findProtection(b) != null)
							lwc.findProtection(b).remove();
						lwc.getPhysicalDatabase().registerProtection(b.getTypeId(), Type.PUBLIC, b.getWorld().getName(), "G4meM0ment" /*TODO get from config a server account*/, "" /*PW*/, b.getX(), b.getY(), b.getZ());
					}
				}
			}
		}
	}
	/**
	 * 
	 * @param r
	 * @param p
	 */
	private void privaticeRentable(Rentable r, Player p) {
		RPGEssentials plugin = (RPGEssentials) Bukkit.getPluginManager().getPlugin("RPGEssentials");
		LWC lwc = plugin.getLWC();
		
		//iterate threw y
		for(int y = r.getMin().getBlockY(); y <= r.getMax().getBlockY(); y++) {
			//iterate threw z
			for(int z = r.getMin().getBlockZ(); z <= r.getMax().getBlockZ(); z++) {
				//iterate threw x
				for(int x = r.getMin().getBlockX(); x <= r.getMax().getBlockX(); x++) {
					//get the block and handle with lwc
					Block b = new Location(r.getMax().getWorld(), x, y, z).getBlock();
					if(lwc.isProtectable(b)) {
						if(lwc.findProtection(b) != null)
							lwc.findProtection(b).remove();
						lwc.getPhysicalDatabase().registerProtection(b.getTypeId(), Type.PRIVATE, b.getWorld().getName(), p.getName(), "" /*PW*/, b.getX(), b.getY(), b.getZ());
					}
				}
			}
		}
	}

	public void removeRentable(Rentable r) {
		if(r == null) return;
		rentData = new RentableData();
		rentData.getConfig().set(r.getID(), null);
	}
}
