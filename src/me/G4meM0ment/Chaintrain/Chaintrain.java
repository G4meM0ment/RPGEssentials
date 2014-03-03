package me.G4meM0ment.Chaintrain;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.G4meM0ment.Chaintrain.DataStorage.PlayerData;
import me.G4meM0ment.Chaintrain.Handler.PlayerHandler;
import me.G4meM0ment.Chaintrain.Listener.PListener;
import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Chaintrain {

	private static RPGEssentials plugin;
	private PListener pListener;
	private static PlayerData pD;
	private static PlayerHandler pH;
	
	private static File configFile;
	private static FileConfiguration config = null;
	
	private static String logTit = "Chaintrain: ";
	private static String dir;
	private static Logger logger;
	private static boolean isEnabled = false;

	public Chaintrain(RPGEssentials plugin)
	{
		Chaintrain.plugin = plugin;
		pH = new PlayerHandler();
		pListener = new PListener(this);
		
		plugin.getServer().getPluginManager().registerEvents(pListener, plugin);
		
		dir = plugin.getDir()+"/Chaintrain";
		logger = plugin.getLogger();
		
		configFile = new File(dir+"/config.yml");
		
		pD = new PlayerData(plugin);
	}
	public Chaintrain()
	{
		
	}

	public boolean onEnable() {
		if(!plugin.getConfig().getBoolean("ChaintrainEnabled"))
			return true;
		
		//creating config or loading
		reloadConfig();
		saveConfig();
		pD.reloadConfig();
		pD.saveConfig();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				pD.loadDataFromFile();		
				initAutoSaver();
			}	
		});
		
		isEnabled = true;
		return true;
	}

	public boolean onDisable() {	
		isEnabled = false;
		return true;
	}
	
	private void initAutoSaver() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				pD.saveDataToFile();				
			}	
		}, 0, 12000);
	}
	
	public void reloadConfig() {
	    if (configFile == null) {
	    	configFile = new File(dir+"/config.yml");
			plugin.getLogger().info(logTit+"Created Config.");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource("defChaintrainConf.yml");
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
	
	public boolean exec(CommandSender sender, Command command, String label, String[] args) {
		if(args.length < 1)
			return false;
		
		if(args[0].equalsIgnoreCase("save")) {
			saveConfig();
			pD.saveDataToFile();
			Messenger.sendMessage(sender, ChatColor.GRAY+"Files saved");
		} else if(args[0].equalsIgnoreCase("reload")) {
			reloadConfig();
			pD.reloadConfig();
			pD.loadDataFromFile();
			Messenger.sendMessage(sender, ChatColor.GRAY+"Files reloaded");
		}
		return false;
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
	
	public RPGEssentials getPlugin() {
		return plugin;
	}
	
	public static PlayerHandler getPlayerHandler() {
		return pH;
	}
}
