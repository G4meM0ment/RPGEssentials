package me.G4meM0ment.DeathAndRebirth.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.G4meM0ment.DeathAndRebirth.DeathAndRebirth;
import me.G4meM0ment.DeathAndRebirth.DataStorage.Message;
import me.G4meM0ment.DeathAndRebirth.Events.PlayerBecomeGhostEvent;
import me.G4meM0ment.DeathAndRebirth.Events.PlayerResurrectEvent;
import me.G4meM0ment.DeathAndRebirth.Types.DARPlayer;
import me.G4meM0ment.DeathAndRebirth.Types.Grave;
import me.G4meM0ment.DeathAndRebirth.Types.Shrine;
import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;

import com.herocraftonline.heroes.characters.Hero;

public class GhostHandler {
	
	private DeathAndRebirth subplugin;
	private InventoryHandler invH;
	private ShrineHandler shrineH;
	private GraveHandler graveH;
	private SpoutcraftHandler scH;
	
	/*
	 * list of all world which contains DARPlayers
	 */
	public static HashMap<String, List<DARPlayer>> darPlayers = new HashMap<String, List<DARPlayer>>();
	
	public GhostHandler(DeathAndRebirth subplugin)
	{
		this.subplugin = subplugin;
		invH = new InventoryHandler(subplugin);
		shrineH = new ShrineHandler();
		graveH = new GraveHandler(this);
		scH = new SpoutcraftHandler();
	}
	public GhostHandler()
	{
		subplugin = ((RPGEssentials) Bukkit.getPluginManager().getPlugin("RPGEssentials")).getDeathAndRebirth();
		invH = new InventoryHandler(subplugin);
		shrineH = new ShrineHandler();
		graveH = new GraveHandler(this);
		scH = new SpoutcraftHandler();
	}
	
	/**
	 * returns a list containing all DARPlayers of the given world
	 * @param worldName
	 * @return
	 */
	public List<DARPlayer> getDARPlayers(String worldName)
	{
		return darPlayers.get(worldName);
	}
	/**
	 * returns the whole map of lists for each world
	 * @return
	 */
	public HashMap<String, List<DARPlayer>> getDARPlayerLists()
	{
		return darPlayers;
	}
	/**
	 * for each enabled world a new list is created
	 */
	public void initPlayerLists()
	{
		for(World w : Bukkit.getServer().getWorlds())
		{
			if(w == null) continue;
			getDARPlayerLists().put(w.getName(), new ArrayList<DARPlayer>());
		}
	}
	
	/**
	 * Returns the registered DARPlayer for the player, if it doesn't exists returns null
	 * @param p
	 * @return
	 */
	public DARPlayer getDARPlayer(Player p, String world)
	{
		if(!ConfigHandler.isDAREnabled(Bukkit.getWorld(world).getName())) return null;
		
		/*
		 * Added temporally to avoid npes TODO remove
		 */
		if(!getDARPlayerLists().containsKey(world))
		{
			getDARPlayerLists().put(world, new ArrayList<DARPlayer>());
			return null;
		}
		
		for(DARPlayer darP : getDARPlayers(world))
			if(darP.getPlayerName().equalsIgnoreCase(p.getName())) 
				return darP;
		return null;
	}
	
	/**
	 * Executes all tasks needed for a player to become a ghost
	 * @param p
	 * @param loc
	 */
	public void die(Player p, Location loc)
	{
		if(p == null) return;

		/*
		 * fires the event to allow other plugins to cancel or modify
		 */
		final PlayerBecomeGhostEvent becomeGhostEvent = new PlayerBecomeGhostEvent(p, loc);
		Bukkit.getServer().getPluginManager().callEvent(becomeGhostEvent);
		if(becomeGhostEvent.isCancelled()) return;
		p = becomeGhostEvent.getPlayer();
		loc = becomeGhostEvent.getLocation();
		
		DARPlayer darP = getDARPlayer(p, loc.getWorld().getName());
		if(darP == null)
			darP = addDARPlayer(p, p.getWorld());
		
		if(darP.isDead()) return;
			
		darP.setDead(true);
		darP.setRobbed(false);
		
		/*
		 * Create new grave or update information
		 */
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
		
		/*
		 * place the grave sign if wanted
		 */
		if(ConfigHandler.graveSign)
			graveH.placeSign(grave, loc.getBlock());		
		
		//save players inventory
		invH.saveInventory(p);
		
		if(subplugin.getPlugin().isSpoutcraftPluginEnabled() && ConfigHandler.useSpoutcraft)
			scH.setDeathOptions(p);
		
		Messenger.sendNotification(p, Message.spoutTitle, Message.spoutMaterial, Message.died);
	}
	
	/**
	 * Executed when player resurrects in any way, execute all tasks to make a player a alive again 
	 * @param p
	 * @param clicked
	 */
	public void resurrect(Player p, Location clicked)
	{
		if(p == null) return;

		/*
		 * fires the event to allow other plugins to cancel or modify
		 */
		final PlayerResurrectEvent resEvent = new PlayerResurrectEvent(p, null, clicked);
		Bukkit.getServer().getPluginManager().callEvent(resEvent);
		if(resEvent.isCancelled()) return;
		p = resEvent.getResurrected();
//		Player resurrecter = resEvent.getResurrecter();
		clicked = resEvent.getClicked();
		
		DARPlayer darP = getDARPlayer(p, clicked.getWorld().getName());
		if(darP == null) return;
		if(!darP.isDead()) return;
		if(System.currentTimeMillis()-darP.getGrave().getPlacedMillis() < ConfigHandler.timeUntilCanRes)
		{
			Messenger.sendNotification(SpoutManager.getPlayer(p.getPlayer()), Message.spoutTitle, Message.spoutMaterial, Message.cantResYet, "%seconds%", ""+(ConfigHandler.timeUntilCanRes-System.currentTimeMillis()-darP.getGrave().getPlacedMillis())/1000);
			return;
		}
		
		darP.setDead(false);
		
		//remove sign if needed
		if(ConfigHandler.graveSign)
			graveH.removeSign(darP.getGrave());
		
		//give back players inventory, delayed to create kind of an effect and avoid inventory is not visible instantly
		final Player fP = p;
		Bukkit.getScheduler().scheduleSyncDelayedTask(subplugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				invH.loadInventory(fP);
			}
		}, 20);
		
		if(ConfigHandler.walkSpeed > 0.0)
			p.setWalkSpeed(0.2F);
		
		if(subplugin.getPlugin().isSpoutcraftPluginEnabled() && ConfigHandler.useSpoutcraft)
			scH.setRebirthOptions(p);
		
		Messenger.sendNotification(p, Message.spoutTitle, Message.spoutMaterial, Message.resurrect);
	}
	
	/**
	 * Executed when a player respawns, to execute all tasks needed after respawning
	 * @param p
	 */
	public void respawn(Player p)
	{
		//give ghosts it's compass and set location
		if(ConfigHandler.compass)
			giveCompass(p);
		
		//finally delete all inventory contents
		p.getInventory().clear();
		
		//settings players walkspeed
		if(ConfigHandler.walkSpeed > 0.0)
			p.setWalkSpeed((float) ConfigHandler.walkSpeed);
		
		if(subplugin.getPlugin().isSpoutcraftPluginEnabled() && ConfigHandler.useSpoutcraft)
			scH.setRespawnOptions(p);
	}
	
	/**
	 * Executes all tasks to punish a self resurrecter
	 * @param p
	 */
	public void punish(Player p)
	{
		if(!ConfigHandler.punishSpawnResurrecter) return;
		
		if(subplugin.getPlugin().getHeroes() != null && ConfigHandler.heroExpLoss)
		{
			Hero h = subplugin.getPlugin().getHeroes().getCharacterManager().getHero(p);
			h.loseExpFromDeath(h.getHeroClass().getExpLoss(), false);
		}
	}
	
	/**
	 * Adds an unregistered player of this world (he's actually in) returns the added player
	 * @param p
	 * @param world
	 * @return
	 */
	public DARPlayer addDARPlayer(Player p, World world)
	{
		if(p == null) return null;
		if(!ConfigHandler.isDAREnabled(p.getWorld().getName())) return null;
		
		if(!getDARPlayerLists().containsKey(p.getWorld().getName()))
			getDARPlayerLists().put(p.getWorld().getName(), new ArrayList<DARPlayer>());
		
		//add the player without any new types included
		DARPlayer darP = new DARPlayer(p.getName(), false, false, false, null, null);
		getDARPlayers(p.getWorld().getName()).add(darP);
		
		return darP;
	}
	
	/**
	 * Returns location defined by config and possible bound soul
	 * @param p
	 * @return
	 */
	public Location getRespawnLocation(DARPlayer p)
	{
		if(ConfigHandler.corpseSpawn)
			return p.getGrave().getLocation();
		if(p.getShrine() != null)
			return p.getShrine().getSpawn();
		else
			return shrineH.getNearestShrineSpawn(p.getGrave().getLocation()).getSpawn();
	}
	
	/**
	 * Gives the ghosts its compass and sets its location
	 * @param p
	 */
	public void giveCompass(final Player p)
	{
		if(p == null) return;
		
		/*
		 * add an short delay to avoid target not set properly
		 */
		Bukkit.getScheduler().scheduleSyncDelayedTask(subplugin.getPlugin(), new Runnable() 
		{
			@Override
			public void run()
			{
				//adds the compass to inventory
				p.getInventory().addItem(new ItemStack(Material.COMPASS));
		
				//set the target location
				Location target = null;
				if(ConfigHandler.corpseSpawn)
					target = shrineH.getNearestShrineSpawn(p.getLocation()).getSpawn();
				else
					target = getDARPlayer(p, p.getWorld().getName()).getGrave().getLocation();
		
				//if none found leave it
				if(target != null)
					p.setCompassTarget(target);
			}
		}, 10);
	}
	
	/**
	 * Bind the soul to the given shrine
	 * @param p
	 * @param s
	 */
	public void bindSoul(DARPlayer p, Shrine s)
	{
		if(p == null) return;
		
		if(s == null)
		{
			p.setShrine(null);
			Messenger.sendNotification(SpoutManager.getPlayer(p.getPlayer()), Message.spoutTitle, Message.spoutMaterial, Message.soulUnbound);
			return;
		}
		p.setShrine(s);
		Messenger.sendNotification(SpoutManager.getPlayer(p.getPlayer()), Message.spoutTitle, Message.spoutMaterial, Message.soulBound);
	}
}
