package me.G4meM0ment.DeathAndRebirth;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.G4meM0ment.DeathAndRebirth.DataStorage.DropData;
import me.G4meM0ment.DeathAndRebirth.DataStorage.Message;
import me.G4meM0ment.DeathAndRebirth.DataStorage.PlayerData;
import me.G4meM0ment.DeathAndRebirth.DataStorage.ShrineData;
import me.G4meM0ment.DeathAndRebirth.Handler.ConfigHandler;
import me.G4meM0ment.DeathAndRebirth.Handler.GhostHandler;
import me.G4meM0ment.DeathAndRebirth.Handler.GraveHandler;
import me.G4meM0ment.DeathAndRebirth.Handler.ShrineHandler;
import me.G4meM0ment.DeathAndRebirth.Listener.BListener;
import me.G4meM0ment.DeathAndRebirth.Listener.EListener;
import me.G4meM0ment.DeathAndRebirth.Listener.PListener;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathAndRebirth {
	
	private RPGEssentials plugin;
	@SuppressWarnings("unused")
	private DARManager manager;
	
	private DropData dropData;
	private PlayerData playerData;
	private ShrineData shrineData;
	
	private PListener pListener;
	private BListener bListener;
	private EListener eListener;
	
	private ShrineHandler sH;
	private GhostHandler gH;
	private GraveHandler graveH;
	
	private static File configFile;
	private static FileConfiguration config = null;
	
	private static String logTit = "DeathAndRebirth: ";
	private static String dir;
	private static Logger logger;
	private static boolean isEnabled = false;

	/**
	 * Death and Rebirth subplugin of
	 * RPGEssentials
	 * @param plugin
	 */
	public DeathAndRebirth(RPGEssentials plugin) 
	{
		this.plugin = plugin;
		pListener = new PListener(this);
		bListener = new BListener();
		eListener = new EListener();
		
		/*
		 * register all event listeners
		 */		
		plugin.getServer().getPluginManager().registerEvents(pListener, plugin);
		plugin.getServer().getPluginManager().registerEvents(bListener, plugin);
		plugin.getServer().getPluginManager().registerEvents(eListener, plugin);		
		
		/*
		 * setup dir, config and logger
		 */
		dir = plugin.getDir()+"/DeathAndRebirth";
		logger = plugin.getLogger();
		configFile = new File(dir+"/config.yml");
		new ConfigHandler();
		new Message();
		
		/*
		 * init handlers and utils
		 */
		dropData = new DropData(this);
		playerData = new PlayerData(this);
		shrineData = new ShrineData(this);
		
		sH = new ShrineHandler();
		gH = new GhostHandler(this);
		graveH = new GraveHandler();
	}
	public DeathAndRebirth() 
	{
		plugin = (RPGEssentials) Bukkit.getPluginManager().getPlugin("RPGEssentials");
		manager = new DARManager(plugin);
		dropData = new DropData(this);
		playerData = new PlayerData(this);
		shrineData = new ShrineData(this);
		
		sH = new ShrineHandler();
		gH = new GhostHandler(this);
		graveH = new GraveHandler();
	}

	/**
	 * Fired when RPGEssentials enabled and enabled DaR
	 * @return
	 */
	public boolean onEnable() {
		//creating config or loading
		reloadConfig();
		saveConfig();
		ConfigHandler.loadSettings();
		
		Message.reloadFile();
		Message.saveFile();
		Message.loadMessages();

		dropData.reloadConfig();
		dropData.saveConfig();
		
		playerData.reloadConfig();
		playerData.saveConfig();
		
		shrineData.reloadConfig();
		shrineData.saveConfig();
		
		/*
		 * For task to execute after server has been started
		 */
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
		{
			@Override
			public void run() 
			{
				ConfigHandler.loadSettings();
				gH.initPlayerLists();
				sH.initShrineLists();
				playerData.loadDataFromFile();
				shrineData.loadDataFromFile();
			}
		});
		
		/*
		 * For auto saving and scheduled repeating tasks
		 */
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() 
		{
			@Override
			public void run() 
			{
				/*
				 * saving data ...
				 */
				saveConfigs();
				saveDataFiles();
				
				/*
				 * checking for too old graves
				 */
				graveH.removeOldSigns();
			}
		}, 0, 12000);
		
		isEnabled = true;
		return true;
	}

	public boolean onDisable() 
	{
		//save all data from cache to file
		ConfigHandler.saveSettings();		
		playerData.saveDataToFile();
		shrineData.saveDataToFile();

		isEnabled = false;
		return true;
	}
	
	/**
	 * reload all configs of dar
	 */
	public void reloadConfigs() 
	{
		reloadConfig();
		Message.reloadFile();
		
		dropData.reloadConfig();
		playerData.reloadConfig();
		shrineData.reloadConfig();
		
		ConfigHandler.loadSettings();
		Message.loadMessages();
	}
	/**
	 * save all data from cache to file
	 */
	public void saveConfigs() 
	{		
		ConfigHandler.saveSettings();
	}
	
	/**
	 * reload all data files of dar
	 */
	public void reloadDataFiles() 
	{
		dropData.reloadConfig();
		playerData.reloadConfig();
		shrineData.reloadConfig();
		
		playerData.loadDataFromFile();
		shrineData.loadDataFromFile();
	}
	/**
	 * save all data from cache to file
	 */
	public void saveDataFiles() 
	{		
		playerData.saveDataToFile();
		shrineData.saveDataToFile();
	}
	
	/**
	 * Reload the config file
	 */
	public void reloadConfig() 
	{
	    if(configFile == null) 
	    {
	    	configFile = new File(dir+"/config.yml");
			plugin.getLogger().info(logTit+"Created Config.");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource("defDaRConf.yml");
	    if(defConfigStream != null) 
	    {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	        config.options().copyDefaults(true);
	    }
		plugin.getLogger().info(logTit+"Config loaded.");
	}
	/**
	 * Returns the File config to work with
	 * @return
	 */
	public FileConfiguration getConfig() {
	    if(config == null)
	        reloadConfig();
	    return config;
	}
	/**
	 * Save the config file
	 */
	public void saveConfig()  {
	    if(config == null || configFile == null) 
	    {
	    	return;
	    }
	    try 
	    {
	        config.save(configFile);
//	        plugin.getLogger().info(logTit+"Config saved");
	    } 
	    catch (IOException ex) 
	    {
	        Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, logTit+"Could not save config to " + configFile, ex);
	    }
	}
	
	public RPGEssentials getPlugin() {
		return plugin;
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
