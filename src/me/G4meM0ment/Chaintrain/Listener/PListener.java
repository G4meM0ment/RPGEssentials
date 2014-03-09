package me.G4meM0ment.Chaintrain.Listener;

import java.util.ArrayList;
import java.util.List;

import me.G4meM0ment.Chaintrain.Chaintrain;
import me.G4meM0ment.Chaintrain.Handler.PermHandler;
import me.G4meM0ment.Chaintrain.Handler.PlayerHandler;
import me.G4meM0ment.DeathAndRebirth.Handler.GhostHandler;
import me.G4meM0ment.DeathAndRebirth.Handler.GraveHandler;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.block.Action;

public class PListener implements Listener {
	
	private Chaintrain subplugin;
	private PlayerHandler pH;
	
	//dar
	private GraveHandler graveHandler;
	private GhostHandler ghostHandler;
	
	private static List<String> msgCooldown = new ArrayList<String>();
	
	public PListener(Chaintrain subplugin) {
		this.subplugin = subplugin;
		pH = new PlayerHandler();
		graveHandler = new GraveHandler();
		ghostHandler = new GhostHandler();
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if(!(event.getRightClicked() instanceof Player)) return;
		if(event.getRightClicked().hasMetadata("NPC")) return;
		
		if(pH.isChained(event.getPlayer())) return;
		if(!event.getPlayer().getItemInHand().getType().equals(Material.LEASH)) return;
		
		Player clicked = (Player) event.getRightClicked();
		Player player = event.getPlayer();
		
		if(!PermHandler.hasChainPerm(player))
			Messenger.sendMessage(player, "Du kannst niemanden fesseln!");
		else
			pH.startChaining(clicked, event.getPlayer());
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(pH.isChained(event.getPlayer())) {
			sendChainedMessage(event.getPlayer());
			event.setCancelled(true);
		}
		
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getPlayer().getItemInHand().getType().equals(Material.LEASH)) {
			if(graveHandler.isGrave(event.getClickedBlock().getLocation())) {
				Player p = Bukkit.getPlayer(graveHandler.getGrave(event.getClickedBlock().getLocation()).getPlayerName());
				if(p != null) {
					ghostHandler.resurrect(p, event.getClickedBlock().getLocation(), true);
					pH.chain(p, event.getPlayer());
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onPlayerMove(PlayerMoveEvent event) {
		if(pH.isChained(event.getPlayer())) {
			sendChainedMessage(event.getPlayer());
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onEntityShootBow(EntityShootBowEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		if(pH.isChained((Player)event.getEntity())) {
			sendChainedMessage((Player)event.getEntity());
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if(pH.isChained(event.getPlayer())) {
			sendChainedMessage(event.getPlayer());
			event.setCancelled(true);
		}
	}
	
	private void sendChainedMessage(final Player player) {
		if(!msgCooldown.contains(player.getName())) {
			Messenger.sendMessage(player, ChatColor.RED+"Du bist gefesselt");
			msgCooldown.add(player.getName());
			Bukkit.getScheduler().scheduleSyncDelayedTask(subplugin.getPlugin(), new Runnable() {
				@Override
				public void run() {
					msgCooldown.remove(player.getName());
				}
			}, 200);
		}
	}
}
