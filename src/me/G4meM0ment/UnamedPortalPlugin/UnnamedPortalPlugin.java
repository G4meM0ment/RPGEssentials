package me.G4meM0ment.UnamedPortalPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.UnamedPortalPlugin.DataStorage.PortalData;
import me.G4meM0ment.UnamedPortalPlugin.Handler.PortalHandler;
import me.G4meM0ment.UnamedPortalPlugin.Listener.PListener;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class UnnamedPortalPlugin {

	private RPGEssentials plugin;
	private PListener plistener;
	private PortalData portalData;
	private PortalHandler ph;
	
	private static File configFile;
	private static FileConfiguration config = null;
	
	private static String logTit = "UnnamedPortalPlugin: ";
	private static String dir;
	private static Logger logger;
	private static boolean isEnabled = false;

	public UnnamedPortalPlugin(RPGEssentials plugin) 
	{
		this.plugin = plugin;
		plistener = new PListener(plugin);
		
		plugin.getServer().getPluginManager().registerEvents(plistener, plugin);
		
		dir = plugin.getDir()+"/UnnamedPortalPlugin";
		logger = plugin.getLogger();
		configFile = new File(dir+"/config.yml");
		
		portalData = new PortalData(this);
		ph = new PortalHandler(this);
	}
	public UnnamedPortalPlugin() 
	{
		portalData = new PortalData(this);
		ph = new PortalHandler(this);
	}

	public boolean onEnable() {
		//creating config or loading
		reloadConfig();
		saveConfig();
		portalData.reloadConfig();
		portalData.saveConfig();
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				portalData.initializePortals();
				ph.loadChunks();
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
		reloadConfig();
		portalData.reloadConfig();
		portalData.initializePortals();
	}
	public void reloadConfig() {
	    if (configFile == null) {
	    	configFile = new File(dir, "/config.yml");
			plugin.getLogger().info(logTit+"Created Config.");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource("defUPPConf.yml");
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
