package me.G4meM0ment.Orbia.Handler;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class CMHandler {
	
	private static List<Player> players;
	
	public CMHandler()
	{
		players  = new ArrayList<Player>();
	}
	
	public boolean isInCombatMode(Player p)
	{
		return players.contains(p);
	}
	
	public void toggleCombatMode(Player p)
	{
		if(players.contains(p))
			players.remove(p);
		else
			players.add(p);
	}
	
	/*public boolean isItemInHotbar(Player player, ItemStack item)
	{
		if(player.getInventory().getItemInHand().getType() == Material.AIR && item.getType() != Material.AIR)
		{
			return true;
		}
		
		for(int i = 0; i < 9; i++) {
		    ItemStack c = player.getInventory().getItem(i);
		    if(c == null) continue;
		    if(c.isSimilar(item))
		    	return true;
		}
		return false;
	} */
}
