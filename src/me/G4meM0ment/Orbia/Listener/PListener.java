package me.G4meM0ment.Orbia.Listener;

import java.util.ArrayList;
import java.util.List;

import me.G4meM0ment.Orbia.Orbia;
import me.G4meM0ment.Orbia.Handler.CMHandler;
import me.G4meM0ment.Orbia.Handler.SIHandler;
import me.G4meM0ment.Orbia.Tutorial.TutorialHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Utils.InvisibilityHandler;
import me.G4meM0ment.ReNature.OtherPlugins.ReTowny;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.dandielo.citizens.traders_v3.traits.TraderTrait;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.input.KeyPressedEvent;

import com.dthielke.herochat.Channel;
import com.dthielke.herochat.ChannelChatEvent;
import com.dthielke.herochat.Chatter;
import com.dthielke.herochat.Chatter.Result;
import com.dthielke.herochat.Herochat;
import com.garbagemule.MobArena.MobArenaHandler;
import com.herocraftonline.heroes.characters.Hero;

public class PListener implements Listener{
	
	private RPGEssentials plugin;
	private Orbia subplugin;
	private TutorialHandler tutHandler;
	private SIHandler sih;
	private CMHandler cmh;
	private InvisibilityHandler iH;
	
	private static List<Player> dropping = new ArrayList<Player>();
	private static List<String> quitting = new ArrayList<String>();
		
	public PListener(RPGEssentials plugin){
		this.plugin = plugin;
		subplugin = new Orbia();
		tutHandler = new TutorialHandler();
		sih = new SIHandler(subplugin);
		cmh = new CMHandler();
		iH = new InvisibilityHandler();
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		if(p == null) return;
		if(p.hasPermission("voxelsniper.litesniper") || p.hasPermission("voxelsniper.sniper"))
			Bukkit.getServer().dispatchCommand(p, "vs disable");
				
		Hero h = plugin.getHeroes().getCharacterManager().getHero(p);
		if(h.isVerbose())
			Bukkit.getServer().dispatchCommand(p, "hero verbose");
		
		event.setJoinMessage(ChatColor.DARK_GRAY+"["+ChatColor.DARK_GREEN+"+"+ChatColor.DARK_GRAY+"] "+p.getName());
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
		{
			@Override
			public void run() 
			{
				if(plugin.getHerochat() != null) 
				{
					try
					{
						plugin.getHerochat();
						Herochat.getChatterManager().getChatter(p).addChannel(Herochat.getChannelManager().getChannel("Lokal"), false, false);
						Herochat.getChatterManager().getChatter(p).addChannel(Herochat.getChannelManager().getChannel("Global"), false, false);
						if(p.hasPermission("herochat.join.Administratoren"))
							Herochat.getChatterManager().getChatter(p).addChannel(Herochat.getChannelManager().getChannel("Administratoren"), false, false);
						Herochat.getChatterManager().getChatter(p).setActiveChannel(Herochat.getChannelManager().getChannel("Global"), false, false);
					}
					catch(NullPointerException e) {}
				}
			}
		}, 20);
		
		if(quitting.contains(p.getName()))
		{
			p.damage(1000L);
			quitting.remove(p.getName());
		}

		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				if(!SpoutManager.getPlayer(p).isSpoutCraftEnabled() && !p.hasPermission("orbia.admin"))
					p.kickPlayer("Du benötigst Spoutcraft: http://spoutcraft.org/downloads/");
			}
		}, 80);
		
		if(!tutHandler.finishedTutorial(p))
		{
			System.out.println("Debug: Player hasn't finished the tutorial");
			tutHandler.startStage(p, tutHandler.getStage(p));
		}
		
		SpoutManager.getSkyManager().setCloudHeight(SpoutManager.getPlayer(p), 174);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerKick(PlayerKickEvent event) {
		Player p = event.getPlayer();
		if(p == null) return;
		event.setLeaveMessage(ChatColor.DARK_GRAY+"["+ChatColor.DARK_RED+"-"+ChatColor.DARK_GRAY+"] "+p.getName());
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) 
	{
		final Player p = event.getPlayer();
		if(p == null) return;
		final String pName = p.getName();
		event.setQuitMessage(ChatColor.DARK_GRAY+"["+ChatColor.DARK_RED+"-"+ChatColor.DARK_GRAY+"] "+p.getName());
		Hero h = plugin.getHeroes().getCharacterManager().getHero(p);
		
		if(h.isInCombat())
		{
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
			{
				@Override
				public void run()
				{
					if(Bukkit.getPlayer(p.getName()) == null)
						quitting.add(pName);
				}
			}, 12000);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onPlayerDropItem(PlayerDropItemEvent event) 
	{
		Player p = event.getPlayer();
		ItemStack i = event.getItemDrop().getItemStack();
		
		if(p == null || i == null || i.getType() == Material.AIR) return;
		/*if(cmh.isInCombatMode(p) && cmh.isItemInHotbar(p, i))
			event.setCancelled(true); */
		if(dropping.contains(p))
			event.setCancelled(true);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK) return;
		if(event.getClickedBlock().getType() == Material.BEACON || event.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE)
		{
			event.setCancelled(true);
			return;
		}
		int range = 1;
		if(event.getAction() == Action.LEFT_CLICK_BLOCK)
			range = -1;
		if(!event.getPlayer().getItemInHand().hasItemMeta()) return; 
		ReTowny reTowny = new ReTowny(plugin);
		try 
		{
			if(event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("Bauhammer") && (reTowny.isTown(event.getClickedBlock().getLocation()) || event.getPlayer().getGameMode() == GameMode.CREATIVE))
				sih.changeSubId(event.getClickedBlock(), range, event.getPlayer().getGameMode().equals(GameMode.CREATIVE));
		} catch(NullPointerException e)
		{
			System.out.println("Debug: "+reTowny+event.getClickedBlock());
		}
	}
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		if(event.getRightClicked() instanceof Horse)
			event.setCancelled(true);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerDamage(EntityDamageEvent event)
	{
		if(!(event.getEntity() instanceof Player)) return;
		final Player p = (Player) event.getEntity();

		if(event.getCause() == DamageCause.FALL || event.getCause() == DamageCause.FALLING_BLOCK)
		{
			Block b = p.getLocation().getBlock().getRelative(0, -1, 0);
			if(b.getType() == Material.WOOL || b.getType() == Material.HAY_BLOCK || b.getType() == Material.LEAVES)
			{
				event.setDamage(0.0);
				//added to remove sound (hopefully :P)
				event.setCancelled(true);
			}
		}
			
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player p = event.getPlayer();
		Block b = p.getLocation().getBlock().getRelative(0, -1, 0);

		if(!p.isSneaking() && iH.isHiden(p) && b.getType() != Material.HAY_BLOCK && b.getType() != Material.LEAVES)
		{
			iH.showPlayer(p);
			return;
		}
		if(p.isSneaking() && (b.getType() == Material.HAY_BLOCK || b.getType() == Material.LEAVES) && !iH.isHiden(p))
		{
			p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, 2));
			iH.hidePlayerForShown(p);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		String msg = event.getMessage();
		
		if(msg.toLowerCase().contains("hero choose"))
			event.setCancelled(true);
		
		if(msg.toLowerCase().startsWith("p "))
		{
			Bukkit.dispatchCommand(event.getPlayer(), "party "+msg.substring(1, msg.length()-1));
			event.setCancelled(true);
		}
		
		if(plugin.getHeroes().getCharacterManager().getHero(event.getPlayer()).isInCombat())
		{
			if((msg.toLowerCase().contains("pet") && !msg.toLowerCase().contains("pet remove")) ||
					msg.toLowerCase().contains("mount") ||
					msg.toLowerCase().contains("mnt") ||
					msg.toLowerCase().contains("ma"))
			{
				SpoutManager.getPlayer(event.getPlayer()).sendNotification("Du bist im Kampf", "Nicht erlaubt!", Material.IRON_SWORD);
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onChannelChat(ChannelChatEvent event) 
	{
		plugin.getHerochat();
		Channel local = Herochat.getChannelManager().getChannel("Lokal");
		Chatter c = event.getSender();
		if(event.getChannel() == local && !isChatterInRange(c, local))
		{
			event.getSender().getPlayer().sendMessage(ChatColor.YELLOW+"Niemand kann dich hören!");
			event.setResult(Result.FAIL);
		}
	}
	
	private boolean isChatterInRange(Chatter chatter, Channel channel) 
	{
		plugin.getHerochat();
		for(Chatter c : Herochat.getChatterManager().getChatters())
		{
			if(chatter == c)
				continue;
			if(chatter.isInRange(c, channel.getDistance()))
				return true;
		}
		return false;
		
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onNPCRightClick(NPCRightClickEvent event)
	{
		if(event.getNPC().hasTrait(TraderTrait.class) && plugin.getHeroes().getCharacterManager().getHero(event.getClicker()).getBind(event.getClicker().getItemInHand().getType()) != null)
		{
			event.getClicker().sendMessage(ChatColor.GRAY+"Nutze einen anderen Gegenstand um das Händlerinventar zu öffnen!");
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onKeyPressed(KeyPressedEvent event)
	{
		final Player p = event.getPlayer().getPlayer();
		if(event.getPlayer().getDropItemKey() == event.getKey() && cmh.isInCombatMode(p))
		{
			dropping.add(p);
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
			{
				@Override
				public void run()
				{
					dropping.remove(p);
				}
			}, 60);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onInventoryOpen(InventoryOpenEvent event)
	{
		MobArenaHandler mah = new MobArenaHandler();
		if(mah.inRegion(event.getPlayer().getLocation())) event.setCancelled(true);
	}
	
	@EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		if(event.getEntityType() == EntityType.HORSE && event.getSpawnReason() == SpawnReason.CUSTOM)
			event.setCancelled(false);
	}
}
