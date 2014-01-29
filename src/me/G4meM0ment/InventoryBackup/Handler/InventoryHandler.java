package me.G4meM0ment.InventoryBackup.Handler;

import java.io.File;

import me.G4meM0ment.InventoryBackup.InventoryBackup;
import me.G4meM0ment.InventoryBackup.DataStorage.InventoryData;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryHandler {
	
	private RPGEssentials plugin;
	private InventoryBackup subplugin;
	private InventoryData invData;
	
	public InventoryHandler(RPGEssentials plugin)
	{
		this.plugin = plugin;
		subplugin = new InventoryBackup();
		invData = new InventoryData();
	}
	
	public void startBackupScheduler()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
		{
			@Override
			public void run() 
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
					backupInventory(p);
				}
			}
		}, 0, subplugin.getConfig().getInt("backupInterval")*60*20);
	}
	
	private void backupInventory(Player p)
	{
		if(p == null) return;
		int iter = 0;
		File file = invData.getPlayerFile(p);
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		if(config == null)
		{
			subplugin.getLogger().warning(subplugin.getLogTit()+"Could not get file to backup "+p.getName()+"'s inventory");
			return;
		}
		
		for(ItemStack i : p.getInventory().getContents())
		{
			if(i == null) continue;
			config.set("items.item"+iter++, i.serialize());
		}
		iter = 0;
		for(ItemStack i : p.getInventory().getArmorContents())
		{	
			if(i == null) continue;
			config.set("armor.item"+iter++, i.serialize());
		}
			
		invData.savePlayerFile(config, file);
		deleteOldFiles(p);
	}
	private void deleteOldFiles(Player p)
	{
		File[] files = invData.getPlayersBackups(p);
		for(int i = files.length; i > subplugin.getConfig().getInt("backupsPerPlayer"); i--)
			files[files.length-i].delete();
	}
	
	public void loadInventory(Player p, FileConfiguration config)
	{
		p.getInventory().clear();
		p.getInventory().setContents(invData.getItemsFromConfig(config, "items"));
		p.getInventory().setArmorContents(invData.getItemsFromConfig(config, "armor"));
	}
}
