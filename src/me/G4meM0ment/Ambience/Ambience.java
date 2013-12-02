package me.G4meM0ment.Ambience;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.G4meM0ment.Ambience.DataStorage.SoundData;
import me.G4meM0ment.Ambience.Handler.CacheHandler;
import me.G4meM0ment.Ambience.Listener.HeroesListener;
import me.G4meM0ment.Ambience.Listener.PListener;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Ambience {

	private RPGEssentials plugin;
	private HeroesListener hl;
	private PListener pl;
	private SoundData sd;
	private CacheHandler ch;
	
	private static File configFile;
	private static FileConfiguration config = null;
	
	private static String logTit = "Ambience: ";
	private static String dir;
	private static Logger logger;
	private static boolean isEnabled = false;

	public Ambience(RPGEssentials plugin)
	{		
		this.plugin = plugin;
		
		dir = plugin.getDir()+"/Ambience";
		logger = plugin.getLogger();
		
		configFile = new File(dir+"/config.yml");
		
		sd = new SoundData(this);
		ch = new CacheHandler(plugin);
		hl = new HeroesListener(plugin);
		pl = new PListener(plugin);

		plugin.getServer().getPluginManager().registerEvents(hl, plugin);
		plugin.getServer().getPluginManager().registerEvents(pl, plugin);
	}
	public Ambience()
	{
		
	}

	public boolean onEnable() {
		if(!plugin.getConfig().getBoolean("AmbienceEnabled"))
			return true;
		
		//creating config or loading
		reloadConfig();
		saveConfig();
		sd.reloadConfig();
		sd.saveConfig();
		
		ch.cacheFiles();
		
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
	    InputStream defConfigStream = plugin.getResource("defAmbienceConf.yml");
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
