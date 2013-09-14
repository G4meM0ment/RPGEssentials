package me.G4meM0ment.Rentables.DataStorage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.G4meM0ment.Rentables.Handler.RentableHandler;
import me.G4meM0ment.Rentables.Rentable.Rentable;
import me.G4meM0ment.Rentables.Rentables;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RentableData {
	
	private Rentables rent;
	private RentableHandler rh;
	
	private static File configFile;
	private static FileConfiguration config = null;
	private static String defConfig = "rentDataFileExample.yml";
	private static String logTit;
	
	private static String dir;

	public RentableData(Rentables rent) {
		this.rent = rent;
		rh = new RentableHandler();
		
		dir = rent.getDir()+"/data";
		logTit = rent.getLogTit();
		configFile = new File(dir+"/data.yml");
	}

	public RentableData() {
		rent = new Rentables();
		rh = new RentableHandler();
	}
	
	public String getDir() {
		return dir;
	}
	
	public void initializeRentables() {
		Iterator<String> rentData = getConfig().getKeys(false).iterator();
		while(rentData.hasNext()) {
			String path = rentData.next();
			Iterator<String> iterCounter = getConfig().getConfigurationSection(path+".location").getKeys(false).iterator();
			List<Block> blocks = new ArrayList<Block>();
			Player owner = null;
			if(!getConfig().getString(path+".owner").isEmpty())
				owner = Bukkit.getPlayer(getConfig().getString(path+".owner"));
				
			Location sign = new Location(Bukkit.getWorld(getConfig().getString(path+".sign.world")),
					getConfig().getInt(path+".sign.x"), getConfig().getInt(path+".sign.y"), getConfig().getInt(path+".sign.z"));
			
			while(iterCounter.hasNext()) {
				String counted = iterCounter.next();
				Location l = new Location(Bukkit.getWorld(getConfig().getString(path+".location."+counted+".world")),
						getConfig().getInt(path+".location."+counted+".x"), getConfig().getInt(path+".location."+counted+".y"), getConfig().getInt(path+".location."+counted+".z"));
				blocks.add(l.getBlock());
			}
			
			Rentable r = new Rentable(sign.getBlock(), blocks, path, getConfig().getString(path+".header"), getConfig().getDouble(path+".price"), getConfig().getInt(path+".time"), owner);
			if(!getConfig().getString(path+".renter").isEmpty())
				r.setRenter(Bukkit.getPlayer(getConfig().getString(path+".renter")));
			if(!getConfig().getString(path+".preRenter").isEmpty())
				r.setPreRenter(Bukkit.getPlayer(getConfig().getString(path+".preRenter")));
			r.setRemaining(getConfig().getInt(path+".remaining"));
			
			rh.getRentables().put(path, r);
		}
	}
	
	public void reloadConfig() {
	    if (configFile == null) {
	    	configFile = new File(dir, "/data.yml");
			rent.getLogger().info(logTit+"Created Config.");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("RPGEssentials").getResource(defConfig);
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	        config.options().copyDefaults(true);
	    }
		rent.getLogger().info(logTit+"Config loaded.");
	}
	public FileConfiguration getConfig() {
	    if (config == null) {
	        reloadConfig();
	    }
	    return config;
	}
	public void saveConfig() {
	    if (config == null || configFile == null) {
	    	return;
	    }
	    try {
	        config.save(configFile);
	        rent.getLogger().info(logTit+"Config saved");
	    } catch (IOException ex) {
	        Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, logTit+"Could not save config to " + configFile, ex);
	    }
	}
}
