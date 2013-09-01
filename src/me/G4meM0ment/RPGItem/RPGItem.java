package me.G4meM0ment.RPGItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.DataStorage.ItemConfig;
import me.G4meM0ment.RPGItem.DataStorage.ItemData;
import me.G4meM0ment.RPGItem.Listener.PListener;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class RPGItem {
	
	private RPGEssentials plugin;
	private ItemConfig itemConfig;
	private ItemData itemData;
	private PListener plistener;
	
	private static File configFile;
	private static FileConfiguration config = null;
	
	private static String logTit = "RPGItem: ";
	private static String dir;
	private static boolean isDisabling;
	private static String defConfig = "defRPGItemConf.yml"; 
	
	public RPGItem(RPGEssentials plugin) {
		this.plugin = plugin;
		itemConfig = new ItemConfig(plugin);
		itemData = new ItemData(plugin);
		plistener = new PListener(plugin);
		dir = plugin.getDir()+"/RPGItem";
		plugin.getServer().getPluginManager().registerEvents(plistener, plugin);
	}
	public RPGItem() {
	}
	
	public boolean onEnable() {
		//TODO load configs
		isDisabling = false;
		File exItem = new File(dir+"/items/RPGItem.yml");
		File exData = new File(dir+"/data/RPGItem.yml");
		reloadConfig();
		saveConfig();
		itemConfig.reloadConfig(exItem);
		itemConfig.saveConfig(exItem);
		itemData.reloadDataFile(exData);
		itemData.saveDataFile(exData);
		itemConfig.initializeItemConfigs();
		itemData.initializeDataFiles();
		return true;
	}
	
	public boolean onDisable() {
		//TODO save configs return true
		isDisabling = true;
		return true;
	}
	
	public void reloadConfig() {
	    if (configFile == null) {
	    	configFile = new File(dir+"/config.yml");
			plugin.getLogger().info(logTit+"Created config.");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource(defConfig);
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
	
	public String getLogTit() {
		return logTit;
	}
}
