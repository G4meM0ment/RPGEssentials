package me.G4meM0ment.InventoryBackup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.G4meM0ment.InventoryBackup.DataStorage.InventoryData;
import me.G4meM0ment.InventoryBackup.Handler.InventoryHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class InventoryBackup {
	
	private RPGEssentials plugin;
	private InventoryHandler invHandler;
	@SuppressWarnings("unused")
	private InventoryData invData;
	
	private static File configFile;
	private static FileConfiguration config = null;
	
	private static String logTit = "InventoryBackup: ";
	private static String dir;
	private static Logger logger;
	private static boolean isDisabling;
	private static boolean isEnabled = false;
	
	public InventoryBackup(RPGEssentials plugin) 
	{
		this.plugin = plugin;
		
		dir = plugin.getDir()+"/InventoryBackup";
		logger = plugin.getLogger();
		configFile = new File(dir+"/config.yml");
		
		invHandler = new InventoryHandler(plugin);
		invData = new InventoryData(this);
	}
	public InventoryBackup() 
	{
	}
	
	public boolean onEnable() 
	{
		if(!plugin.getConfig().getBoolean("InventoryBackupEnabled"))
			return true;
		
		isDisabling = false;
		
		//creating config or loading
		reloadConfig();
		saveConfig();
		
		//starting the recreating process
		invHandler.startBackupScheduler();
		
		isEnabled = true;
		return true;
	}
	
	public boolean onDisable() 
	{
		isDisabling = true;				
		isEnabled = false;
		return true;
	}
	
	public void reloadConfig() 
	{
	    if (configFile == null) 
	    {
	    	configFile = new File(plugin.getDataFolder(),dir+"config.yml");
			plugin.getLogger().info(logTit+"Created config.");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource("defIBConf.yml");
	    if(defConfigStream != null) 
	    {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	        config.options().copyDefaults(true);
	    }
		plugin.getLogger().info(logTit+"Config loaded.");
	}
	public FileConfiguration getConfig() 
	{
	    if (config == null)
	        reloadConfig();
	    return config;
	}
	public void saveConfig()
	{
	    if (config == null || configFile == null)
	    	return;
	    try {
	        config.save(configFile);
	        plugin.getLogger().info(logTit+"Config saved");
	    } catch (IOException ex) {
	        Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, logTit+"Could not save config to " + configFile, ex);
	    }
	}
	
	public boolean isDisabling() 
	{
		return isDisabling;
	}
	
	public String getLogTit() 
	{
		return logTit;
	}
	public String getDir() 
	{
		return dir;
	}	
	public Logger getLogger() 
	{
		return logger;
	}
	public boolean isEnabled() 
	{
		return isEnabled;
	}
}
