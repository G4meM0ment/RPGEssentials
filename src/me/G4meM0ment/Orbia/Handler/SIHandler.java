package me.G4meM0ment.Orbia.Handler;

import java.util.List;

import me.G4meM0ment.Orbia.Orbia;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;

public class SIHandler {

	private static List<Integer> items; 
	
	public SIHandler(final Orbia subplugin)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable() {
			@Override
			public void run() {
				items = subplugin.getConfig().getIntegerList("ChangeableSubId");
			}
		});
	}
	
	public void changeSubId(Block b)
	{
		if(items.contains(b.getType().getId()))
		{
			b.setData((byte) (b.getData()+1));
		}
	}
}
