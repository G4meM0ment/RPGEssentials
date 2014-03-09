package me.G4meM0ment.DeathAndRebirth.DataStorage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import me.G4meM0ment.DeathAndRebirth.DeathAndRebirth;
import me.G4meM0ment.DeathAndRebirth.Framework.Shrine;
import me.G4meM0ment.DeathAndRebirth.Handler.ShrineHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
		subplugin = ((RPGEssentials) Bukkit.getPluginManager().getPlugin("RPGEssentials")).getDeathAndRebirth();
		shrineH = new ShrineHandler();
	}
	
	public String getDir()
	{
		return dir;
	}
	
	public void reloadConfig() 
	{
	    if(configFile == null) 
	    {
	    	configFile = new File(dir, "/shrines.yml");
	    	subplugin.getLogger().info(logTit+"Created Config.");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = subplugin.getPlugin().getResource(defConfig);
	    if(defConfigStream != null)
	    {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	        config.options().copyDefaults(true);
	    }
	    subplugin.getLogger().info(logTit+"Shrine data loaded.");
	}
	public FileConfiguration getConfig() 
	{
	    if(config == null)
	        reloadConfig();
	    return config;
	}
	public void saveConfig()
	{
	    if(config == null || configFile == null)
	    	return;
	    
	    try 
	    {
	        config.save(configFile);
	    } 
	    catch (IOException ex) 
	    {
	    	subplugin.getLogger().log(Level.SEVERE, logTit+"Could not save data to " + configFile, ex);
	    }
	}
	
	/**
	 * save all data to file, flat file changed data is lost
	 */
	public void saveDataToFile()
	{
		
		FileConfiguration config = getConfig();
	
		/*
		 * save cache data to file
		 */
		for(String worldName : shrineH.getShrineLists().keySet())
		{
			//remove old saves
			config.set(worldName, null);
			
			for(Shrine s : shrineH.getShrineLists().get(worldName))
			{			
				config.set(worldName+"."+s.getName()+".max.x", s.getMax().getBlockX());
				config.set(worldName+"."+s.getName()+".max.y", s.getMax().getBlockY());
				config.set(worldName+"."+s.getName()+".max.z", s.getMax().getBlockZ());
				
				config.set(worldName+"."+s.getName()+".min.x", s.getMin().getBlockX());
				config.set(worldName+"."+s.getName()+".min.y", s.getMin().getBlockY());
				config.set(worldName+"."+s.getName()+".min.z", s.getMin().getBlockZ());
				
				config.set(worldName+"."+s.getName()+".spawn.x", s.getSpawn().getBlockX());
				config.set(worldName+"."+s.getName()+".spawn.y", s.getSpawn().getBlockY());
				config.set(worldName+"."+s.getName()+".spawn.z", s.getSpawn().getBlockZ());
				
				config.set(worldName+"."+s.getName()+".binding", s.hasBinding());
				saveConfig();
			}
		}
	}
	
	/**
	 * load data to cache all not saved changes in cache are lost
	 */
	public void loadDataFromFile()
	{
		FileConfiguration config = getConfig();
		for(String worldName : config.getKeys(false))
		{
			List<Shrine> shrines = new ArrayList<Shrine>();
			World world = Bukkit.getWorld(worldName);
			for(String shrineName : config.getConfigurationSection(worldName).getKeys(false))
			{
				String path = worldName+"."+shrineName+".";
				Location max = new Location(world, config.getInt(path+"max.x"), config.getInt(path+"max.y"), config.getInt(path+"max.z"));
				Location min = new Location(world, config.getInt(path+"min.x"), config.getInt(path+"min.y"), config.getInt(path+"min.z"));
				Location spawn = new Location(world, config.getInt(path+"spawn.x"), config.getInt(path+"spawn.y"), config.getInt(path+"spawn.z"));
				boolean binding = config.getBoolean(path+".binding");
				
				//the loaded data transferred into a shrine and added to it's world list
				shrines.add(new Shrine(shrineName, max, min, spawn, binding));
			}
			shrineH.getShrineLists().put(worldName, shrines);
		}
	}
}
