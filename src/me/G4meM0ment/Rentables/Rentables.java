package me.G4meM0ment.Rentables;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.Rentables.Listener.BListener;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Rentables {
	
	private RPGEssentials plugin;
	private BListener bListener;
	
	private static File configFile;
	private static FileConfiguration config = null;
	
	private static String logTit = "Rentable: ";
	private static String dir;
	private static Logger logger;

	public Rentables(RPGEssentials plugin) {
		this.plugin = plugin;
		bListener = new BListener(plugin);
		
		plugin.getServer().getPluginManager().registerEvents(bListener, plugin);
		
		dir = plugin.getDir()+"/Rentable";
		logger = plugin.getLogger();
		configFile = new File(dir+"/config.yml");
		
	}
	public Rentables() {
	}

	public boolean onEnable() {
		//creating config or loading
		reloadConfig();
		saveConfig();
		return true;
	}

	public boolean onDisable() {
		return true;
	}
	
	public void reloadConfig() {
	    if (configFile == null) {
	    	configFile = new File(dir, "/config.yml");
			plugin.getLogger().info(logTit+"Created Config.");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource("defRentConf.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	        config.options().copyDefaults(true);
	    }
		plugin.getLogger().info(logTit+"Config loaded.");
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
	        plugin.getLogger().info(logTit+"Config saved");
	    } catch (IOException ex) {
	        Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, logTit+"Could not save config to " + configFile, ex);
	    }
	}
	
	public String getLogTit() {
		return logTit;
	}
	public String getDir() {
		return dir;
	}
	public Logger getLogger() {
		return logger;
	}
}
