package me.G4meM0ment.Orbia.Handler;

import java.util.List;

import me.G4meM0ment.Orbia.Orbia;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;

public class SIHandler {

	private Orbia subplugin;
	
	private static List<Integer> items; 
	
	public SIHandler(Orbia subplugin)
	{
		this.subplugin = subplugin;
		final Orbia orbia = subplugin;
		Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable() 
		{
			@Override
			public void run() 
			{
				items = orbia.getConfig().getIntegerList("ChangeableSubId");
			}
		});
	}
	
	public void reloadList()
	{
		items = subplugin.getConfig().getIntegerList("ChangeableSubId");
	}
	public void changeSubId(Block b, boolean force)
	{
		if(items.contains(b.getType().getId()) || force)
		{
			b.setData((byte) (b.getData()+1));
		}
	}
}
