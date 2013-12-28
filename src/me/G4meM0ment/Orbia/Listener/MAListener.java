package me.G4meM0ment.Orbia.Listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.garbagemule.MobArena.events.ArenaPlayerDeathEvent;

public class MAListener implements Listener{
	
	private static List<String> players = new ArrayList<String>();
	
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
		Player p = event.getPlayer();
		if(p == null) return;
		if(players.contains(p.getName())) 
		{
			Bukkit.dispatchCommand(p, "/ma leave");
			players.remove(p.getName());
		}
	}

}
