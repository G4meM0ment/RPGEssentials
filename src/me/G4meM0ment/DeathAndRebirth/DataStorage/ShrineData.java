package me.G4meM0ment.DeathAndRebirth.DataStorage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import me.G4meM0ment.DeathAndRebirth.DeathAndRebirth;
import me.G4meM0ment.DeathAndRebirth.Handler.ShrineHandler;
import me.G4meM0ment.DeathAndRebirth.Types.Shrine;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ShrineData {
	
	private DeathAndRebirth subplugin;
	private ShrineHandler shrineH;
	
	private static File configFile;
	private static FileConfiguration config = null;
	private static String defConfig = "dropDataFileExample.yml";
	private static String logTit;
	
	private static String dir;

	public ShrineData(DeathAndRebirth subplugin) 
	{
		this.subplugin = subplugin;
		shrineH = new ShrineHandler();
		
		dir = subplugin.getDir()+"/data";
		logTit = subplugin.getLogTit();
		configFile = new File(dir+"/shrines.yml");
	}

	public ShrineData() 
	{
		subplugin = new DeathAndRebirth();
		shrineH = new ShrineHandler();
	}
	
	public String getDir()
	{
		return dir;
	}
	
	public void reloadConfig() 
	{
	    if (configFile == null) 
	    {
	    	configFile = new File(dir, "/shrines.yml");
	    	subplugin.getLogger().info(logTit+"Created Config.");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = subplugin.getPlugin().getResource(defConfig);
	    if (defConfigStream != null)
	    {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	        config.options().copyDefaults(true);
	    }
	    subplugin.getLogger().info(logTit+"Shrine data loaded.");
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
	    	return;
	    
	    try 
	    {
	        config.save(configFile);
	    } catch (IOException ex) 
	    {
	    	subplugin.getLogger().log(Level.SEVERE, logTit+"Could not save data to " + configFile, ex);
	    }
	}
	
	public void saveDataToFile()
	{
		for(Shrine s : shrineH.getShrines())
		{
			FileConfiguration config = getConfig();
			
			config.set(s.getName()+".p1.x", s.getP1().getBlockX());
			config.set(s.getName()+".p1.y", s.getP1().getBlockY());
			config.set(s.getName()+".p1.z", s.getP1().getBlockZ());
			
			config.set(s.getName()+".p2.x", s.getP2().getBlockX());
			config.set(s.getName()+".p2.y", s.getP2().getBlockY());
			config.set(s.getName()+".p2.z", s.getP2().getBlockZ());
			
			config.set(s.getName()+".spawn.x", s.getSpawn().getBlockX());
			config.set(s.getName()+".spawn.y", s.getSpawn().getBlockY());
			config.set(s.getName()+".spawn.z", s.getSpawn().getBlockZ());
		}
	}
	
	public void loadDataFromFile()
	{
		
	}
}
