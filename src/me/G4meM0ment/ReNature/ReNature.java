package me.G4meM0ment.ReNature;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.ReNature.Handler.ReplaceHandler;
import me.G4meM0ment.ReNature.Listener.BListener;

public class ReNature {

	private ReplaceHandler rh;
	private RPGEssentials plugin;
	private BListener blistener;
	
	private static File configFile;
	private static FileConfiguration config = null;
	
	private static String logTit = "ReNature: ";
	private static String dir;
	private static boolean isDisabling;
	
	public ReNature(RPGEssentials plugin) {
		this.plugin = plugin;
		rh = new ReplaceHandler(plugin);
		blistener = new BListener(plugin);
		plugin.getServer().getPluginManager().registerEvents(blistener, plugin);
		
		dir = plugin.getDir()+"/ReNature";
		configFile = new File(dir+"/config.yml");
	}
	public ReNature() {
		rh = new ReplaceHandler();
	}
	
	public boolean onEnable() {
		if(!plugin.getConfig().getBoolean("ReNatureEnabled"))
			return true;
		
		isDisabling = false;
		
		//creating config or loading
		reloadConfig();
		saveConfig();
		
		//starting the recreating process
		rh.start();
		return true;
	}
	
	public boolean onDisable() {
		isDisabling = true;
		saveConfig();
		
		rh.workList();
		return true;
	}
	
	public void reloadConfig() {
	    if (configFile == null) {
	    	configFile = new File(plugin.getDataFolder(),dir+"config.yml");
			plugin.getLogger().info(logTit+"Created config.");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource("defRNConf.yml");
	    if(defConfigStream != null) {
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
	
	public boolean isDisabling() {
		return isDisabling;
	}
	
	public String getLogTit() {
		return logTit;
	}
}
