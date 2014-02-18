package me.G4meM0ment.Karma;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.G4meM0ment.Karma.DataStorage.PlayerData;
import me.G4meM0ment.Karma.Handler.PlayerHandler;
import me.G4meM0ment.Karma.Listener.HListener;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Karma {

	private RPGEssentials plugin;
	private HListener pListener;
	private PlayerData pD;
	private static PlayerHandler pH;
	
	private static File configFile;
	private static FileConfiguration config = null;
	
	private static String logTit = "Karma: ";
	private static String dir;
	private static Logger logger;
	private static boolean isEnabled = false;
	
	public static int karmaKilledGood = -10;
	public static int karmaKilledBad = 10;

	public Karma(RPGEssentials plugin)
	{
		this.plugin = plugin;
		pH = new PlayerHandler();
		pListener = new HListener();
		
		plugin.getServer().getPluginManager().registerEvents(pListener, plugin);
		
		dir = plugin.getDir()+"/Karma";
		logger = plugin.getLogger();
		
		configFile = new File(dir+"/config.yml");
		
		pD = new PlayerData(plugin);
	}
	public Karma()
	{
		
	}

	public boolean onEnable() {
		if(!plugin.getConfig().getBoolean("KarmaEnabled"))
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
	    	configFile = new File(plugin.getDataFolder()+dir, "/config.yml");
			plugin.getLogger().info(logTit+"Created Config.");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource("defKarmaConf.yml");
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
		String playerName = "";
		if(args.length > 0) {
			if(args.length > 1 && args[0].equalsIgnoreCase("get")) {
				if(Bukkit.getPlayer(args[1]) == null && !pH.getKarmaList().containsKey(args[1])) {
					sender.sendMessage(ChatColor.GRAY+"Spieler nicht gefunden");
				} else {
					playerName = args[1];
				}
			} else if(sender instanceof Player) {
				playerName = sender.getName();
			}
		} else
			playerName = sender.getName();
		if(!playerName.isEmpty()) {
			sender.sendMessage(ChatColor.GRAY+"Karma von "+playerName+": "+ChatColor.WHITE+pH.getKarma(playerName));
			return true;
		}
		
		if(args.length < 1) return false;
		
		if(sender.hasPermission("karma.admin")) {
			if(args[0].equalsIgnoreCase("reload")) {
				reloadConfig();
				karmaKilledGood = getConfig().getInt("karmaKilledGood");
				karmaKilledBad = getConfig().getInt("karmaKilledBad");
			
				pD.reloadConfig();			
				pD.loadDataFromFile();
				sender.sendMessage("Configs reloaded");
				return true;
		
			} else if(args[0].equalsIgnoreCase("save")) {
				saveConfig();
				pD.saveDataToFile();
				sender.sendMessage("Configs saved");
				return true;
		
			} else if(args[0].equalsIgnoreCase("set") && args.length > 2) {
				if(Bukkit.getPlayer(args[1]) == null && !pH.getKarmaList().containsKey(args[1])) {
					sender.sendMessage("Player not found");
					return true;
				} else {
					try {
						pH.setKarma(args[1], Integer.parseInt(args[2]));
						return true;
					} catch(NumberFormatException e) {
						sender.sendMessage(args[2]+" is not a number!");
						return true;
					}
				}
			}
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
	
	public static PlayerHandler getPlayerHandler() {
		return pH;
	}
}
