package me.G4meM0ment.Orbia.Listener;

import java.util.ArrayList;
import java.util.List;

import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.garbagemule.MobArena.events.ArenaPlayerDeathEvent;

public class MAListener implements Listener{
	
	private RPGEssentials plugin;
	
	private static List<String> players = new ArrayList<String>();
	
	public MAListener(RPGEssentials plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onArenaDeath(ArenaPlayerDeathEvent event)
	{
		Player p = event.getPlayer();
		if(p == null) return;
		players.add(p.getName());
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		final Player p = event.getPlayer();
		if(p == null) return;
		if(players.contains(p.getName())) 
		{
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
			{
				@Override
				public void run() 
				{
					Bukkit.dispatchCommand(p, "ma leave");
				}
				
			}, 60);
			players.remove(p.getName());
		}
		
	}

}
