package me.G4meM0ment.Chaintrain.DataStorage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import me.G4meM0ment.Chaintrain.Chaintrain;
import me.G4meM0ment.Chaintrain.Handler.PlayerHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerData {
	
	private RPGEssentials plugin;
	private Chaintrain subplugin;
	private PlayerHandler pH;
	
	private static File configFile;
	private static FileConfiguration config = null;
	private static String defConfig = "dropDataFileExample.yml";
	
	private static String dir;

	public PlayerData(RPGEssentials plugin) 
	{
		this.plugin = plugin;
		subplugin = new Chaintrain();
		pH = new PlayerHandler();
		
		dir = subplugin.getDir()+"/data";
		configFile = new File(dir+"/player");
	}

	public PlayerData() 
	{
		plugin = ((RPGEssentials) Bukkit.getPluginManager().getPlugin("RPGEssentials"));
		subplugin = new Chaintrain();
		pH = new PlayerHandler();
	}
	
	public void reloadConfig() {
	    if (configFile == null) {
	    	configFile = new File(dir+"/player");
	    	plugin.getLogger().info("Created player file.");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource(defConfig);
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	        config.options().copyHeader(true);
	        config.options().copyDefaults(true);
	    }
	    plugin.getLogger().info("Player data loaded.");
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
	    
	    try 
	    {
	        config.save(configFile);
	    } catch (IOException ex) 
	    {
	    	plugin.getLogger().log(Level.SEVERE, "Could not save data to " + configFile, ex);
	    }
	}
	
	public void saveDataToFile() {
		//clear old file
		for(String s : getConfig().getRoot().getKeys(false)) {
			pH.setChained(s,  null);
		}
		
		//save cache map to file
		for(String s : pH.getChainedList().keySet()) {
			getConfig().set(s, pH.getChainer(s));
		}
		saveConfig();
	}
	
	public void loadDataFromFile() {
		for(String s : getConfig().getRoot().getKeys(false)) {
			pH.setChained(s, getConfig().getString(s));
		}
	}
	
}
