package me.G4meM0ment.DeathAndRebirth.DataStorage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import me.G4meM0ment.DeathAndRebirth.DeathAndRebirth;

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
		subplugin = new DeathAndRebirth();
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
	
	@SuppressWarnings("unchecked")
	public ItemStack[] getItemsFromConfig(FileConfiguration invFile, String path)
	{
		Set<String> keys = invFile.getConfigurationSection(path).getKeys(false);
		ItemStack [] itemstack = new ItemStack[keys.size()];
		Map<String, Object> item = null;
		int i = 0;
		//Map<Enchantment, Integer> enchant = null;
		
		for(String key : keys) 
		{
			if(!(invFile.get(path+"."+key) instanceof LinkedHashMap)) 
			{
				if(!(invFile.get(path+"."+key) instanceof MemorySection)) 
					return null;
				Map<String, Object> map = ((MemorySection) invFile.get(path+"."+key)).getValues(false);
				item = map;
				/*// enchantments
				if (map.containsKey("enchantments")) {
					enchant = new HashMap<Enchantment, Integer>();
		             Object raw = ((MemorySection) map.get("enchantments")).getValues(false);
		 
		             if (raw instanceof Map) {
		                 Map<?, ?> enchants = (Map<?, ?>) raw;
		 
		                 for (Map.Entry<?, ?> entry : enchants.entrySet()) {
		                     Enchantment enchantment = Enchantment.getByName(entry.getKey().toString());
		 
		                     if ((enchantment != null) && (entry.getValue() instanceof Integer)) {
		                         enchant.put(enchantment, (Integer) entry.getValue());
		                     }
		                 }
		             }
				}
				else {
					enchant = null;
				}
				// end enchantments */
			}
			else
				item = (LinkedHashMap<String, Object>) invFile.get(path+"."+key);
			
			itemstack[i] = ItemStack.deserialize(item);
			//if (enchant != null) itemstack[i].addUnsafeEnchantments(enchant);
			i++;
		}
		return itemstack;
	}
}
