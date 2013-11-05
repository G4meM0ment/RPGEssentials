package me.G4meM0ment.RPGItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.DataStorage.ItemConfig;
import me.G4meM0ment.RPGItem.DataStorage.ItemData;
import me.G4meM0ment.RPGItem.Handler.CustomItemHandler;
import me.G4meM0ment.RPGItem.Handler.MetaHandler;
import me.G4meM0ment.RPGItem.Handler.PowerHandler;
import me.G4meM0ment.RPGItem.Listener.BListener;
import me.G4meM0ment.RPGItem.Listener.EListener;
import me.G4meM0ment.RPGItem.Listener.HeroesListener;
import me.G4meM0ment.RPGItem.Listener.InvListener;
import me.G4meM0ment.RPGItem.Listener.PListener;
import net.dandielo.citizens.traders_v3.utils.items.ItemFlag;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class RPGItem {	
	private RPGEssentials plugin;
	private ItemConfig itemConfig;
	private ItemData itemData;
	private PListener pListener;
	private BListener bListener;
	private EListener eListener;
	private InvListener invListener;
	private HeroesListener hListener;
	@SuppressWarnings("unused")
	private PowerHandler ph;
	private CustomItemHandler customItemHandler;
	
	private static File configFile;
	private static FileConfiguration config = null;
	
	private static String logTit = "RPGItem: ";
	private static Logger logger;
	private static String dir;
	private static boolean isDisabling;
	private static boolean isEnabled = false;
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
		customItemHandler = new CustomItemHandler(plugin);
		
		logger = plugin.getLogger();
		dir = plugin.getDir()+"/RPGItem";
		plugin.getServer().getPluginManager().registerEvents(pListener, plugin);
		plugin.getServer().getPluginManager().registerEvents(bListener, plugin);
		plugin.getServer().getPluginManager().registerEvents(eListener, plugin);
		plugin.getServer().getPluginManager().registerEvents(invListener, plugin);
		if(plugin.getHeroes() != null)
			plugin.getServer().getPluginManager().registerEvents(hListener, plugin);
	}
	public RPGItem() {
		plugin = (RPGEssentials) Bukkit.getPluginManager().getPlugin("RPGEssentials");
	}
	
	public boolean onEnable(){
		isDisabling = false;
		File exItem = new File(dir+"/items/RPGItem.yml");
		File exData = new File(dir+"/data/RPGItem.yml");
		reloadConfig();
		saveConfig();
		itemConfig.reloadConfig(exItem);
		itemConfig.saveConfig(exItem);
		itemData.addDataFile(exData);
		itemData.saveDataFile(exData);
		itemConfig.initializeItemConfigs();
		itemData.initializeDataFiles();
		MetaHandler.setSplitter(getConfig().getInt("FormatLineSize"));
		if(plugin.getDtlTraders() != null && plugin.getDtlTraders().isEnabled())
			registerDtlTraderFlag();
		
		//Run methods which needs an enabled server/plugin
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() 
			{			
				customItemHandler.initializeAutoRepair();
			}
		});
		
		isEnabled = true;
		return true;
	}
	
	public boolean onDisable() {
		isDisabling = true;
		itemData.saveDataToFiles();
		
		isEnabled = false;
		return true;
	}
	
	public void reloadConfigs() {
		itemConfig = new ItemConfig();
		itemData = new ItemData();
		
		reloadConfig();
		itemConfig.reloadConfigs();
		itemConfig.initializeItemConfigs();
		itemData.saveDataToFiles();
		itemData.reloadDataFiles();
		itemData.initializeDataFiles();
	}
	public void reloadConfig() {
	    if (configFile == null) {
	    	configFile = new File(dir+"/config.yml");
	    	getLogger().info(logTit+"Created config.");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource(defConfig);
	    if(defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	        config.options().copyDefaults(true);
	    }
	    getLogger().info(logTit+"Config loaded.");
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
	        getLogger().info(logTit+"Config saved");
	    } catch (IOException ex) {
	    	getLogger().log(Level.SEVERE, logTit+"Could not save config to " + configFile, ex);
	    }
	}
	
	public String getLogTit() {
		return logTit;
	}
	
	public Logger getLogger() {
		return logger;
	}
	
	public boolean isDisabling() {
		return isDisabling;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	private void registerDtlTraderFlag(){
		if(plugin.getDtlTraders() != null && plugin.getDtlTraders().isEnabled()) {
			try {
				ItemFlag.registerFlag(me.G4meM0ment.RPGItem.OtherPlugins.RPGItem.class);
			} catch (Exception e) {
				plugin.getLogger().info(logTit+"Could not register dtlTrader flag!");
			} 
		}
	}
}
