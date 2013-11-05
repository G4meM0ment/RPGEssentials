package me.G4meM0ment.Junkie;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.G4meM0ment.Junkie.DataStorage.DrugData;
import me.G4meM0ment.Junkie.Handler.DrugHandler;
import me.G4meM0ment.Junkie.Listener.PListener;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

public class Junkie {

	private RPGEssentials plugin;
	private PListener plistener;
	private DrugData dd;
	private DrugHandler dh;
	
	private static File configFile;
	private static FileConfiguration config = null;
	
	private static String logTit = "Junkie: ";
	private static String dir;
	private static Logger logger;
	private static boolean isEnabled = false;

	public Junkie(RPGEssentials plugin)
	{
		this.plugin = plugin;
		plistener = new PListener(plugin);
		dh = new DrugHandler(this);
		
		plugin.getServer().getPluginManager().registerEvents(plistener, plugin);
		
		dir = plugin.getDir()+"/Junkie";
		logger = plugin.getLogger();
		
		configFile = new File(dir+"/config.yml");
		
		dd = new DrugData(this);
	}
	public Junkie()
	{
		
	}

	public boolean onEnable() {
		if(!plugin.getConfig().getBoolean("JunkieEnabled"))
			return true;
		
		//creating config or loading
		reloadConfig();
		saveConfig();
		dd.reloadConfig();
		dd.saveConfig();
		
		isEnabled = true;
		return true;
	}

	public boolean onDisable() {	
		isEnabled = false;
		return true;
	}
	
	public void reloadConfig() {
	    if (configFile == null) {
	    	configFile = new File(plugin.getDataFolder()+dir, "/config.yml");
			plugin.getLogger().info(logTit+"Created Config.");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource("defJunkConf.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	        config.options().copyDefaults(true);
	    }
		plugin.getLogger().info(logTit+"Config Loaded.");
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
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public String getDir()
	{
		return dir;
	}
	
	public String getLogTit()
	{
		return logTit;
	}
	
	public Logger getLogger()
	{
		return logger;
	}
}
