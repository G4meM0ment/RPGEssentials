package me.G4meM0ment.RPGItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.DataStorage.ItemConfig;
import me.G4meM0ment.RPGItem.DataStorage.ItemData;
import me.G4meM0ment.RPGItem.Handler.MetaHandler;
import me.G4meM0ment.RPGItem.Handler.PowerHandler;
import me.G4meM0ment.RPGItem.Listener.BListener;
import me.G4meM0ment.RPGItem.Listener.EListener;
import me.G4meM0ment.RPGItem.Listener.HeroesListener;
import me.G4meM0ment.RPGItem.Listener.InvListener;
import me.G4meM0ment.RPGItem.Listener.PListener;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class RPGItem {
	
	/*
	 * TODO finish powers (add, unlimited, remove, configurable)
	 * TODO make powers loss by less durability
	 * TODO make next line for meta available
	 * TODO add sub-ids
	 * TODO make enchantments configurable
	 * TODO fix leather armor coloring
	 * TODO add bow dmg
	 * TODO save data
	 * TODO reload item configs on reload
	 */
	
	private RPGEssentials plugin;
	private ItemConfig itemConfig;
	private ItemData itemData;
	private PListener pListener;
	private BListener bListener;
	private EListener eListener;
	private InvListener invListener;
	private HeroesListener hListener;
	private PowerHandler ph;
	
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
		pListener = new PListener(plugin);
		bListener = new BListener(plugin);
		eListener = new EListener(plugin);
		invListener = new InvListener(plugin);
		hListener = new HeroesListener();
		ph = new PowerHandler(plugin);
		
		dir = plugin.getDir()+"/RPGItem";
		plugin.getServer().getPluginManager().registerEvents(pListener, plugin);
		plugin.getServer().getPluginManager().registerEvents(bListener, plugin);
		plugin.getServer().getPluginManager().registerEvents(eListener, plugin);
		plugin.getServer().getPluginManager().registerEvents(invListener, plugin);
		if(plugin.getHeroes() != null)
			plugin.getServer().getPluginManager().registerEvents(hListener, plugin);
	}
	public RPGItem() {
	}
	
	public boolean onEnable() {
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
		MetaHandler.setSplitter(getConfig().getInt("FormatLineSize"));
		return true;
	}
	
	public boolean onDisable() {
		isDisabling = true;
		saveData();
		return true;
	}
	
	public void reloadConfigs() {
		reloadConfig();
		itemConfig.reloadConfigs();
		itemData.reloadDataFiles();
	}
	public void saveData() {
		itemData = new ItemData();
		itemData.saveDataToFiles();
	}
	
	public void reloadConfig() {
	    if (configFile == null) {
	    	configFile = new File(dir+"/config.yml");
			Bukkit.getLogger().info(logTit+"Created config.");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource(defConfig);
	    if(defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	        config.options().copyDefaults(true);
	    }
	    Bukkit.getLogger().info(logTit+"Config loaded.");
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
	        Bukkit.getLogger().info(logTit+"Config saved");
	    } catch (IOException ex) {
	        Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, logTit+"Could not save config to " + configFile, ex);
	    }
	}
	
	public String getLogTit() {
		return logTit;
	}
	
	public boolean isDisabling() {
		return isDisabling;
	}
}
