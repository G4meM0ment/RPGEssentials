package me.G4meM0ment.InventoryBackup.DataStorage;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import me.G4meM0ment.InventoryBackup.InventoryBackup;

import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryData {
	
	private InventoryBackup subplugin;
	
	private static String dir;
	
	public InventoryData(InventoryBackup subplugin) 
	{
		this.subplugin = subplugin;
		
		dir = subplugin.getDir()+"/data";
	}
	public InventoryData() 
	{
		subplugin = new InventoryBackup();
	}
	
	public String getDir() 
	{
		return dir;
	}
	
	public File getPlayerFile(Player p)
	{
		if(p == null) return null;
		Date date = new Date();
	    return new File(dir+"/"+p.getName()+"/"+new Timestamp(date.getTime()).toString());
	}
	public void savePlayerFile(FileConfiguration config, File file)
	{
		if(file == null) return;
		try 
		{
			config.save(file);
		} catch (IOException e) 
		{
			subplugin.getLogger().warning(subplugin.getLogTit()+"Could not save "+file.toString());
		}
	}
	public File[] getPlayersBackups(Player p)
	{
		File folder = new File(dir+"/"+p.getName());
		File[] files = folder.listFiles();

		Arrays.sort(files, new Comparator<File>() 
		{ 
		    public int compare(File f1, File f2)
		    {
		        return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
		    }
		});
		
		return files;
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