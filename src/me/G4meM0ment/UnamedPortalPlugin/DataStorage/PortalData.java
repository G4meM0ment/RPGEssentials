package me.G4meM0ment.UnamedPortalPlugin.DataStorage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import me.G4meM0ment.UnamedPortalPlugin.UnnamedPortalPlugin;
import me.G4meM0ment.UnamedPortalPlugin.Handler.PortalHandler;
import me.G4meM0ment.UnamedPortalPlugin.Portal.Portal;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PortalData {
	
	private UnnamedPortalPlugin subplugin;
	private PortalHandler ph;
	
	private static File configFile;
	private static FileConfiguration config = null;
	private static String defConfig = "portalDataFileExample.yml";
	private static String logTit;
	
	private static String dir;

	public PortalData(UnnamedPortalPlugin subplugin) {
		this.subplugin = subplugin;
		ph = new PortalHandler(subplugin);
		
		dir = subplugin.getDir()+"/data";
		logTit = subplugin.getLogTit();
		configFile = new File(dir+"/data.yml");
	}

	public PortalData() {
		subplugin = new UnnamedPortalPlugin();
		ph = new PortalHandler(subplugin);
	}
	
	public String getDir() {
		return dir;
	}
	
	public void initializePortals() {
		Iterator<String> portalData = getConfig().getKeys(false).iterator();
		while(portalData.hasNext()) {
			String path = portalData.next();
			Iterator<String> iterCounter = getConfig().getConfigurationSection(path+".location").getKeys(false).iterator();
			List<Block> blocks = new ArrayList<Block>();
			
			Location dest = new Location(Bukkit.getWorld(getConfig().getString(path+".destination.world")),
					getConfig().getInt(path+".destination.x"), getConfig().getInt(path+".destination.y"), getConfig().getInt(path+".destination.z"));
			
			while(iterCounter.hasNext()) {
				String counted = iterCounter.next();
				Location l = new Location(Bukkit.getWorld(getConfig().getString(path+".location."+counted+".world")),
						getConfig().getInt(path+".location."+counted+".x"), getConfig().getInt(path+".location."+counted+".y"), getConfig().getInt(path+".location."+counted+".z"));
				blocks.add(l.getBlock());
			}
			ph.getPortals().put(path, new Portal(path, blocks, dest));
		}
		subplugin.getLogger().info(logTit+"Portals loaded and initialized");
	}
	
	public void reloadConfig() {
	    if (configFile == null) {
	    	configFile = new File(dir, "/data.yml");
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
	        subplugin.getLogger().info(logTit+"Config saved");
	    } catch (IOException ex) {
	    	subplugin.getLogger().log(Level.SEVERE, logTit+"Could not save config to " + configFile, ex);
	    }
	}
}
