package me.G4meM0ment.DeathAndRebirth.DataStorage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import me.G4meM0ment.DeathAndRebirth.DeathAndRebirth;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class DropData {
	
	private DeathAndRebirth subplugin;
	
	private static File configFile;
	private static FileConfiguration config = null;
	private static String defConfig = "dropDataFileExample.yml";
	private static String logTit;
	
	private static String dir;

	public DropData(DeathAndRebirth subplugin) {
		this.subplugin = subplugin;
		
		dir = subplugin.getDir()+"/data";
		logTit = subplugin.getLogTit();
		configFile = new File(dir+"/drops");
	}

	public DropData() 
	{
		subplugin = ((RPGEssentials) Bukkit.getPluginManager().getPlugin("RPGEssentials")).getDeathAndRebirth();
	}
	
	public String getDir() {
		return dir;
	}
	
	public void reloadConfig() 
	{
	    if (configFile == null) 
	    {
	    	configFile = new File(dir, "/drops");
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
	    subplugin.getLogger().info(logTit+"Drop data loaded.");
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
	    	subplugin.getLogger().log(Level.SEVERE, logTit+"Could not save data to " + configFile, ex);
	    }
	}
	
	/**
	 * After player revive get items from data file back to the player
	 * @param invFile
	 * @param path
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ItemStack[] getItemsFromConfig(FileConfiguration invFile, String path)
	{
		Set<String> keys = invFile.getConfigurationSection(path).getKeys(false);
		ItemStack [] itemstack = new ItemStack[keys.size()];
		Map<String, Object> item = null;
		int i = 0;
		
		for(String key : keys) 
		{
			if(!(invFile.get(path+"."+key) instanceof LinkedHashMap)) 
			{
				if(!(invFile.get(path+"."+key) instanceof MemorySection)) 
					return null;
				Map<String, Object> map = ((MemorySection) invFile.get(path+"."+key)).getValues(false);
				item = map;
			}
			else
				item = (LinkedHashMap<String, Object>) invFile.get(path+"."+key);
			
			itemstack[i] = ItemStack.deserialize(item);
			i++;
		}
		return itemstack;
	}
	
	/**
	 * remove players data after his items were given to him
	 * @param pName
	 * @param wName
	 */
	public void removePlayersSection(String pName, String wName)
	{
		config.set(pName+"."+wName, null);
	}
}
