package me.G4meM0ment.UnamedPortalPlugin.Handler;

import java.util.HashMap;

import me.G4meM0ment.UnamedPortalPlugin.UnnamedPortalPlugin;
import me.G4meM0ment.UnamedPortalPlugin.DataStorage.PortalData;
import me.G4meM0ment.UnamedPortalPlugin.Event.PortalEvent;
import me.G4meM0ment.UnamedPortalPlugin.Portal.Portal;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PortalHandler {
	
	private UnnamedPortalPlugin subplugin;
	private PortalData portalData;
	
	private static HashMap<String, Portal> portals = new HashMap<String, Portal>();
	private static HashMap<Player, Long> usedMillis = new HashMap<Player, Long>(); 
	
	public PortalHandler(UnnamedPortalPlugin subplugin)
	{
		this.subplugin = subplugin;
	}
	
	public boolean isPortal(Block b) 
	{
		if(b == null) return false;
		
		for(Portal p : getPortals().values())
		{
			for(Block block : p.getBlocks())
			{
				if(block.getLocation().getWorld() != b.getLocation().getWorld()) continue;
				if(block.getLocation().distance(b.getLocation()) == 0)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public Portal getPortal(Block b) 
	{
		for(Portal p : getPortals().values()) 
		{
			for(Block block : p.getBlocks()) 
			{
				if(block.getLocation().getWorld() != b.getLocation().getWorld()) continue;
				if(block.getLocation().distance(b.getLocation()) == 0)
					return p;
			}
		}
		return null;
	}
	public Portal getPortal(String id)
	{
		for(Portal p : getPortals().values())
			if(p.getID().equalsIgnoreCase(id))
				return p;
		return null;
	}
	
	public HashMap<String, Portal> getPortals() 
	{
		return portals;
	}
	
	public HashMap<Player, Long> getUsedPortalMillis() 
	{
		return usedMillis;
	}

	public void accessedPortal(final Player p, final Portal portal) 
	{
		if(p == null || portal == null || portal.getDestination() == null) return;
		
		final PortalEvent portalEvent = new PortalEvent(portal, p);
		Bukkit.getServer().getPluginManager().callEvent(portalEvent);
		if(!portalEvent.isCancelled()) 
		{
			getUsedPortalMillis().put(p, System.currentTimeMillis());
			if(!subplugin.getConfig().getBoolean("UseOnMove")) 
			{
				p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 240, 1000));
				Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("RPGEssentials"), new Runnable() 
				{
					@Override
					public void run() 
					{
						for(Block b : portal.getBlocks()) 
						{
							if(p.getLocation().distance(b.getLocation()) < 1)
							{
								playWooshEffect(p.getLocation());
								p.teleport(portalEvent.getPortal().getDestination());
								playWooshEffect(portalEvent.getPortal().getDestination());
							}
						}
					}
				}, 80L); 
			}
			else { 
				playWooshEffect(p.getLocation());
				p.teleport(portalEvent.getPortal().getDestination());
				playWooshEffect(portalEvent.getPortal().getDestination());
			}
		}
	}
	
	public void createPortal(Portal portal) 
	{
		if(portal == null) return;
		portalData = new PortalData();
		int counter = 1;
		for(Block b : portal.getBlocks()) 
		{
			portalData.getConfig().set(portal.getID()+".location."+counter+".world", b.getWorld().getName());
			portalData.getConfig().set(portal.getID()+".location."+counter+".x", b.getLocation().getBlockX());
			portalData.getConfig().set(portal.getID()+".location."+counter+".y", b.getLocation().getBlockY());
			portalData.getConfig().set(portal.getID()+".location."+counter+".z", b.getLocation().getBlockZ());
			portalData.saveConfig();
			counter++;
		}
		if(portal.getDestination() != null) 
		{
			portalData.getConfig().set(portal.getID()+".destination.world", portal.getDestination().getWorld().getName());
			portalData.getConfig().set(portal.getID()+".destination.x", portal.getDestination().getBlockX());
			portalData.getConfig().set(portal.getID()+".destination.y", portal.getDestination().getBlockY());
			portalData.getConfig().set(portal.getID()+".destination.z", portal.getDestination().getBlockZ());
			portalData.saveConfig();
		}
		getPortals().put(portal.getID(), portal);
	}
	public void removePortal(Portal portal) 
	{
		if(portal == null) return;
		portalData = new PortalData();
		getPortals().remove(portal.getID());
		portalData.getConfig().set(portal.getID(), null);
		portalData.saveConfig();
	}
	
	private void playWooshEffect(Location loc) 
	{
		if(!subplugin.getConfig().getBoolean("WooshEffect")) return;
		World world = loc.getWorld();
		world.playEffect(loc, Effect.ENDER_SIGNAL, 0);
		world.playEffect(loc, Effect.ENDER_SIGNAL, 0);
		world.playEffect(loc, Effect.SMOKE, 4);
		world.playEffect(loc, Effect.SMOKE, 4);
		world.playEffect(loc, Effect.SMOKE, 4);
		world.playEffect(loc, Effect.GHAST_SHOOT, 0);
	}

	public boolean hasToWait(Player p) 
	{
		if(!getUsedPortalMillis().containsKey(p))
			return false;
		if(System.currentTimeMillis() - getUsedPortalMillis().get(p) < subplugin.getConfig().getLong("PortalCooldown"))
			return true;
		else
			return false;
	}
	
	public void loadChunks()
	{
		for(Portal p : getPortals().values())
		{
			if(p == null) continue;
			if(p.getDestination() == null) continue;
			if(p.getDestination().getChunk() == null) continue;
			p.getDestination().getChunk().load();
		}
	}
}
