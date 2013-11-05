package me.G4meM0ment.Junkie.Listener;

import java.util.ArrayList;
import java.util.List;

import me.G4meM0ment.Junkie.Junkie;
import me.G4meM0ment.Junkie.Handler.DrugHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PListener implements Listener {
	
	@SuppressWarnings("unused")
	private RPGEssentials plugin;
	@SuppressWarnings("unused")
	private Junkie junkie;
	private DrugHandler dh;
		
	public PListener(RPGEssentials plugin){
		this.plugin = plugin;
		junkie = new Junkie();
		dh = new DrugHandler();
	}
		

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerInteractEvent(PlayerInteractEvent event) 
	{
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR)
			return;
		
		Player p = event.getPlayer();
		ItemStack item = p.getItemInHand();
		List<Integer> drugs = new ArrayList<Integer>();
		//drugs = junkie.getConfig().getIntegerList("drugIds");
		drugs.add(353);
		drugs.add(357);
		
		if(drugs.contains(item.getType().getId())) 
		{	
			item.setAmount(p.getItemInHand().getAmount()-1);
			dh.consum(p, item.getType().getId());
		}
	}
}
