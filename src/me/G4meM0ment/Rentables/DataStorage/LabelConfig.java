package me.G4meM0ment.Rentables.DataStorage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.G4meM0ment.Rentables.Rentables;

public class LabelConfig {

		private Rentables subplugin;
		
		private static File configFile;
		private static FileConfiguration config = null;
		private static String defConfig = "rentLabelDataFileExample.yml";
		private static String logTit;
		
		private static String dir;

		public LabelConfig(Rentables subplugin) {
			this.subplugin = subplugin;
			
			dir = subplugin.getDir();
			logTit = subplugin.getLogTit();
			configFile = new File(dir+"/labels.yml");
		}

		public LabelConfig() {
			subplugin = new Rentables();
		}
		
		public String getDir() {
			return dir;
		}

		public void reloadConfig() {
		    if (configFile == null) {
		    	configFile = new File(dir, "labels.yml");
		    	subplugin.getLogger().info(logTit+"Created Config.");
		    }
		    config = YamlConfiguration.loadConfiguration(configFile);
		 
		    // Look for defaults in the jar
		    InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("RPGEssentials").getResource(defConfig);
		    if (defConfigStream != null) {
		        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		        config.setDefaults(defConfig);
		        config.options().copyDefaults(true);
		    }
		    subplugin.getLogger().info(logTit+"Config loaded.");
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
		    } catch (IOException ex) {
		    	subplugin.getLogger().log(Level.SEVERE, logTit+"Could not save config to " + configFile, ex);
		    }
		}
}
