package me.G4meM0ment.Junkie;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.G4meM0ment.Junkie.Listener.PListener;
import me.G4meM0ment.RPGEssentials.RPGEssentials.RPGEssentials;

public class Junkie {

	private RPGEssentials plugin;
	private PListener plistener;
	
	private File configFile;
	private FileConfiguration config = null;
	
	private String logTit = "Junkie: ";
	private String dir;

	public Junkie(RPGEssentials plugin) {
		this.plugin = plugin;
		plistener = new PListener(plugin);
		
		plugin.getServer().getPluginManager().registerEvents(plistener, plugin);
		
		dir = plugin.getDir()+"/Junkie";
		
		configFile = new File(dir+"/config.yml");
	}
	public Junkie() {

	}

	public boolean onEnable() {
		//creating config or loading
		reloadConfig();
		saveConfig();
		
		return true;
	}

	public boolean onDisable() {
		//saving config
		saveConfig();
		plugin.getLogger().info(logTit+"Config saved");
		return true;
	}
	
	public void reloadConfig() {
	    if (configFile == null) {
	    	configFile = new File(plugin.getDataFolder(), dir+"/config.yml");
			plugin.getLogger().info(logTit+"Created Config.");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource("defRNConf");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
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
	

}
