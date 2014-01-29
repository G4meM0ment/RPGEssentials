package me.G4meM0ment.DeathAndRebirth;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.G4meM0ment.DeathAndRebirth.DataStorage.DropData;
import me.G4meM0ment.DeathAndRebirth.DataStorage.PlayerData;
import me.G4meM0ment.DeathAndRebirth.DataStorage.ShrineData;
import me.G4meM0ment.DeathAndRebirth.Handler.ConfigHandler;
import me.G4meM0ment.DeathAndRebirth.Handler.ShrineHandler;
import me.G4meM0ment.DeathAndRebirth.Listener.PListener;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathAndRebirth {
	
	private RPGEssentials plugin;
	
	private DropData dropData;
	private PlayerData playerData;
	private ShrineData shrineData;
	
	private PListener pListener;
	
	private ShrineHandler sH;
	private static ConfigHandler cH;
	
	private static File configFile;
	private static FileConfiguration config = null;
	
	private static String logTit = "DeathAndRebirth: ";
	private static String dir;
	private static Logger logger;
	private static boolean isEnabled = false;

	public DeathAndRebirth(RPGEssentials plugin) 
	{
		this.plugin = plugin;
		pListener = new PListener(this);
		
		plugin.getServer().getPluginManager().registerEvents(pListener, plugin);
		
		dir = plugin.getDir()+"/DeathAndRebirth";
		logger = plugin.getLogger();
		configFile = new File(dir+"/config.yml");
		
		dropData = new DropData(this);
		playerData = new PlayerData(this);
		shrineData = new ShrineData(this);
		
		sH = new ShrineHandler();
	}
	public DeathAndRebirth() 
	{
//		portalData = new PortalData(this);
//		ph = new PortalHandler(this);
	}

	public boolean onEnable() {
		//creating config or loading
		reloadConfig();
		saveConfig();
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
			}
		});
		
		isEnabled = true;
		return true;
	}

	public boolean onDisable() 
	{
		isEnabled = false;
		return true;
	}
	
	public void reloadConfigs() 
	{
		reloadConfig();
		dropData.reloadConfig();
		playerData.reloadConfig();
		shrineData.reloadConfig();
	}
	public void reloadConfig() 
	{
	    if (configFile == null) 
	    {
	    	configFile = new File(dir, "/config.yml");
			plugin.getLogger().info(logTit+"Created Config.");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource("defDaRConf.yml");
	    if (defConfigStream != null) 
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
	    {
	    	return;
	    }
	    try 
	    {
	        config.save(configFile);
	        plugin.getLogger().info(logTit+"Config saved");
	    } catch (IOException ex) 
	    {
	        Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, logTit+"Could not save config to " + configFile, ex);
	    }
	}
	
	public RPGEssentials getPlugin()
	{
		return plugin;
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
	
	public ConfigHandler getConfigHandler()
	{
		return cH;
	}
}
