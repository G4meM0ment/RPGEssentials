package me.G4meM0ment.Ambience.Handler;

import java.util.HashMap;

import me.G4meM0ment.Ambience.Ambience;
import me.G4meM0ment.Ambience.DataStorage.SoundData;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class TriggerHandler {
	
	private RPGEssentials plugin;
	private Ambience subplugin;
	private SoundHandler sh;
	private SoundData sd;
	private CacheHandler ch;
	
	private static HashMap<String, Location> locations = new HashMap<String, Location>();
	
	public TriggerHandler(RPGEssentials plugin)
	{
		this.plugin = plugin;
		subplugin = new Ambience();
		sh = new SoundHandler();
		sd = new SoundData();
		ch = new CacheHandler();
	}
	
	public void triggeredRegion(Player p)
	{
        String region = getPriortizedRegion(p.getLocation());
        if(region != null && !region.isEmpty())
        	sh.prePlay(region, SpoutManager.getPlayer(p));
	}
	
	public boolean triggeredBiome(Player p)
	{
        byte stoneCount = 0;
        Location loc = p.getLocation();
        World world = p.getWorld();
        Environment environment = world.getEnvironment();
        Integer dist = null;
        if(getLocations().containsKey(p.getName()) && getLocations().get(p.getName()).getWorld() == p.getWorld())
        	dist = (int) getLocations().get(p.getName()).distance(p.getLocation());
		Material[] stones = { Material.STONE, Material.DIRT, Material.GRASS, Material.BEDROCK, Material.GRAVEL, Material.GOLD_ORE, Material.IRON_ORE, Material.COAL_ORE, Material.REDSTONE_ORE, Material.GLOWING_REDSTONE_ORE, Material.SMOOTH_BRICK };
		
		for (int y = loc.getBlockY(); y < world.getMaxHeight(); y++) 
        {
            Material type = world.getBlockAt(loc.getBlockX(), y,
            		loc.getBlockZ()).getType();
            if (blockChecker(stones, type)) 
            {
                stoneCount++;
                if (stoneCount > 15)
                    // No need to count higher than 15
                    break;
            }
        }

        if (stoneCount < 5)
        {
            if (dist == null || dist > subplugin.getConfig().getInt("nodeSeparation")) 
            {
                updateLocation(p, p.getLocation());                
                
                if (environment.equals(Environment.NORMAL))
                {
                	 sh.prePlay(ch.getBiomeSound(p.getLocation().getBlock().getBiome().toString()), SpoutManager.getPlayer(p));
                	 return true;
                }
                else if (environment.equals(Environment.NETHER))
                {
                	 sh.prePlay("nether", SpoutManager.getPlayer(p));
                	 return true;
                }
                else if (environment.equals(Environment.THE_END))
                {
               	 	sh.prePlay("end", SpoutManager.getPlayer(p));
               	 	return true;
                }
            }
        }
        else if (stoneCount > 15)
        {
        	if (dist == null || dist > subplugin.getConfig().getInt("nodeSeparation")) 
            {
            	updateLocation(p, p.getLocation());
            	sh.prePlay("underground", SpoutManager.getPlayer(p));
            	return true;
            }  
        }
        return false;
	}


	private static boolean blockChecker(final Material[] blocks, final Material type) 
    {
        for (Material block : blocks) 
            if (type == block) 
                return true;
        return false;
    }
    private void updateLocation(Player p, Location l)
    {
    	getLocations().put(p.getName(), l);
    }
	public HashMap<String, Location> getLocations()
	{
		return locations;
	}
	
    public String getPriortizedRegion(Location loc)
    {
        RegionManager manager = plugin.getWorldGuard().getRegionManager(loc.getWorld());
        ApplicableRegionSet set = manager.getApplicableRegions(loc);
    	for(String s : sd.getConfig().getKeys(false))
    		for(ProtectedRegion each : set)
    			if(each.getId().equalsIgnoreCase(s))
    				return each.getId();
        return null;
    }

}
