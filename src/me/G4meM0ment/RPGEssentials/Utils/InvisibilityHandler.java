package me.G4meM0ment.RPGEssentials.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InvisibilityHandler {
	
	private static List<Player> hiden = new ArrayList<Player>();
	
	public void hidePlayer(Player p)
	{
		if(p == null || isHiden(p)) return;
		for(Player i : Bukkit.getOnlinePlayers())
			i.hidePlayer(p);
		hiden.add(p);
	}
	
	public void hidePlayerForShown(Player p)
	{
		if(p == null || isHiden(p)) return;
		for(Player i : Bukkit.getOnlinePlayers())
			if(!isHiden(i))
				i.hidePlayer(p);
		hiden.add(p);
	}
	
	public void showPlayer(Player p)
	{
		//TODO add ghhost support
		if(p == null || !isHiden(p)) return;
		for(Player i : Bukkit.getOnlinePlayers())
			i.showPlayer(p);
		hiden.remove(p);
	}
	
	public boolean isHiden(Player p)
	{
		return hiden.contains(p);
	}

}
