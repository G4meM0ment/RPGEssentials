package me.G4meM0ment.DeathAndRebirth.DataStorage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import me.G4meM0ment.DeathAndRebirth.DeathAndRebirth;
import me.G4meM0ment.DeathAndRebirth.Handler.GhostHandler;
import me.G4meM0ment.DeathAndRebirth.Handler.ShrineHandler;
import me.G4meM0ment.DeathAndRebirth.Types.DARPlayer;
import me.G4meM0ment.DeathAndRebirth.Types.Grave;
import me.G4meM0ment.DeathAndRebirth.Types.Shrine;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerData {
	
	private DeathAndRebirth subplugin;
	private GhostHandler gH;
	private ShrineHandler shrineH;
	
	private static File configFile;
	private static FileConfiguration config = null;
	private static String defConfig = "dropDataFileExample.yml";
	private static String logTit;
	
	private static String dir;

	public PlayerData(DeathAndRebirth subplugin) {
		this.subplugin = subplugin;
		gH = new GhostHandler();
		shrineH = new ShrineHandler();
		
		dir = subplugin.getDir()+"/data";
		logTit = subplugin.getLogTit();
		configFile = new File(dir+"/ghosts");
	}

	public PlayerData() 
	{
		subplugin = new DeathAndRebirth();
		gH = new GhostHandler();
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
	    	configFile = new File(dir, "/ghosts");
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
	    subplugin.getLogger().info(logTit+"Player (ghosts) data loaded.");
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
		FileConfiguration config = getConfig();
		for(String worldName : gH.getDARPlayerLists().keySet())
		{
			for(DARPlayer p : gH.getDARPlayers(Bukkit.getWorld(worldName)))
			{
				if(p.getShrine() != null)
					config.set(worldName+"."+p.getPlayer().getName()+".shrine", p.getShrine().getName());
				config.set(worldName+"."+p.getPlayer().getName()+".dead", p.isDead());
				config.set(worldName+"."+p.getPlayer().getName()+".robbed", p.isRobbed());
				if(p.getGrave() != null)
				{
					Grave g = p.getGrave();
					config.set(worldName+"."+p.getPlayer().getName()+".grave.material", g.getBlockMaterial().toString());	
					config.set(worldName+"."+p.getPlayer().getName()+".grave.materialData", g.getBlockMaterial().getData().toString());
					config.set(worldName+"."+p.getPlayer().getName()+".grave.location.x", g.getLocation().getBlockX());
					config.set(worldName+"."+p.getPlayer().getName()+".grave.location.y", g.getLocation().getBlockY());
					config.set(worldName+"."+p.getPlayer().getName()+".grave.location.z", g.getLocation().getBlockZ());
					config.set(worldName+"."+p.getPlayer().getName()+".grave.location.millis", g.getPlacedMillis());
				}

			}
		}
	}
	
	public void loadDataFromFile()
	{	
		FileConfiguration config = getConfig();
		for(String worldName : config.getKeys(false))
		{
			List<DARPlayer> list = new ArrayList<DARPlayer>();
			
			for(String pName : config.getConfigurationSection(worldName).getKeys(false))
			{
				//Getting darplayer values from data file
				boolean dead = config.getBoolean(worldName+"."+pName+".dead");
				boolean robbed = config.getBoolean(worldName+"."+pName+".robbed");
				
				/*
				 * Getting grave data from config
				 */
				Material m = Material.getMaterial(config.getString(worldName+"."+pName+".grave.material"));
				int data = config.getInt(config.getString(worldName+"."+pName+".grave.materialData"));
				Location loc = new Location(Bukkit.getWorld(worldName),
						config.getInt(worldName+"."+pName+".grave.location.x"), 
						config.getInt(worldName+"."+pName+".grave.location.y"), 
						config.getInt(worldName+"."+pName+".grave.location.z"));
				long millis = config.getLong(worldName+"."+pName+".grave.location.millis");
				Sign sign = null;
				try
				{
					sign = (Sign) loc.getBlock();
				}
				catch(ClassCastException e) {}

				Grave g = new Grave(loc, sign, pName, millis);
				g.setBlockMaterial(m);
				g.setData(data);
				
				//Getting bound shrine
				Shrine s = shrineH.getShrine(config.getString(worldName+"."+pName+".shrine"));
				
				//add new darplayer to list
				list.add(new DARPlayer(pName, dead, true, robbed, g, s));
			}
			gH.getDARPlayerLists().put(worldName, list);
		}
	}
}
