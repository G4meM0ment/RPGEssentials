package me.G4meM0ment.Chaintrain.Listener;

import java.util.ArrayList;
import java.util.List;

import me.G4meM0ment.Chaintrain.Chaintrain;
import me.G4meM0ment.Chaintrain.Handler.PermHandler;
import me.G4meM0ment.Chaintrain.Handler.PlayerHandler;
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

public class PListener implements Listener {
	
	private Chaintrain subplugin;
	private PlayerHandler pH;
	
	private static List<String> msgCooldown = new ArrayList<String>();
	
	public PListener(Chaintrain subplugin) {
		this.subplugin = subplugin;
		pH = new PlayerHandler();
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if(!(event.getRightClicked() instanceof Player)) return;
		System.out.println("Debug: 1");
		if(event.getRightClicked().hasMetadata("NPC")) {
			event.setCancelled(true);
			return;
		}
		System.out.println("Debug: 2");
		Player clicked = (Player) event.getRightClicked();
		
		if(pH.isChained(event.getPlayer())) return;
		System.out.println("Debug: 3");
		if(!event.getPlayer().getItemInHand().equals(Material.STRING)) return;
		System.out.println("Debug: 4");
		
		if(!PermHandler.hasChainPerm(clicked)) {
			//Messenger.sendMessage(event.getPlayer(), "Du kannst niemanden fesseln!");
			System.out.println("Debug: perm");
		} else {
			System.out.println("Debug: 5");
			pH.startChaining(clicked, event.getPlayer());
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(pH.isChained(event.getPlayer())) {
			sendChainedMessage(event.getPlayer());
			event.setCancelled(true);
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
