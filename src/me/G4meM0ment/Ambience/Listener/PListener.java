package me.G4meM0ment.Ambience.Listener;

import me.G4meM0ment.Ambience.DataStorage.SoundData;
import me.G4meM0ment.Ambience.Handler.SoundHandler;
import me.G4meM0ment.Ambience.Handler.TriggerHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class PListener implements Listener{
	
	private RPGEssentials plugin;
	private SoundData sd;
	private TriggerHandler th;
	private SoundHandler sh;
	
	public PListener(RPGEssentials plugin)
	{
		this.plugin = plugin;
		sd = new SoundData();
		th = new TriggerHandler(plugin);
		sh = new SoundHandler();
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Player p = event.getPlayer();
		if(p == null) return;
        
        if(sh.getPlayersMusic().containsKey(p.getName()))
        	sh.getPlayersMusic().remove(p.getName());
        if(sh.getPlayersExactMusic().containsKey(p.getName()))
        	sh.getPlayersExactMusic().remove(p.getName());
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player p = event.getPlayer();
		if(p == null) return;
        
        if(isInListedRegion(p))
        	th.triggeredRegion(p);
        
        th.triggeredBiome(p);
	}

    public boolean isInListedRegion(Player p)
    {
    	for(String s : sd.getConfig().getKeys(false))
    		if(isWithinRegion(p.getLocation(), s))
    			return true;
    	return false;
    }
    public boolean isWithinRegion(Location loc, String region)
    {
        RegionManager manager = plugin.getWorldGuard().getRegionManager(loc.getWorld());
        ApplicableRegionSet set = manager.getApplicableRegions(loc);
        for (ProtectedRegion each : set)
            if (each.getId().equalsIgnoreCase(region))
                return true;
        return false;
    }
    
}
