package me.G4meM0ment.Orbia.Handler.Duell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.herocraftonline.heroes.characters.Hero;

public class DuellHandler {

	private static RPGEssentials plugin;
	private static HashMap<String, HashMap<String, DuellState>> duells = new HashMap<String, HashMap<String, DuellState>>();
	private static List<Player> gracers = new ArrayList<Player>();
	
	public DuellHandler(RPGEssentials plugin) 
	{
		DuellHandler.plugin = plugin;
	}
	public DuellHandler()
	{
		
	}

	public void initDuellRequest(Player p, Player r, boolean request)
	{
		if(p == null || r == null) return;
		Hero h = plugin.getHeroes().getCharacterManager().getHero(p);
       	Hero h2 = plugin.getHeroes().getCharacterManager().getHero(r);
       	HashMap<String, DuellState> duell = new HashMap<String, DuellState>();
       	if(h == null || h2 == null) return;

       	{
       		duell.put(h2.getName(), DuellState.REQUEST);
       		duells.put(h.getName(), duell);
       	}	
   		if(request)
   			initDuellRequest(r, p, false);
	}

	public void initDuell(String p, boolean first)
   	{
		if(p == null) return;
		if(!duells.containsKey(p)) return;
		HashMap<String, DuellState> duell = duells.get(p);
		String key = getDuellPartner(p);
		if(key == null) return;
		
		duell.put(key, DuellState.STARTED);
		
		Player player = Bukkit.getPlayer(p);
		if(player != null)
			player.setCustomName(ChatColor.DARK_PURPLE+p);
		
		if(first)
			initDuell(key, false);
   	}
	
	public void removeDuell(String p)
	{
		if(p == null) return;
		if(!duells.containsKey(p)) return;
		String key = getDuellPartner(p);
		if(key == null) return;
		
		Player player = Bukkit.getPlayer(p);
		Player player2 = Bukkit.getPlayer(key);
		if(player != null)
			player.setCustomName(p);
		if(player2 != null)
			player.setCustomName(key);
		
		duells.remove(p);
		duells.remove(key);
	}

	public boolean isInDuell(Player p, boolean isParentRequest)
	{
		if(p == null) return false;
		if(plugin.getHeroes().getCharacterManager().getHero(p).getParty() != null)
			p = getRegisteredPartyMember(p);
			
		if(!duells.containsKey(p.getName())) return false;
		HashMap<String, DuellState> duell = duells.get(p.getName());
		
		if(duell.get(getDuellPartner(p.getName())) == DuellState.STARTED)
			return true;
		return false;
	}
	public DuellState getDuellState(Player p)
	{
		if(p == null) return DuellState.NONE;
		if(plugin.getHeroes().getCharacterManager().getHero(p).getParty() != null)
			p = getRegisteredPartyMember(p);
			
		if(!duells.containsKey(p.getName())) return DuellState.NONE;
		HashMap<String, DuellState> duell = duells.get(p.getName());
		
		return duell.get(getDuellPartner(p.getName()));
	}
	
	public String getDuellPartner(String p)
	{
		if(!duells.containsKey(p)) return null;
		HashMap<String, DuellState> duell = duells.get(p);
		String key = "";
		for(String s : duell.keySet())
		{
			key = s;
			break;
		}
		if(key.isEmpty()) return null;
		else return key;
	}
	
	public Player getRegisteredPartyMember(Player p)
	{
		Hero h = plugin.getHeroes().getCharacterManager().getHero(p);
		if(h == null || h.getParty() == null) return null;
		
		for(Hero m : h.getParty().getMembers())
		{
			if(isInDuell(m.getPlayer(), false))
				return m.getPlayer();
		}
		return null;
	}
	
	public List<Player> getGracers()
	{
		return gracers;
	}
}
