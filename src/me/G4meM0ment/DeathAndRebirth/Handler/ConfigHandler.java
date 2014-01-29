package me.G4meM0ment.DeathAndRebirth.Handler;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;

public class ConfigHandler {
	
	public static boolean dropItems = false;
	public static boolean corpseSpawn = true;
	
	public static int maxGraveDistance = 3;
	public static boolean graveSign = true;
	public static String signText = "R.I.P";
	
	public static List<World> enabledWorlds = new ArrayList<World>();
	
	/*
	 * 
	 * Death settings
	 * 
	 */
	public boolean getDropItems()
	{
		return dropItems;
	}
	public void setDropItems(boolean dropItems)
	{
		ConfigHandler.dropItems = dropItems;
	}

	public boolean getCorpseSpawn()
	{
		return corpseSpawn;
	}
	public void setCorpseSpawn(boolean corpseSpawn)
	{
		ConfigHandler.corpseSpawn = corpseSpawn;
	}
	
	/*
	 * 
	 * World settings
	 * 
	 */
	public boolean isDAREnabled(World world)
	{
		return enabledWorlds.contains(world);
	}
	public void addWorldEnabled(World world)
	{
		if(world == null) return;
		enabledWorlds.add(world);
	}
	public void removeWorldEnabled(World world)
	{
		if(world == null) return;
		if(!isDAREnabled(world)) return;
		enabledWorlds.remove(world);
	}
	
	/*
	 * 
	 * Grave settings
	 * 
	 */
	public int getMaxGraveDistance()
	{
		return maxGraveDistance;
	}
	public void setMaxGraveDistance(int distance)
	{
		maxGraveDistance = distance;
	}
	
	public boolean getGraveSign()
	{
		return graveSign;
	}
	public void setGraveSign(boolean graveSign)
	{
		ConfigHandler.graveSign = graveSign;
	}
	
	public String getSignText() 
	{
		return signText;
	}
	public void setSignText(String signText) 
	{
		ConfigHandler.signText = signText;
	}
}
