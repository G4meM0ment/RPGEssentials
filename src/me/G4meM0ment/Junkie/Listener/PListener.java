package me.G4meM0ment.Junkie.Listener;

import java.util.ArrayList;
import java.util.List;

import me.G4meM0ment.Junkie.Junkie;
import me.G4meM0ment.Junkie.DataStorage.DrugData;
import me.G4meM0ment.Junkie.Handler.DrugHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PListener implements Listener {
	
	@SuppressWarnings("unused")
	private RPGEssentials plugin;
	private Junkie junkie;
	private DrugHandler dh;
	private DrugData dd;
		
	public PListener(RPGEssentials plugin)
	{
		this.plugin = plugin;
		junkie = new Junkie();
		dh = new DrugHandler();
		dd = new DrugData();
	}
		
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractEvent(PlayerInteractEvent event) 
	{
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR)
			return;
		if(event.getPlayer().getGameMode() == GameMode.CREATIVE)
			return;
		
		Player p = event.getPlayer();
		ItemStack item = p.getItemInHand();
		List<Integer> drugs = new ArrayList<Integer>();
		drugs = junkie.getConfig().getIntegerList("drugIds");
		
		int id = item.getTypeId();
		int amnt = item.getAmount();
		if(drugs.contains(id)) 
		{
			event.setCancelled(true);
			if(item.getAmount() <= 1)
				p.getInventory().remove(item);
			else
				item.setAmount(--amnt);
			dh.consum(p, id);
			
			if(item.getType() == Material.MUSHROOM_SOUP)
				p.getInventory().addItem(new ItemStack(Material.BOWL, 1));
			
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) 
	{
		if(!(event.getRightClicked() instanceof Player)) return;
		Player p = event.getPlayer();
		Player e = (Player) event.getRightClicked();
		if(p == null || e == null) return;
		if(!(p.hasPermission("junkie.test") && p.getItemInHand().getType() == Material.GLASS_BOTTLE) || !e.isSneaking()) return;
			
		p.sendMessage("Ergebniss:");
		for(String drug : dd.getConfig().getConfigurationSection(e.getName()).getKeys(false))
		{
			if((dd.getConfig().getInt(e.getName()+"."+drug+".consum")-System.currentTimeMillis()) < 1800000)
			{
				p.sendMessage(drug+": Positiv seit "+(dd.getConfig().getInt(e.getName()+"."+drug+".consum")-System.currentTimeMillis())/1000);
			}
		}
		p.sendMessage("Keine weiteren Drogen im Urin");
	}
}
