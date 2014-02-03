package me.G4meM0ment.DeathAndRebirth.Handler;

import java.util.HashMap;

import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.getspout.spoutapi.sound.SoundEffect;

public class SpoutcraftHandler {
	
	private RPGEssentials plugin;
	
	private static HashMap<String, Integer> tasks = new HashMap<String, Integer>();
	
	public SpoutcraftHandler()
	{
		plugin = (RPGEssentials) Bukkit.getPluginManager().getPlugin("RPGEssentials");
	}
	
	/**
	 * Checks p == null and if he got the spoutcraft client
	 * @param p
	 * @return
	 */
	public boolean hasSpoutcraft(Player p)
	{
		if(p == null) return false;
		
		if(SpoutManager.getPlayer(p).isSpoutCraftEnabled())
			return true;
		return false;
	}
	
	/**
	 * Executed to set all death options
	 * @param p
	 */
	public void setDeathOptions(Player p)
	{
		if(!hasSpoutcraft(p)) return;
		SpoutPlayer sp = SpoutManager.getPlayer(p);
		
		//if death sound url is defined play sound
		if(!ConfigHandler.deathSound.isEmpty())
			SpoutManager.getSoundManager().playCustomSoundEffect(plugin, sp, getResourceFromUrl(ConfigHandler.deathSound), false);
	}
	
	/**
	 * Executed to set all respawn options
	 * @param p
	 */
	public void setRespawnOptions(final Player p)
	{
		if(!hasSpoutcraft(p)) return;
		final SpoutPlayer sp = SpoutManager.getPlayer(p);
		
		//if skin url is defined set it
		if(!ConfigHandler.ghostSkin.isEmpty())
			sp.setSkin(getResourceFromUrl(ConfigHandler.ghostSkin));
		
		if(!ConfigHandler.ghostResourcepack.isEmpty() && ConfigHandler.ghostResourcepack.endsWith(".zip"))
			sp.setTexturePack(getResourceFromUrl(ConfigHandler.ghostResourcepack));
		
		if(!ConfigHandler.skyColor.isEmpty())
			SpoutManager.getSkyManager().setSkyColor(sp, ConfigHandler.getFloatColor(ConfigHandler.skyColor));
		if(!ConfigHandler.fogColor.isEmpty())
			SpoutManager.getSkyManager().setFogColor(sp, ConfigHandler.getFloatColor(ConfigHandler.fogColor));
		if(!ConfigHandler.cloudColor.isEmpty())
			SpoutManager.getSkyManager().setCloudColor(sp, ConfigHandler.getFloatColor(ConfigHandler.cloudColor));
		
		if(ConfigHandler.hideHUD)
			sp.getMainScreen().toggleSurvivalHUD(false);
		
		if(ConfigHandler.playBackgroundSounds)
			tasks.put(p.getName(), Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
			{
				@Override
				public void run() 
				{
					//each second it is calculated with 10% chance to play the sound
					if(Math.random()*100 > 90)
						SpoutManager.getSoundManager().playSoundEffect(sp, SoundEffect.GHAST_MOAN_1, p.getLocation(), 10, 100);
				}
			}, 0, 20));
	}
	
	/**
	 * Executed to reset a death options and set rebirth options
	 * @param p
	 */
	public void setRebirthOptions(Player p)
	{
		if(!hasSpoutcraft(p)) return;
		SpoutPlayer sp = SpoutManager.getPlayer(p);
		
		if(!ConfigHandler.rebirthSound.isEmpty())
			SpoutManager.getSoundManager().playCustomSoundEffect(plugin, sp, getResourceFromUrl(ConfigHandler.rebirthSound), false);
		
		if(ConfigHandler.playBackgroundSounds && tasks.containsKey(p.getName()))
			Bukkit.getScheduler().cancelTask(tasks.get(p.getName()));
		
		if(!ConfigHandler.ghostResourcepack.isEmpty())
			sp.resetTexturePack();
		
		if(!ConfigHandler.ghostSkin.isEmpty())
			sp.resetSkin();
		
		if(!ConfigHandler.skyColor.isEmpty())
			SpoutManager.getSkyManager().setSkyColor(sp, Color.remove());
		if(!ConfigHandler.fogColor.isEmpty())
			SpoutManager.getSkyManager().setFogColor(sp, Color.remove());
		if(!ConfigHandler.cloudColor.isEmpty())
			SpoutManager.getSkyManager().setCloudColor(sp, Color.remove());
		
		if(ConfigHandler.hideHUD)
			sp.getMainScreen().toggleSurvivalHUD(true);
	}
	
	/**
	 * Executed to set all options for players who resurrect others
	 * @param p
	 */
	public void setResurrectOptions(Player p)
	{
		if(!hasSpoutcraft(p)) return;
		SpoutPlayer sp = SpoutManager.getPlayer(p);
		
		if(!ConfigHandler.resurrectSound.isEmpty())
			SpoutManager.getSoundManager().playCustomSoundEffect(plugin, sp, getResourceFromUrl(ConfigHandler.resurrectSound), false);
	}
	
	/**
	 * Returns the splitted url only leaving the resource name
	 * @param url
	 * @return
	 */
	private String getResourceFromUrl(String url)
	{
		return url.split("/")[url.split("/").length-1].replace("/", "");			
	}

}
