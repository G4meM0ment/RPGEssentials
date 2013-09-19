package me.G4meM0ment.Rentables;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.Rentables.DataStorage.LabelConfig;
import me.G4meM0ment.Rentables.DataStorage.RentableData;
import me.G4meM0ment.Rentables.Handler.RentableHandler;
import me.G4meM0ment.Rentables.Listener.BListener;
import me.G4meM0ment.Rentables.Listener.PListener;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Rentables {
	
	private RPGEssentials plugin;
	private BListener bListener;
	private PListener pListener;
	private RentableData rentData;
	private LabelConfig labelConfig;
	private RentableHandler rentHandler;
	
	private static File configFile;
	private static FileConfiguration config = null;
	
	private static String logTit = "Rentables: ";
	private static String dir;
	private static Logger logger;
	private static boolean isEnabled = false;

	public Rentables(RPGEssentials plugin) {
		this.plugin = plugin;
		bListener = new BListener(plugin);
		pListener = new PListener();
		rentHandler = new RentableHandler();

		plugin.getServer().getPluginManager().registerEvents(bListener, plugin);
		plugin.getServer().getPluginManager().registerEvents(pListener, plugin);
		
		dir = plugin.getDir()+"/Rentables";
		logger = plugin.getLogger();
		configFile = new File(dir+"/config.yml");
		
	}
	public Rentables() {
	}

	public boolean onEnable() {
		//creating config or loading
		rentData = new RentableData(this);
		labelConfig = new LabelConfig(this);
		
		reloadConfig();
		saveConfig();
		labelConfig.reloadConfig();
		labelConfig.saveConfig();
		rentData.reloadConfig();
		rentData.saveConfig();
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				rentData.initializeRentables();
				rentHandler.startRentableChecker();
			}
		});
		
		isEnabled = true;
		return true;
	}

	public boolean onDisable() {
		isEnabled = false;
		return true;
	}
	
	public void reloadConfigs() {
		rentData = new RentableData();
		labelConfig = new LabelConfig();
		
		reloadConfig();
		labelConfig.reloadConfig();
		rentData.reloadConfig();
		rentData.initializeRentables();
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
	
	public boolean isEnabled() {
		return isEnabled;
	}
}
