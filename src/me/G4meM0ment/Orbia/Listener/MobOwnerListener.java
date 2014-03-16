package me.G4meM0ment.Orbia.Listener;

import java.util.ArrayList;
import java.util.List;

import me.G4meM0ment.Orbia.Framework.StringMetadata;
import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class MobOwnerListener implements Listener {
	
	private RPGEssentials plugin;
	
	private static List<EntityType> protectable = new ArrayList<EntityType>();
	private int maxProtected = 16;
	
	public MobOwnerListener(RPGEssentials plugin) {
		this.plugin = plugin;
		
		protectable.add(EntityType.CHICKEN);
		protectable.add(EntityType.COW);
		protectable.add(EntityType.OCELOT);
		protectable.add(EntityType.SHEEP);
		protectable.add(EntityType.PIG);
		protectable.add(EntityType.WOLF);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if(!protectable.contains(event.getRightClicked().getType())) return;
		if(!event.getPlayer().getItemInHand().getType().equals(Material.NAME_TAG)) return;
		Player p = event.getPlayer();
		int protectedAnimals = countPlayerAnimals(p);
		String owner = getOwner(event.getRightClicked());
		
		if(protectedAnimals >= maxProtected)
			Messenger.sendMessage(p, ChatColor.GRAY+"Du besitzt bereits "+maxProtected+" von "+maxProtected+" Tieren");
		else if(!owner.isEmpty()) {
			Messenger.sendMessage(p, ChatColor.GRAY+"Dieses Tier gehört bereits "+owner);
		} else {
			event.getRightClicked().setMetadata("MobOwner", new StringMetadata(plugin, owner));
			Messenger.sendMessage(p, ChatColor.GRAY+"Neues Tier gezähmt. Du besitzt jetzt "+(protectedAnimals+1)+" von "+maxProtected+" Tieren");
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onEntityDamage(EntityDamageEvent event) {
		if(!protectable.contains(event.getEntityType())) return;
		Entity e = event.getEntity();
		String owner = getOwner(e);
		
		if(owner.isEmpty())
			return;
		else {
			event.setDamage(0);
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if(!protectable.contains(event.getEntityType())) return;
		Entity e = event.getEntity();
		String owner = getOwner(e);
		
		if(owner.isEmpty())
			return;
		else {
			if(event.getDamager() instanceof Player)
				Messenger.sendMessage((Player) event.getDamager(), ChatColor.GRAY+"Dieses Tier gehört "+owner);
			
			event.setDamage(0);
			event.setCancelled(true);
		}
	}
	
	private int countPlayerAnimals(Player p) {
		int counter = 0;
	
		for(World w : Bukkit.getServer().getWorlds())
			for(Entity e : w.getEntities())
				if(protectable.contains(e.getType()))
					if(e.hasMetadata("MobOwner")) {
						System.out.println("Debug: value: "+(String)e.getMetadata("MobOwner").get(0).value());
						if(((String)e.getMetadata("MobOwner").get(0).value()).equalsIgnoreCase(p.getName())) {
							
							counter++;
						}
					}
	
		return counter;
	}

	private String getOwner(Entity e) {
		if(!protectable.contains(e.getType())) return "";
		if(!e.hasMetadata("MobOwner")) {System.out.println("Debug: no metadata"); return "";}
	
		return (String) e.getMetadata("MobOwner").get(0).value();
	}
	
	/*private int countPlayerAnimals(Player p) {
		int counter = 0;
		
		for(World w : Bukkit.getServer().getWorlds())
			for(Entity e : w.getEntities())
				if(protectable.contains(e.getType()))
					if(e.hasMetadata("MobOwner:"+p.getName()))
						counter++;
		
		return counter;
	}*/
	
	/*private String getOwner(Entity e) {
		if(!protectable.contains(e.getType())) return "";
 		
		List<String> names = new ArrayList<String>();
		for(Player p : Bukkit.getOnlinePlayers())
			names.add(p.getName());
		for(OfflinePlayer p : Bukkit.getOfflinePlayers())
			names.add(p.getName());
		
		for(String name : names)
			if(e.hasMetadata("MobOwner:"+name))
				return name;
		
		return "";
	}*/
	
	/*private boolean isProtected(Entity e) {
		if(!protectable.contains(e.getType())) return false;
 		
		if(getOwner(e).isEmpty())
			return false;
		else
			return true;
	}*/

}
