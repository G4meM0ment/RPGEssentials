package me.G4meM0ment.DeathAndRebirth.Listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.garbagemule.MobArena.MobArenaHandler;

import me.G4meM0ment.DeathAndRebirth.DeathAndRebirth;
import me.G4meM0ment.DeathAndRebirth.Handler.ConfigHandler;
import me.G4meM0ment.DeathAndRebirth.Handler.GhostHandler;
import me.G4meM0ment.DeathAndRebirth.Handler.GraveHandler;
import me.G4meM0ment.DeathAndRebirth.Handler.PermHandler;
import me.G4meM0ment.DeathAndRebirth.Handler.ShrineHandler;
import me.G4meM0ment.DeathAndRebirth.Handler.SpoutcraftHandler;
import me.G4meM0ment.DeathAndRebirth.Types.DARPlayer;

public class PListener implements Listener {
	
	private DeathAndRebirth subplugin;
	private GhostHandler gH;
	private GraveHandler graveH;
	private ShrineHandler sH;
	private PermHandler permH;
	private SpoutcraftHandler scH;
	
	private static List<String> shrineAreaCooldown = new ArrayList<String>();
	
	public PListener(DeathAndRebirth subplugin)
	{
		this.subplugin = subplugin;
		gH = new GhostHandler(subplugin);
		graveH = new GraveHandler();
		sH = new ShrineHandler();
		permH = new PermHandler();
		scH = new SpoutcraftHandler();
	}
	
	/**
	 * Checks for players death
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onPlayerDeath(PlayerDeathEvent event)
	{		
		Player p = event.getEntity();
		if(p == null) return;
		
		//check for npcs like citizens
		if(event.getEntity().hasMetadata("NPC")) return;
		if(!ConfigHandler.isDAREnabled(p.getWorld().getName())) return;
		if(Bukkit.getPluginManager().getPlugin("MobArena") != null)
			if(new MobArenaHandler().isPlayerInArena(p)) return;
		if(permH.hasIgnorePerm(p)) return;
		
		/*
		 * TODO add config option
		 * if(p.getLastDamageCause().toString().equalsIgnoreCase("void")) return;
		 */
		
		//Some plugins avoid death by increasing health
		if(p.getHealth() > 0) return;

		//if needed remove drops to save the afterwards
		if(permH.hasNoDropPerm(p) || !ConfigHandler.dropItems)
			event.getDrops().clear();
		
		gH.die(p, p.getLocation());
	}
	

	/**
	 * Check for respawning and teleport the ghost to its given location
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		if(!ConfigHandler.isDAREnabled(event.getPlayer().getLocation().getWorld().getName())) return;
		
		Player p = event.getPlayer();
		if(p == null) return;
		
		if(permH.hasIgnorePerm(p)) return;
		
		DARPlayer darP = gH.getDARPlayer(p, p.getWorld().getName());
		if(darP == null) return;
		
		//Set location where the ghost spawns
		Location respawnLoc = gH.getRespawnLocation(darP);
		if(respawnLoc == null)
			event.setRespawnLocation(p.getWorld().getSpawnLocation());
		else
			event.setRespawnLocation(respawnLoc);
		
		gH.respawn(p);
	}
	
	/**
	 * Cancel events if player isn't allowed to interact or res if he's a ghost
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

		if(!ConfigHandler.isDAREnabled(event.getClickedBlock().getWorld().getName())) return;
		
		Location clicked = event.getClickedBlock().getLocation();
		Player p = event.getPlayer();
		if(p == null) return;
		DARPlayer darP = gH.getDARPlayer(p, p.getWorld().getName());
		if(darP == null)
			darP = gH.addDARPlayer(p, p.getWorld());
		Block block = event.getClickedBlock();
		
		/*
		 * ghost interactions
		 */
		if(darP.isDead())
		{
			/*
			 * check if clicked loaction is shrine or grave
			 */
			if(graveH.isPlayersGrave(darP, clicked) || sH.isShrine(clicked, ConfigHandler.shrineRadius))
			{
				//resurrect the player
				if(gH.resurrect(p, clicked))
				{
					//punish if he resurrected where he respawned
					if(ConfigHandler.corpseSpawn && graveH.isPlayersGrave(darP, clicked) && !sH.isShrine(clicked, ConfigHandler.shrineRadius))
						gH.punish(p);
					else if(!ConfigHandler.corpseSpawn && sH.isShrine(clicked, ConfigHandler.shrineRadius) && !graveH.isPlayersGrave(darP, clicked))
						gH.punish(p);
				}
			}
			
			/*
			 * check the block and cancel if not allowed
			 */
			try 
			{
				Material type = block.getType();
				// chest, furnace			
				if(type.equals(Material.FURNACE) ||	type.equals(Material.CHEST)
						|| (ConfigHandler.blockGhostInteractions 
								&& (type.equals(Material.BED) 
								|| type.equals(Material.LEVER)
								|| type.equals(Material.STONE_BUTTON)
								|| type.equals(Material.WOODEN_DOOR) 
								|| type.equals(Material.IRON_DOOR)))) 
				{
					p.sendMessage("You can't do that while being a ghost!");
					event.setCancelled(true);
					return;
				}
			}
			catch (NullPointerException e) {}
		}
		else
		{
			if(sH.isShrine(clicked, 0))
			{
				if(sH.getShrine(clicked, 0) == darP.getShrine())
					gH.bindSoul(darP, null);
				else
					gH.bindSoul(darP, sH.getShrine(clicked, 0));
			}					
		}
	}
	
	/**
	 * Listens to send shrine area messages
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		final Player p = event.getPlayer();
		DARPlayer darP = gH.getDARPlayer(p, p.getWorld().getName());
		if(darP == null) return;
		
		if(ConfigHandler.isDAREnabled(event.getPlayer().getWorld().getName()) && sH.isShrine(event.getTo(), ConfigHandler.shrineRadius) && !shrineAreaCooldown.contains(p.getName()) && darP.isDead())
		{
			p.sendMessage("You are on a shrine area");
			shrineAreaCooldown.add(p.getName());
			Bukkit.getScheduler().scheduleSyncDelayedTask(subplugin.getPlugin(), new Runnable()
			{
				@Override
				public void run() 
				{
					shrineAreaCooldown.remove(p.getName());
				}
			}, 200);
		}
	}
	
	/**
	 * Listens to give compass again and set it's location
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event)
	{		
		final Player p = event.getPlayer();
		if(p == null) return;
		DARPlayer darP = gH.getDARPlayer(p, p.getWorld().getName());
		if(darP == null) return;
		
		if(darP.isDead())
		{
			/*
			 * avoid problems with compass location setting and send message to inform
			 */
			Bukkit.getScheduler().scheduleSyncDelayedTask(subplugin.getPlugin(), new Runnable()
			{
				@Override
				public void run() 
				{
					//give compass again to avoid problems after leaving directly after death
					p.getInventory().clear();
					gH.giveCompass(p);
					//set spoutcraft options
					if(subplugin.getPlugin().isSpoutcraftPluginEnabled() && ConfigHandler.useSpoutcraft)
						scH.setRespawnOptions(p);
					p.sendMessage("You're a ghost");
				}
			}, 100);
		}
	}
	
	/**
	 * To remove players spoutcraft options
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Player p = event.getPlayer();
		if(p == null) return;
		DARPlayer darP = gH.getDARPlayer(p, p.getWorld().getName());
		if(darP == null) return;
		
		if(darP.isDead())
		{
			//reset spoutcraft options
			if(subplugin.getPlugin().isSpoutcraftPluginEnabled() && ConfigHandler.useSpoutcraft)
				scH.setRebirthOptions(p);
		}
	}
	/**
	 * To remove players spoutcraft options
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerKick(PlayerKickEvent event)
	{
		Player p = event.getPlayer();
		if(p == null) return;
		DARPlayer darP = gH.getDARPlayer(p, p.getWorld().getName());
		if(darP == null) return;
		
		if(darP.isDead())
		{
			//reset spoutcraft options
			if(subplugin.getPlugin().isSpoutcraftPluginEnabled() && ConfigHandler.useSpoutcraft)
				scH.setRebirthOptions(p);
		}
	}
	
	/**
	 * Too keep players food level
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onFoodLevelChange(FoodLevelChangeEvent event)
	{
		if(!(event.getEntity() instanceof Player)) return;
		
		Player p = (Player) event.getEntity();
		if(p == null) return;
		DARPlayer darP = gH.getDARPlayer(p, p.getWorld().getName());
		if(darP == null) return;
		
		if(darP.isDead())
			return;
	}
	
	/**
	 * Denies the player to drop items while being a ghost
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerDropItem(PlayerDropItemEvent event)
	{
		if(!ConfigHandler.isDAREnabled(event.getPlayer().getWorld().getName())) return;
		Player p = event.getPlayer();
		if(p == null) return;
		DARPlayer darP = gH.getDARPlayer(p, p.getWorld().getName());
		if(darP == null) return;
		
		if(darP.isDead())
			event.setCancelled(true);
	}
	
	/**
	 * Denies the player to pickup items while being a ghost
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerPickupItem(PlayerPickupItemEvent event)
	{
		if(!ConfigHandler.isDAREnabled(event.getPlayer().getWorld().getName())) return;
		Player p = event.getPlayer();
		if(p == null) return;
		DARPlayer darP = gH.getDARPlayer(p, p.getWorld().getName());
		if(darP == null) return;
		
		if(darP.isDead())
			event.setCancelled(true);
	}
	
	/**
	 * Denies the player to use certain commands while being a ghost
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		if(!ConfigHandler.isDAREnabled(event.getPlayer().getWorld().getName())) return;
		Player p = event.getPlayer();
		if(p == null) return;
		DARPlayer darP = gH.getDARPlayer(p, p.getWorld().getName());
		if(darP == null) return;
		if(!darP.isDead()) return;

		for(String s : ConfigHandler.blockedCommands)
			if(event.getMessage().toLowerCase().startsWith(s.toLowerCase()))
			{
				event.setCancelled(true);
				//TODO add messenger
				event.getPlayer().sendMessage("Command disabled while being a ghost");
				return;
			}
	}
	
	/**
	 * Denies the player to chat while being a ghost
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
	{
		if(!ConfigHandler.isDAREnabled(event.getPlayer().getWorld().getName())) return;
		Player p = event.getPlayer();
		if(p == null) return;
		DARPlayer darP = gH.getDARPlayer(p, p.getWorld().getName());
		if(darP == null) return;
		
		if(!ConfigHandler.ghostChat && darP.isDead())
			event.setCancelled(true);		
	}
	
	/**
	 * Denies the player to open inventories while being a ghost (eg. backpack)
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerOpenInventory(InventoryOpenEvent event)
	{
		if(!(event.getPlayer() instanceof Player)) return;
		
		Player p = (Player) event.getPlayer();
		if(p == null) return;
		DARPlayer darP = gH.getDARPlayer(p, p.getWorld().getName());
		if(darP == null) return;
		
		if(darP.isDead())
			event.setCancelled(true);
	}
	
	/**
	 * Creates a new DARPlayer if cross worlds is enabled
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerTeleport(PlayerTeleportEvent event)
	{
		String worldFrom = event.getFrom().getWorld().getName();
		String worldTo = event.getTo().getWorld().getName();
		if(worldFrom.equals(worldTo)) return;
		
		if(!ConfigHandler.isDAREnabled(event.getTo().getWorld().getName())) return;
		Player p = event.getPlayer();
		if(p == null) return;
		DARPlayer darP = gH.getDARPlayer(p, worldFrom);
		if(darP == null) return;
		
		if(ConfigHandler.crossWorldGhost)
			gH.getDARPlayers(worldTo).add(darP);
	}
	/**
	 * Creates a new DARPlayer if cross worlds is enabled
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerPortal(PlayerPortalEvent event)
	{
		String worldFrom = event.getFrom().getWorld().getName();
		String worldTo = event.getTo().getWorld().getName();
		if(worldFrom.equals(worldTo)) return;
		
		if(!ConfigHandler.isDAREnabled(event.getTo().getWorld().getName())) return;
		Player p = event.getPlayer();
		if(p == null) return;
		DARPlayer darP = gH.getDARPlayer(p, worldFrom);
		if(darP == null) return;
		
		if(ConfigHandler.crossWorldGhost)
			gH.getDARPlayers(worldTo).add(darP);
	}
}
