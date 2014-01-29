package me.G4meM0ment.DeathAndRebirth.Handler;

import java.util.HashMap;
import java.util.List;

import me.G4meM0ment.DeathAndRebirth.DeathAndRebirth;
import me.G4meM0ment.DeathAndRebirth.Events.PlayerBecomeGhostEvent;
import me.G4meM0ment.DeathAndRebirth.Events.PlayerResurrectEvent;
import me.G4meM0ment.DeathAndRebirth.Types.DARPlayer;
import me.G4meM0ment.DeathAndRebirth.Types.Grave;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GhostHandler {
	
	private DeathAndRebirth subplugin;
	private InventoryHandler invH;
	private ConfigHandler cH;
	private ShrineHandler shrineH;
	private GraveHandler graveH;
	
	public static HashMap<String, List<DARPlayer>> darPlayers = new HashMap<String, List<DARPlayer>>();
	
	public GhostHandler(DeathAndRebirth subplugin)
	{
		this.subplugin = subplugin;
		invH = new InventoryHandler(subplugin);
		cH = new ConfigHandler();
		shrineH = new ShrineHandler();
		graveH = new GraveHandler();
	}
	public GhostHandler()
	{
		subplugin = new DeathAndRebirth();
		invH = new InventoryHandler(subplugin);
		cH = new ConfigHandler();
		shrineH = new ShrineHandler();
		graveH = new GraveHandler();
	}
	
/*	public boolean isDead(Player p)
	{
		return false;
	} 
	public void setDead(Player p, boolean dead)
	{
		
	} */
	
	public List<DARPlayer> getDARPlayers(World world)
	{
		return darPlayers.get(world.getName());
	}
	
	public HashMap<String, List<DARPlayer>> getDARPlayerLists()
	{
		return darPlayers;
	}
	
	public DARPlayer getDARPlayer(Player p)
	{
		for(DARPlayer darP : getDARPlayers(p.getWorld()))
		{
			if(darP.getPlayer() == null) continue;
			if(darP.getPlayer().equals(p)) return darP;
		}
		return null;
	}
	
	public void died(Player p, Location loc)
	{
		if(p == null) return;
		DARPlayer darP = getDARPlayer(p);
		if(darP == null)
			addDARPlayer(p);
		if(darP.isDead()) return;
		
		PlayerBecomeGhostEvent event = new PlayerBecomeGhostEvent(p, loc);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if(event.isCancelled()) return;
			
		darP.setDead(true);
		darP.setRobbed(false);
		
		//Create new grave or update information
		Grave grave = darP.getGrave();
		if(grave == null)
			grave = new Grave(loc, null, p.getName(), System.currentTimeMillis());
		else
		{
			grave.setLocation(loc);
			grave.setSign(null);
			grave.setPlayerName(p.getName());
			grave.setPlacedMillis(System.currentTimeMillis());
		}
		darP.setGrave(grave);
		
		if(ConfigHandler.graveSign)
			graveH.placeSign(grave, loc.getBlock());		
		
		invH.saveInventory(p);
	}
	
	public void resurrected(Player p, Location clicked)
	{
		if(p == null) return;
		DARPlayer darP = getDARPlayer(p);
		if(darP == null) return;
		if(!darP.isDead()) return;
		
		PlayerResurrectEvent event = new PlayerResurrectEvent(p, null, clicked);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if(event.isCancelled()) return;
		
		darP.setDead(false);
		
		invH.loadInventory(p);
	}
	
	public void respawned(Player p)
	{
		giveCompass(p);
	}
	
	public void addDARPlayer(Player p)
	{
		if(p == null) return;
		getDARPlayers(p.getWorld()).add(new DARPlayer(p.getName(), false, false, false, null, null));
	}
	
	public Location getRespawnLocation(DARPlayer p)
	{
		if(cH.getCorpseSpawn())
			return p.getGrave().getLocation();
		if(p.getShrine() != null)
			return p.getShrine().getSpawn();
		else
			return shrineH.getNearestShrine(p.getPlayer().getLocation()).getSpawn();
	}
	
	public void giveCompass(Player p)
	{
		if(p == null) return;
		p.getInventory().addItem(new ItemStack(Material.COMPASS));
	}
}
