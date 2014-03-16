package me.G4meM0ment.Orbia.Listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EListener implements Listener {
	
	private List<EntityType> noCreeperDmg = new ArrayList<EntityType>();
	
	public EListener() {
		noCreeperDmg.add(EntityType.BAT);
		noCreeperDmg.add(EntityType.BLAZE);
		noCreeperDmg.add(EntityType.CAVE_SPIDER);
		noCreeperDmg.add(EntityType.CHICKEN);
		noCreeperDmg.add(EntityType.COW);
		noCreeperDmg.add(EntityType.CREEPER);
		noCreeperDmg.add(EntityType.DROPPED_ITEM);
		noCreeperDmg.add(EntityType.ENDER_DRAGON);
		noCreeperDmg.add(EntityType.ENDERMAN);
		noCreeperDmg.add(EntityType.GHAST);
		noCreeperDmg.add(EntityType.GIANT);		
		noCreeperDmg.add(EntityType.IRON_GOLEM);
		noCreeperDmg.add(EntityType.MAGMA_CUBE);
		noCreeperDmg.add(EntityType.OCELOT);
		noCreeperDmg.add(EntityType.PIG);
		noCreeperDmg.add(EntityType.PIG_ZOMBIE);
		noCreeperDmg.add(EntityType.SHEEP);
		noCreeperDmg.add(EntityType.SILVERFISH);
		noCreeperDmg.add(EntityType.SKELETON);
		noCreeperDmg.add(EntityType.SKELETON);
		noCreeperDmg.add(EntityType.SNOWMAN);
		noCreeperDmg.add(EntityType.SPIDER);
		noCreeperDmg.add(EntityType.SQUID);
		noCreeperDmg.add(EntityType.VILLAGER);
		noCreeperDmg.add(EntityType.WITCH);
		noCreeperDmg.add(EntityType.WITHER);
		noCreeperDmg.add(EntityType.WITHER_SKULL);
		noCreeperDmg.add(EntityType.WOLF);
		noCreeperDmg.add(EntityType.ZOMBIE);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Creeper && noCreeperDmg.contains(event.getEntityType())) {
				event.setDamage(0.0);
				event.setCancelled(true);
		}
	}
}
