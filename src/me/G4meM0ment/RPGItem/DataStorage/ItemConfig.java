package me.G4meM0ment.RPGItem.DataStorage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.DataStorage.FileHandler;
import me.G4meM0ment.RPGItem.RPGItem;
import me.G4meM0ment.RPGItem.Handler.ListHandler;

public class ItemConfig {
	
	private RPGEssentials plugin;
	private RPGItem subplugin;
	private FileHandler fileHandler;
	private ListHandler lh;
	
	private static String defConfig = "itemConfExample.yml";
	private static String logTit;
	
	private static String dir;
	private static File folder;
	private static HashMap<String, File> itemConfigs = new HashMap<String, File>();
	
	public ItemConfig(RPGEssentials plugin) {
		this.plugin = plugin;
		subplugin = new RPGItem();
		fileHandler = new FileHandler();
		lh = new ListHandler();
		
		dir = plugin.getDir()+"/RPGItem/items";
		folder = new File(dir);
		logTit = subplugin.getLogTit();
	}
	public ItemConfig() {
		plugin = (RPGEssentials) Bukkit.getPluginManager().getPlugin("RPGEssentials");
		subplugin = new RPGItem();
		fileHandler = new FileHandler();
		lh = new ListHandler();
	}
	
	public void initializeItemConfigs() {
		List<File> files = fileHandler.getFiles(folder);
		for(File file : files) {
			getItemConfigs().put(file.getName().replace(".yml", ""), file);
		}
	}
	
	public void addConfigFile(File file) {
		getItemConfigs().put(file.getName().replace(".yml", ""), file);
	}
	
	public String getDir() {
		return dir;
	}
	
	public static HashMap<String, File> getItemConfigs() {
		return itemConfigs;
	}
	
	public File getFile(String name) {
		File file = getItemConfigs().get(name);
//		if(file == null) {
//			addConfigFile(new File(getDir()+"/"+name+".yml"));
//			return getItemConfigs().get(name);
//		} else
		return file;
	}
	
	public void reloadConfig(File configFile) {
		if (configFile == null) {
	    	configFile = new File(dir+"/RPGItem.yml");
			plugin.getLogger().info(logTit+"Created config.");
	    }
		FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource(defConfig);
	    if(defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	        config.options().copyDefaults(true);
	        try {
				config.save(configFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		plugin.getLogger().info(logTit+configFile.getName()+" config loaded.");
	}
	public FileConfiguration getConfig(File configFile) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
	    if (config == null) {
	        reloadConfig(configFile);
	        config = YamlConfiguration.loadConfiguration(configFile);
	    }
	    return config;
	}
	public void saveConfig(File configFile) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
	    if (config == null || configFile == null) {
	    	return;
	    }
	    try {
	        config.save(configFile);
	        plugin.getLogger().info(logTit+configFile.getName()+" config saved");
	    } catch (IOException ex) {
	        plugin.getLogger().log(Level.SEVERE, logTit+"Could not save config to " + configFile, ex);
	    }
	}
	
	public void reloadConfigs() {
		for(File file : itemConfigs.values()) {
			reloadConfig(file);
		}
		lh.updateItems();
	}

}
