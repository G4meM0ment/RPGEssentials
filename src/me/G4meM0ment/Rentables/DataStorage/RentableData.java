package me.G4meM0ment.Rentables.DataStorage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import me.G4meM0ment.Rentables.Handler.RentableHandler;
import me.G4meM0ment.Rentables.Rentable.Rentable;
import me.G4meM0ment.Rentables.Rentables;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class RentableData {
	
	private Rentables subplugin;
	private RentableHandler rh;
	
	private static File configFile;
	private static FileConfiguration config = null;
	private static String defConfig = "rentDataFileExample.yml";
	private static String logTit;
	
	private static String dir;

	public RentableData(Rentables subplugin) {
		this.subplugin = subplugin;
		rh = new RentableHandler();
		
		dir = subplugin.getDir()+"/data";
		logTit = subplugin.getLogTit();
		configFile = new File(dir+"/data.yml");
	}

	public RentableData() {
		subplugin = new Rentables();
		rh = new RentableHandler();
	}
	
	public String getDir() {
		return dir;
	}
	
	public void initializeRentables() {
		Iterator<String> rentData = getConfig().getKeys(false).iterator();
		while(rentData.hasNext()) {
			String path = rentData.next();
				
			Location sign = new Location(Bukkit.getWorld(getConfig().getString(path+".sign.world")),
					getConfig().getInt(path+".sign.x"), getConfig().getInt(path+".sign.y"), getConfig().getInt(path+".sign.z"));
			
			Location max = new Location(Bukkit.getWorld(getConfig().getString(path+".location.max.world")),
					getConfig().getInt(path+".location.max.x"), getConfig().getInt(path+".location.max.y"), getConfig().getInt(path+".location.max.z"));
			
			Location min = new Location(Bukkit.getWorld(getConfig().getString(path+".location.min.world")),
					getConfig().getInt(path+".location.min.x"), getConfig().getInt(path+".location.min.y"), getConfig().getInt(path+".location.min.z"));
			
			Rentable r = new Rentable(sign.getBlock(), max, min, path, getConfig().getString(path+".header"), getConfig().getDouble(path+".price"), getConfig().getInt(path+".time"), getConfig().getString(path+".owner"));
			
			r.setRenter(getConfig().getString(path+".renter"));
			r.setPreRenter(getConfig().getString(path+".preRenter"));
			r.setRemaining(getConfig().getInt(path+".remaining"));
			
			rh.getRentables().put(path, r);
		}
		subplugin.getLogger().info(logTit+"Rentables loaded and initialized");
	}
	
	public void reloadConfig() {
	    if (configFile == null) {
	    	configFile = new File(dir, "data.yml");
	    	subplugin.getLogger().info(logTit+"Created Config.");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("RPGEssentials").getResource(defConfig);
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	        config.options().copyDefaults(true);
	    }
	    subplugin.getLogger().info(logTit+"Config loaded.");
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
	    } catch (IOException ex) {
	    	subplugin.getLogger().log(Level.SEVERE, logTit+"Could not save config to " + configFile, ex);
	    }
	}
}
