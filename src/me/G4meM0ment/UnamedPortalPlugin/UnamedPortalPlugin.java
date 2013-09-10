package me.G4meM0ment.UnamedPortalPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.UnamedPortalPlugin.Listener.PListener;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class UnamedPortalPlugin {

	private RPGEssentials plugin;
	private PListener plistener;
	
	private static File configFile;
	private static FileConfiguration config = null;
	
	private static String logTit = "UnamedPortalPlugin: ";
	private static String dir;

	public UnamedPortalPlugin(RPGEssentials plugin) {
		this.plugin = plugin;
		plistener = new PListener(plugin);
		
		plugin.getServer().getPluginManager().registerEvents(plistener, plugin);
		
		dir = plugin.getDir()+"/Junkie";
		
		configFile = new File(dir+"/config.yml");
	}
	public UnamedPortalPlugin() {

	}

	public boolean onEnable() {
		if(!plugin.getConfig().getBoolean("UnamedPortalPluginEnabled"))
			return true;
		
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
	    	configFile = new File(plugin.getDataFolder()+dir, "/config.yml");
			plugin.getLogger().info(logTit+"Created Config.");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource("defUPPConf.yml");
	    if (defConfigStream != null) {
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
}
